import { readFile, writeFile, mkdir } from "fs/promises";
import path from "path";
import pLimit from "p-limit";
import pDebounce from "p-debounce";
import ejs from "ejs";
import { common, createStarryNight } from "@wooorm/starry-night";
import { toHtml } from "hast-util-to-html";
import markdownit from "markdown-it";
import markdownItFrontMatter from "markdown-it-front-matter";
import ini from "ini";
import { log, config, isFileInDir, makeConvertAll } from "../utils.cjs";

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

// this is very odd, but it seems markdown-it doesn't have a good API
let lastParsedFrontMatter = null;
md.use(markdownItFrontMatter, (frontMatter) => {
  try {
    lastParsedFrontMatter = ini.parse(frontMatter);
  } catch (err) {
    lastParsedFrontMatter = null;
  }
});

function markdownitRender(...args) {
  // reset state (yuck)
  lastParsedFrontMatter = null;
  return md.render(...args);
}

function shouldConvert(relativePath) {
  const { ext } = path.parse(relativePath);
  return (
    isTemplateFile(relativePath) ||
    ([".md", ".markdown"].includes(ext) &&
      isFileInDir(relativePath, config.markdownit.inputDir))
  );
}

async function convertFile(relativePath) {
  if (isTemplateFile(relativePath)) {
    // rebuild the entire site if the template file changes
    log("template file changed, rebuilding site");
    return convertAll();
  }

  // treat the .md file like an EJS template, then render it
  const pageEjs = await limitReadFile(() =>
    readFile(relativePath, { encoding: "utf8" })
  );
  const pageMarkdown = ejs.render(pageEjs, { prefix: config.urlPrefix });
  const pageHtml = markdownitRender(pageMarkdown);

  // insert the HTML into the template file
  const templateEjs = await readFile(
    path.join(config.markdownit.inputDir, "template.ejs"),
    { encoding: "utf8" }
  );

  const html = ejs.render(templateEjs, {
    ...(lastParsedFrontMatter || {}),
    content: pageHtml,
    prefix: config.urlPrefix,
  });

  // save the file
  const { dir, name } = path.parse(relativePath);
  const outputFileName = path.join(dir, name + ".html");

  const outputPath = path.join(
    config.outputDir,
    path.relative(config.markdownit.inputDir, outputFileName)
  );

  await mkdir(path.parse(outputPath).dir, { recursive: true });

  log(`md => html: ${relativePath} => ${outputPath}`);
  return writeFile(path.join(config.outputDir, outputPath), html);
}

function isTemplateFile(filePath) {
  return path.relative(config.markdownit.inputDir, filePath) == "template.ejs";
}

const convertAll = makeConvertAll(config.markdownit.inputDir, convertFile, {
  skip: ["template.ejs"],
});

export default {
  shouldConvert,
  convertFile: pDebounce(convertFile, 500),
  convertAll,
};
