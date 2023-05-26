import java.util.*;

public class MazeGenerator {
    private int width = 0;
    private int height = 0;
    private int[][] maze;
    private boolean[][] visited;
    private static final int PATH = 0;
    private static final int WALL = 1;

    public MazeGenerator(int row, int col) {
        this.width = col;
        this.height = row;
        this.maze = new int[height][width];
        this.visited = new boolean[height][width];
    }

    public void generateMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = WALL;
                visited[i][j] = false;
            }
        }
        generatePath(0, 0);
        maze[0][0] = PATH;                          // Start point
        maze[height - 1][width - 1] = PATH;         // End point
        if(height>2&&width>1)
            if(maze[height-2][width-1] == WALL)
                maze[height-2][width-1] = PATH;
    }

    private void generatePath(int row, int col) {
        visited[row][col] = true;
        int[][] directions = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}};
        Collections.shuffle(Arrays.asList(directions));
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            int midRow = row + dir[0] / 2;
            int midCol = col + dir[1] / 2; 
            if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width && !visited[newRow][newCol]) {
                maze[newRow][newCol] = PATH;
                maze[midRow][midCol] = PATH;
                generatePath(newRow, newCol);
            }
        }
    }

    public static int[][] getMaze(int n, int m){
        MazeGenerator mazeGenerator = new MazeGenerator(n, m);
        mazeGenerator.generateMaze();
        return mazeGenerator.maze;
    }

    public void printMaze() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j] == WALL) {
                    System.out.print("# ");  // Wall
                } else {
                    System.out.print(". ");  // Path
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int n = new Random().nextInt(50);
        int m = new Random().nextInt(50);
        
        MazeGenerator mazeGenerator = new MazeGenerator(n, m);
        mazeGenerator.generateMaze();
        mazeGenerator.printMaze();
    }
}
