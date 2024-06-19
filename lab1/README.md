# Lab 1

The test-driven development (TDD) methodology:

Starting from stable code that is covered by tests,

1. add a **failing** test case
2. write only the code that makes it pass again
3. refactor as needed
4. go to 1


## The `ArrayBag` class

Examine each part of the `ArrayBag` class. Be sure to notice:

- In `contains()`, we must use `equals()` instead of `==` for references
- In `contains()`, the loop bound must be `numItems`, not `items.length`
- `contains()` must return `false` outside of the loop

Now write an implementation of `remove()`. To avoid leaving "holes" in the
array, removing an item involves shifting all the items to the left by one
position:

```
remove 2:
+---+---+---+---+
| 4 | 2 | 9 | 6 |
+---+---+---+---+
         /   /
        v   v
  +---+---+---+---+
  | 4 | 9 | 6 | 0 |
  +---+---+---+---+
```

In this case, we changed array index 1 to have the value 9. Then we changed
array index 2 to have the value 6. Then we set index 3 to zero.

Now write a constructor for the class that accepts another `ArrayBag` and copies
its references into a new bag's array.


## Bonus: to-do list app

Build a to-do list app that keeps list items in an array.

The app must offer these features:

- Add a to-do list item
- Mark a to-do list item as done
- Delete a to-do list item

Some questions to ask:

- What state does my application manage?
- What are the possible ways the state can change?

Try to use test-driven development (TDD) to build each
feature without needing to write code in `main()` that
interacts with a user.
