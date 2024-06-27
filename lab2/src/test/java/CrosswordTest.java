import org.junit.Test;
import org.junit.Assert;

public class CrosswordTest {

    @Test
    public void testFindSolutions() {
        String[] dictionary = { "AFT", "LASER", "ALE", "LEE", "EEL", "LINE",
                "HEEL", "SAILS", "HIKE", "SHEET", "HOSES",
                "STEER", "KEEL", "TIE", "KNOT" };

        var cw = new Crossword(6, 5, dictionary,
                "1 > 0 0 5",
                "2 v 0 2 5",
                "3 v 0 4 5",
                "4 > 2 1 4",
                "5 v 2 3 4",
                "6 v 3 0 3",
                "7 > 3 2 3",
                "8 > 4 0 5");

        cw.findSolutions(0);
    }
}
