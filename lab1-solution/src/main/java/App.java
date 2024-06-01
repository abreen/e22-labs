public class App {
    private static final int DEFAULT_MAX_TODOS = 100;
    private static final String FILE_NAME = "todos.txt";

    public static void main(String[] args) {
        var storage = new TodoStorage();
        String[] todosFromFile = storage.loadFromFile(FILE_NAME);

        var app = new TodoApp(
                todosFromFile == null ? new String[DEFAULT_MAX_TODOS] : todosFromFile);

        var cli = new TodoCli(app, System.in);
        cli.interactWithUser();

        storage.saveToFile(FILE_NAME, app.getTodosArray());
    }
}
