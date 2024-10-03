import java.util.*;

/** Tree with two pointers.
 * @since 1.8
 */
public class Tnode {

   private String name;
   private Tnode firstChild;
   private Tnode nextSibling;

   public Tnode(String name){
      this.name = name;
   }
   public Tnode(String name, Tnode first, Tnode next){
      this.name = name;
      firstChild = first;
      nextSibling = next;
   }
   @Override
   public String toString() {
      StringBuffer b = new StringBuffer();
      Tnode cNode = this;
      b.append(cNode.name);
      if (cNode.firstChild != null && cNode.nextSibling != null) {
         b.append("(");
         b.append(cNode.firstChild);
         b.append(",");
         b.append(cNode.nextSibling);
         b.append(")");
      }
      return b.toString();
   }

   public static Tnode buildFromRPN (String pol) {
      String[] nodes = pol.split(" ");
      Stack<Tnode> subNodes = new Stack<>();
      for (String node : nodes) {
         if (isNumeric(node)) {
            Tnode tnode = new Tnode(node);
            subNodes.push(tnode);
         } else if (Arrays.asList("+", "-", "*", "/").contains(node)){
            if (subNodes.size() < 2) {
               throw new RuntimeException(String.format("Too few numbers in expression: %s", pol));
            }
            Tnode tnode = new Tnode(node);
            tnode.nextSibling = subNodes.pop();
            tnode.firstChild = subNodes.pop();
            subNodes.push(tnode);
         } else if ("SWAP".equals(node)){
            if (subNodes.size() < 2) {
               throw new RuntimeException(String.format("Too few numbers in expression: %s", pol));
            }
            Tnode top = subNodes.pop();
            Tnode next = subNodes.pop();
            subNodes.push(top);
            subNodes.push(next);

         } else if ("DUP".equals(node)){
            if (subNodes.size() < 1) {
               throw new RuntimeException(String.format("Too few numbers in expression: %s", pol));
            }
            Tnode currentNode = subNodes.pop();
            Tnode param = new Tnode(currentNode.name);
            Tnode newNode = duplicate(currentNode, param, param);
            subNodes.push(currentNode);
            subNodes.push(newNode);

         } else if ("ROT".equals(node)){
            if (subNodes.size() < 3) {
               throw new RuntimeException(String.format("Too few numbers in expression: %s", pol));
            }
            Tnode top = subNodes.pop();
            Tnode second = subNodes.pop();
            Tnode third = subNodes.pop();
            subNodes.push(second);
            subNodes.push(top);
            subNodes.push(third);

         } else {
            throw new RuntimeException(String.format("Illegal character '%s' expression: %s", node, pol));
         }
      }
      if (subNodes.size() > 1) {
         throw new RuntimeException(String.format("Too many numbers in expression: %s", pol));
      }
      if (subNodes.size() == 0) {
         throw new RuntimeException("Expression is empty");
      }
      return subNodes.pop();
   }

   public static void main (String[] param) {
      String rpn = "1";
      System.out.println ("RPN: " + rpn);
      Tnode res = buildFromRPN (rpn);
      System.out.println ("Tree: " + res);

   }
   private static Tnode duplicate(Tnode current, Tnode newNode, Tnode result) {
      if (current.firstChild != null && current.nextSibling != null) {
         newNode.firstChild = new Tnode(current.firstChild.name);
         newNode.nextSibling = new Tnode(current.nextSibling.name);
      } else {
         return result;
      }
      duplicate(current.firstChild, newNode.firstChild, result);
      return duplicate(current.nextSibling, newNode.nextSibling, result);
   }


   private static boolean isNumeric(String str) {
      return str.matches("-?\\d+(\\.\\d*)?");
   }
}
