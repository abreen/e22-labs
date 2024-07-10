import { readdir } from "fs/promises";
import { fork } from "child_process";
import path from "path";
import pDebounce from "p-debounce";
import { config, isFileInDir, runMarp } from "../utils.cjs";

const slidesOutputDir = path.join(config.outputDir, "slides");

function shouldConvert(relativePath) {
  const { ext } = path.parse(relativePath);
  return (
    [".md", ".markdown"].includes(ext) &&
    isFileInDir(relativePath, config.marpit.slidesDir)
  );
}

async function convertFile(relativePath) {
  const fromSlides = path.relative(config.marpit.slidesDir, relativePath);
  const { name } = path.parse(fromSlides);

  const htmlPath = path.join(slidesOutputDir, name + ".html");
  const pdfPath = path.join(slidesOutputDir, name + ".pdf");

  await runMarp(relativePath, "--output", htmlPath);
  await runMarp(relativePath, "--pdf", "--output", pdfPath);

  const pdf2UpPath = path.join(slidesOutputDir, name + "-2up.pdf");
  return createTwoUpPdf(pdfPath, pdf2UpPath);
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

export default {
  shouldConvert,
  convertFile: pDebounce(convertFile, 1000),
  convertAll,
};
