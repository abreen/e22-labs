import { writeFile } from "fs/promises";
import path from "path";
import { render } from "ejs";
import { config } from "../utils.js";

function renderTemplates() {
  let people = ["geddy", "neil", "alex"];
  let html = render('<%= people.join(", "); %>', { people: people });
  console.log(html);
}

function shouldConvert(relativePath) {
  // always rebuild the HTML for the site
  return true;
}

function convertFile(relativePath) {
  let people = ["geddy", "neil", "alex"];
  let html = render('<%= people.join(", "); %>', { people: people });

  return writeFile(path.join(config.outputDir, "index.html"), html);
}

function convertAll() {
  return convertFile(null);
}

export default { shouldConvert, convertFile, convertAll };
