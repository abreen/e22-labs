import { writeFile } from "fs/promises";
import path from "path";
import { render } from "ejs";
import { config } from "../utils.js";

function renderTemplates() {
  let people = ["geddy", "neil", "alex"];
  let html = render('<%= people.join(", "); %>', { people: people });
  console.log(html);
}

async function filter(relativePath) {
  // always rebuild the HTML for the site
  return true;
}

async function convertFile(relativePath) {
  let people = ["geddy", "neil", "alex"];
  let html = render('<%= people.join(", "); %>', { people: people });

  await writeFile(path.join(config.outputDir, "index.html"), html);
}

async function convertAll() {
  return convertFile(null);
}

export default { filter, convertFile, convertAll };
