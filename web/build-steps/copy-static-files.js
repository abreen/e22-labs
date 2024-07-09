import { mkdir, copyFile, readFile, writeFile } from "fs/promises";
import path from "path";
import pDebounce from "p-debounce";
import CleanCSS from "clean-css";
import { log, config, isFileInDir, makeConvertAll } from "../utils.cjs";

const clean = new CleanCSS();

function shouldConvert(relativePath) {
  return isFileInDir(relativePath, config.static.inputDir);
}

async function convertFile(relativePath) {
  const outputPath = path.join(
    config.outputDir,
    path.relative(config.static.inputDir, relativePath)
  );

  await mkdir(path.parse(outputPath).dir, { recursive: true });

  const ext = path.parse(relativePath).ext.substring(1) || "?";
  if (ext == "css" && !process.argv.includes("--watch")) {
    const input = await readFile(relativePath, { encoding: "utf8" });
    const cleaned = clean.minify(input);
    log(`css => minified-css: ${relativePath} => ${outputPath}`);
    return writeFile(outputPath, cleaned.styles);
  } else {
    log(`${ext} => ${ext}: ${relativePath} => ${outputPath}`);
    return copyFile(relativePath, outputPath);
  }
}

export default {
  shouldConvert,
  convertFile: pDebounce(convertFile, 500),
  convertAll: makeConvertAll(config.static.inputDir, convertFile),
};
