/* 2018 February Contest, Gold Problem #1: Snow Boots
http://www.usaco.org/index.php?page=viewproblem2&cpid=813
topics: sorting, data structures */
import java.io.*;
import java.util.*;
public class snowboots {
    public static void main (String[] args) throws IOException {
        // read in input
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("snowboots.in")));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int N = Integer.parseInt(st.nextToken());
        int B = Integer.parseInt(st.nextToken());
        LinkedList<Pair> tiles = new LinkedList<>(); // set of tiles sorted by depth of snow
        st = new StringTokenizer(in.readLine());
        for (int i=0;i<N;i++) {
            // ignore first and last tile as these will be considered automatically later in solution
            if (i!=0 && i!=N-1) tiles.add(new Pair(Integer.parseInt(st.nextToken()), i, true));
        }
        LinkedList<Pair> boots = new LinkedList<>();
        for (int i=0;i<B;i++) {
            st = new StringTokenizer(in.readLine());
            boots.add(new Pair(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), i)); }
        Collections.sort(tiles);
        Collections.sort(boots);
        // set up variables to solve
        boolean[] canCross = new boolean[B];
        /* first set is defined as intervals of tiles where the endpoints are less than or equal to a given depth X
        (i.e. intervals that must be fully stepped over other than endpoints if boots can only go in depth <= X)
        where X will increase as we go through the tiles [and therefore sizes of intervals will decrease] */
        TreeSet<Pair> intervals = new TreeSet<>();
        // this keeps track of the same intervals as the previous, but in sorted order of their length
        TreeSet<Pair> intervalsByLength = new TreeSet<>((o1, o2) -> {
            if (o1.length() == o2.length()) return o1.x - o2.x;
            return o1.length() -  o2.length(); });
        // automatically add original interval as being from 0 to N-1 (since we know both of these are equal to 0)
        intervals.add(new Pair(0, N-1, false));
        intervalsByLength.add(new Pair(0, N-1, false));
        // solve -- iterate through tiles in increasing order of snow depth to answer queries for boots
        while (!tiles.isEmpty()) {
            Pair curr = tiles.poll();
            // if we have processed all tiles with depth less than current boot then we can now check if that boot can cross
            while (curr.x > boots.get(0).x) {
                Pair boot = boots.poll();
                canCross[boot.i] = boot.y >= intervalsByLength.last().length();
            }
            // split interval that our current tile is in
            Pair currInterval = intervals.lower(new Pair(curr.i, curr.i, false));
            Pair i_one = new Pair(currInterval.x, curr.i, false), i_two = new Pair(curr.i, currInterval.y, false);
            intervals.remove(currInterval);
            intervals.add(i_one); intervals.add(i_two);
            intervalsByLength.remove(currInterval);
            intervalsByLength.add(i_one); intervalsByLength.add(i_two);
        }
        /* all boots left are greater than every tile's depth and so can automatically cross
        (since step distance is guaranteed to be at least 1) */
        while (!boots.isEmpty()) canCross[boots.poll().i] = true;
        // write result
        //PrintWriter out = new PrintWriter(System.out);
        PrintWriter out = new PrintWriter(new FileWriter("snowboots.out"));
        for (int i=0;i<B;i++) {
            if (canCross[i]) out.println(1);
            else out.println(0);
        }
        out.close();

    }

    static class Pair implements Comparable<Pair> {
        int x, y, i;
        Pair(int a, int b, boolean needsIndex) {
            x = a;
            if (needsIndex) i=b;
            else y = b;
        }
        Pair(int a, int b, int c) {
            x = a;
            y = b;
            i = c;
        }
        @Override
        public int compareTo(Pair o2) {
            return x-o2.x;
        }
        int length() {return y-x;}
        public String toString() {
            return x + " " + y + " " + i;
        }
    }
}
