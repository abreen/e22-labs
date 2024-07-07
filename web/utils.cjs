const path = require("path");
const { marpCli } = require("@marp-team/marp-cli");

const packageJson = require("./package.json");
const config = {
  ...packageJson.config,
  outputDir: process.env.OUTPUT_DIR || "../_site",
  urlPrefix: process.env.URL_PREFIX || "",
};

async function runMarp(...args) {
  const argv = (config.marpit.marpArgs || []).concat(args);

  const exitCode = await marpCli(argv);
  return exitCode != 0 ? Promise.reject(exitCode) : Promise.resolve();
}

/** Get the file path, but relative to the current directory */
function relativeToHere(filePath) {
  return path.relative(path.dirname(""), filePath);
}

/** Print a string to the standard out */
function log(str) {
  const scriptName = path.basename(process.argv[1]);
  console.log(`${scriptName}:`, str);
}

module.exports = {
  config,
  log,
  relativeToHere,
  runMarp,
};
