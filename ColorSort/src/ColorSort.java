/** Sorting of balls.
 * @since 1.8
 */
public class ColorSort {

   enum Color {red, green, blue}

   public static void main(String[] args) {

   }

   public static void reorder (Color[] balls) {
      int redCount = 0;
      int blueCount = 0;
      for (Color ball : balls) {
         redCount += Math.max((ball.ordinal() - 1) * (-1), 0);
         blueCount += Math.max((ball.ordinal() - 1), 0);
      }
      overWriteArrayElements(balls, redCount, blueCount);
   }
   private static void overWriteArrayElements (Color[] balls, int redCount, int blueCount) {

      int arrayLength = balls.length - 1;

      for (int i = 0; i < redCount; i++) {
         balls[i] = Color.red;
      }
      for (int i = redCount; i < arrayLength + 1 - blueCount; i++) {
         balls[i] = Color.green;
      }
      for (int i = 0; i < blueCount; i++) {
         balls[arrayLength - i] = Color.blue;
      }
   }
}

