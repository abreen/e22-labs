import coherentpdf from "coherentpdf";
import { log } from "./utils.cjs";

const inputFilePath = process.argv[2];
const outputFilePath = process.argv[3];

const pdf = coherentpdf.fromFile(inputFilePath, "");

log(`transforming ${inputFilePath}...`);
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
coherentpdf.toFile(pdf, outputFilePath, false, false);
log(`saved two-up verson of PDF to ${outputFilePath}`);
