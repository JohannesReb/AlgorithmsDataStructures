
import static org.junit.Assert.*;
import org.junit.Test;

/** Testklass.
 * @author Jaanus
 */
public class TnodeTest {

   @Test (expected=RuntimeException.class)
   public void testDUPUnderflow() {
      Tnode t = Tnode.buildFromRPN ("DUP");
   }

   @Test (expected=RuntimeException.class)
   public void testSWAPUnderflow() {
      Tnode t = Tnode.buildFromRPN ("2 SWAP");
   }

   @Test (expected=RuntimeException.class)
   public void testROTUnderflow() {
      Tnode t = Tnode.buildFromRPN ("2 3 ROT");
   }

   @Test (timeout=1000)
   public void testSimpleSWAP() {
      String s = "2 5 SWAP -";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "-(5,2)", r);
   }

   @Test (timeout=1000)
   public void testSimpleDUP() {
      String s = "3 DUP *";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "*(3,3)", r);
   }
   @Test (timeout=1000)
   public void testSimpleROT() {
      String s = "2 5 9 ROT - +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(5,-(9,2))", r);
   }

   @Test (timeout=1000)
   public void testROTAndSWAP() {
      String s = "2 5 9 ROT + SWAP -";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "-(+(9,2),5)", r);
   }

   @Test (timeout=1000)
   public void testROTAndDUP() {
      String s = "2 5 DUP ROT - + DUP *";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "*(+(5,-(5,2)),+(5,-(5,2)))", r);
   }

   @Test (timeout=1000)
   public void testComplexROTnSWAPnDUP() {
      String s = "-3 -5 -7 ROT - SWAP DUP * +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(-(-7,-3),*(-5,-5))", r);
   }
   @Test (timeout=1000)
   public void testBuildFromRPN() { 
      String s = "1 2 +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(1,2)", r);
      s = "2 1 - 4 * 6 3 / +";
      t = Tnode.buildFromRPN (s);
      r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(*(-(2,1),4),/(6,3))", r);
   }

   @Test (timeout=1000)
   public void testBuild2() {
      String s = "512 1 - 4 * -61 3 / +";
      Tnode t = Tnode.buildFromRPN (s);
      String r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "+(*(-(512,1),4),/(-61,3))", r);
      s = "5";
      t = Tnode.buildFromRPN (s);
      r = t.toString().replaceAll("\\s+", "");
      assertEquals ("Tree: " + s, "5", r);
   }

   @Test (expected=RuntimeException.class)
   public void testEmpty1() {
      Tnode t = Tnode.buildFromRPN ("\t\t");
   }

   @Test (expected=RuntimeException.class)
   public void testEmpty2() {
      Tnode t = Tnode.buildFromRPN ("\t \t ");
   }

   @Test (expected=RuntimeException.class)
   public void testIllegalSymbol() {
      Tnode t = Tnode.buildFromRPN ("2 xx");
   }

   @Test (expected=RuntimeException.class)
   public void testIllegalSymbol2() {
      Tnode t = Tnode.buildFromRPN ("x");
   }

   @Test (expected=RuntimeException.class)
   public void testIllegalSymbol3() {
      Tnode t = Tnode.buildFromRPN ("2 1 + xx");
   }

   @Test (expected=RuntimeException.class)
   public void testTooManyNumbers() {
      Tnode root = Tnode.buildFromRPN ("2 3");
   }

   @Test (expected=RuntimeException.class)
   public void testTooManyNumbers2() {
      Tnode root = Tnode.buildFromRPN ("2 3 + 5");
   }

   @Test (expected=RuntimeException.class)
   public void testTooFewNumbers() {
      Tnode t = Tnode.buildFromRPN ("2 -");
   }

   @Test (expected=RuntimeException.class)
   public void testTooFewNumbers2() {
      Tnode t = Tnode.buildFromRPN ("2 5 + -");
   }

  @Test (expected=RuntimeException.class)
   public void testTooFewNumbers3() {
      Tnode t = Tnode.buildFromRPN ("+");
   }
}

