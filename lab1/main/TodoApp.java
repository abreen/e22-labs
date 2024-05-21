import java.util.Scanner;

public class TodoApp {

    // talk about access modifiers: need to hide

    public static String[] todos = new String[100];
    public static boolean[] complete = new boolean[100];

    // talk about alternative approaches: blueprint class
    // for a to-do list item

    // talk about fixed array capacity

    public static void main(String[] args) {
        System.out.println("Welcome! Use the 'help' command.");

        Scanner lines = new Scanner(System.in);

        System.out.print("> ");
        while (lines.hasNextLine()) {
            String[] tokens = lines.nextLine().split("\\s+");

            if (tokens.length < 1) {
                // no command
                System.out.print("> ");
                continue;
            }

            handleCommand(tokens);

            printTodos();

            System.out.print("> ");
        }
        System.out.println();

        System.out.println("Goodbye!");
        lines.close();
    }

    public static void handleCommand(String[] tokens) {
        String command = tokens[0];

        if (command.equals("help")) {
            System.out.println("Available commands:");
            System.out.println("add <todo>");
            System.out.println("done <todo number>");
            System.out.println("delete <todo number>");

        } else if (command.equals("add")) {
            addTodo(tokens[1]);

        } else if (command.equals("done")) {
            markDone(Integer.parseInt(tokens[1]));

        } else if (command.equals("delete")) {
            delete(Integer.parseInt(tokens[1]));

        } else {
            System.err.println("unknown command: " + command);
        }
    }

    public static void addTodo(String item) {
        for (int i = 0; i < todos.length; i++) {
            if (todos[i] == null) {
                todos[i] = item;
                complete[i] = false; // could be done in delete()
                break;
            }
        }
    }

    public static void markDone(int index) {
        complete[index] = true;
    }

    public static void delete(int index) {
        todos[index] = null;
    }

    public static void printTodos() {
        for (int i = 0; i < todos.length; i++) {
            if (todos[i] != null) {
                System.out.printf("%d. %s", i, todos[i]);
            }
        }
    }
}