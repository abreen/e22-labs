# `common`: functionality shared across projects

This Gradle subproject is included by each of the `lab*` Gradle projects.
See the `dependencies` block in `buildSrc/shared.gradle`.

## `TutorTrace`

Inspired by [Python Tutor][python-tutor], `TutorTrace.java` is a custom Java
debugger that generates a line-by-line trace of a program.

- It uses the [Java Debug Interface][jdi] (JDI) to pause execution of a
  program and generate a snapshot of the stack & heap, line-by-line
  - The JDI is the same API that debuggers and IDEs use

- It requires the target program to be compiled with the `-g` option (this
  allows the JDI to read line numbers and the names of methods and variables)
  - The `build.gradle` file in this directory contains a line that adds this
    option for the "build" task

- Uses the `Json` class to save the snapshots into JSON string format
  - The `.json` file is read by the `build-slides.js` script to generate
    a presentation slide for each step of the trace


[python-tutor]: https://pythontutor.com/render.html#mode=edit
[jdi]: https://docs.oracle.com/javase/8/docs/jdk/api/jpda/jdi/
