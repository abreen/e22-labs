public interface Terminal {
    /** Get the number of columns the terminal can display */
    int getColumns();

    /** Get the number of lines (rows) the terminal can display */
    int getLines();

    /** Set the current text color to the specified ANSI (8-bit) color */
    void setColor(int ansiColor);

    /** Reset the text color */
    void reset();

    /** Print a string to the terminal */
    void print(String s);

    default void print(char c) {
        print(Character.toString(c));
    }

    /** Print a string and a newline to the terminal */
    void println(String s);

    /** Print a newline to the terminal */
    default void println() {
        println("");
    }

    /** Print an int and a newline to the terminal */
    default void println(int n) {
        println(Integer.toString(n));
    }
}
