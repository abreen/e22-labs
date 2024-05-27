public class App {
    public static void main(String[] args) {
        var app = new TodoApp();
        var cli = new TodoCli(app, System.in);
        cli.interactWithUser();
    }
}
