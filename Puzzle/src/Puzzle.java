import java.util.*;

/** Word puzzle.
 * @since 1.8
 */
public class Puzzle {

   /**
    * Solve the word puzzle.
    *
    * @param args three words (addend1 addend2 sum)
    */
   public void main(String[] args) {

      Map<Character, Integer> chars = new HashMap<>();
      String fullString = args[0] + args[1] + args[2];
      int i = 0;
      for (char chr : fullString.toCharArray()) {
         if (!chars.containsKey(chr)) {
            chars.put(chr, i);
            i++;
         }
      }
      PuzzleSolver solver = new PuzzleSolver(chars.size(), args[0], args[1], args[2], args[3]);
      solver.solve();


   }

   public class PuzzleSolver {

      private Map<Character, Integer> chars = new HashMap<>();
      private List<List<Condition>> conditions = new ArrayList<>();
      private List<Condition> multConditions = new ArrayList<>();
      private List<Integer> carryOvers = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
      private Set<Character> notZeros = new HashSet<>();
      private int size;
      private final int maxValue = 9;
      private int[] board;
      private String op;
      private String a;
      private String b;
      private String r;

      PuzzleSolver(int n, String A, String B, String R, String operation) {
         a = A;
         b = B;
         r = R;
         op = operation;
         size = n;
         board = new int[size];
         Arrays.fill(board, -1);
      }

      public void solve() {

         System.out.println(a + " " + op + " " + b + " = " + r);
         notZeros.add(a.charAt(0));
         notZeros.add(b.charAt(0));
         notZeros.add(r.charAt(0));
         int LenA = a.length();
         int LenB = b.length();
         int LenR = r.length();
         int Len = Math.max(Math.max(LenA, LenB), LenR);
         int k = 0;
         for (int i = 1; i <= Len; i++) {
            if (LenA >= i) {
               if (!chars.containsKey(a.charAt(LenA - i))) {
                  chars.put(a.charAt(LenA - i), k);
                  k++;
               }
            }
            if (LenB >= i) {
               if (!chars.containsKey(b.charAt(LenB - i))) {
                  chars.put(b.charAt(LenB - i), k);
                  k++;
               }
            }
            if (LenR >= i) {
               if (!chars.containsKey(r.charAt(LenR - i))) {
                  chars.put(r.charAt(LenR - i), k);
                  k++;
               }
            }
         }
         if (chars.size() > 10) {
            System.out.println("There must be less than 11 unique characters in words!");
            return;
         }
         if ("+".equals(op)) {
            createSumConditions();
         } else if ("-".equals(op)) {
            String temp = r;
            r = a;
            a = temp;
            op = "+";
            createSumConditions();
         } else if ("*".equals(op)) {
            createMultConditions();
         } else if ("/".equals(op)) {
            String temp = r;
            r = a;
            a = temp;
            op = "*";
            createMultConditions();
         }
         place();

      }

      public void createSumConditions() {
         for (int i = 0; i < size; i++) {
            conditions.add(new ArrayList<>());
         }
         int LenA = a.length();
         int LenB = b.length();
         int LenR = r.length();

         int a1;
         int b1;
         int r1;
         int index = 0;
         for (int i = 1; i <= LenR; i++) {
            if (i > LenA) {
               a1 = -1;
            } else {
               a1 = chars.get(a.charAt(LenA - i));
            }
            if (i > LenB) {
               b1 = -1;
            } else {
               b1 = chars.get(b.charAt(LenB - i));
            }
            r1 = chars.get(r.charAt(LenR - i));
            index = Math.max(Math.max(Math.max(a1, b1), r1), index);
            Condition condition = new Condition();
            condition.addSubCondition(new SubCondition(a1, b1, i - 1, r1));
            conditions.get(index).add(condition);
         }
      }

      public void createMultConditions() {
         int LenA = a.length();
         int LenB = b.length();
         int LenR = r.length();
         int Len = Math.max(Math.max(LenA, LenB), LenR);
         for (int i = 0; i < Math.max(size, Len); i++) {
            conditions.add(new ArrayList<>());
         }
         if (LenR > LenA + LenB) {
            System.out.println("Finished with 0 solutions");
            System.exit(0);
         }
         if (LenR - LenA - LenB + 1 < 0) {
            System.out.println("Finished with 0 solutions");
            System.exit(0);
         }
         int a1;
         int b1;
         int r1;
         int index = 0;
         for (int i = 0; i < LenR; i++) {
            multConditions.add(new Condition());
         }
         for (int i = 1; i <= LenA; i++) {
            for (int j = 1; j <= LenB; j++) {
               a1 = chars.get(a.charAt(LenA - i));
               b1 = chars.get(b.charAt(LenB - j));
               r1 = chars.get(r.charAt(LenR - i - j + 1));

               index = Math.max(Math.max(Math.max(a1, b1), r1), index);
               Condition c = multConditions.get(i + j - 2);
               c.addSubCondition(new SubCondition(a1, b1, i + j - 2, r1));
               c.index = Math.max(index, c.index);
            }
         }
         if (LenR == LenA + LenB) {
            Condition c = multConditions.get(LenR - 1);
            c.addSubCondition(new SubCondition(-1, -1, LenR - 1, chars.get(r.charAt(0))));
            c.index = Math.max(index + 1, c.index);
         }
         for (Condition c : multConditions) {
            conditions.get(c.index).add(c);
         }
      }

