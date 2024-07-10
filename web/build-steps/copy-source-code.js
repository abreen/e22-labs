import { mkdir, copyFile, readFile, writeFile } from "fs/promises";
import path from "path";
import ejs from "ejs";
import pDebounce from "p-debounce";
import { createStarryNight } from "@wooorm/starry-night";
import sourceJava from "@wooorm/starry-night/source.java";
import { toHtml } from "hast-util-to-html";
import { log, debug, config, isFileInDir, makeConvertAll } from "../utils.cjs";
import { getSubprojectFromPath } from "./build-traces.js";
import { gutter2 } from "./hast-util-starry-night-gutter.js";

const starryNight = await createStarryNight([sourceJava]);
const starryNightJavaScope = starryNight.flagToScope("java");

function shouldConvert(relativePath) {
  const { ext } = path.parse(relativePath);
  if (
    ext != ".java" ||
    config.code.inputDirs.filter((dir) => isFileInDir(relativePath, dir))
      .length == 0
  ) {
    return false;
  }

  const subproject = getSubprojectFromPath(relativePath);
  return (
    (subproject != null && subproject.endsWith("-solution")) ||
    subproject == "common"
  );
}

async function convertFile(relativePath) {
  // "lab1-solution/src/main/java/ArrayBag.java"
  const relativeToGradle = path.relative(config.traces.gradleDir, relativePath);
  debug("relativeToGradle", relativeToGradle);

  const parts = relativeToGradle.split(path.sep);

  const [subproject, mainOrTest] = [parts[0], parts[2]];
  const restOfPath = path.join(...parts.slice(4));

  const subprojectNoSolution = subproject.replace("-solution", "");

  // "lab1-solution/src/main/java/x/y/z/App.java" => "/code/lab1/main/x/y/z/App.java"
  const outputPath = path.join(
    config.outputDir,
    "code",
    subprojectNoSolution,
    mainOrTest,
    restOfPath
  );
  await mkdir(path.parse(outputPath).dir, { recursive: true });

  const rawOutputPath = path.join(
    config.outputDir,
    "code",
    "raw",
    subprojectNoSolution,
    mainOrTest,
    restOfPath
  );
  await mkdir(path.parse(rawOutputPath).dir, { recursive: true });

  const rawUrl = [
    config.prefix,
    "code",
    "raw",
    subprojectNoSolution,
    mainOrTest,
    restOfPath,
  ].join("/");

  // https://github.com/abreen/e22-labs/blob/main/lab2-solution/src/main/java/App.java
  // https://github.com/abreen/e22-labs/blob/lab3-solution/src/test/java/SortTest.java
  const gitHubUrl = [
    "https://github.com/abreen/e22-labs/blob/main",
    relativePath.split(path.sep).slice(1).join("/"),
  ].join("/");
  console.log("githubUrl", gitHubUrl);

  const input = await readFile(relativePath, { encoding: "utf8" });

  const html = await renderSourceCodePage(
    restOfPath,
    rawUrl,
    gitHubUrl,
    subprojectNoSolution,
    input
  );

  log(`java => html: ${relativePath} => ${outputPath + ".html"}`);
  await writeFile(outputPath + ".html", html);

  // also copy the original .java file to "code/raw/"
  log(`java => java: ${relativePath} => ${rawOutputPath}`);
  return writeFile(rawOutputPath, input);
}

async function renderSourceCodePage(
  fileName,
  fileUrl,
  gitHubUrl,
  subproject,
  sourceCode
) {
  const tree = starryNight.highlight(sourceCode, starryNightJavaScope);
  gutter2(tree);
  const highlightedCode = toHtml(tree);

  const pageHtml = await ejs.renderFile("build-steps/ejs/source-code.ejs", {
    fileName,
    fileUrl,
    gitHubUrl,
    project: subproject,
    sourceCode: highlightedCode,
  });

  return ejs.renderFile(path.join(config.markdownit.inputDir, "template.ejs"), {
    title: fileName,
    content: pageHtml,
    prefix: config.urlPrefix,
  });
}

export default {
  shouldConvert,
  convertFile: pDebounce(convertFile, 500),
  convertAll: makeConvertAll(
    shouldConvert,
    convertFile,
    ...config.code.inputDirs
  ),
};
