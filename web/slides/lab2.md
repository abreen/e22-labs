---
marp: true
title: Lab 2
---

# Practice with recursion

Examine this partially written **recursive** method that takes a `String` and
returns a new `String` without any capital (upper case) letters.

```java
public static String removeCapitals(String str) {
    if (str == null)
        throw new IllegalArgumentException();

    if (str.equals(""))
        return "";

    String removedFromRest = _________________;
    ...
}
```

- `removeCapitals("ABCdef123 xyWZ")` should return what?
* `"def123 xy"`

---

# Recursive problem solving

Recursion involves breaking the input into smaller input:

```
"ABCdef123 xyWZ" -> 'A' + "BCdef123 xyWZ"
 "BCdef123 xyWZ" -> 'B' + "Cdef123 xyWZ"
  "Cdef123 xyWZ" -> 'C' + "def123 xyWZ"
   "def123 xyWZ" -> 'd' + "ef123 xyWZ"
```

Each call to `removeCapitals()` will have its own `String`.
We use `str.substring(1)` to chop the first character off.
When the base case is reached, an empty string is returned.

```
          "xyWZ" -> 'x' + "yWZ"
           "yWZ" -> 'y' + "WZ"
            "WZ" -> 'W' + "Z"
             "Z" -> 'Z' + ""
              "" -> base case
```

---

# Recursive problem solving

When writing a recursive method, ask yourself:

- What's the base case?
  * When `str` is the empty string
- What's the recursive subproblem?
  * Removing the capitals from the rest of the string
- What work do we need to do before returning?
  * Test whether the first character in the string is a capital
  * If it's capital, don't return it

---

# `removeCapitals()`

- Let's write the rest of the code together
- Create a test case for the input: `"ABCdef123 xyWZ"`
- Use the debugger to trace the recursive calls
  - Watch how the stack frames grow and shrink

* What is the total number of calls to process a string of length **n**?
  * One call per character, plus one more for the base case
  * Number in terms of **n** = **n** + 1

* Let's keep this in mind as we look at another recursive method

---

# The Fibonacci sequence

The Fibonacci sequence arises from a process of adding the previous two numbers
in the sequence:

> F<sub>0</sub> = 0  
> F<sub>1</sub> = 1  
> F<sub>n</sub> = F<sub>n &ndash; 1</sub> + F<sub>n &ndash; 2</sub>

This formula is simple enough to implement directly:

```java
public static long fib(int n) {
    if (n == 0 || n == 1)
        return n;

    return ____________________;
}
```

- What should the recursive case be?

---

# `fib()`

- Let's write `fib()` together
- Create test cases that cover F<sub>0</sub> through F<sub>5</sub>
- Use the debugger as before to trace the recursive calls

* What do you notice about the total number of calls?
  * For a call to `fib(4)`, the `fib()` method was called 9 times,
    not 4 or 5 times as you might expect
* Draw a call tree starting at `fib(4)`
* What about a larger input, like `fib(50)`?
  - The number of calls increases exponentially for the initial value of **n**.
    That means 10 **billion** method calls are made to calculate `fib(50)`!

---

# Fibonacci, but smarter

To improve this, we need a totally different approach.

Instead of keeping track of every F<sub>n</sub> on the stack, we only need to
keep the previous **two** Fibonacci values to compute the next one.

```java
long previous = 0; // F_0
long current = 1;  // F_1

long tmp = previous + current;
previous = current;
current = tmp;
```

- What does this code fragment do?
  * Calculate the next Fibonacci number, save it in `tmp`
  * Put the current value into `previous`
  * Put the `tmp` value into `current`

---

# `fibIterative()`

Add a loop, return `current`, and no more than 1 total call is needed.

```java
public static long fibIterative(int n) {
    if (n <= 0) {
        return 0;
    }

    long previous = 0;
    long current = 1;

    for (int i = 2; i <= n; i++) {
        long tmp = previous + current;
        previous = current;
        current = tmp;
    }

    return current;
}
```



---

# `Crossword.java`

Letters are added to a 2-dimensional array known as the *game board*.
Each cell on the game board is blank, solid, or it is the starting cell
of a word space.

- Each *word space* has a number and a direction, either *across* or *down*
- Each word space has a fixed length, and it only accepts words whose length is
  **equal** to the word space's length
- Candidate words are drawn from a *dictionary*
- A word from the dictionary cannot be used more than once

---
<!-- class: bonus -->

# Bonus: combination sum

The combination sum problem states:

> Given a list of distinct integers and an integer `goal`, print all combinations of
> integers from the list that sum to `goal`.

For example, if the input integers are `{3, 2, 1}` and the target is 5:

```java
3 + 2 = 5
3 + 1 + 1 = 5
2 + 3 = 5
2 + 2 + 1 = 5
2 + 1 + 2 = 5
1 + 3 + 1 = 5
1 + 2 + 2 = 5
1 + 1 + 3 = 5
```

The order of the combinations does not matter. Use recursive backtracking to
implement `combinationSum()`.
