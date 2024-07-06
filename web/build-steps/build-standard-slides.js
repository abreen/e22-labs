// run Marp to convert Markdown to slides
// convert to HTML and PDF
// from .trace.json files, generate the slides

import path from "path";
import coherentpdf from "coherentpdf";
import { config, runMarp } from "../utils.js";

async function filter(relativePath) {
  return relativePath.startsWith("slides/");
}

async function convertFile(relativePath) {
  await runMarp(
    path,
    `--output ${relativePath.join(config.outputDir, "slides")}`
  );
}

async function convertAll() {
  await runMarp(
    `--input-dir ${config.marpit.slidesDir}`,
    `--output ${path.join(config.outputDir, "slides")}`
  );

  // TODO generate PDFs
}

function generateTwoUpPdf(pdfFileName) {
  const pdf = coherentpdf.fromFile(pdfFileName + ".pdf", "");
  coherentpdf.impose(
    pdf,
    1.0,
    2.0,
    false,
    false,
    false,
    false,
    false,
    150.0,
    150.0,
    2.0
  );
  coherentpdf.scaleToFitPaper(
    pdf,
    coherentpdf.all(pdf),
    coherentpdf.usletterportrait,
    1.0
  );
  coherentpdf.toFile(pdf, pdfFileName + "-2up.pdf", false, false);
  coherentpdf.deletePdf(pdf);
}

export default { filter, convertFile, convertAll };
