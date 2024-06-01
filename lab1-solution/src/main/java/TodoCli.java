import java.util.Scanner;
import java.io.InputStream;

public class TodoCli {

    private TodoApp app;
    private InputStream in;

    public TodoCli(TodoApp app, InputStream in) {
        this.app = app;
        this.in = in;
    }

    public void interactWithUser() {
        System.out.println("Welcome! Here's the todo list:");
        app.printList();

        Scanner s = new Scanner(in);

        System.out.print("enter a command: ");
        while (s.hasNextLine()) {
            String command = s.next();
            String args = s.nextLine().trim();

            if (command.equals("add")) {
                app.addTodo(args);
            } else if (command.equals("done")) {
                int index = Integer.parseInt(args);
                app.markDone(index);
            } else if (command.equals("delete")) {
                int index = Integer.parseInt(args);
                app.deleteTodo(index);
            } else if (command.equals("quit")) {
                break;
            } else {
                System.err.println("unknown command: " + command);
            }

            app.printList();
            System.out.print("enter a command: ");
        }

        System.out.println("Goodbye!");
        s.close();
    }
}
