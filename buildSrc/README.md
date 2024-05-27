The `buildSrc/` directory contains the shared logic imported from each
subdirectory's `build.gradle` file.

[This directory is treated specially by Gradle][buildSrc], it's processed
before the actual building begins.

[buildSrc]: https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources
