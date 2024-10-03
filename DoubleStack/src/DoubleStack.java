import java.util.*;

/** Stack manipulation.
 * @since 1.8
 */
public class DoubleStack implements Cloneable{

   private LinkedList<Double> doubleStack;
   private static final List<String> legalOperations = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "SWAP", "DUP", "ROT"));
   public static void main (String[] args) {
      System.out.println(interpret("2 5 -"));
   }

   DoubleStack() {
      doubleStack = new LinkedList<>();
   }

   @Override
   public Object clone() throws CloneNotSupportedException {
      DoubleStack clone = new DoubleStack();
      clone.doubleStack = new LinkedList<>();
      clone.doubleStack.addAll(0, this.doubleStack);
      return clone;
   }

   public boolean stEmpty() {
      return doubleStack.isEmpty();
   }

   public void push (double a) {
      doubleStack.push(a);
   }

   public double pop() {
      if (stEmpty()){
         throw new RuntimeException("Stack is empty");
      }
      return doubleStack.pop();
   }

   public void op(String s) {
      if (!(isNumeric(s) || legalOperations.contains(s))){
         throw new RuntimeException(String.format("Illegal element: %s in expression!", s));
      }
      if (stEmpty()){
         throw new RuntimeException(String.format("Not enough elements in stack for expression %s", s));
      }
      Double topElement = doubleStack.pop();

      if (stEmpty()){
         throw new RuntimeException(String.format("Not enough elements in stack for expression %s", s));
      }
      doubleStack.push(topElement);
      Double second = doubleStack.pop();
      Double first = doubleStack.pop();
      if ("+".equals(s)) {
         doubleStack.push(first + second);
      } else if ("-".equals(s)) {
         doubleStack.push(first - second);
      } else if ("*".equals(s)) {
         doubleStack.push(first * second);
      } else if ("/".equals(s)) {
         doubleStack.push(first / second);
      }
   }


  
   public double tos() {
      if (stEmpty()){
         throw new RuntimeException("Stack is empty");
      }
      return doubleStack.peek();
   }

   @Override
   public boolean equals (Object o) {
      if (!(o instanceof DoubleStack)) {
         return false;
      }
      DoubleStack other = (DoubleStack) o;
      return (this.doubleStack).containsAll(other.doubleStack) && (other.doubleStack).containsAll(this.doubleStack);
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      for (Double element : doubleStack){
         builder.append(element).append(" ");
      }
      return builder.reverse().toString().trim();
   }

   public static double interpret (String pol) {
      LinkedList<Double> doubleStack = new LinkedList<>();
      if (pol == null || "".equals(pol.trim())) {
         throw new RuntimeException(String.format("Empty expression: (%s)!", pol));
      }
      pol = pol.replaceAll("\t+", " ");
      String[] elements = pol.trim().split(" ");
      for (String element : elements) {
         if ("".equals(element)){
            continue;
         }
         if (!(isNumeric(element) || legalOperations.contains(element))){
            throw new RuntimeException(String.format("Illegal element: (%s) in expression (%s)!", element, pol));
         }
         if (isNumeric(element)) {
            doubleStack.push(Double.parseDouble(element));
         } else if (legalOperations.contains(element)) {
            try {
               opInterpreter(element, doubleStack);
            } catch (RuntimeException e) {
               throw new RuntimeException(String.format("Too many operations in expression (%s)!", pol));
            }

         }
      }
      if (doubleStack.size() > 1) {
         throw new RuntimeException(String.format("Too many numbers in expression (%s)!", pol));
      }
      return doubleStack.pop();
   }

   private static void opInterpreter(String s, LinkedList<Double> doubleStack) {
      if (!(isNumeric(s) || legalOperations.contains(s))){
         throw new RuntimeException(String.format("Illegal element: %s in expression!", s));
      }
      if (doubleStack.isEmpty()){
         throw new RuntimeException(String.format("Not enough elements in stack for expression %s", s));
      }
      else if ("DUP".equals(s)) {
         doubleStack.push(doubleStack.peek());
         return;
      }
      Double topElement = doubleStack.pop();

      if (doubleStack.isEmpty()){
         throw new RuntimeException(String.format("Not enough elements in stack for expression %s", s));
      }
      doubleStack.push(topElement);
      Double second = doubleStack.pop();
      Double first = doubleStack.pop();
      if ("+".equals(s)) {
         doubleStack.push(first + second);
      } else if ("-".equals(s)) {
         doubleStack.push(first - second);
      } else if ("*".equals(s)) {
         doubleStack.push(first * second);
      } else if ("/".equals(s)) {
         doubleStack.push(first / second);
      } else if ("SWAP".equals(s)) {
         doubleStack.push(first);
         doubleStack.push(second);
         if (doubleStack.isEmpty()){
            throw new RuntimeException("Stack is empty");
         }
         Double op1 = doubleStack.pop();
         if (doubleStack.isEmpty()){
            throw new RuntimeException("Stack is empty");
         }
         Double op2 = doubleStack.pop();
         doubleStack.push(op1);
         doubleStack.push(op2);
      } else if ("ROT".equals(s)) {
         doubleStack.push(first);
         doubleStack.push(second);
         if (doubleStack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
         }
         Double op1 = doubleStack.pop();
         if (doubleStack.isEmpty()){
            throw new RuntimeException("Stack is empty");
         }
         Double op2 = doubleStack.pop();
         if (doubleStack.isEmpty()){
            throw new RuntimeException("Stack is empty");
         }
         Double op3 = doubleStack.pop();
         doubleStack.push(op2);
         doubleStack.push(op1);
         doubleStack.push(op3);
      }
   }

   public static boolean isNumeric(String str) {
      return str.matches("-?\\d+(\\.\\d*)?");
   }

}

