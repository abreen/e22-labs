public class TodoApp {

    private String[] todos;
    private boolean[] done;

    public TodoApp(String[] initialTodos) {
        todos = initialTodos;
        done = new boolean[initialTodos.length];
    }

    public String getTodo(int index) {
        return todos[index];
    }

    public void markDone(int index) {
        done[index] = true;
    }

    public boolean getDone(int index) {
        return done[index];
    }

    public boolean addTodo(String todo) {
        // find the first empty slot
        for (int i = 0; i < todos.length; i++) {
            if (todos[i] == null) {
                todos[i] = todo;
                return true;
            }
        }

        // the array must be full
        return false;
    }

    public void deleteTodo(int index) {
        todos[index] = null;
        done[index] = false;
    }

    public void printList() {
        int numPrinted = 0;

        for (int i = 0; i < todos.length; i++) {
            if (todos[i] != null) {
                System.out.print(i + ". " + todos[i]);
                if (done[i]) {
                    System.out.print(" - done");
                }
                System.out.println();

                numPrinted++;
            }
        }

        if (numPrinted == 0) {
            System.out.println("(The list is empty.)");
            return;
        }
    }

    public String[] getNotDoneTodos() {
        String[] doneTodos = new String[todos.length];
        int i = 0;
        for (int j = 0; j < todos.length; j++) {
            if (!done[j]) {
                doneTodos[i++] = todos[j];
            }
        }
        return doneTodos;
    }
}
