This document accompanies the source code for CSCI S-22 lab exercises.

# CSCI S-22

This course covers fundamental data structures and classic algorithms using Java.

## Schedule

Lectures take place over Zoom, on Mondays and Wednesdays. There are optional
one-hour section meetings on Tuesdays and Thursdays that attempt to enrich the
slides covered in lecture with practical coding examples.

| week   | date      | lecture/lab      | topics                                              |
| ------ | --------- | ---------------- | --------------------------------------------------- |
| week 1 | June 24   | Lecture 1        | Intro, ADTs and object-oriented programming         |
| _Tue_  | June 25   | Lab 1            | To-do list, test-driven development                 |
| _Wed_  | June 26   | Lecture 2        | Recursion and backtracking                          |
| _Thu_  | June 27   | Lab 2            | Debuggers, combination sum                          |
| week 2 | July 1    | Lecture 3        | Sorting and algorithm analysis (1/2)                |
| _Tue_  | July 2\*  | Lab 3            | Music playlists?                                    |
| _Wed_  | July 3    | Lecture 4        | Sorting and analysis (2/2), linked lists (1/2)      |
| _Thu_  | July 4    | Lab 4 (recorded) | video: `partition()` trace, merge sort trace        |
| week 3 | July 8    | Lecture 5        | Linked lists (2/2); lists, stacks & queues (1/2)    |
| _Tue_  | July 9\*  | Lab 5            | Command pattern, undo & redo                        |
| _Wed_  | July 10   | Lecture 6        | Lists, stacks and queues (2/2)                      |
| _Thu_  | July 11   | _Review session_ | Practice midterm exam                               |
| week 4 | July 15   | _Midterm exam_   |                                                     |
| _Tue_  | July 16   | Lab 6            | Something with queues?                              |
| _Wed_  | July 17   | Lecture 7        | Binary trees, Huffman encoding                      |
| _Thu_  | July 18   | Lab 7            | `ExprTree` class, something else?                   |
| week 5 | July 22   | Lecture 8        | Binary search trees, balanced search trees          |
| _Tue_  | July 23\* | Lab 8            | Mini DB app? Strategy pattern for linear vs. binary |
| _Wed_  | July 24   | Lecture 9        | Heaps and priority queues                           |
| _Thu_  | July 25   | Lab 9            | Keeping data in order? Out of order packets?        |
| week 6 | July 29   | Lecture 10       | Hash tables, graphs (1/2)                           |
| _Tue_  | July 30\* | Lab 10           | Something with tables?                              |
| _Wed_  | July 31   | Lecture 11       | Graphs (2/2)                                        |
| _Thu_  | Aug 1     | Lab 11           | Maze solver?                                        |
| week 7 | Aug 5     | _Review session_ | Practice final exam                                 |
| _Tue_  | Aug 6     |                  |                                                     |
| _Wed_  | Aug 7     | _Final exam_     |                                                     |

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

### Build, test, and run

Using the `gradle` command (or the Gradle extension in your editor), use any of
these "run" tasks to compile and run the code for the appropriate lab:

    gradle :lab1:run
    gradle :lab2:run
    gradle :lab3:run
    ...

Or if your current directory is `lab3/` (for example), you can do `gradle run`.

Other than `run`, there are other tasks available:

- To run our solution, do `gradle :labX:run-solution`.
- To compile the code but not run it, use `gradle build` or `gradle :labX:build`.
- To run the tests, use `gradle test` or `gradle :labX:test`.

#### Testing

As mentioned earlier, you can run tests using `gradle test`
or `gradle :labX:test`.

We use the industry-standard
[JUnit 5][junit] framework which assumes that test
code is located in a separate `test/` directory. Test code
should begin with these two `import` statements:

UPDATE FOR JUNIT 4 INSTEAD OF 5

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
```

Here is a brief summary of the imported members (your editor
or IDE can provide inline documentation):

- The `@Test` annotation: put this before test case (a method
  that calls your code and makes assertions)
- `assertNull(value)` will fail the test if the supplied value is
  not `null` (also see `assertNotNull()`)
- `assertTrue(booleanValue)` will fail the test if the supplied
  value is not `true`
- `assertFalse(booleanValue)` will fail the test if the supplied
  value is not `false`
- `assertEquals(expected, actual)` asks JUnit to compare the
  given values and if `actual` doesn't equal `expected`, the test
  fails (also see `assertNotEquals()`); note that the expected
  value should be passed **first**
- `assertThrows()` takes an `Executable` (a block of code) and
  fails the test if the code doesn't throw a specific exception
- `assertArrayEquals()` is like `assertEquals()`, but compares
  invidivual array elements

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

- We can run the code for any lab exercise with one command
- It allows us to easily use open source libraries
- Microsoft built a nice [VS Code extension][gradle-extension] for it
- We use [toolchains][toolchains] to specify the Java language version

[^1]:
    To get a `.java` file from a `.java.md` file, the code blocks in
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
[junit]: https://junit.org/junit5/
