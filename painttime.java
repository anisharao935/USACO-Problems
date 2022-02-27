import java.io.*;
import java.util.*;
public class painttime {
    public static void main (String[] args) throws IOException {
        // read in initial input
        //BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("painttime.in")));
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(input.readLine());
        int N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());
        String inputFence = input.readLine();
        int[] fence = new int[N];
        for (int i=0;i<N;i++) { // convert fence to number code
            fence[i] = (int) inputFence.charAt(i) - (int) ('A'); }
        //System.out.println(Arrays.toString(fence));
        // calculate prefix and suffix sums
        int[] prefix_sums = calculate_sums(fence);
        //System.out.println(Arrays.toString(prefix_sums));
        int[] suffix_sums = reverse(calculate_sums(reverse(fence)));
        //System.out.println(Arrays.toString(suffix_sums));
        // answer queries
        PrintWriter out = new PrintWriter(System.out);
        for (int q=0;q<Q;q++) {
            st = new StringTokenizer(input.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            //System.out.println(a + " " + b);
            //System.out.println(prefix_sums[a-1] + " " + suffix_sums[b]);
            int ans = 0;
            if (a>1) ans += prefix_sums[a-1];
            if (b<N) ans += suffix_sums[b];
            out.println(ans); }
        out.close();
    }
    static int[] reverse(int[] arr) {
        Integer[] newArr = new Integer[arr.length];
        Arrays.setAll(newArr, i->arr[i]);
        List<Integer> arrList = Arrays.asList(newArr);
        Collections.reverse(arrList);
        Object[] reversedArr = arrList.toArray();
        int[] outputArr = new int[arr.length];
        Arrays.setAll(outputArr, i->(int)reversedArr[i]);
        return outputArr;
    }
    static int[] calculate_sums(int[] input) {
        // calculates the prefix sums for a given input fence
        int[] prefix_sums = new int[input.length+1];
        prefix_sums[0] = 0;
        int[] last_appeared = new int[26];
        Arrays.fill(last_appeared, -1);
        for (int i=1;i<=input.length;i++) {
            //System.out.println(Arrays.toString(prefix_sums));
            prefix_sums[i] = prefix_sums[i-1];
            int color = input[i-1];
            if (last_appeared[color] == -1) {
                prefix_sums[i] += 1; }
            else {
                boolean needsNewCoat = false;
                for (int c=0;c<color;c++) {
                    if (last_appeared[c] > last_appeared[color]) needsNewCoat = true; }
                if (needsNewCoat) prefix_sums[i] += 1;
            }
            last_appeared[color] = i; }
        return prefix_sums;
    }
}
