/* 2021 US Open, Silver Problem #1: Maze Tac Toe
http://usaco.org/index.php?page=viewproblem2&cpid=1134
topics: grids/floodfill, breadth-first search */
import java.io.*;
import java.util.*;
public class maze {
    public static String[][] grid;
    public static boolean[][][] visitedstates;
    public static HashSet<Integer> configurations;
    public static int N;
    public static ArrayList<int[]> directions;
    public static void main (String[] args) throws IOException {
        // read in input
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(input.readLine());
        grid = new String[N][N];
        int[] start = new int[2];
        for (int i=0;i<N;i++) {
            String[] line = input.readLine().split("");
            for (int j=0;j<N;j++) {
                String section = line[3*j] + line[3*j+1] + line[3*j+2];
                if (section.equals("###")) grid[i][j] = "#";
                else {
                    if (section.equals("...")) grid[i][j] = ".";
                    else {
                        if (section.equals("BBB")) {
                            grid[i][j] = ".";
                            start[0] = i;
                            start[1] = j; }
                        else grid[i][j] = section;
                    } }
            } }

        // set up variables for dfs
        configurations = new HashSet<>();
        directions = new ArrayList<>(4);
        directions.add(new int[] {0, 1});
        directions.add(new int[] {0, -1});
        directions.add(new int[] {1, 0});
        directions.add(new int[] {-1, 0});
        int[][] board = new int[3][3];
        for (int[] line: board) Arrays.fill(line, -1);
        state startingState = new state(start[0], start[1], board);
        visitedstates = new boolean[N][N][(int) Math.pow(3, 9)];
        visitedstates[start[0]][start[1]][calculate_id(board)] = true;
        // carry out dfs
        dfs(startingState);
        // write result
        PrintWriter out = new PrintWriter(System.out);
        out.println(configurations.size());
        out.close();

    }

    static int calculate_id(int[][] boardState) {
        // represents a given 3x3 grid as a base-3 number
        int[] base3 = new int[9];
        base3[0] = 1;
        for (int i=1;i<9;i++) base3[i] = base3[i-1]*3;
        int ans = 0;
        int digit = 0;
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                ans += (boardState[i][j]+1)*base3[digit];
                digit += 1;
            }
        }
        return ans;


    }

    static void dfs(state S) {
        /* performs depth-first-search from given state, keeping track of winning game-board configurations found */
        if (S.has_won()) {
            int id = calculate_id(S.get_board());
            configurations.add(id);
        }
        else {
            for (int[] dir: directions) {
                state next = S.move(dir[0], dir[1]);
                if (next != null) {
                    int id = calculate_id(next.get_board());
                    if (!visitedstates[next.i][next.j][id]) {
                        visitedstates[next.i][next.j][id] = true;
                        dfs(next);
                    }
                } } }
    }

    static boolean is_moo(int a, int b, int c) {
        // checks if a given three numbers where all are either 0 and 1s ('O's and 'M's) create a MOO
        int ones = 0;
        int zeros = 0;
        if (a==1) ones += 1;
        if (a==0) zeros += 1;
        if (b==1)  ones += 1;
        if (b==0) zeros += 1;
        if (c==1)  ones += 1;
        if (c==0) zeros += 1;
        return ones==1 && zeros==2 && (a==1 || c==1);
    }
    static boolean in_grid(int a, int b) {
        return 0<=a && a<N && 0<=b && b<N;
    }

    static class state {
        /* class that keeps track of a given unique state, describing it with current location (i, j) in the maze and its current tic-tac-toe gameboard */
        int i, j;
        int[][] gameboard;
        public state (int a, int b, int[][] c) {
            this.i = a;
            this.j = b;
            this.gameboard = c;
        }
        public void update(int x, int y, int letter) {
            if (gameboard[x][y] == -1) gameboard[x][y] = letter;
        }
        public state move (int change_r, int change_c) {
            // finds new state generated when moving this state over by a given amount of row and columns
            int a = i + change_r;
            int b = j + change_c;
            if (in_grid(a, b) && !grid[a][b].equals("#")) {
                int[][] new_board = new int[3][3];
                for (int i=0;i<3;i++) {
                    for (int j=0;j<3;j++) new_board[i][j] = gameboard[i][j];
                }
                state output = new state(a, b, new_board);
                if (grid[a][b].length() == 3) {
                    int x = (Character.getNumericValue(grid[a][b].charAt(1))) - 1;
                    int y = (Character.getNumericValue(grid[a][b].charAt(2))) - 1;
                    int letter;
                    if (grid[a][b].charAt(0)=='M') letter = 1;
                    else letter = 0;
                    output.update(x, y, letter);
                }
                return output;
            }
            return null;
        }
        public String toString() {
            return i + " " + j + "\n" + Arrays.toString(this.gameboard[0]) + "\n" + Arrays.toString(this.gameboard[1]) +
                    "\n" + Arrays.toString(this.gameboard[2]);
        }

        private boolean has_won() {
            // check each row
            for (int i=0;i<3;i++) {
                if (is_moo(gameboard[i][0], gameboard[i][1], gameboard[i][2])) return true;
            }
            // check columns
            for (int j=0;j<3;j++) {
                if (is_moo(gameboard[0][j], gameboard[1][j], gameboard[2][j])) return true;
            }
            // check diagonals
            if (is_moo(gameboard[0][0], gameboard[1][1], gameboard[2][2]) || is_moo(gameboard[2][0], gameboard[1][1], gameboard[0][2])) return true;
            return false;
        }

        public int[][] get_board() {
            return gameboard;
        }
    }
}
