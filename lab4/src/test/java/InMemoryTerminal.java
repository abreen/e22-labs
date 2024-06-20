/**
 * A dummy terminal of a fixed size that stores output in memory
 *
 * This terminal is used instead of an actual terminal in the test environment
 * so that the terminal output can be checked for correctness.
 */
public class InMemoryTerminal implements Terminal {

    private final int columns, lines;

    private final StringBuilder buffer;

    public InMemoryTerminal() {
        this(80, 24);
    }

    public InMemoryTerminal(int columns, int lines) {
        this.columns = columns;
        this.lines = lines;
        this.buffer = new StringBuilder();
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int getLines() {
        return lines;
    }

    @Override
    public void print(String s) {
        buffer.append(s);
    }

    @Override
    public void println(String s) {
        buffer.append(s).append(System.lineSeparator());
    }

    @Override
    public void setColor(int ansiColor) {
        buffer.append("{color=").append(ansiColor).append("}");
    }

    @Override
    public void reset() {
        buffer.append("{reset}");
    }

    public String getBufferValue() {
        return buffer.toString();
    }
}
