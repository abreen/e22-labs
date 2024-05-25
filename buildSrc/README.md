The `buildSrc/` directory contains the custom plugin that processes `.java.md`
files into `.java` files. It also contains the shared logic imported from
each lab's `build.gradle` file.

[This directory is treated specially by Gradle][buildSrc], it's processed
before the actual building begins.

[buildSrc]: https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources
