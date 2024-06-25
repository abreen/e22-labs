const { marpCli } = require("@marp-team/marp-cli");
const coherentpdf = require("coherentpdf");
const { existsSync: fileExists } = require("fs");
const fs = require("fs/promises");

fs.mkdir("./slides", { recursive: true })
  .then(
    Promise.all(
      [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        .filter((n) => fileExists(`./lab${n}/slides.md`))
        .map((n) => Promise.all([n, convertToHtml(n), convertToPdf(n)]))
    ).then((ns) => {
      const links = ns.map(([n, ...rest]) => {
        const htmlAnchor = `<a href="/e22-labs/lab${n}.html">Lab ${n}</a>`;
        const pdfAnchor = `<a href="/e22-labs/lab${n}.pdf">pdf</a>`;
        const pdf2UpAnchor = `<a href="/e22-labs/lab${n}-2up.pdf">2-up</a>`;

        return `${htmlAnchor} (${pdfAnchor}, ${pdf2UpAnchor})`;
      });

      const body = `<ul>${links.map((link) => `<li>${link}</li>`)}</ul>`;

      const html = makePage(body);
      return fs.writeFile("docs/index.html", html);
    })
  )
  .catch((error) => {
    console.error("error", error);
  });

function convertToHtml(n) {
  const fileName = `./lab${n}/slides.md`;
  const argv = [
    "--html",
    "true",
    "--output",
    `docs/lab${n}.html`,
    "--theme",
    "gerard",
    "--theme-set",
    "gerard.css",
    "--",
    fileName,
  ];

  return marpCli(argv).then((exitStatus) => {
    if (exitStatus > 0) {
      return Promise.reject(n);
    } else {
      return Promise.resolve(n);
    }
  });
}

function convertToPdf(n) {
  const fileName = `./lab${n}/slides.md`;
  const pdfFileName = `docs/lab${n}.pdf`;
  const argv = [
    "--html",
    "true",
    "--pdf",
    "--pdf-notes",
    "--pdf-outlines",
    "--output",
    pdfFileName,
    "--theme",
    "gerard",
    "--theme-set",
    "gerard.css",
    "--",
    fileName,
  ];

  return marpCli(argv)
    .then((exitStatus) => {
      if (exitStatus > 0) {
        return Promise.reject(pdfFileName);
      } else {
        return Promise.resolve([n, pdfFileName]);
      }
    })
    .then(([n, pdfFileName]) => {
      const pdf = coherentpdf.fromFile(pdfFileName, "");
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
      coherentpdf.toFile(pdf, `docs/lab${n}-2up.pdf`, false, false);
      coherentpdf.deletePdf(pdf);
    });
}

function makePage(body) {
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
<!-- generated ${new Date().toLocaleString()} -->
`;
}
