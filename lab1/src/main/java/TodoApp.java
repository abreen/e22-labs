public class TodoApp {

    private ArrayBag todos;

    public TodoApp() {
        todos = new ArrayBag();
    }

    public boolean addTodo(String item) {
        return todos.add(item);
    }

    public boolean deleteTodo(String item) {
        return todos.remove(item);
    }

    public void markDone(String item) {
        throw new UnsupportedOperationException();
    }

    public boolean isDone(String item) {
        return !todos.contains(item);
    }
}
