import java.util.*;
import edu.princeton.cs.algs4.*;

public class Astar {
    private static double HV_COST = 1;
    private static double DI_COST = 1.4;
    private node[][] nodeSet;
    private int ROW;
    private int COL;
    private PriorityQueue<node> openSet;
    private HashSet<node> closeSet;
    private node END;
    private node START;
    private double epsi;
    private node currentPosition;
    private List<node> currentPath;
    private List<node> attackedNode;

    public Astar(int n, int m, boolean maze[][], double epsi){
        this.epsi = epsi;
        this.nodeSet = new node[n][m];
        for(int i = n-1; i >=0; i--){
            for(int j = m-1; j >=0; j--){
                nodeSet[i][j] = new node(i,j);
                if(i==n-1 && j ==m-1)
                    this.END = nodeSet[i][j];
                if(i==0 && j ==0)
                    this.START = nodeSet[i][j];
                nodeSet[i][j].setBlock(maze[i][j]); //0->false
                nodeSet[i][j].setH(1, END);
            }
        }
        this.closeSet = new HashSet<node>();
        this.openSet = new PriorityQueue<node>(new Comparator<node>() {
            @Override
            public int compare(node node0, node node1) {
                return Double.compare(node0.getF(), node1.getF());
            }
        });
        this.ROW = nodeSet.length;
        this.COL = nodeSet[0].length;
        this.currentPosition = START;
        // this.currentPosition = nodeSet[4][0];
    }

    public List<node> ARAStarFindPath(){
        double epsi = getEpsi();
        List<node> tempPath = new ArrayList<node>();
        while(epsi > 1){
            tempPath = findPath();
            setEpsi(epsi-=0.2);
        }
        return tempPath;
    }

    private List<node> findPath(){
        this.openSet.add(getCurrentPosition());
        while(!openSet.isEmpty()){
            node currentNode = openSet.poll();
            closeSet.add(currentNode);
            if(isEND(currentNode)){
                this.openSet.clear();
                this.closeSet.clear();
                return getPath(currentNode);
            }
            else{
                nextNode(currentNode);
            }        
        }
        return new ArrayList<node>();
    }

    public void move(){
        List<node> path = findPath();
        if(path.size()>1){
            path.remove(0);
            setCurrentPosition(path.get(0));
            getCurrentPosition().setParent(null);
        }     
    }

    public void nextNode(node currentNode){
        int r = currentNode.getRow();
        int c = currentNode.getCol();
        if(r == 0){
            checkNode(this.nodeSet[r+1][c], currentNode, HV_COST);
            if(c ==0){
                checkNode(this.nodeSet[r][c+1], currentNode, HV_COST);
                checkNode(this.nodeSet[r+1][c+1], currentNode, DI_COST);
            }
            else if(c == this.COL-1){
                checkNode(this.nodeSet[r][c-1], currentNode, HV_COST);
                checkNode(this.nodeSet[r+1][c-1], currentNode, DI_COST);
            }   
            else{
                checkNode(this.nodeSet[r][c-1], currentNode, HV_COST);
                checkNode(this.nodeSet[r][c+1], currentNode, HV_COST);
                checkNode(this.nodeSet[r+1][c+1], currentNode, DI_COST);
                checkNode(this.nodeSet[r+1][c-1], currentNode, DI_COST);
            }
        }else if(r == this.ROW-1){
            checkNode(this.nodeSet[r-1][c], currentNode, HV_COST);
            if(c ==0){
                checkNode(this.nodeSet[r][c+1], currentNode, HV_COST);
                checkNode(this.nodeSet[r-1][c+1], currentNode, DI_COST);
            }
            else if(c == this.COL-1){
                checkNode(this.nodeSet[r-1][c-1], currentNode, DI_COST);
                checkNode(this.nodeSet[r][c-1], currentNode, HV_COST);
            }
            else{
                checkNode(this.nodeSet[r][c-1], currentNode, HV_COST);
                checkNode(this.nodeSet[r][c+1], currentNode, HV_COST);
                checkNode(this.nodeSet[r-1][c+1], currentNode, DI_COST);
                checkNode(this.nodeSet[r-1][c-1], currentNode, DI_COST);
            }
        }else if(c == 0){
            checkNode(this.nodeSet[r][c+1], currentNode, HV_COST);
            checkNode(this.nodeSet[r-1][c], currentNode, HV_COST);
            checkNode(this.nodeSet[r+1][c], currentNode, HV_COST);
            checkNode(this.nodeSet[r-1][c+1], currentNode, DI_COST);
            checkNode(this.nodeSet[r+1][c+1], currentNode, DI_COST);
        }else if(c == this.COL-1){
            checkNode(this.nodeSet[r][c-1], currentNode, HV_COST);
            checkNode(this.nodeSet[r-1][c], currentNode, HV_COST);
            checkNode(this.nodeSet[r+1][c], currentNode, HV_COST);
            checkNode(this.nodeSet[r-1][c-1], currentNode, DI_COST);
            checkNode(this.nodeSet[r+1][c-1], currentNode, DI_COST);
        }else{
            checkNode(this.nodeSet[r][c+1], currentNode, HV_COST);
            checkNode(this.nodeSet[r-1][c+1], currentNode, DI_COST);
            checkNode(this.nodeSet[r+1][c+1], currentNode, DI_COST);
            checkNode(this.nodeSet[r][c-1], currentNode, HV_COST);
            checkNode(this.nodeSet[r-1][c-1], currentNode, DI_COST);
            checkNode(this.nodeSet[r+1][c-1], currentNode, DI_COST);
            checkNode(this.nodeSet[r-1][c], currentNode, HV_COST);
            checkNode(this.nodeSet[r+1][c], currentNode, HV_COST);
        }
    } 

