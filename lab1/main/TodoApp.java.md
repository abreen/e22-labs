# To-do list app

## Building the `main()` method

We'll use the `Scanner` class from `java.util` to handle input
from the user.

    :::java
    import java.util.Scanner;

Let's start with a Java class to hold our `main()` method:

    :::java
    public class TodoApp {

Our `main()` method will create a `Scanner` object attached to the
standard in (the terminal).

    :::java
    public static void main(String[] args) {
        Scanner lines = new Scanner(System.in);

We're calling it `lines` since we're only going to use the two
methods of `Scanner` that give us each line of user input.

    :::java command-loop
    while (lines.hasNextLine()) {
        String command = lines.nextLine();
    }

This `while` loop allows us to handle any number of user input
lines repeatedly.

### Handling user input

We'll give the user a command-based interface to the application.
The first word of each line will be interpreted as a command.

For example, if the user types the line `"add Buy eggs and milk"`
our application will create a new to-do item `"Buy eggs and milk"`.

    :::java command-loop
    while (lines.hasNextLine()) {
        String command = lines.nextLine();

        String[] words = command.split("\\s+");
        if (words[0].equals("add")) {
            // add to-do list item
        } else if (words[0].equals("quit")) {
            break;
        }
    }

We'll close the class definition:

    :::java
    }
