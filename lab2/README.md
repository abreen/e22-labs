The combination sum finder app

Given a list of distinct integers and a target integer,
return a list of combinations of integers that can be summed
to get the target.

For example, if the input integers are {3, 2, 1} and the target is 5, then
the returned list should look like this:

```java
[
    [3, 2, 1],          // 3 + 2 + 1 = 5
    [3, 1, 1],          // 3 + 1 + 1 = 5
    [2, 2, 1]           // 2 + 2 + 1 = 5
]
```

The order of the combinations does not matter.

Follow up questions:

- Modify your code so that it uses an integer only once. For the
  above example, this means that `[3, 1, 1]` is not a valid combination.
