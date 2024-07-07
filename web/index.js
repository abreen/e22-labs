import path from "path";
import { mkdir } from "fs/promises";
import Watcher from "watcher";
import { log, config, relativeToHere } from "./utils.cjs";

import traces from "./build-steps/build-traces.js";
import slides from "./build-steps/build-standard-slides.js";
import site from "./build-steps/build-site.js";

const converters = [traces, slides, site];

if (process.argv[2] == "-w" || process.argv[2] === "--watch") {
  await init();

  log("starting watch mode");

  const dirsToWatch = [config.traces.gradleDir, config.marpit.slidesDir];
  new Watcher(dirsToWatch, getWatcherConfig(), (event, absolutePath) => {
    const relativePath = relativeToHere(absolutePath);
    if (skipFile(relativePath)) {
      return;
    }

    converters.forEach(async ({ shouldConvert, convertFile }) => {
      if (await shouldConvert(relativePath)) {
        convertFile(relativePath);
      }
    });
  });
} else {
  await init();

  log("building everything");

  await Promise.all(converters.map(({ convertAll }) => convertAll()));
}

function init() {
  return mkdir(config.outputDir, { recursive: true });
}

function skipFile(filePath) {
  const fileName = path.basename(filePath);
  return [".", "_"].includes(fileName[0]);
}

function getWatcherConfig() {
  return {
    recursive: config.watcher.recursive,
    renameDetection: true,
    ignoreInitial: true,
    debounce: config.watcher.debounce,
  };
}
