import edu.princeton.cs.algs4.*;

public class test {
    private static node[][] nodeSet;
    private static int ROW;
    private static int COL;
    private static node END;
    private static node START;

    public static void generateMaze(int n, int m){
        test.nodeSet = new node[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j <m; j++){
                nodeSet[i][j] = new node();
                nodeSet[i][j].setPosition(i, j);
                nodeSet[i][j].setIsBlock(false);
                if( i ==0 && j == 0)
                    START = nodeSet[i][j];
                if( i== n-1 && j == m-1)
                    END = nodeSet[i][j];
            }
        }
        ROW = nodeSet.length;
        COL = nodeSet[0].length;
    }

    public static void nextNode(node currNode){
        int i = currNode.getRow();
        int j = currNode.getCol();
        if(i -1 >=0 && j -1 >= 0 && i +1 < ROW && j + 1 < COL && !nodeSet[i-1][j].getIsBlock())
            nodeSet[i-1][j].setParent(currNode);
        if(i -1 >=0 && j -1 >= 0 && i +1 < ROW && j + 1 < COL && !nodeSet[i+1][j].getIsBlock())
            nodeSet[i+1][j].setParent(currNode);
        if(i -1 >=0 && j -1 >= 0 && i +1 < ROW && j + 1 < COL && !nodeSet[i][j-1].getIsBlock())
            nodeSet[i][j-1].setParent(currNode);
        if(i -1 >=0 && j -1 >= 0 && i +1 < ROW && j + 1 < COL && !nodeSet[i][j+1].getIsBlock())
            nodeSet[i][j+1].setParent(currNode);
    }

    public static String toString(node n){
        return n.getRow() + " " + n.getCol();
    }

    public static void main(String[] args) {
        int n = 10;
        int m = 10;
        generateMaze(n,m);
        nextNode(nodeSet[5][5]);
        StdOut.print("good "+ toString(nodeSet[5][4].getParent()));
    }
}
