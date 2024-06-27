const { marpCli, waitForObservation } = require("@marp-team/marp-cli");
const coherentpdf = require("coherentpdf");
const {
  existsSync: fileExists,
  mkdirSync: makeDirectory,
  writeFileSync: writeFile,
} = require("fs");

const INPUT_DIR = process.env.INPUT_DIR || "slides";
const OUTPUT_DIR = process.env.OUTPUT_DIR || "_site";
const URL_PREFIX = process.env.URL_PREFIX || "";

makeDirectory(OUTPUT_DIR, { recursive: true });

const labsWithSlides = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11].filter((n) =>
  fileExists(`${INPUT_DIR}/lab${n}.md`)
);

makeIndexPage(labsWithSlides);

const args = process.argv.slice(2);
if (args.includes("-w") || args.includes("--watch")) {
  // save resources and run quicker by not generating PDFs
  runMarp(args);
} else {
  runMarp(args);
  runMarp(args, "--pdf").then(() => {
    labsWithSlides
      .map((n) => `${OUTPUT_DIR}/lab${n}`)
      .forEach(generateTwoUpPdf);
  });
}

function makeIndexPage(nums) {
  const links = nums.map((n) => {
    const htmlAnchor = `<a href="${URL_PREFIX}/lab${n}.html">Lab ${n}</a>`;
    const pdfAnchor = `<a href="${URL_PREFIX}/lab${n}.pdf">PDF (one slide per page)</a>`;
    const pdf2UpAnchor = `<a href="${URL_PREFIX}/lab${n}-2up.pdf">PDF (two slides per page)</a>`;

    return `${htmlAnchor}<br />${pdfAnchor}<br />${pdf2UpAnchor}`;
  });

  const body = `<ul>${links.map((link) => `<li>${link}</li>`)}</ul>`;

  writeFile("_site/index.html", renderIndexPage(body));
}

function runMarp(args = [], ...additionalArgs) {
  const argv = args
    .concat([
      "--html",
      "--theme",
      "gerard",
      "--theme-set",
      "gerard.css",
      "--input-dir",
      "slides/",
      "--output",
      "_site/",
    ])
    .concat(additionalArgs);

  return marpCli(argv).then((exitCode) =>
    exitCode != 0 ? Promise.reject() : Promise.resolve()
  );
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

function renderIndexPage(body) {
  return `<!doctype html>
<html>
  <head>
    <title>Labs</title>
    <style>
      body {
        font-family: -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI",
          Roboto, Ubuntu;
      }
      a {
        color: inherit;
        text-decoration: none;
      }
      a:hover {
        text-decoration: underline;
      }
      @media (prefers-color-scheme: dark) {
        body {
          background: #222;
          color: white;
        }
      }
    </style>
  </head>
  <body>
    <h1>Labs</h1>
    ${body}
  </body>
</html>
`;
}
