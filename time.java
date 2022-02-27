/* 2020 January Contest, Gold Problem #1: Time is Mooney
http://www.usaco.org/index.php?page=viewproblem2&cpid=993
topics: dynamic programming, greedy */
import java.io.*;
import java.util.*;
public class time {
    public static void main (String[] args) throws IOException {
        // read in input data
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("time.in")));
        StringTokenizer st = new StringTokenizer(input.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int C = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(input.readLine());
        int[] cityVals = new int[N+1]; // keeps track of value of each city
        int maxVal = 0;
        for (int i = 1; i <= N; i++) {
            cityVals[i] = Integer.parseInt(st.nextToken());
            maxVal = Math.max(cityVals[i], maxVal); }
        ArrayList<Integer>[] connections = new ArrayList[N+1]; // keeps track of one-way paths
        for (int i=1;i<=N;i++) connections[i] = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(input.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            connections[a].add(b);
        }
        // create dp array
        /* dp[t][c] is maximum profit that could be made ending at city c on day t (not including cost of travel for now)
        maximum number of days is set as highest value of any city divided by C [in practice would probably be smaller but this is easy to calculate]  */
        int[][] dp = new int[maxVal/C][N+1];
        for (int[] row : dp) Arrays.fill(row, -1);
        dp[0][1] = 0; // initial state -- city 1, day 0, profit 0
        for (int day=0;day<dp.length-1;day++) {
            for (int city=1;city<=N;city++) {
                if (dp[day][city] != -1) { // ignore if state is unattainable, represented by -1s (e.g. city 1 on day 2)
                    for (int next: connections[city]) {
                        dp[day+1][next] = Math.max(dp[day+1][next], dp[day][city] + cityVals[next]); // constructive dp, values calculated going forward rather than looking back
                    } }
            } }
        // find best choice allowing Bessie to end at city 1, now considering cost of travel as well
        int best = 0;
        for (int day=0;day<dp.length;day++) {
            if (dp[day][1] != -1) best = Math.max(best, dp[day][1] - C*day*day); }
        // write result
        PrintWriter out = new PrintWriter("time.out");
        out.println(best);
        out.close();
    }
}
