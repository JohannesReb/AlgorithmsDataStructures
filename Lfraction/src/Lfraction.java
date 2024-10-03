import java.util.*;

/** This class represents fractions of form n/d where n and d are long integer
 * numbers. Basic operations and arithmetics for fractions are provided.
 */
public class Lfraction implements Comparable<Lfraction> {

   /** Main method. Different tests. */
   public static void main (String[] param) {
   }

   private long n;
   private long d;

   /** Constructor.
    * @param a numerator
    * @param b denominator > 0
    */
   public Lfraction (long a, long b) {
      if (b == 0) {
         throw new RuntimeException("Denominator cannot be 0");
      }
      long gcd = findGCD(a, b);
      if (gcd != 0) {
         a /= gcd;
         b /= gcd;

         if (b < 0) {
            a = -a;
            b = -b;
         }
      }
      n = a;
      d = b;
   }

   /** Public method to access the numerator field.
    * @return numerator
    */
   public long getNumerator() {
      return n;
   }

   /** Public method to access the denominator field.
    * @return denominator
    */
   public long getDenominator() {
      return d;
   }

   /** Conversion to string.
    * @return string representation of the fraction
    */
   @Override
   public String toString() {
      return n + "/" + d;
   }

   /** Equality test.
    * @param m second fraction
    * @return true if fractions this and m are equal
    */
   @Override
   public boolean equals (Object m) {
      if (!(m instanceof Lfraction)) {
         return false;
      }
      Lfraction other = (Lfraction) m;
      return compareTo(other) == 0;
   }

   /** Hashcode has to be the same for equal fractions and in general, different
    * for different fractions.
    * @return hashcode
    */
   @Override
   public int hashCode() {
      Lfraction f = fractionPart();
      return Arrays.hashCode(new long[]{integerPart(), f.n, f.d});
   }

   /** Power of fraction.
    * @param n power
    * @return this^n
    */

   public Lfraction pow (long n) {
      if (n == 0) {
         return new Lfraction(1, 1);
      } else if (n == 1) {
         return new Lfraction(this.n, d);
      } else if (n == -1) {
         if (this.n == 0) {
            throw new RuntimeException("Cannot divide by 0");
         }
         return inverse();
      } else if (n < 0) {
         if (this.n == 0) {
            throw new RuntimeException("Cannot divide by 0");
         }
         return times(pow(-n-1)).inverse();
      }
      return times(pow(n-1));
   }
   public Lfraction plus (Lfraction m) {
      long lcm = findLCM(d, m.d);
      return new Lfraction(n * (lcm / d) + m.n * (lcm / m.d), lcm);
   }

   /** Multiplication of fractions.
    * @param m second factor
    * @return this*m
    */
   public Lfraction times (Lfraction m) {
      return new Lfraction(n * m.n, d * m.d);
   }

   /** Inverse of the fraction. n/d becomes d/n.
    * @return inverse of this fraction: 1/this
    */
   public Lfraction inverse() {
      return new Lfraction(d, n);
   }

   /** Opposite of the fraction. n/d becomes -n/d.
    * @return opposite of this fraction: -this
    */
   public Lfraction opposite() {
      return new Lfraction(-n, d);
   }

   /** Difference of fractions.
    * @param m subtrahend
    * @return this-m
    */
   public Lfraction minus (Lfraction m) {
      return plus(m.opposite());
   }

   /** Quotient of fractions.
    * @param m divisor
    * @return this/m
    */
   public Lfraction divideBy (Lfraction m) {
      if (m.n == 0) {
         throw new RuntimeException("Cannot divide by 0");
      }
      return times(m.inverse());
   }

   /** Comparision of fractions.
    * @param m second fraction
    * @return -1 if this < m; 0 if this==m; 1 if this > m
    */
   @Override
   public int compareTo (Lfraction m) {
      long lcm = findLCM(d, m.d);
      if (integerPart() > m.integerPart()) {
         return 1;
      } else if (integerPart() < m.integerPart()) {
         return -1;
      } else if (n * (lcm / d) > m.n * (lcm / m.d)) {
         return 1;
      } else if (n * (lcm / d) < m.n * (lcm / m.d)) {
         return -1;
      }
      return 0;
   }

   /** Clone of the fraction.
    * @return new fraction equal to this
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      return new Lfraction(n, d);
   }

   /** Integer part of the (improper) fraction.
    * @return integer part of this fraction
    */
   public long integerPart() {
      return n / d;
   }

   /** Extract fraction part of the (improper) fraction
    * (a proper fraction without the integer part).
    * @return fraction part of this fraction
    */
   public Lfraction fractionPart() {
      return new Lfraction(n % d, d);
   }

   /** Approximate value of the fraction.
    * @return real value of this fraction
    */
   public double toDouble() {
      return ((double)n / (double)d);
   }

   /** Double value f presented as a fraction with denominator d > 0.
    * @param f real number
    * @param d positive denominator for the result
    * @return f as an approximate fraction of form n/d
    */
   public static Lfraction toLfraction (double f, long d) {
      return new Lfraction(Math.round(f * d), d);
   }

   /** Conversion from string to the fraction. Accepts strings of form
    * that is defined by the toString method.
    * @param s string form (as produced by toString) of the fraction
    * @return fraction represented by s
    */
   public static Lfraction valueOf (String s) {
      String[] ab = s.split("/", 2);
      if (ab.length != 2){
         throw new RuntimeException(String.format("Incorrect string input: %s", s));
      }
      long a;
      long b;
      try {
         a = Long.parseLong(ab[0]);
         b = Long.parseLong(ab[1]);
      } catch (RuntimeException e) {
         throw new RuntimeException(String.format("Incorrect string input: %s", s));
      }
      return new Lfraction(a, b);
   }

//   private static Lfraction reduceFraction(Lfraction m){
//      long gcd = findGCD(m.n, m.d);
//      if (gcd != 0) {
//         m.n /= gcd;
//         m.d /= gcd;
//
//         if (m.d < 0) {
//            m.n = -m.n;
//            m.d = -m.d;
//         }
//      }
//      return new Lfraction(m.n, m.d);
//   }

   private static long findLCM(long a, long b) {
      return a * b;
//      a = Math.abs(a);
//      b = Math.abs(b);
//      long s = Math.min(a, b);
//      if (s == 0) {
//         return 0;
//      }
//      long l = Math.max(a, b);
//      long lcm = l;
//      while (lcm % s != 0) {
//         lcm += l;
//      }
//
//      return lcm;
   }

   private static long findGCD(long a, long b) {
      if (a == 0){
         return b;
      } if (b == 0){
         return a;
      }
      a = Math.abs(a);
      b = Math.abs(b);
      if (b > a) {
         long tmp = b;
         b = a;
         a = tmp;
      }
      long remainder = a % b;
      return findGCD(b, remainder);
   }
}

