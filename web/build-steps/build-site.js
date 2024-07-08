import { readFile, writeFile, readdir } from "fs/promises";
import path from "path";
import pLimit from "p-limit";
import ejs from "ejs";
import { common, createStarryNight } from "@wooorm/starry-night";
import { toHtml } from "hast-util-to-html";
import markdownit from "markdown-it";
import { config } from "../utils.cjs";

const limitReadFile = pLimit(5);

const starryNight = await createStarryNight(common);

const md = markdownit({
  ...config.markdownit.options,
  highlight: function (str, lang) {
    if (lang) {
      try {
        const scope = starryNight.flagToScope(lang);
        return toHtml(starryNight.highlight(str, scope));
      } catch (error) {
        console.error(error);
      }
    }

    return "";
  },
});

function shouldConvert(relativePath) {
  return relativePath.startsWith(config.markdownit.inputDir);
}

async function convertFile(relativePath) {
  const { dir, name } = path.parse(relativePath);
  const outputFilePath = path.join(dir, name + ".html");

  const data = {
    prefix: config.urlPrefix,
  };

  const ejsText = await limitReadFile(() =>
    readFile(relativePath, { encoding: "utf8" })
  );
  const markdown = ejs.render(ejsText, data);
  const html = md.render(markdown);

  return writeFile(
    path.join(
      config.outputDir,
      path.relative(config.markdownit.inputDir, outputFilePath)
    ),
    html
  );
}

async function convertAll() {
  const fileNames = await readdir(config.markdownit.inputDir);
  return Promise.all(
    fileNames
      .map((name) => path.join(config.markdownit.inputDir, name))
      .map(convertFile)
  );
}

export default { shouldConvert, convertFile, convertAll };
