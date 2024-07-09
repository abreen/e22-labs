import { readFile, writeFile, readdir, mkdir } from "fs/promises";
import path from "path";
import { promisify } from "util";
import child_process from "child_process";
import StreamZip from "node-stream-zip";
import pLimit from "p-limit";
import pDebounce from "p-debounce";
import ejs from "ejs";
import { createStarryNight } from "@wooorm/starry-night";
import sourceJava from "@wooorm/starry-night/source.java";
import { toHtml } from "hast-util-to-html";
import { log, config } from "../utils.cjs";
import { starryNightGutter } from "./hast-util-starry-night-gutter.js";

const starryNight = await createStarryNight([sourceJava]);
const starryNightJavaScope = starryNight.flagToScope("java");

const exec = promisify(child_process.exec);

const tracesOutputDir = path.join(config.outputDir, "traces");

const limitReadZips = pLimit(2);
const limitReadZipEntries = pLimit(1);
const limitWriteFile = pLimit(30);

/** Rebuild traces when a file changes in one of the known Gradle subprojects */
function shouldConvert(relativePath) {
  if (
    !relativePath.startsWith(path.join(config.traces.gradleDir, "lab")) ||
    !relativePath.endsWith(".java")
  ) {
    return false;
  }

  const subproject = relativePath.split(path.sep)[1];
  return (
    subproject.endsWith("-solution") &&
    config.traces.gradleSubprojects.includes(subproject)
  );
}

/** Given a path to a .java file that changed, do `gradle run` and generate traces */
async function convertFile(relativePath) {
  // "../lab2-solution/src/main/java/App.java" -> "lab2-solution"
  const subproject = relativePath.split(path.sep)[1];

  log("building Gradle subproject", subproject);
  await exec(`pushd .. && ./gradlew :${subproject}:run; popd`);

  return buildTraces(subproject);
}

/** Does `gradle run` and generates all traces from subprojects listed in package.json */
async function convertAll() {
  const subprojects = config.traces.gradleSubprojects || [];
  const tasks = subprojects.map((proj) => `:${proj}:run`);

  log("building Gradle subprojects", subprojects.join(", "));
  await exec(`pushd .. && ./gradlew --parallel ${tasks.join(" ")}; popd`);

  await Promise.all(subprojects.map(buildTraces));
}

/** Render trace ZIP files (e.g., Quicksort.trace.zip) into many HTMX files */
async function buildTraces(subproject) {
  // "lab2-solution" => "lab2"
  const noSolutionsName = subproject.replace("-solution", "");

  // "../_site/traces/lab2"
  const outputDir = path.join(tracesOutputDir, noSolutionsName);
  await mkdir(outputDir, { recursive: true });

  // "../lab2-solution"
  const inputDir = `../${subproject}`;

  const files = await readdir(inputDir);
  return Promise.all(
    files
      .filter((name) => name.endsWith(".trace.zip"))
      .map((name) => path.join(inputDir, name))
      .map((zipPath) => [
        zipPath,
        limitReadZips(() => new StreamZip.async({ file: zipPath })),
      ])
      .map(async ([zipPath, myTurnToRead]) => {
        const zipFile = await myTurnToRead;

        const javaFileName = path
          .basename(zipPath)
          .replace(".trace.zip", ".java");

        const javaClassName = javaFileName.replace(".java", "");

        const javaSourceFile = await readFile(
          path.join(inputDir, "src", "main", "java", javaFileName),
          { encoding: "utf8" }
        );
        const javaSourceLines = javaSourceFile.split(/\r?\n/);

        const entries = Object.values(await zipFile.entries());
        const numEntries = entries.length;

        const outputFilePaths = await Promise.all(
          entries
            .map((entry) => [
              entry,
              numEntries,
              limitReadZipEntries(() => zipFile.entryData(entry)),
            ])
            .map(async ([entry, numEntries, myTurnToReadData]) => {
              const data = await myTurnToReadData;

              // "ComboSum.trace.743.json"
              const nameParts = entry.name.split(".");
              const stepNumber = parseInt(nameParts[2]);

              const stepUrlPrefix = `${config.urlPrefix}/traces/${noSolutionsName}/${javaClassName}/${javaClassName}.trace.`;

              // reads the EJS template and renders it
              const output = await renderTraceStep(
                stepNumber,
                numEntries,
                JSON.parse(data),
                stepUrlPrefix,
                javaSourceLines
              );

              const entryNameNoExt = path.parse(entry.name).name;
              const outputPathBase = path.join(
                tracesOutputDir,
                noSolutionsName,
                javaClassName
              );
              const outputPath = path.join(
                outputPathBase,
                entryNameNoExt + ".html"
              );

              await mkdir(outputPathBase, { recursive: true });

              await limitWriteFile(() => writeFile(outputPath, output));
              return outputPath;
            })
        );

        log("generated HTMX traces for", javaFileName);

        // all entries are rendered to HTMX, use step 1 to render index.html
        const firstStepHtml = await readFile(outputFilePaths[0], {
          encoding: "utf8",
        });
        const indexOutput = await renderIndexPage(
          noSolutionsName,
          javaClassName,
          firstStepHtml
        );

        const classDir = path.join(
          tracesOutputDir,
          noSolutionsName,
          javaClassName
        );
        await mkdir(classDir, { recursive: true });
        return writeFile(path.join(classDir, "index.html"), indexOutput);
      })
  );
}

