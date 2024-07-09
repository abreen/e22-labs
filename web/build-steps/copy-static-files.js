import { mkdir, copyFile } from "fs/promises";
import path from "path";
import pDebounce from "p-debounce";
import { log, config, isFileInDir, makeConvertAll } from "../utils.cjs";

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
  log(`${ext} => ${ext}: ${relativePath} => ${outputPath}`);

  return copyFile(relativePath, outputPath);
}

export default {
  shouldConvert,
  convertFile: pDebounce(convertFile, 500),
  convertAll: makeConvertAll(config.static.inputDir, convertFile),
};
