# CS203B Spring2023 Final Project Report

12011921 赵睿涵  12011917 张键

**项目运行方式：**

###### 选择GUI或terminal：

​	1.使用终端编译并运行game.java.

​	默认的args[]长度为1，终端中参数传入“gui”,将运行GUI窗口，终端中参数传入"terminal",在命令行中输出结果。

​	2.如果使用vscode打开项目，在launch.json文件中找到game对应的大括号，找到args那一行

```java
 "args": ["gui"],
```

​	修改中括号中的值为“gui”或“terminal”来选择运行GUI或terminal。

###### 数据输入：

​	提供两种数据输入方式

1. 在终端中输入args参数运行程序后，直接将输入数据复制到命令行中。

2. 将数据复制到project目录下“testData2.txt”文件中，注释掉game.java第6行并将第8行解除注释，运行程序即可。

   此外，提供随机生成数据模式，将game.java第6行注释掉，第7、8行解除注释，指定输入参数n,m，将生成最大范围n*m的迷宫。

### 1.项目基本结构

本项目的分为如下几个部分：

##### 1.1 node类

​	在本项目中，将迷宫中的每个点定义为一个node类的对象，具有以下成员变量：g值，h值，f值，在迷宫中的位置（row, col），是否为墙壁，以及父节点对象；并提供了一些方法去修改或获取这些成员变量。

##### 1.2 ARAStar类

​	Anytime Repairing A*算法的实现部分。

​	通过构造函数指定想要生成的迷宫大小，墙壁的位置，以及初始的ε值。

​	成员变量有妮可对角移动、直线移动的代价，当前的膨胀系数ε， 二维node对象数组实现的迷宫，迷宫的大小（row, col），一个node类型的PriorityQueue作为openset，一个node类型的hashset作为closeSet，起点与终点的node对象，当前妮可所在的位置（用node表示），被魔法攻击过的node，储存在node类型的List中。

​	提供了移动妮可，发动魔法攻击，获取当前位置到终点的路径，以及动态维护成员变量等方法。

##### 1.3 game类

​	运行game类中的main方法开始游戏。

​	首先可以修改代码来选择读取输入的方式，直接从命令行读取、或从指定文档中读取。在选择从文档中读取输入时，提供了代码可以随机生成测试数据。

​	修改代码选择完成输入方式后，运行main方法开始游戏，选择是否需要GUI即可。

##### 1.4 MazeGenerator类

​	在本类中，实现了使用深度优先算法，生成一个任意大小的迷宫。

​	提供了静态方法getMaze(int n, int m)得到大小为n*m的迷宫，用二维int数组表示，数组中1表示当前位置为墙壁，0表示不为墙壁。

##### 1.5 RandamInputGenerator类

​	生成随机的输入，并储存在一个txt类型的文档中。

​	提供了静态方法generateRandamInput(int maxRow, int maxCol)， 参数为想要生成的迷宫行列的最大值，该方法将生成行列范围[1-最大值]之间的迷宫，以及符合输入格式的数据。

### 2.具体实现

##### 2.1 node.java

介绍node类中一些重要函数的具体实现。

###### 2.1.1 设置迷宫中每个node的H值

```java
public void setH(int type, node finalNode){
        double h =0;
        if(type == 0)
            h =  Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
        else if(type == 1)
            h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
        else if(type == 2){
            double dx = Math.abs(finalNode.getRow() - getRow());
            double dy = Math.abs(finalNode.getCol() - getCol());
            double min = Math.min(dx,dy);
            h = (dx+dy+ (Math.sqrt(2)-2) * min);
        }
        this.h = h;
    }
```

​	setH()方法的两个参数分别为启发函数的类型、终点node的对象。type=0时，启发函数采用曼哈顿距离；type=1时，启发函数采用欧几里得距离；type=2时，采用对角线距离。

###### 2.1.2  设置每个node的父节点、实际代价g以及计算综合成本f

```java
public void setData(node formerNode, double gCost, double epsi){
        double g = formerNode.getG() + gCost;
        setParent(formerNode);
        this.g = g;
        calculateFinalCost(epsi);
    }
```

​	setData()方法有三个参数，分别为父节点，当前节点的实际代价g, 当前的ε。设置指针指向给的父节点，根据父节点更新当前节点的实际代价，最后计算当前节点的综合成本。其中calculateFinalCost()方法采用ARA*的方式计算H。

###### 2.1.3 刷新当前节点的g代价

```java
public boolean refreshG(node currentNode, double cost, double epsi){
        double gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setData(currentNode, cost, epsi);
            return true;
        }
        return false;
    }
```

​	对比当前节点的gcost与父节点加上路径的cost得到的gcost比较，选择较小的作为当前节点的gcost。

##### 2.2 ARAStar.java

​	在本项目中，一个迷宫可视为一个ARAStar对象，ARAStar的toString方法将会以字符的形式返回迷宫的样貌，在没有GUI的情况下方便查看。

###### 2.2.1 构造方法

```java
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
```

​	在构造方法中，建立了输入参数中二维布尔数组与成员变量nodeSet的映射，同时构造closeSet与openSet。

