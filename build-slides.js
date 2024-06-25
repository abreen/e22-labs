const { marpCli, waitForObservation } = require("@marp-team/marp-cli");
const coherentpdf = require("coherentpdf");
const {
  existsSync: fileExists,
  mkdirSync: makeDirectory,
  writeFileSync: writeFile,
} = require("fs");

const URL_PREFIX = "/e22-labs/";

const labsWithSlides = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11].filter((n) =>
  fileExists(`slides/lab${n}.md`)
);

function makeLinks(nums) {
  return nums.map((n) => {
    const htmlAnchor = `<a href="${URL_PREFIX}/lab${n}.html">Lab ${n}</a>`;
    const pdfAnchor = `<a href="${URL_PREFIX}/lab${n}.pdf">PDF (one slide per page)</a>`;
    const pdf2UpAnchor = `<a href="${URL_PREFIX}/lab${n}-2up.pdf">PDF (two slides per page)</a>`;

    return `${htmlAnchor}<br />${pdfAnchor}<br />${pdf2UpAnchor}`;
  });
}

const links = makeLinks(labsWithSlides);
const body = `<ul>${links.map((link) => `<li>${link}</li>`)}</ul>`;

writeFile("_site/index.html", makeIndexPage(body));

function runMarp(additionalArgs) {
  const argv = [
    isWatchMode() ? "--watch" : null,
    "--theme",
    "gerard",
    "--theme-set",
    "gerard.css",
    "--input-dir",
    "slides/",
    "--output",
    "_site/",
  ]
    .filter((x) => x != null)
    .concat(additionalArgs);

  console.log(argv);

  marpCli(argv)
    .then((exitCode) => console.log(`Done with exit code ${exitCode}`))
    .catch(console.error);
}

runMarp(["--pdf"]);
runMarp(["--pdf"]);

waitForObservation().then(({ stop }) => {
  console.log("Observed");

  // Stop observations to resolve marpCli()'s Promise
  stop();
});

//function convertToHtml(n) {
//  const fileName = `./lab${n}/slides.md`;
//  const argv = [
//    isWatchMode() ? "--watch" : null,
//    "--html",
//    "true",
//    "--output",
//    `_site/lab${n}.html`,
//    "--theme",
//    "gerard",
//    "--theme-set",
//    "gerard.css",
//    "--",
//    fileName,
//  ].filter((x) => x != null);
//
//  return marpCli(argv).then((exitStatus) => {
//    if (exitStatus > 0) {
//      return Promise.reject(n);
//    } else {
//      return Promise.resolve(n);
//    }
//  });
//}
//
//function convertToPdf(n) {
//  const fileName = `./lab${n}/slides.md`;
//  const pdfFileName = `_site/lab${n}.pdf`;
//  const argv = [
//    isWatchMode() ? "--watch" : null,
//    "--html",
//    "true",
//    "--pdf",
//    "--pdf-notes",
//    "--pdf-outlines",
//    "--output",
//    pdfFileName,
//    "--theme",
//    "gerard",
//    "--theme-set",
//    "gerard.css",
//    "--",
//    fileName,
//  ].filter((x) => x != null);
//
//  return marpCli(argv)
//    .then((exitStatus) => {
//      if (exitStatus > 0) {
//        return Promise.reject(pdfFileName);
//      } else {
//        return Promise.resolve([n, pdfFileName]);
//      }
//    })
//    .then(([n, pdfFileName]) => {
//      const pdf = coherentpdf.fromFile(pdfFileName, "");
//      coherentpdf.impose(
//        pdf,
//        1.0,
//        2.0,
//        false,
//        false,
//        false,
//        false,
//        false,
//        150.0,
//        150.0,
//        2.0
//      );
//      coherentpdf.scaleToFitPaper(
//        pdf,
//        coherentpdf.all(pdf),
//        coherentpdf.usletterportrait,
//        1.0
//      );
//      coherentpdf.toFile(pdf, `_site/lab${n}-2up.pdf`, false, false);
//      coherentpdf.deletePdf(pdf);
//    });
//}

function makeIndexPage(body) {
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

function isWatchMode() {
  return process.argv.length > 2 && ["-w", "--watch"].includes(process.argv[2]);
}
