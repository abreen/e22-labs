import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A storage mechanism for the to-do list app
 *
 * This class offers methods for loading & saving strings to a plain text file.
 */
public class TodoStorage {

    private static final int MAX_NUM_STRINGS_FROM_FILE = 25;

    public String[] loadFromFile(String fileName) {
        var todos = new String[MAX_NUM_STRINGS_FROM_FILE];

        try (Scanner s = new Scanner(new File(fileName))) {
            int i = 0;
            while (s.hasNextLine()) {
                String todo = s.nextLine().trim();
                todos[i] = todo;
                i++;
            }

        } catch (FileNotFoundException e) {
            return null;
        }

        return todos;
    }

    public boolean saveToFile(String fileName, String[] todos) {
        try (var file = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            for (int i = 0; i < todos.length; i++) {
                if (todos[i] != null) {
                    file.append(todos[i]);
                    file.append("\r\n");
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
