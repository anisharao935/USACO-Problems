/* 2021 January Contest, Silver Problem #1: Dance Mooves
http://usaco.org/index.php?page=viewproblem2&cpid=1086
topics: graph theory, cycles */
import java.io.*;
import java.util.*;
public class mooves {
    public static HashSet<Integer>[] visited;
    public static int[] count;
    public static boolean[] cycled;
    public static void main (String[] args) throws IOException {
        // read in  input
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("test.in")));
        StringTokenizer st = new StringTokenizer(input.readLine());
        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        ArrayList<int[]> dance = new ArrayList<>();
        for (int i=0;i<K;i++) {
            st = new StringTokenizer(input.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            dance.add(new int[] {a, b}); }
        // find each position's cycle in one K
        ArrayList<Integer>[] positions = new ArrayList[N+1];
        int[] current = new int[N+1];
        for (int i=1;i<=N;i++) {
            current[i] = i;
            positions[i] = new ArrayList<>();
            positions[i].add(i); }
        for (int[] step: dance) {
            int a = step[0];
            int b = step[1];
            positions[current[a]].add(b);
            positions[current[b]].add(a);
            int temp = current[a];
            current[a] = current[b];
            current[b] = temp; }
        // count number of positions reached by each number
        cycled = new boolean[N+1];
        visited = new HashSet[N+1];
        for (int i=1;i<=N;i++) visited[i] = new HashSet<Integer>();
        count = new int[N+1];
        for (int i=1;i<=N;i++) {
            if (!cycled[i]) traverse(i, positions, N); }
        // find answer
        PrintWriter out = new PrintWriter(System.out);
        for (int i=1;i<=N;i++) {
            out.println(visited[i].size());
        }
        out.close(); }


    static void traverse(int n, ArrayList<Integer>[] positions, int N) {
        /* follows a position n through its cycle and all linked positions */
        HashSet<Integer> toAdd = new HashSet<>();
        toAdd.add(n);
        int[] timesInCycle = new int[N+1];
        timesInCycle[n] += 1;
        HashSet<Integer> visitedPositions = new HashSet<>();
        visitedPositions.addAll(positions[n]);
        int current = positions[n].get(positions[n].size()-1);
        int next;
        while (!toAdd.isEmpty()) {
            timesInCycle[current] += 1;
            if (timesInCycle[current] < 2) toAdd.add(current);
            next = positions[current].get(positions[current].size()-1);
            visitedPositions.addAll(positions[current]);
            if (toAdd.contains(current) && timesInCycle[current] >= 2) {
                toAdd.remove(current);
                cycled[current] = true;
                visited[current] = visitedPositions;
            }
            current = next;
        }
    }


}

