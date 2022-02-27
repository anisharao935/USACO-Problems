/* 2020 US Open Contest, Silver Problem #2: Cereal
http://www.usaco.org/index.php?page=viewproblem2&cpid=1039 topics: recursion, simulation */
import java.io.*;
import java.util.*;
public class cereal {
    public static int[] cowToCereal;
    public static int[] cerealToCow;
    public static boolean[] hasNext;
    public static cow[] cows;
    public static int count;
    public static void main (String[] args) throws IOException {
        // read in data
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("cereal.in")));
        StringTokenizer st = new StringTokenizer(input.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        cows = new cow[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(input.readLine());
            cows[i] = new cow(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), i);
        }
        // set up variables
        hasNext = new boolean[N]; // keeps track of whether each cow has a second choice still available (i.e. has only used first or has not been considered yet)
        cowToCereal = new int[N]; // keeps track of which cereal 1 to M cow has
        cerealToCow = new int[M+1]; // keeps track of which cow 0 to N-1 has which cereal
        Arrays.fill(hasNext, true);
        Arrays.fill(cowToCereal, -1);
        Arrays.fill(cerealToCow, -1);
        // solve by going through each cow -- backwards
        int[] answers = new int[N];
        count = 0;
        for (int i=N-1;i>=0;i--) {
            assign(i, 1);
            answers[i] = count;
        }
        // write result
        //PrintWriter out = new PrintWriter(System.out);
        PrintWriter out = new PrintWriter(new FileWriter("cereal.out"));
        for (int ans: answers) out.println(ans);
        out.close();

    }

    static void assign(int i, int c) {
        /* assigns cow i their cereal choice c (where c==1 or c==2) */
        cow curr = cows[i];
        int cerealChoice = curr.get_choice(c);
        // if desired cereal is not taken can easily assign cow the cereal
        if (cerealToCow[cerealChoice] == -1) {
            cerealToCow[cerealChoice] = i;
            cowToCereal[i] = cerealChoice;
            if (c==2) hasNext[i] = false; // remember to keep track of if the cow has already used up both their choices
            count += 1; // this is the only event that can increase overall count -- other changes will keep overall count the same (count can never decrease)
        }
        else {
            // [attempt] to remove the cow blocking this cow from having its choice
            cow occupying = cows[cerealToCow[cerealChoice]];
            if (occupying.index < curr.index) { // cannot remove a cow with a lower index since that cow came earlier in the line
                if (c==1) assign(i, 2); // only option is to go to second choice if that is a possibility
                else cowToCereal[i] = -1;
            }
            else {
                // otherwise you can remove the blocking cow, and apply the same above process to it recursively
                cerealToCow[cerealChoice] = i;
                cowToCereal[i] = cerealChoice;
                if (c==2) hasNext[i] = false;
                if (hasNext[occupying.index]) {
                    assign(occupying.index, 2);
                }
                else cowToCereal[occupying.index] = -1;
            }
        }
    }

    static class cow {
        int first, second, index;
        cow (int a, int b, int c) {
            first = a;
            second = b;
            index = c;
        }
        int get_choice(int choice) {
            if (choice==1) return first;
            if (choice==2) return second;
            return -1;
        }
        public String toString() {
            return first + " " + second;
        }
    }
}
