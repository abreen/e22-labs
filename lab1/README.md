A to-do list app that keeps list items in an array.

The app must offer these features:

- Add a to-do list item
- Mark a to-do list item as done
- Delete a to-do list item

Some questions to ask:

- What state does my application manage?
- What are the possible ways the state can change?

The test-driven development (TDD) methodology:

Starting from stable code that is covered by tests,

1. add a **failing** test case
2. write only the code that makes it pass again
3. refactor as needed
4. go to 1

We can build all the features of `TodoApp` with confidence that
our code is free of bugs, without needing a `main()` method and
without tedious manual testing.
