import org.junit.Test;
import org.junit.Assert;

public class ArrayPrinterTest {

    @Test
    public void testColors() {
        var term = new InMemoryTerminal();
        var printer = new ArrayPrinter(new Sort(), term);

        int[] arr = { 1, 2, 3, 4, 5 };
        printer.rainbow(arr);

        String output = term.getBufferValue();

        Assert.assertTrue("1 is red", output.contains("{color=196}1{reset}"));
        Assert.assertTrue("2 is yellow", output.contains("{color=190}2{reset}"));
        Assert.assertTrue("3 is green", output.contains("{color=48}3{reset}"));
        Assert.assertTrue("4 is blue", output.contains("{color=33}4{reset}"));
        Assert.assertTrue("5 is purple", output.contains("{color=165}5{reset}"));
    }

    @Test
    public void testBars() {
        var term = new InMemoryTerminal(80, 10);
        var printer = new ArrayPrinter(new Sort(), term);

        int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        printer.bars(arr);

        String output = term.getBufferValue();
        String[] lines = output.split(System.lineSeparator());

        Assert.assertEquals(10, lines.length);

        Assert.assertEquals("         █", lines[0]);
        Assert.assertEquals("        ██", lines[1]);
        Assert.assertEquals("       ███", lines[2]);
        Assert.assertEquals("      ████", lines[3]);
        Assert.assertEquals("     █████", lines[4]);
        Assert.assertEquals("    ██████", lines[5]);
        Assert.assertEquals("   ███████", lines[6]);
        Assert.assertEquals("  ████████", lines[7]);
        Assert.assertEquals(" █████████", lines[8]);
        Assert.assertEquals("██████████", lines[9]);
    }
}