​	openSet采用优先队列的数据结构，并重写copmare方法，确保了每次执行poll方法所返回的对象具有最小的综合成本。

​	closeSet没有排序的需求，使用hashSet优化性能。

###### 2.2.2 维护openSet

​	nextNode(node currentNode)方法，判断当前节点周围节点是否越界，周围节点在不越界的情况下通过checkNode()方法判断更新openSet。

```java
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
```

​	checkNode()判断当前节点，若不是墙壁且没在closeSet中，判断是否在openList中，若在，刷新gcost的值，并从openSet中删除后重新加入，对openSet重新排序；若不在openSet中，设置当前节点的数据并加入openSet中。

###### 2.2.3 找到通向终点的路径

```java
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
```

​	findPath()方法动态修改openSet，并使用openSet的poll方法更新当前妮可的位置，对当前节点调用nextNode()方法向前寻路，直到妮可走到终点。走到终点后使用getPath()方法获得路径，为一个List，并将openSet与closeSet清空。

​	如果最终没有到达终点(不存在通向终点的路径)，清空openSet与closeSet，并返回空的List。

```java
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
```

​	findPath()方法维护了一个List，加入当前节点，并将当前节点的父节点加入List中，直到某个节点的父节点为空，这意味着已经搜索到了妮可的当前位置。最后返回这个List.	

findPath()方法的参数为一个node对象，在正常情况下（走到了终点），该参数为成员变量END，为了测试方便，故该参数不是必须的。但为了方便测试，仍增加了该参数。

###### 2.2.4 ARA*算法实现

​	上述方法是在ε固定情况下的寻路，只能根据当前ARAStar类中的的ε找到一条路径。

```java
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
```

​	在ARAStarFindPath()中，使ε递减，判断在当前ε的情况下得到的路径与之前路径的长度，若长度减小，则将当前路径设为新路径，不断循环直到ε降为1或被打断。

​	增加了判断步骤，如果时间超过一次findPath时间的5倍，则强制打断。

###### 2.2.5 小妮可移动、魔法师攻击、坤坤询问的实现

```java
public void move(){
        List<node> path = ARAStarFindPath();
        if(path.size()>1){
            path.remove(0);
            setCurrentPosition(path.get(0));
            getCurrentPosition().setParent(null);
            setEpsi(this.epsi-=1);
        }     
    }
```

​	move()方法获得当前位置到终点的路径，如果路径长度不为0，说明没有到达终点，则将当前位置设为路径的List中的第一个node，并设置该node的父节点为空。

​	attck()方法将指定位置的node设为墙壁，加入attackedList中，如果攻击的位置为当前位置，则免疫这次攻击，并仍将该位置加入attackedList中。

​	在询问时间，调用ARAStarFindPath，得到一个node类型的List。

##### 2.3 MazeGenerator.java

​	该类通过深度优先算法生成指定大小的迷宫。维护了int类型的二维数组，以0、1储存生成的迷宫，维护了boolean类型的二维数组，存放迷宫中每个点是否被访问过。

​	在生成的开始，将迷宫所有位置设为墙壁，所有位置均设为未访问。从起点位置开始递归执行生成路径算法generatePath()。

```java
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
```

​	首先定义了四个方向，在每次前进前随机选一个方向，如果这个方向上的点没有越界，则移动到目标点，并将路点加入path中。每次移动前进两格，确保生成的迷宫路径是连续的，没有间隔或死胡同。

​	在该类中提供getMaze(int n, int m)方法，得到指定大小随机生成的迷宫。

##### 2.4 RandamInputGenerator.java

###### 2.4.1 写入迷宫大小、epsilon值以及迷宫

​	生成符合输入格式的随机输入数据。

​	generateRandamInput()方法的参数为期望生成迷宫的最大行数、列数，调用该方法，会将符合输入格式的所有输入写入“testData2.txt”文件夹中。

​	调用getMaze()方法，通过深度优先搜索方式先生成迷宫，将是墙壁的点加入一个list中，并打乱List的顺序，删除然后删除1/8的墙壁，使通路多样化。

​	将迷宫的epsilon设置为1，使用findPath()方法，得到最短路径的长度，将该值乘3作为数据的epsilon值，确保能走到终点。

###### 2.4.2 写入魔法攻击时间及位置数据

**生成攻击时间：**

​	generateRandamArray()方法，可以生成指定长度，指定范围，不重复且降序排序的数据，使用hashSet确保数据中没有重复元素，生成指定长度的随机数据后对数组排序，并逆序返回。

​	应当注意的是输入的数组长度应当小于给定范围的大小，否则会陷入死循环。

**生成攻击位置：**

​	将迷宫中的不是墙壁的点写入一个长度为2的数组（分别记录该点位置）类型的List中，对List洗牌，然后遍历整个List，尝试对每个位置添加障碍物，如果仍有通路，则将该点记录为攻击的位置，如果没有通路，取消这个点的障碍，并跳过。直到满足攻击次数或遍历完整个List。

###### 2.4.3 写入询问时间及位置数据

**生成询问时间：**

​	与生成攻击时间相同，生成指定长度，指定范围，不重复且降序排序的数据。