      /**
       * Sources: https://enos.itcollege.ee/~japoia/algoritmid/tehnikad.html
       */
      public boolean peaceful(int k) {
         if (k >= size)
            throw new RuntimeException("Wrong index of the char " + k);
         if (board[k] == 0) {
            for (Character chr : notZeros) {
               if (k == chars.get(chr)) {
                  return false;
               }
            }
         }
         for (int i = 0; i < k; i++) {
            if (board[i] == board[k])
               return false; // same value
         }
         for (Condition c : conditions.get(k)) {
            if (!evaluate(c)) {
               return false;
            }
         }
         if (k == size) {
            for (int i = k; i < conditions.size(); i++) {
               for (Condition c : conditions.get(i)) {
                  if (!evaluate(c)) {
                     return false;
                  }
               }
            }
         }
         int lenA = a.length();
         int lenB = b.length();
         int lenR = r.length();

         if (k == size - 1) {
            char[] arrA = a.toCharArray();
            char[] arrB = b.toCharArray();
            char[] arrR = r.toCharArray();
            long a1 = 0;
            long b1 = 0;
            long r1 = 0;
            for (int i = 1; i <= lenA; i++) {
               a1 += board[chars.get(arrA[lenA - i])] * Math.pow(10, i - 1);
            }
            for (int i = 1; i <= lenB; i++) {
               b1 += board[chars.get(arrB[lenB - i])] * Math.pow(10, i - 1);
            }
            for (int i = 1; i <= lenR; i++) {
               r1 += board[chars.get(arrR[lenR - i])] * Math.pow(10, i - 1);
            }
            if ("+".equals(op)) {
               if (a1 + b1 != r1) {
                  return false;
               }
            } else if ("*".equals(op)) {
               if (a1 * b1 != r1) {
                  return false;
               }
            }

         }

         return true;
      }

      public boolean evaluate(Condition c) {
         int a1;
         int b1;
         int c0;
         int r1;
         int c1;
         if ("+".equals(op)) {
            for (SubCondition sc : c.subConditions) {
               if (sc.a == -1) {
                  a1 = 0;
               } else {
                  a1 = board[sc.a];
               }
               if (sc.b == -1) {
                  b1 = 0;
               } else {
                  b1 = board[sc.b];
               }
               c0 = carryOvers.get(sc.i);
               if (sc.r == -1) {
                  r1 = 0;
               } else {
                  r1 = board[sc.r];
               }
               c1 = (a1 + b1 + c0) / 10;
               carryOvers.set(sc.i + 1, c1);
               if (a1 + b1 + c0 != r1 + 10 * c1) {
                  return false;
               }
            }

         } else if ("*".equals(op)) {
            int sum = 0;
            for (SubCondition sc : c.subConditions) {
               if (sc.a == -1) {
                  a1 = 0;
               } else {
                  a1 = board[sc.a];
               }
               if (sc.b == -1) {
                  b1 = 0;
               } else {
                  b1 = board[sc.b];
               }
               sum += a1 * b1;
            }
            SubCondition sc = c.subConditions.get(0);
            c0 = carryOvers.get(sc.i);
            if (sc.r == -1) {
               r1 = 0;
            } else {
               r1 = board[sc.r];
            }
            c1 = (sum + c0) / 10;
            if (sum + c0 != r1 + 10 * c1) {
               return false;
            }
            carryOvers.set(sc.i + 1, c1);

         } else {
            throw new RuntimeException("Unknown operation: " + op);
         }
         return true;
      }

      /**
       * Sources: https://enos.itcollege.ee/~japoia/algoritmid/tehnikad.html
       */
      public void place() {
         int numsol = 0;
         int n = 0;
         while (n >= 0) {

            board[n]++;
            if (board[n] <= maxValue) {
               if (peaceful(n)) {
                  if (n < size - 1) {
                     n++;
                  } else {
                     numsol++; // solution found
                     process();
                  }
               }

            } else {
               board[n] = -1;
               n--;
            }
         }
         System.out.println("Finished with " + numsol + " solutions");
      }

      /**
       * Sources: https://enos.itcollege.ee/~japoia/algoritmid/tehnikad.html
       */
      public void process() {
         System.out.println(this);
      }

      /**
       * Sources: https://enos.itcollege.ee/~japoia/algoritmid/tehnikad.html
       */
      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         if (size < 2) return "1 ";
         for (char chr : chars.keySet()) {
            sb.append(chr);
            sb.append("=");
            sb.append(board[chars.get(chr)]);
            sb.append(" ");
         }
         return sb.toString();
      }
   }

   public class Condition {
      public int index = -1;
      private List<SubCondition> subConditions = new ArrayList<>();
      public void addSubCondition(SubCondition condition) {
         subConditions.add(condition);
      }
   }

   public class SubCondition {

      public int a;
      public int b;
      public int i;
      public int r;

      public SubCondition(int var1, int var2, int carry, int res) {
         a = var1;
         b = var2;
         i = carry;
         r = res;
      }

      @Override
      public String toString() {
         return "[%s, %s, %s, %s]".formatted(a, b, i, r);
      }
   }
}