    public void checkNode(node childNode, node fatherNode,double gCost){
        if(!childNode.getIsBlock() && !getCloseSet().contains(childNode)){
            if(!getOpenSet().contains(childNode)){
                childNode.setData(fatherNode, gCost, getEpsi());
                getOpenSet().add(childNode);
            }
            else{
                if(childNode.refreshG(childNode, gCost, getEpsi())){
                    getOpenSet().remove(childNode);
                    getOpenSet().add(childNode);
                }
            }
        }
    }

    private boolean isEND(node currentNode) {
        return currentNode.compareTo(END);
    }

    public void setEpsi(double epsi) {
        this.epsi = epsi;
    }
    
    public void attack(int i, int j){
        this.attackedNode.add(getNodeSet()[i][j]);
        int currentRow = getCurrentPosition().getRow();
        int currentCol = getCurrentPosition().getCol();
        if(currentCol == j && currentRow == i)
            return;
        if(i <= ROW-1 && i >=0 && j <= COL-1 && j >= 0 )
            this.nodeSet[i][j].setBlock(true);
    }

    public void setBlock(int i, int j){
        int currentRow = getCurrentPosition().getRow();
        int currentCol = getCurrentPosition().getCol();
        if(currentCol == j && currentRow == i)
            return;
        if(i <= ROW-1 && i >=0 && j <= COL-1 && j >= 0 )
            this.nodeSet[i][j].setBlock(true);
    }

    public void setCurrentPosition(node currentNode){
        this.currentPosition = currentNode;
    }
    
    public node getCurrentPosition(){
        return this.currentPosition;
    }

    public List<node> getPath(node currentNode){
        List<node> path = new ArrayList<node>();
        path.add(currentNode);
        node parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }
    
    public double getEpsi(){
        return this.epsi;
    }

    public node[][] getNodeSet(){
        return this.nodeSet;
    }

    public PriorityQueue<node> getOpenSet(){
        return this.openSet;
    }
    
    public HashSet<node> getCloseSet(){
        return this.closeSet;
    }

    public String toString() {
        String str = "";
        List<node> path = findPath();
        for(int i=0; i<this.nodeSet.length;i++){
            for(int j =0; j <this.nodeSet[0].length;j++){
                if(path.contains(this.nodeSet[i][j])){
                    if(this.nodeSet[i][j].compareTo(START)){
                        str +="I* ";
                        continue;
                    }
                    if(this.nodeSet[i][j] .compareTo(END)){
                        str +="F* ";
                        continue;
                    }
                    str+="* ";
                    continue;
                }
                if(this.nodeSet[i][j] .compareTo(START))
                    str += "I ";
                else if(this.nodeSet[i][j] .compareTo(END))
                    str += "F ";
                else if(this.nodeSet[i][j].getIsBlock())
                    str+="B ";
                else
                    str+="- ";
            }
            str += "\n";
        }
        return str;
    }
    public static void main(String[] args) {
        int n = 7;
        int m = 6;
        boolean maze[][] = new boolean[n][m];
        for(int i =0;i<n;i++){
            for(int j =0;j<m;j++){
                maze[i][j] = false;
            }
        }
        Astar ss = new Astar(n,m,maze,1);
        ss.setBlock(1,0);
        ss.setBlock(2,0);
        ss.setBlock(1,2);
        ss.setBlock(1,3);
        ss.setBlock(1,4);
        ss.setBlock(2,2);
        ss.setBlock(2,3);
        ss.setBlock(2,4);
        ss.setBlock(3,2);
        ss.setBlock(4,1);
        ss.setBlock(4,2);
        ss.setBlock(6,0);
        ss.setBlock(6,1);
        ss.setBlock(6,2);
        ss.setBlock(6,3);
        ss.setBlock(6,4);
        ss.setBlock(6,4);
        ss.setBlock(5,4);
        ss.setBlock(4,4);
        
        ss.setEpsi(2.5);

        StdOut.println(ss);
        StdOut.println(ss.getCloseSet());
        StdOut.println(ss.getOpenSet().size());
        StdOut.println(ss.getCurrentPosition());
        
        StdOut.print(ss.getEpsi()+"\n");
        for(int i = 0; i < n;i ++){
            for(int j = 0; j < m; j++){
                StdOut.print(ss.getNodeSet()[i][j].getG()+" ");
            }
            StdOut.print("\n");
        }
        StdOut.print("\n");
        for(int i = 0; i < n;i ++){
            for(int j = 0; j < m; j++){
                StdOut.print(ss.getNodeSet()[i][j].getF()+" ");
            }
            StdOut.print("\n");
        }
        StdOut.print("\n");
        for(int i = 0; i < n;i ++){
            for(int j = 0; j < m; j++){
                StdOut.print(ss.getNodeSet()[i][j].getH()+" ");
            }
            StdOut.print("\n");
        }
        StdOut.println(ss);
        ss.setEpsi(1);
        for(int i = 0; i < n;i ++){
            for(int j = 0; j < m; j++){
                StdOut.print(ss.getNodeSet()[i][j].getG()+" ");
            }
            StdOut.print("\n");
        }
        StdOut.println(ss);
     }
    
}
