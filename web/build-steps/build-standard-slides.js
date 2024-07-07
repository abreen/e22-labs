import { readdir } from "fs/promises";
import child_process from "child_process";
import path from "path";
import { promisify } from "util";
import { config, runMarp } from "../utils.js";

const fork = promisify(child_process.fork);

async function filter(relativePath) {
  return relativePath.startsWith("slides/");
}

async function convertFile(relativePath) {
  const slidesOutputDir = path.join(config.outputDir, "slides");
  const noExt = path.parse(relativePath).name;

  // generate the HTML file
  await runMarp(relativePath, "--output", slidesOutputDir);

  // generate the PDF file
  await runMarp(relativePath, "--pdf", "--output", slidesOutputDir);

  // get the path to the PDF we just generated
  const pdfFilePath = path.join(slidesOutputDir, noExt + ".pdf");
  const twoUpFilePath = path.join(slidesOutputDir, noExt + "-2up.pdf");

  await createTwoUpPdf(pdfFilePath, twoUpFilePath);
}

async function convertAll() {
  const slidesOutputDir = path.join(config.outputDir, "slides");

  await Promise.all([
    generateHtml(slidesOutputDir),
    generatePdfsAndTwoUpPdfs(slidesOutputDir),
  ]);
}

async function generatePdfsAndTwoUpPdfs(slidesOutputDir) {
  // generate all the PDFs
  await generatePdfs(slidesOutputDir);

  // for each PDF, generate a two-up version (in parallel)
  const files = await readdir(slidesOutputDir);
  await Promise.all(
    files
      .filter((filePath) => filePath.endsWith(".pdf"))
      .filter((filePath) => !filePath.endsWith("-2up.pdf"))
      .map((filePath) => path.parse(filePath).name)
      .map((name) => ({
        input: path.join(slidesOutputDir, name + ".pdf"),
        output: path.join(slidesOutputDir, name + "-2up.df"),
      }))
      .map(({ input, output }) => createTwoUpPdf(input, output))
  );
}

function generateHtml(slidesOutputDir) {
  return runMarp(
    "--input-dir",
    config.marpit.slidesDir,
    "--output",
    slidesOutputDir
  );
}

function generatePdfs(slidesOutputDir) {
  return runMarp(
    "--input-dir",
    config.marpit.slidesDir,
    "--pdf",
    "--output",
    slidesOutputDir
  );
}

/** Run separate script in a child process to create a two-up version of a PDF */
function createTwoUpPdf(...args) {
  return fork("create-two-up-pdf.js", [...args], {});
}

export default { filter, convertFile, convertAll };
