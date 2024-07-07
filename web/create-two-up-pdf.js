import coherentpdf from "coherentpdf";

const inputFilePath = process.argv[2];
const outputFilePath = process.argv[3];

const pdf = coherentpdf.fromFile(inputFilePath, "");
log(`opened ${inputFilePath}, transforming PDF...`);
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
log(`wrote two-up PDF to ${outputFilePath}`);
coherentpdf.deletePdf(pdf);

function log(str) {
  console.log("coherentpdf:", str);
}
