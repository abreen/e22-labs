Here are some known limitations of this solution:

- The `todos.txt` file does not store the "done" state for a todo item
- Although `TodoStorage` takes the file name as a parameter, there
  currently is no way to change it from `todos.txt`
- No way to change the location of `todos.txt` on the hard drive
- Instead of a CLI, a graphical user interface could be implemented
- If todos are added and then deleted, we end up with lots of `null`s
  in the array
- Not keeping track of the number of todos in the array

If you have trouble running the app using Gradle, for example if you
get an error like this:

    Error: Could not find or load main class App
    Caused by: java.lang.ClassNotFoundException: App

You can run the app with this simple command line (assuming Gradle
compiled the code into a `bin/` directory):

    java -cp bin/main App

The `-cp` option adds the compiled `.class` files from `bin/main`
in the Java classpath so that the `App` class can be found.
