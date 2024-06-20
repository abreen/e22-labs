---
marp: true
title: Lab 1
description: ArrayBag, add(), contains(), remove(), constructor
author: CSCI S-22 Course Staff
footer: CSCI S-22, Harvard Summer School, 2024
---

# The `ArrayBag.add()` method

In lecture, we looked at the `ArrayBag` implementation of the `add()` method:

```java
public boolean add(Object item) {
    if (item == null) {
        throw new IllegalArgumentException("item must be non-null");
    } else if (this.numItems == this.items.length) {
        return false; // no more room!
    } else {
        this.items[this.numItems] = item;
        this.numItems++;
        return true;
    }
}
```

We return `true` if we're able to add the item, and `false` otherwise.

---

# The `ArrayBag.add()` method

Note that we are using the `equals()` method, rather than `==`, because we're
comparing reference types.

* Why is the loop bound by the `numItems` field and not `items.length`?
  * If the bag is not full, there will be `null`s towards the end of the array
  * There are `numItems` many non-`null` elements

* Why can the method safely return `true` inside the loop?
  * All we need to know is that the bag contains *at least one*
    occurrence of the item. As soon as we see an occurrence, we are done.

---

# Returning `false` inside the loop

Why can't we return `false` inside the loop, as shown below?

```java
for (int i = 0; i < this.numItems; i++) {
    if (this.items[i].equals(item)) {
        return true;
    } else {
        return false;
    }
}
```

* This method returns during the first iteration of the loop. It will only ever
  consider at one element.
* What if the item appears later in the array?

---

# Writing `ArrayBag.remove()`

Let's now write the `ArrayBag` implementation of `remove()`.

* Our loop should be bounded by `maxItems`
* We should use the `equals()` method to compare references (like `contains()`)
* Shifting requires its own loop
* Don't forget to decrement `numItems`

---

# Copying objects

Recall that variables that represent objects or arrays actually store a
reference to the object or array---i.e., the **memory address** of the object or
array on the heap.

What would things look like in memory after the following lines are executed?

```java
ArrayBag b1 = new ArrayBag();
b1.add("hello");
b1.add("world");
```
