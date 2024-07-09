import path from "path";
import { mkdir, lstat } from "fs/promises";
import Watcher from "watcher";
import {
  debug,
  config,
  relativeToHere,
  isFileInDir,
  fileIsHidden,
} from "./utils.cjs";
import traces from "./build-steps/build-traces.js";
import slides from "./build-steps/build-standard-slides.js";
import site from "./build-steps/build-site.js";
import copyStatic from "./build-steps/copy-static-files.js";

const DIRS_TO_WATCH = [
  config.marpit.slidesDir,
  config.markdownit.inputDir,
  config.static.inputDir,
  config.traces.gradleDir,
];
const DIRS_TO_IGNORE = [".gitignore", "node_modules", config.outputDir];

const converters = {
  traces,
  slides,
  site,
  copyStatic,
};

if (process.argv.includes("-w") || process.argv.includes("--watch")) {
  debug("starting watch mode");

  await init();

  new Watcher(
    DIRS_TO_WATCH,
    getWatcherConfig(DIRS_TO_IGNORE),
    async (event, absolutePath) => {
      debug(`got ${event} event for ${absolutePath}`);
      if (!shouldHandleEvent(event)) {
        return;
      }

      const fileName = path.basename(absolutePath);
      if (fileIsHidden(fileName) || (await lstat(absolutePath)).isDirectory()) {
        return;
      }

      const relativePath = relativeToHere(absolutePath);

      await Promise.all(
        Object.entries(converters).map(
          async ([name, { shouldConvert, convertFile }]) => {
            if (await shouldConvert(relativePath)) {
              debug(name, "converting", relativePath);
              return convertFile(relativePath);
            }

            debug(name, "skipping");
          }
        )
      );
    }
  );
} else {
  debug("building everything");

  await init();

  await Promise.all(
    Object.values(converters).map(({ convertAll }) => convertAll())
  );
}

function init() {
  return mkdir(config.outputDir, { recursive: true });
}

function getWatcherConfig(ignoreDirs) {
  return {
    ...config.watcher,
    ignoreInitial: true,
    ignore: (filePath) =>
      fileIsHidden(filePath) ||
      ignoreDirs.filter((dir) => isFileInDir(filePath, dir)).length > 0,
  };
}

function shouldHandleEvent(event) {
  return ["add", "change"].includes(event);
}
