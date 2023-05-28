import java.util.*;
import edu.princeton.cs.algs4.*;

public class game {
    public static void main(String[] args) {
        // In newIn = new In(); //从terminal读取输入
        RandamInputGenerator.generateRandamInput(50,50);
        In newIn = new In("testData2.txt");//从testData2.txt读取输入(随机生成的输入)
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
        if(args[0].equals("terminal") ){
            int setBlockTime = 0;
            int inquiryTime = 0;
            while(e>=1){
                if(block.length>0 && e == block[setBlockTime][0]){
                    ss.attack(block[setBlockTime][1], block[setBlockTime][2]);
                    if(setBlockTime <p-1)
                        setBlockTime +=1;
                }
                if(inquiry.length>0 && e == inquiry[inquiryTime]){
                    List<node> currentPath = ss.getCurrentPath();
                    StdOut.println(currentPath.size());
                    for(node node : currentPath){
                        StdOut.print(node);
                    }
                    StdOut.print("\n");
                    if(inquiryTime < k-1){
                        inquiryTime += 1;
                    }
                }
                e--;
                ss.move();
                if(ss.getCurrentPosition().compareTo(ss.getEndNode())){
                    return;
                }
                
            }
            
        }   
        else if(args[0].equals("gui")){
            GameFrame gf = new GameFrame(n, m, maze, ss);
            NextStep nextStep = new NextStep(e, p, block, k, inquiry, ss, gf);
            gf.initButton(nextStep);
        }
        
    }

    
}
