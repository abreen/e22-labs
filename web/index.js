import { mkdir, lstat } from "fs/promises";
import Watcher from "watcher";
import { debug, config, relativeToHere, ignorePath } from "./utils.cjs";
import traces from "./build-steps/build-traces.js";
import slides from "./build-steps/build-standard-slides.js";
import site from "./build-steps/build-site.js";
import copyStatic from "./build-steps/copy-static-files.js";
import copySourceCode from "./build-steps/copy-source-code.js";

const DIRS_TO_WATCH = [
  config.marpit.slidesDir,
  config.markdownit.inputDir,
  config.static.inputDir,
  ...config.code.inputDirs,
];

const converters = {
  traces,
  slides,
  site,
  copyStatic,
  copySourceCode,
};

if (process.argv.includes("-w") || process.argv.includes("--watch")) {
  debug("starting watch mode");
  await init();
  new Watcher(
    DIRS_TO_WATCH,
    { ...config.watcher, ignore: ignorePath },
    handleEvent
  );
} else {
  debug("building everything");
  await init();
  await Promise.all(
    Object.values(converters).map(({ convertAll }) => convertAll())
  );
}

async function handleEvent(event, absolutePath) {
  debug(`got ${event} event for ${absolutePath}`);
  if (!shouldHandleEvent(event)) {
    return;
  }

  if ((await lstat(absolutePath)).isDirectory()) {
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

function init() {
  return mkdir(config.outputDir, { recursive: true });
}

function shouldHandleEvent(event) {
  return ["add", "change"].includes(event);
}
