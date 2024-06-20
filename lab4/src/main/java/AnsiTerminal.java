import java.io.PrintStream;

/**
 * A class representing a real terminal
 */
public class AnsiTerminal implements Terminal {

    private final PrintStream out;

    public AnsiTerminal() {
        this(System.out);
    }

    public AnsiTerminal(PrintStream out) {
        this.out = out;
    }

    public static String ansiSetColor(int ansiColor) {
        if (ansiColor < 0 || ansiColor > 255) {
            throw new IllegalArgumentException("must be an 8-bit color (max value = 255)");
        }
        return "\u001b[38;5;" + ansiColor + "m";
    }

    public static String ansiReset() {
        return "\u001b[0m";
    }

    public void setColor(int ansiColor) {
        out.print(ansiSetColor(ansiColor));
    }

    public void reset() {
        out.print(ansiReset());
    }

    @Override
    public int getColumns() {
        try {
            return Integer.parseInt(System.getenv("COLUMNS"));
        } catch (NumberFormatException e) {
            return 80;
        }
    }

    @Override
    public int getLines() {
        try {
            return Integer.parseInt(System.getenv("LINES"));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    @Override
    public void print(String s) {
        out.print(s);
    }

    @Override
    public void println(String s) {
        out.println(s);
    }
}
