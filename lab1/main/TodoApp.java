public class TodoApp {

    private String[] todos = new String[100];

    public String getTodo(int index) {
        return todos[index];
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
}