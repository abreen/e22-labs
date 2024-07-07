import { readdir } from "fs/promises";
import { fork } from "child_process";
import path from "path";
import { config, runMarp } from "../utils.cjs";

const slidesOutputDir = path.join(config.outputDir, "slides");

function shouldConvert(relativePath) {
  return relativePath.startsWith("slides/");
}

async function convertFile(relativePath) {
  const noExt = path.parse(relativePath).name;

  // generate the HTML file
  await runMarp(relativePath, "--output", slidesOutputDir);

  // generate the PDF file
  await runMarp(relativePath, "--pdf", "--output", slidesOutputDir);

  // get the path to the PDF we just generated
  const pdfFilePath = path.join(slidesOutputDir, noExt + ".pdf");
  const twoUpFilePath = path.join(slidesOutputDir, noExt + "-2up.pdf");

  return createTwoUpPdf(pdfFilePath, twoUpFilePath);
}

function convertAll() {
  return Promise.all([
    generateHtml(slidesOutputDir),
    generatePdfsAndTwoUpPdfs(slidesOutputDir),
  ]);
}

async function generatePdfsAndTwoUpPdfs(slidesOutputDir) {
  // generate all the PDFs
  await generatePdfs(slidesOutputDir);

  // for each PDF, generate a two-up version (in parallel)
  const files = await readdir(slidesOutputDir);
  return Promise.all(
    files
      .filter((filePath) => filePath.endsWith(".pdf"))
      .filter((filePath) => !filePath.endsWith("-2up.pdf"))
      .map((filePath) => path.parse(filePath).name)
      .map((name) => ({
        input: path.join(slidesOutputDir, name + ".pdf"),
        output: path.join(slidesOutputDir, name + "-2up.pdf"),
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
  return new Promise((resolve, reject) => {
    const child = fork("create-two-up-pdf.js", [...args], {});
    child.on("error", reject);
    child.on("exit", (exitCode) => {
      if (exitCode != 0) {
        reject(exitCode);
      } else {
        resolve();
      }
    });
  });
}

export default { shouldConvert, convertFile, convertAll };
