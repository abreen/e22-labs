import path from "path";
import { marpCli } from '@marp-team/marp-cli';
import packageJson from "./package.json" with { type: "json" };

export const config = {
    ...packageJson.config,
    outputDir: process.env.OUTPUT_DIR || "../_site",
    urlPrefix: process.env.URL_PREFIX || ""
};

export function runMarp(...args) {
    const argv = (config.marpit.marpArgs || [])
        .concat(args);

    return marpCli(argv).then((exitCode) => {
        console.log(exitCode);
        return exitCode != 0 ? Promise.reject() : Promise.resolve();
    }
    );
}

export function relativeToHere(filePath) {
    return path.relative(path.dirname(""), filePath);
}
