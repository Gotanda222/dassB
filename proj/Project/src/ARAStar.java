import java.util.*;
import edu.princeton.cs.algs4.*;

public class ARAStar {
    private static double HV_COST = 20;
    private static double DI_COST = 28;
    private double epsi;
    private node[][] nodeSet;
    private int ROW;
    private int COL;
    private PriorityQueue<node> openSet;
    private HashSet<node> closeSet;
    private node END;
    private node START;
    private node currentPosition;
    private List<node> attackedNodeList;

    public ARAStar(int n, int m, boolean maze[][], double epsi){
        this.epsi = epsi;
        this.nodeSet = new node[n][m];
        for(int i = n-1; i >=0; i--){
            for(int j = m-1; j >=0; j--){
                nodeSet[i][j] = new node(i,j);
                if(i==n-1 && j ==m-1)
                    this.END = nodeSet[i][j];
                if(i==0 && j ==0)
                    this.START = nodeSet[i][j];
                nodeSet[i][j].setBlock(maze[i][j]); //0->false->空
                nodeSet[i][j].setH(2, END);
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
        this.attackedNodeList = new ArrayList<node>();
    }

    public List<node> ARAStarFindPath(){
        long stime = System.currentTimeMillis();
        long etime = System.currentTimeMillis();
        double epsi = getEpsi();
        double epsi1 = epsi;
        List<node> tempPath = new ArrayList<node>();
        List<node> tampPath1 =  findPath();
        while(epsi > 1){
            setEpsi(epsi-=1);
            long stime2 = System.currentTimeMillis();
            tempPath = findPath();
            long tempTime = System.currentTimeMillis();
            if(tempPath.size()< tampPath1.size())
                tampPath1 = tempPath;
            etime =System.currentTimeMillis();
            if((etime - stime) > 5*(tempTime-stime2))
                break;
        }
        setEpsi(epsi1);
        return tempPath;
    }

    public List<node> findPath(){
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
        this.openSet.clear();
        this.closeSet.clear();
        return new ArrayList<node>();
    }

    public void move(){
        List<node> path = ARAStarFindPath();
        if(path.size()>1){
            path.remove(0);
            setCurrentPosition(path.get(0));
            getCurrentPosition().setParent(null);
            setEpsi(this.epsi-=1);
        }     
    }
   
    public void attack(int i, int j){
        this.attackedNodeList.add(getNodeSet()[i][j]);
        int currentRow = getCurrentPosition().getRow();
        int currentCol = getCurrentPosition().getCol();
        if(currentCol == j && currentRow == i)
            return;
        if(i <= ROW-1 && i >=0 && j <= COL-1 && j >= 0 )
            this.nodeSet[i][j].setBlock(true);
    }

    public void nextNode(node currentNode){
        int r = currentNode.getRow();
        int c = currentNode.getCol();
        if(this.ROW==1){
            if(this.COL == 1) return;
            if(this.COL > 1) {
                checkNode(this.nodeSet[r][c+1], currentNode, HV_COST);
                return;
            }
        }
        if(this.COL ==1){
            checkNode(this.nodeSet[r+1][c], currentNode, HV_COST);
            return;
        }
        if(r == 0 ){
            checkNode(this.nodeSet[r+1][c], currentNode, HV_COST);
            if(c ==0 ){
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

    public void setBlock(int i, int j){
        int currentRow = getCurrentPosition().getRow();
        int currentCol = getCurrentPosition().getCol();
        if(currentCol == j && currentRow == i)
            return;
        if(i <= ROW-1 && i >=0 && j <= COL-1 && j >= 0 )
            this.nodeSet[i][j].setBlock(true);
    }

    public void setBlock(node node){
        int i = node.getRow();
        int j = node.getCol();
        this.nodeSet[i][j].setBlock(true);
    }

    public void removeBlock(node node){
        int i = node.getRow();
        int j = node.getCol();
        this.nodeSet[i][j].setBlock(false);
    }

    public void removeBlock(int i, int j){
        this.nodeSet[i][j].setBlock(false);
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

    public List<node> getCurrentPath(){
        return this.ARAStarFindPath();
    }

    public List<node> getAttackList(){
        return this.attackedNodeList;
    }

    public node getEndNode(){
        return this.END;
    }
  
    public String toString() {
        String str = "";
        List<node> path = ARAStarFindPath();
        for(int i=0; i<this.nodeSet.length;i++){
            for(int j =0; j <this.nodeSet[0].length;j++){
                if(path.contains(this.nodeSet[i][j])){
                    if(this.nodeSet[i][j].compareTo(this.currentPosition))
                        str+="C";
                    if(this.nodeSet[i][j].compareTo(START)){
                        str +="I*  ";
                        continue;
                    }
                    if(this.nodeSet[i][j] .compareTo(END)){
                        str +="F*  ";
                        continue;
                    }
                    str+="*  ";
                    continue;
                }
                if(this.nodeSet[i][j] .compareTo(START))
                    str += "I  ";
                
                else if(this.nodeSet[i][j] .compareTo(END))
                    str += "F  ";
                else if(this.nodeSet[i][j].getIsBlock())
                    str+="B  ";
                else
                    str+="-  ";
            }
            str += "\n";
        }
        return str;
    }
    
    public static void main(String[] args) {
        In newIn = new In("task2_data.txt");
        int n = newIn.readInt();
        int m = newIn.readInt();
        double e = newIn.readDouble();
        boolean currentMaze[][] = new boolean[n][m];
        int[][] maze= new int[n][m];
        for(int i =0;i<n;i++){
            for(int j =0;j<m;j++){
                maze[i][j] = newIn.readInt();
                if(maze[i][j] == 0) currentMaze[i][j] = false; 
                else if(maze[i][j] == 1) currentMaze[i][j] = true;   
            }
        }
        int p = newIn.readInt();
        int[][] block = new int[p][3];
        for(int i = 0; i < p; i++){
            block[i][0] = newIn.readInt();
            block[i][1] = newIn.readInt();
            block[i][2] = newIn.readInt();
        }
        int k = newIn.readInt();
        int[] inquiry = new int[k];
        for(int i = 0; i < k; i++){
            inquiry[i] = newIn.readInt();
            // StdOut.println(inquiry[i]);
        }

        ARAStar ss = new ARAStar(n, m, currentMaze, e);
        while(ss.getCurrentPosition()!=ss.getEndNode()){
            ss.move();
            if(ss.getCurrentPath().size()>30 && ss.getCurrentPath().size()<50)
                StdOut.println(ss+"\n");
        }
    }
    
}
