Each module in this directory is a *converter* which exports three functions:

* `shouldConvert(filePath)` returns `true` if the converter cares about the file
  located at `filePath` (e.g., return `true` if it ends with `.md` for a Markdown
  converter)
* `convertFile(filePath)` does the necessary file operations/runs subprocesses
  to convert the `filePath` into an output file in `OUTPUT_DIR`
* `convertAll()` converts all the files into `OUTPUT_DIR`

All of the functions may return `Promise`s. They get configuration values from
`package.json`.

The converters were written with these principles in mind:

- Prioritize speed: use `Promise.all()`, subprocesses, parallelization
  - Use `./gradlew --parallel`
- Maximize use of config in `package.json`
- Use env var for the output path (`process.env.OUTPUT_DIR`)
- Don't pass around huge strings, do everything with files/subprocesses
- Use relative paths for all the file watching logic
- When a file changes, do the minimum amount of work (allow converters to skip
  conversion if they don't care about a file that changed)
