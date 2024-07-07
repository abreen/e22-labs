import path from "path";
import { mkdir } from "fs/promises";
import Watcher from "watcher";
import { config } from "./utils.js";

import traces from "./build-steps/build-traces.js";
import slides from "./build-steps/build-standard-slides.js";

const converters = [traces, slides];

if (process.argv[2] == "-w" || process.argv[2] === "--watch") {
  await init();

  log("starting watch mode");

  const dirsToWatch = [config.traces.gradleDir, config.marpit.slidesDir];
  new Watcher(
    dirsToWatch,
    {
      recursive: config.watcher.recursive,
      renameDetection: true,
      ignoreInitial: true,
      debounce: config.watcher.debounce,
    },
    (event, absolutePath) => {
      // "/Users/abreen/repo/web/file.md" -> "web/file.md"
      const relativePath = path.relative(path.dirname(""), absolutePath);
      const fileName = path.basename(absolutePath);

      if ([".", "_"].includes(fileName[0])) {
        return;
      }

      converters.forEach(async ({ filter, convertFile }) => {
        if (await filter(relativePath)) {
          convertFile(relativePath);
        }
      });
    }
  );
} else {
  await init();

  log("building everything");

  converters.forEach(async ({ convertAll }) => {
    await convertAll();
  });
}

async function init() {
  await mkdir(config.outputDir, { recursive: true });
}

function log(str) {
  console.log("build:", str);
}
