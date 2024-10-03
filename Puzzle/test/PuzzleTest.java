import static org.junit.Assert.*;
import org.junit.Test;

/** Testklass.
 * @author jaanus
 */
public class PuzzleTest {

   @Test
   public void test() {
      Puzzle puzzle = new Puzzle();
      long start = System.currentTimeMillis();
      // puzzle.main (new String[]{First operand, Second operand, result, operation: +, -, *, /});
      puzzle.main (new String[]{"KOLM", "KAKS", "YKS", "-"}); // 234 solutions
      puzzle.main (new String[]{"SEND", "MORE", "MONEY", "+"}); // 1 solution
      puzzle.main (new String[]{"ABCDEFGHIJAB", "ABCDEFGHIJA", "ACEHJBDFGIAC", "+"});  // 2 solutions
      // {A=1, B=2, C=3, D=4, E=5, F=6, G=7, H=8, I=9, J=0},
      // {A=2, B=3, C=5, D=1, E=8, F=4, G=6, H=7, I=9, J=0}
      puzzle.main (new String[]{"CBEHEIDGEI", "CBEHEIDGEI", "BBBBBBBBBB", "+"}); // no solutions
      puzzle.main (new String[]{"DEA", "CA", "AB", "/"});

      long end = System.currentTimeMillis();
      System.out.println(end-start);
      assertTrue ("There are no formal tests", true);
   }
}