/** Starting at the given line, search upwards for a blank line in the code */
function improveStartLine(startLine, sourceLines) {
  for (let i = startLine - 1; i >= startLine - 5; i--) {
    if (sourceLines[i - 1] != null && sourceLines[i - 1].trim().length == 0) {
      return i + 1;
    }
  }

  return startLine;
}

function renderTraceStep(
  stepNumber,
  numSteps,
  data,
  stepPrefix,
  javaSourceLines
) {
  // process the top (active) frame to get the start & end code line ranges
  const { startLine, endLine, currentLine } = parseMethodName(
    data.stack[0].methodName
  );

  const sourceCode = javaSourceLines
    .slice(startLine - 3, endLine + 1)
    .join("\n");

  // do syntax highlighting
  const tree = starryNight.highlight(sourceCode, starryNightJavaScope);
  starryNightGutter(tree, startLine - 2, currentLine);
  const highlightedCode = toHtml(tree);

  // render the step
  const stepUrl = (n) => stepPrefix + n + ".html";

  return ejs.renderFile(
    "build-steps/traces/step.ejs",
    {
      ...data,
      stack: data.stack.map((frame) => ({
        ...frame,
        ...parseMethodName(frame.methodName),
      })),
      sourceCode: highlightedCode,
      stepNumber,
      numSteps,
      stepPrefix,
      firstStepUrl: stepUrl(1),
      nextStepUrl: stepUrl(Math.min(stepNumber + 1, numSteps)),
      prevStepUrl: stepUrl(Math.max(1, stepNumber - 1)),
      lastStepUrl: stepUrl(numSteps),
    },
    { async: true }
  );
}

function renderIndexPage(noSolutionsName, className, firstStepHtml) {
  return ejs.renderFile(
    "build-steps/traces/index.ejs",
    { className, project: noSolutionsName, firstStepHtml },
    { async: true }
  );
}

function parseMethodName(methodName) {
  let currentLine, startLine, endLine;
  let methodNameAndLineNumber;

  if (methodName.indexOf("&") > 0) {
    // "findSolutions:34&29:51" (current line 34, method range 29-51)
    const parts = methodName.split("&");
    [methodNameAndLineNumber, [startLine, endLine]] = [
      parts[0],
      parts[1].split(":"),
    ];
    startLine = parseInt(startLine);
    endLine = parseInt(endLine);
  } else {
    methodNameAndLineNumber = methodName;
  }

  // "findSolutions:34" -> ["findSolutions", "34"]
  [methodName, currentLine] = methodNameAndLineNumber.split(":");
  currentLine = parseInt(currentLine);

  return { methodName, currentLine, startLine, endLine };
}

export default {
  shouldConvert,
  convertFile: pDebounce(convertFile, 5000),
  convertAll,
};
