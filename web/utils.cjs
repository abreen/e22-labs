const path = require("path");
const { readdir, lstat } = require("fs/promises");
const { marpCli } = require("@marp-team/marp-cli");

const packageJson = require("./package.json");
const config = {
  ...packageJson.config,
  outputDir:
    process.env.OUTPUT_DIR || packageJson.config.outputDir || "../_site",
  urlPrefix: process.env.URL_PREFIX || packageJson.config.urlPrefix || "",
};

const IGNORED_DIRS = [
  config.outputDir,
  ".git",
  "node_modules",
  ".gradle",
  "gradle",
  "build",
  "bin",
  "temp",
  "tmp",
];

const dateFormat = new Intl.DateTimeFormat("en-US", {
  hour: "numeric",
  minute: "numeric",
  second: "numeric",
  hour12: true,
});

async function runMarp(...args) {
  const argv = (args || []).concat(config.marpit.marpArgs);
  debug("Marp CLI args", argv);

  const exitCode = await marpCli(argv);
  return exitCode != 0 ? Promise.reject(exitCode) : Promise.resolve();
}

/** Get the file path, but relative to the current directory */
function relativeToHere(filePath) {
  return path.relative(path.dirname(""), filePath);
}

function isFileInDir(filePath, dirPath) {
  const normalizedFilePath = path.normalize(filePath);
  const normalizedDirPath = path.normalize(dirPath);

  const relativePath = path.relative(normalizedDirPath, normalizedFilePath);

  return !relativePath.startsWith("..") && !path.isAbsolute(relativePath);
}

/** Print a string to the standard out */
function log(...args) {
  const timestamp = dateFormat.format(new Date());

  const { name } = path.parse(process.argv[1]);
  if (name != "index") {
    console.log(timestamp, `${name}:`, ...args);
  } else {
    console.log(timestamp, ...args);
  }
}

/** If --debug was passed on the command line, print a message to the stdout */
function debug(...args) {
  if (!process.argv.includes("--debug")) {
    return;
  }
  log(...args);
}

/** Return true if any part of the path starts with a dot (except '.' and '..') */
function fileIsHidden(filePath) {
  const startsWithDot = (s) => s[0] == ".";
  return (
    path
      .normalize(filePath)
      .split(path.sep)
      .filter((part) => part != "." && part != "..")
      .filter(startsWithDot).length > 0
  );
}

/** The core function for ignoring paths, used by the watcher & makeConvertAll() */
function ignorePath(dirOrFilePath) {
  return (
    fileIsHidden(dirOrFilePath) ||
    IGNORED_DIRS.filter((dir) => isFileInDir(dirOrFilePath, dir)).length > 0
  );
}

/** Create a convertAll() function by iterating through files in the input dirs */
function makeConvertAll(shouldConvert, convertFile, ...inputDirs) {
  return () => {
    debug("in synthetic convertAll for dirs", inputDirs);
    return Promise.all(
      inputDirs.map(async (inputDir) => {
        const fileNames = await readdir(inputDir, { recursive: true });

        const filePathsToConvert = fileNames
          .map((name) => path.join(inputDir, name))
          .filter((filePath) => !ignorePath(filePath))
          .filter(shouldConvert);

        debug("filePathsToConvert", filePathsToConvert);

        return Promise.all(
          filePathsToConvert.map(async (filePath) => [
            filePath,
            await lstat(filePath),
          ])
        ).then((items) =>
          items
            .filter(([_, stats]) => stats.isFile())
            .map(([filePath, _]) => filePath)
            .map(relativeToHere)
            .map(convertFile)
        );
      })
    );
  };
}

module.exports = {
  config,
  log,
  debug,
  relativeToHere,
  isFileInDir,
  fileIsHidden,
  ignorePath,
  runMarp,
  makeConvertAll,
};
