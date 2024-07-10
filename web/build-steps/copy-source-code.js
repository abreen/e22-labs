import { mkdir, copyFile, readFile, writeFile } from "fs/promises";
import path from "path";
import ejs from "ejs";
import pDebounce from "p-debounce";
import { createStarryNight } from "@wooorm/starry-night";
import sourceJava from "@wooorm/starry-night/source.java";
import { toHtml } from "hast-util-to-html";
import { log, config, isFileInDir, makeConvertAll } from "../utils.cjs";
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
  return subproject != null && subproject.endsWith("-solution");
}

async function convertFile(relativePath) {
  // "lab1-solution/src/main/java/ArrayBag.java"
  const relativeToGradle = path.relative(config.gradleDir, relativePath);
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

  const input = await readFile(relativePath, { encoding: "utf8" });

  const html = await renderSourceCodePage(restOfPath, subproject, input);

  log(`java => html: ${relativePath} => ${outputPath + ".html"}`);
  return writeFile(outputPath + ".html", html);
}

async function renderSourceCodePage(fileName, subproject, sourceCode) {
  const tree = starryNight.highlight(sourceCode, starryNightJavaScope);
  gutter2(tree);
  const highlightedCode = toHtml(tree);

  const pageHtml = await ejs.renderFile("build-steps/ejs/source-code.ejs", {
    fileName,
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
