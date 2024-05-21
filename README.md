This document accompanies the source code for CSCI S-22 lab exercises.

# CSCI S-22

This course covers fundamental data structures and classic algorithms using Java.

## Schedule

Lectures take place over Zoom, on Mondays and Wednesdays. There are optional
one-hour section meetings on Tuesdays and Thursdays that attempt to enrich the
slides covered in lecture with practical coding examples.

| week    | date     | lecture/lab      | topics |
|---------|----------|------------------|--------|
| week 1  | June 24  | Lecture 1        | Intro, ADTs and object-oriented programming |
| *Tue*   | June 25  | Lab 1            | To-do list, test-driven development |
| *Wed*   | June 26  | Lecture 2        | Recursion and backtracking |
| *Thu*   | June 27  | Lab 2            | Debuggers, combination sum |
| week 2  | July 1   | Lecture 3        | Sorting and algorithm analysis (1/2) |
| *Tue*   | July 2*  | Lab 3            | Music playlists? |
| *Wed*   | July 3   | Lecture 4        | Sorting and analysis (2/2), linked lists (1/2) |
| *Thu*   | July 4   | Lab 4 (recorded) | video: `partition()` trace, merge sort trace |
| week 3  | July 8   | Lecture 5        | Linked lists (2/2); lists, stacks & queues (1/2) |
| *Tue*   | July 9*  | Lab 5            | Command pattern, undo & redo |
| *Wed*   | July 10  | Lecture 6        | Lists, stacks and queues (2/2) |
| *Thu*   | July 11  | *Review session* | Practice midterm exam |
| week 4  | July 15  | *Midterm exam*   |  |
| *Tue*   | July 16  | Lab 6            | Something with queues? |
| *Wed*   | July 17  | Lecture 7        | Binary trees, Huffman encoding |
| *Thu*   | July 18  | Lab 7            | `ExprTree` class, something else? |
| week 5  | July 22  | Lecture 8        | Binary search trees, balanced search trees |
| *Tue*   | July 23* | Lab 8            | Mini DB app? Strategy pattern for linear vs. binary |
| *Wed*   | July 24  | Lecture 9        | Heaps and priority queues |
| *Thu*   | July 25  | Lab 9            | Keeping data in order? Out of order packets? |
| week 6  | July 29  | Lecture 10       | Hash tables, graphs (1/2) |
| *Tue*   | July 30* | Lab 10           | Something with tables? |
| *Wed*   | July 31  | Lecture 11       | Graphs (2/2) |
| *Thu*   | Aug 1    | Lab 11           | Maze solver? |
| week 7  | Aug 5    | *Review session* | Practice final exam |
| *Tue*   | Aug 6    |                  |  |
| *Wed*   | Aug 7    | *Final exam*     |  |

\* Indicates a problem set due date


## Lab exercises

Except for pre-recorded section meetings, each one-hour meeting will be mostly spent
writing code. In the first 10 minutes, we'll review the topics from the last lecture.
Then we'll discuss the code for the lab exercise and hack on it until the hour is over.
You can consult me, your fellow students, and the solution freely during the hour.


### How to use

First, install Gradle. Using [SDKMAN](https://sdkman.io/) is recommended, which
involves these steps:

0. If you use Windows, install the [Windows Subsystem for Linux][wsl]
1. Install SDKMAN using the instructions on their site (or just do `curl -s "https://get.sdkman.io" | bash`)
2. Install Gradle using the `sdk` command: `sdk install gradle 8.7`
3. Verify the installation by doing `gradle -v` (you should see a version number)
4. Get this source code (download it from GitHub, or do `git clone https://github.com/abreen/s22-labs.git`)
5. If you use VS Code, install the [Gradle extension][gradle-extension]

You can also use SDKMAN to download and switch between different versions of Java for
local development.


#### Build, test, and run

Using the `gradle` command (or the Gradle extension in your editor), use any of
these "run" tasks to compile and run the code for the appropriate lab:

    gradle :lab1:run
    gradle :lab2:run
    gradle :lab3:run
    ...

Or if your current directory is `lab3/` (for example), you can do `gradle run`.

Other than `run`, there are other tasks available:

* To run our solution, do `gradle :labX:run-solution`.
* To compile the code but not run it, use `gradle build` or `gradle :labX:build`.
* To run the tests, use `gradle test` or `gradle :labX:test`.


### Solutions

First, note that each directory contains one or more `.java.md` files which
contain our solution across multiple Java code blocks, explained step-by-step.

As mentioned earlier, you can run this solution using `gradle run-solution`.

An example of [literate programming][literate], the actual Java code is
built directly from these `.md` files.[^1]


### Why Gradle?

Knowing Gradle isn't required for this course, and it's not an official topic.
We don't use Gradle with the problem sets. However, it offers the following
benefits specifically for lab exercises:

* We can run the code for any lab exercise with one command
* It allows us to easily use open source libraries
* Microsoft built a nice [VS Code extension][gradle-extension] for it
* We use [toolchains][toolchains] to specify the Java language version



[^1]: To get a `.java` file from a `.java.md` file, the code blocks in
      the document are concatenated together, and everything else (e.g.,
      any paragraphs of explanatory text) is removed.
      (If more than one code block has the same
      [info string][info-string], only the last version is used. This
      allows us to iteratively build on an idea in the text, but only
      keep the final version.)


[wsl]: https://learn.microsoft.com/en-us/windows/wsl/install
[gradle-extension]: https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-gradle
[toolchains]: https://docs.gradle.org/current/samples/sample_jvm_multi_project_with_toolchains.html
[literate]: https://en.wikipedia.org/wiki/Literate_programming
[info-string]: https://spec.commonmark.org/0.31.2/#info-string