# An example command line interface

This class implements a command line interface (CLI) for the
to-do list app. It handles the details of user interaction
over a terminal using the `Scanner` class.

```java
import java.util.Scanner;

public class CmdLine {
```

Our class will offer one public method that takes
an `InputStream` object (for example, `System.in` which is
attached to the terminal) and an instance of `TodoApp` on which
to call methods on behalf of the user.

```java
public void run(InputStream in, TodoApp app) {
```

In a loop, we process one line at a time from the user.
We'll define a structure for the command lines like this:

    <command> <arg1> <arg2> ... <argN>

Each part of the command line is separated by spaces.
The first part is the command, which must be one of:
`add`, `done`, `delete`, `quit`.

For `add`, all the arguments are used together as the
description for the list item (e.g., `add Buy eggs and milk`).

For `done` and `delete`, only `<arg1>` is used. It should be
the index into the array of list items.

```java
    Scanner s = new Scanner(in);
    while (s.hasNextLine()) {
        String command = s.next();
        String args = s.nextLine();
```

Based on the given command, we'll call the appropriate
method of `TodoApp`. If the command is unknown, we'll
print an error message.

```java
        if (command.equals("add")) {
            app.addTodo(args);
        } else if (command.equals("done")) {
            int index = Integer.parseInt(args);
            app.markDone(index);
        } else if (command.equals("delete")) {
            int index = Integer.parseInt(args);
            app.deleteTodo(index);
        } else if (command.equals("quit")) {
            break;
        } else {
            System.err.println(
                "unknown command: " + command
            );
        }
```

Before ending the loop and waiting for more input, we'll
print the list items:

```java
       app.printList();
    }
```

If we make it outside the loop, `hasNextLine()` returned `false`
(indicating the terminal was closed) or the user supplied
the `quit` command.

```java
    System.out.println("Goodbye!");
    s.close();
}
```
