import java.util.*;

/** Container class to different classes, that makes the whole
 * set of classes one class formally.
 * @since 1.8
 */
public class GraphTask {

   /** Main method. */
   public static void main (String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /** Actual main method to run examples and everything. */
   public void run() {
      // Aruande test 1
      Graph a = new Graph("A");
      a.createVertex("v2");
      a.createVertex("v1");
      a.createArc("av1_v2", a.first, a.first.next);
      System.out.println("Graph given:");
      System.out.println(a);
      System.out.println ("Looking for path from " + a.first + " to " + a.first.next);
      List<Arc> resulta = a.findShortestPathByRemovingUpToKWalls(a.first, a.first.next, 0);
      System.out.println("Shortest path: " + resulta);

      // Aruande test 2
      Graph b = new Graph("B");
      b.createVertex("v2");
      b.createVertex("v1");
      b.createArc("av1_v2", b.first, b.first.next);
      b.addWallsToRandomVerticesInGraph(100);
      System.out.println("Graph given:");
      System.out.println(b);
      System.out.println ("Looking for path from " + b.first + " to " + b.first.next);
      List<Arc> resultb = b.findShortestPathByRemovingUpToKWalls(b.first, b.first.next, 0);
      System.out.println("Shortest path: " + resultb);

      // Aruande testid 3, 4, 5, 6
      Graph c = new Graph ("C");
      c.createRandomGraphWithWalls (10, 20, 40);
      System.out.println("Graph given:");
      System.out.println(c);
      System.out.println ("Looking for path from " + c.first + " to " + c.first.next.next.next.next.next);
      List<Arc> resultc = c.findShortestPathByRemovingUpToKWalls(c.first, c.first.next.next.next.next.next, 1);
      System.out.println("Shortest path: " + resultc);

      // Aruande test 7
      Graph d = new Graph ("D");
      d.createRandomGraphWithWalls (2500, 6000, 40);
      Random rnd = new Random();
      Vertex to = d.first;
      for (int i = 0; i < rnd.nextInt(2500); i++) {
         to = to.next;
      }
      System.out.println("n = 2500");
      System.out.println("m = 6000");
      System.out.println("p = 40");
      System.out.println("k = 20");
      System.out.println ("Looking for path from " + d.first + " to " + to);
      long start = System.currentTimeMillis();
      List<Arc> resultd = d.findShortestPathByRemovingUpToKWalls(d.first, to, 20);
      long end = System.currentTimeMillis();
      System.out.println("Time in milliseconds: " + (end - start));
      System.out.println("Shortest path: " + resultd);
   }

   /** Vertex represents one circle in the graph. Blocked vertices are
    * represented by two '|' symbols surrounding the vertex.
    */
   class Vertex {

      private String id;
      private Vertex next;
      private Arc first;
      private int info = 0;

      private List<DistKPair> distKPairs = new ArrayList<>(Arrays.asList(new DistKPair(this)));
      private boolean wall = false;

      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         if (wall) {
            return "|" + id + "|";
         }
         return id;
      }

   }


   /** Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;

      Arc (String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

   } 


   /** This header represents a graph.
    */ 
   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;
      private List<DistKPair> distKPairsOfVertices = new ArrayList<>();
      List<Arc> result = new ArrayList<>();

