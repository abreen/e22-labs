/**
 * A class containing core logic for a to-do list app
 *
 * This class requires an array of strings (the initial to-do list).
 */
public class TodoApp {

    /** Things still left to do */
    private ArrayBag notDone;

    /** Things that are done */
    private ArrayBag done;

    public TodoApp(String[] initialTodos) {
        if (initialTodos == null) {
            // instead of throwing an exception, just use an empty array
            initialTodos = new String[0];
        }

        notDone = new ArrayBag(initialTodos.length);
        for (String item : initialTodos) {
            if (item != null) {
                notDone.add(item);
            }
        }

        done = new ArrayBag();
    }

    public String[] getTodos() {
        return (String[]) notDone.toArray();
    }

    public void markDone(String item) {
        if (notDone.contains(item)) {
            notDone.remove(item);
            done.add(item);
        }
    }

    public boolean isDone(String item) {
        return done.contains(item);
    }

    public boolean addTodo(String item) {
        return notDone.add(item);
    }

    public void deleteTodo(String item) {
        if (notDone.contains(item)) {
            notDone.remove(item);
        } else if (done.contains(item)) {
            done.remove(item);
        }
    }

    public void printList() {
        Object[] notDoneArray = notDone.toArray();
        Object[] doneArray = done.toArray();

        int numPrinted = 0;

        if (notDoneArray.length > 0) {
            System.out.println("to do:");
            for (int i = 0; i < notDoneArray.length; i++) {
                if (notDoneArray[i] != null) {
                    System.out.println(i + ". " + notDoneArray[i]);
                    numPrinted++;
                }
            }
        }

        if (doneArray.length > 0) {
            System.out.println("done:");
            for (int i = 0; i < doneArray.length; i++) {
                if (doneArray[i] != null) {
                    System.out.println(i + ". " + doneArray[i]);
                    numPrinted++;
                }
            }
        }

        if (numPrinted == 0) {
            System.out.println("(The list is empty.)");
            return;
        }
    }

    public String[] getNotDoneTodos() {
        Object[] notDoneArray = notDone.toArray();
        String[] strings = new String[notDoneArray.length];
        int numStrings = 0;

        for (Object item : notDoneArray) {
            if (item instanceof String) {
                strings[numStrings++] = (String) item;
            }
        }

        // shouldn't be possible since we only put strings into the ArrayBag
        if (numStrings < notDoneArray.length) {
            String[] temp = new String[numStrings];
            System.arraycopy(strings, 0, temp, 0, numStrings);
            strings = temp;
        }

        return strings;
    }
}
