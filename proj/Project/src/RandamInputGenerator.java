import java.util.*;

import edu.princeton.cs.algs4.*;

public class RandamInputGenerator {
    public static void main(String[] args) {
        generateRandamInput(20,20);
 	}

    public static void generateRandamInput(int maxRow, int maxCol){
        Out newOut = new Out("testData2.txt");        
        int n = new Random().nextInt(maxRow)+3;
        int m = new Random().nextInt(maxCol)+3;
        // StdOut.print(n+" "+m);
        
        int[][] mazeI = MazeGenerator.getMaze(n, m);
        boolean[][] maze = new boolean[n][m];
        for(int i =0;i<n;i++){
            for(int j =0;j<m;j++){
                if(mazeI[i][j] == 0)
                    maze[i][j] = false; 
                else maze[i][j] = true; 
            }
        }
        List<int[]> indices0 = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(maze[i][j] == true)
                    indices0.add(new int[]{i, j});
            }
        }
        Collections.shuffle(indices0, new Random());
        int[] num = generateRandamArray((int)(indices0.size()/7),0, indices0.size()-1);
        
        int numCount = 0;
        for(int i = indices0.size()-1; i>=0;i--){
            if(i == num[numCount]){
                int j = indices0.get(i)[0];
                int k = indices0.get(i)[1];
                maze[j][k] = false;
                numCount++;
           }
            if(numCount==num.length-1) break;
            
        }
        for(int i =0;i<n;i++){
            for(int j =0;j<m;j++){
                if(maze[i][j] == false)
                    mazeI[i][j] = 0; 
                else mazeI[i][j] = 1; 
            }
        }


        ARAStar ss = new ARAStar(n,m,maze,1);
        List<node> path = ss.findPath();
        int e = path.size()*3;
        if(e<6 ) e=6;

        newOut.print(n+" ");
        newOut.print(m+" ");
        newOut.print(e+"\n");
        ss.setEpsi(e);
        /************************************** */
        for(int i =0;i<n;i++){
            for(int j =0;j<m;j++){
                newOut.print(mazeI[i][j] +" "); 
            }
            newOut.print("\n");
        }
        /***************************************** */
        int p = new Random().nextInt(e/6)+e/12;
        newOut.println(p);

        /********************************************** */
       
        int[] sequence1 = generateRandamArray(p, e/6, e-1);
        int[][] attackLocation = new int[p][2];
        int pp =0;
        List<int[]> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(maze[i][j] == false)
                    indices.add(new int[]{i, j});
            }
        }
        Collections.shuffle(indices, new Random());

        for (int[] index : indices) {
            int i = index[0];
            int j = index[1];
            if(i==0 && j==0) continue;
                if(i==n-1 && j==m-1) continue;
                if(p < pp+1) break;
                if(p > 1 && !maze[i][j] ){
                    ss.getNodeSet()[i][j].setBlock(true);
                    int pl = ss.ARAStarFindPath().size();
                    if(pl==0){
                        ss.getNodeSet()[i][j].setBlock(false);
                        ss.move();
                    }else{
                        attackLocation[pp][0] = i;
                        attackLocation[pp][1] = j;
                        ss.move();
                        // ss.removeBlock(i, j);
                        pp+=1;
                    }
                }
            }

        for(int i = 0; i < p; i++){            
            newOut.print(sequence1[i] +" " + attackLocation[i][0] +" " +attackLocation[i][1]+"\n");         
        }
        /************************************************************ */
        int k = new Random().nextInt(e/6)+e/6;
        newOut.print(k+"\n");
        int[] sequence2 = generateRandamArray(k, e/3, e-1);
        for(int i = 0; i < k; i++){
            newOut.print(sequence2[i]+"\n");
        }
    }
    
    public static int[] generateRandamArray(int length, int min, int max){
        int[] array = new int[length];
        Random random = new Random();
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < length; i++) {
            int randomNumber;
            do {
                randomNumber = random.nextInt(max - min + 1) + min;
            } while (set.contains(randomNumber));
            set.add(randomNumber);
            array[i] = randomNumber;
        }

        Arrays.sort(array);
        reverseArray(array);

        return array;
    }

    

    public static void reverseArray(int[] array) {
        int left = 0;
        int right = array.length - 1;

        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;

            left++;
            right--;
        }
    }
    
}