      Graph (String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph (String s) {
         this (s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty ("line.separator");
         StringBuffer sb = new StringBuffer (nl);
         sb.append (id);
         sb.append (nl);
         Vertex v = first;
         while (v != null) {
            sb.append (v.toString());
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append (" ");
               sb.append (a.toString());
               sb.append (" (");
               sb.append (v.toString());
               sb.append ("->");
               sb.append (a.target.toString());
               sb.append (")");
               a = a.next;
            }
            sb.append (nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         return res;
      }

      public Arc createArc (String aid, Vertex from, Vertex to) {
         Arc res = new Arc (aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * @param n number of vertices added to this graph
       */
      public void createRandomTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + (n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               createArc ("a" + varray [vnr].toString() + "_"
                  + varray [i].toString(), varray [vnr], varray [i]);
               createArc ("a" + varray [i].toString() + "_"
                  + varray [vnr].toString(), varray [i], varray [vnr]);
            }
         }
      }

      /**
       * Create a connected directed random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * @param n number of vertices added to this graph
       */
      public void createRandomDirectedTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + (n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               createArc ("a" + varray[vnr] + "_" + varray[i], varray[vnr], varray[i]);
            }
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int [info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res [i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraphWithWalls (int n, int m, int p) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException 
               ("Impossible number of edges: " + m);
         first = null;
         createRandomTree (n);       // n-1 edges created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         addWallsToRandomVerticesInGraph(p); // add walls
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j) 
               continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0) 
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected [j][i] = 1;
            edgeCount--;  // a new edge happily created
         }
      }

      /**
       * Create a connected (directed, no loops, no multiple
       * arcs) random graph with n vertices and m arcs.
       * @param n number of vertices
       * @param m number of arcs
       * @param p probability in percentages by which walls are created on vertices
       */
      public void createRandomGraphWithWalls (int n, int m, int p) {
         if (n <= 0)
            return;
         if (n > 5000)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1))
            throw new IllegalArgumentException
               ("Impossible number of arcs: " + m);
         first = null;
         createRandomDirectedTree(n);       // n-1 arcs created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         addWallsToRandomVerticesInGraph(p); // add walls
         int[][] connected = createAdjMatrix();
         int arcCount = m - n + 1;  // remaining arcs
         while (arcCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j)
               continue;  // no loops
            if (connected [i][j] != 0)
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            arcCount--;  // a new arc happily created
         }
      }
      /**
       * Add walls to random vertices in graph.
       * @param p probability in percentages by which walls are created
       */
      public void addWallsToRandomVerticesInGraph(int p){
         Random rnd = new Random();
         Vertex current = this.first;
         while (current != null){
            if (rnd.nextInt(99) < p) {
               current.wall = true;
            }
            current = current.next;
         }
      }

      /**
       * Finds the shortest path from one vertex to another when allowed to bypass k number of walls.
       * @param from starting vertex
       * @param to destination vertex
       * @param k number of walls allowed to bypass
       * @return a list of Arcs that were used in the shortest path or empty list '[]' if no path exists
       */
      public List<Arc> findShortestPathByRemovingUpToKWalls(Vertex from, Vertex to, int k){

         if (from == null) {
            throw new RuntimeException("Start vertex cannot be null!");
         }
         if (to == null) {
            throw new RuntimeException("End vertex cannot be null!");
         }
         if (k < 0) {
            throw new RuntimeException("k cannot be negative!");
         }
         DistKPair dKPair = from.distKPairs.get(0);
         dKPair.k = k;
         dKPair.dist = 0;
         distKPairsOfVertices.add(dKPair);
         while (!distKPairsOfVertices.isEmpty()){
            dKPair = distKPairsOfVertices.remove(0);
            System.out.println(dKPair);
            Vertex curVtx = dKPair.owner;
            if (curVtx == to) {
               return createPathList(dKPair);

            }
            Arc curArc = curVtx.first;
            int travelCost = 1; // Allows adding distance to arc, for example: int travelCost = curArc.length;
            int n = 0;
            if (curVtx.wall){
               n = 1;
            }
            if (dKPair.k >= n) {
               while (curArc != null) {
                  int i = 0;
                  List<DistKPair> curTargetDistK = curArc.target.distKPairs;

                  int size = curTargetDistK.size();
                  boolean isDifferent = false;
                  while (curTargetDistK.size() > i) {
                     if (curTargetDistK.get(i).dist >= dKPair.dist + travelCost && curTargetDistK.get(i).k <= dKPair.k - n) {
                        DistKPair element = curTargetDistK.get(i);
                        curTargetDistK.remove(element);
                        distKPairsOfVertices.remove(element);
                        continue;
                     } else if (curTargetDistK.get(i).dist > dKPair.dist + travelCost && curTargetDistK.get(i).k > dKPair.k - n ||
                             curTargetDistK.get(i).dist < dKPair.dist + travelCost && curTargetDistK.get(i).k < dKPair.k - n) {
                        isDifferent = true;
                     }
                     i++;
                  }
                  if (curTargetDistK.size() < size || isDifferent) {
                     int wallsToBreak = dKPair.k;
                     if (curVtx.wall){
                        wallsToBreak -= 1;
                     }
                     DistKPair newPair = new DistKPair(dKPair.dist + travelCost, wallsToBreak, curArc.target, dKPair);
                     curTargetDistK.add(newPair);
                     binaryInsertion(newPair);
                  }
                  curArc = curArc.next;
               }
            }
         }
         return createPathList(null);
      }
      /**
       * Creates a list of arcs that were used for the path.
       * @param destination distance and k pair of destination vertex, returns null if param = null
       * @return a list of Arcs that were used in the shortest path, returns empty list if no path exists
       */
      private List<Arc> createPathList(DistKPair destination){

         DistKPair lastStep = destination;
         if (lastStep == null) {
            return result;
         }
         while (lastStep.parent != null){
            Arc current = lastStep.parent.owner.first;
            while (current != null) {
               if (current.target == lastStep.owner) {
                  result.add(0, current);
                  break;
               }
               current = current.next;
            }
            lastStep = lastStep.parent;
         }

         return result;
      }

      /**
       * Adds DistKPair to distKPairsOfVertices list in ascending order based on dist param of dKPair using binary search.
       * @param dKPair distance and k pair with the lowest current distance for a specific vertex , throws RuntimeException if null
       */
      private void binaryInsertion(DistKPair dKPair){
         if (dKPair == null) {
            throw new RuntimeException("dKPair of current vertex must not be null!");
         }
         int b = dKPair.dist;
         int current = distKPairsOfVertices.size() - 1;
         if (current < 0) {
            current = 0;
         }
         int left = 0;
         int right = distKPairsOfVertices.size();
         while (!(current == left && current == right)) {
            if (distKPairsOfVertices.get(current).dist < b) {
               left += (right - left) / 2 + 1;
               current = left;
            } else if (distKPairsOfVertices.get(current).dist > b) {
               right -= (right - left) / 2 + 1;
               current = right;
            } else {
               break;
            }
         }
         if (distKPairsOfVertices.isEmpty()) {
            distKPairsOfVertices.add(dKPair);
         } else {
            distKPairsOfVertices.add(current, dKPair);
         }
      }

   }
   /** Custom data type for vertex.
    * public int dist: distance between start vertex and owner vertex
    * public int k: number of walls that can be removed
    * public Vertex owner: vertex that has these dist and k values
    * public DistKPair parent: DistKPair of the previous vertex in path
    */
   class DistKPair {
      public int dist = Integer.MAX_VALUE;
      public int k = Integer.MIN_VALUE;
      public Vertex owner;
      public DistKPair parent;

      DistKPair(int distanceFromStart, int wallRemovalsLeft, Vertex current, DistKPair previous){
         dist = distanceFromStart;
         k = wallRemovalsLeft;
         owner = current;
         parent = previous;
      }
      DistKPair(Vertex current){
         owner = current;
      }

      @Override
      public String toString() {
         return dist + "|" + k + "-" + owner;
      }
   }
}

