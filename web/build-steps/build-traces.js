// run Gradle
// unzip & convert .trace.json to htmx
// from .trace.json files, generate Markdown files
// run Marp to convert Markdown to slides (no PDFs)

// TODO use AdditionalArguments for runMarp to change --output

import path from "path";
import { promisify } from "util";
import child_process from "child_process";
import { config, runMarp } from "../utils.js";

const exec = promisify(child_process.exec);

function shouldConvert(relativePath) {
  return relativePath.startsWith("../lab") && relativePath.endsWith(".java");
}

/** Given a path to a .java file that changed, do `gradle run` and generate traces */
function convertFile(relativePath) {
  // "../lab2-solution/src/main/java/App.java" -> "lab2-solution"
  const subproject = relativePath.split(path.sep)[1];

  if (!config.traces.gradleSubprojects.includes(subproject)) {
    console.warn(
      `changed subproject "${subproject}" not in config.traces.gradleSubprojects`
    );
    return Promise.resolve();
  }

  return exec(`pushd .. && ./gradlew :${changedSubproject}:run`);
  // TODO read the ZIP files and create the htmx files using a template (?)
}

/** Does `gradle run` and generates all traces from subprojects listed in package.json */
function convertAll() {
  const subprojects = config.traces.gradleSubprojects || [];
  console.log(subprojects);
  const tasks = subprojects.map((proj) => `:${proj}:run`);
  console.log(tasks);
  console.log(`pushd .. && ./gradlew --parallel ${tasks.join(" ")}`);

  return Promise.resolve();
  // TODO read the ZIP files and create the htmx files using a template (?)
}

async function generateTraces() {}

function tracesToHypertext() {}

function traceToHypertext(path) {}

export default { shouldConvert, convertFile, convertAll };
