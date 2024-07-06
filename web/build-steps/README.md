These scripts were written with these principles in mind:

- Import any constants or configurable values through `package.json`
- Don't pass around huge strings
- Functions should do filesystem work immediately when called
- All file paths can be relative and `index.js` always runs in the same dir as `package.json`
- `package.json` specifies a single output directory for everything
- Use `async` as much as possible (`fs/promises`, `Promise` for Marp CLI)
- Parallelize work as much as possible
  - Gradle builds and runs can be parallel with `./gradlew --parallel :lab2-solution:run :lab3-solution:run`
- Build logic inside event handler (file change event) in `index.js`
  - When a file changes, do the minimum amount of work
- Use EJS templates if needed, but keep them small
- `index.js` reads `package.json` and passes config and other deps to function
- The default function takes a path for the file that changed
