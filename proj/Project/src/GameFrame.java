import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.util.*;

public class GameFrame extends JFrame {
    private boolean isSucced = false;
    private boolean mapIsOpen = false;
    private boolean isCheat = false;

    private int[][] maze;
    private final int WALL = 1;
    private List<Wall> wallList;
    private List<Wall> mapList;
    private List<node> nowPath;
    // private List<node> oldPath;
    private node lastNode;
    private MapControl mapControl;
    private Cheat cheat2;

    private int nRow = 0;
    private int nCol = 0;

    private JPanel board = new JPanel();
    private JPanel back = new JPanel();
    private JPanel map = new JPanel();
    private JPanel mapBoard = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    private JButton mapButton;
    private JRadioButton switchButton2;
    private JButton jb;
    private JLabel text1;
    private JLabel text2;
    private JLabel text3;
    private JLabel text4;
    private JLabel text5;
        
    public GameFrame(int row, int col, int[][] maze, ARAStar ss) {
        
        setTitle("小妮可找宝藏");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        initial(row, col);
        initMaze(maze);
        initButton();
        initNico();

        setVisible(true);
        setResizable(false);

        String s1 = "e的值为" + ss.getEpsi();
        String s2 = "当前坐标为(" + ss.getCurrentPosition().getRow() + "," + ss.getCurrentPosition().getCol() + ")";
        String s3 = "下一个显示方向的时间为e=";
        String s4 = "下一次攻击时间为e=";
        String s5 = "";
        rewriteText(s1, s2, s3, s4, s5);
    }

    public boolean isSucced(int i, int j) {
        if((i == nRow-1) && (j == nCol-1)) isSucced = true;
        return isSucced;
    }

    public void initial(int row, int col) {
        int width;
        int height;

        this.nRow = row;
        this.nCol = col;
        this.wallList = new ArrayList<Wall>();
        this.mapList = new ArrayList<Wall>();
        this.nowPath = new ArrayList<node>();
        // this.oldPath = new ArrayList<node>();
        this.mapControl = new MapControl(this);
        this.cheat2 = new Cheat(this);

        this.add(back, BorderLayout.WEST);
        back.setLayout(null);
        back.setBackground(Color.LIGHT_GRAY);
        back.setPreferredSize(new Dimension(720, 720));

        back.add(board);
        board.setLayout(new GridLayout(row, col));
        board.setBackground(Color.LIGHT_GRAY);
        if(row > col) {
            width = 690*col/row;
            height = 690;
        } else {
            height = 690*row/col;
            width = 690;
        }
        board.setSize(width, height);
        board.setLocation((710-width)/2, (690-height)/2);
    }

    public void initNico() {
        Wall nico = findWall(0, 0);
        nico.setBackground(Color.GREEN);
        lastNode = null;
    }

    public void readMaze(int[][] maze) {
        this.maze = maze;
    }

    public void initMaze(int[][] maze) {
        readMaze(maze);
        for(int i = 0; i < nRow; i++) {
            for(int j = 0; j < nCol; j++) {
                Wall wall = new Wall(i, j);
                board.add(wall);
                if(maze[i][j] == WALL) wall.setBackground(Color.BLACK);
                wall.setOpaque(true);
                wallList.add(j + i*nCol, wall);
            }
        }
    }

    public void initButton() {
        int width;
        int height;
        if(nRow > nCol) {
            height = 500;
            width = 500*nCol/nRow;
        } else {
            width = 500;
            height = 500*nRow/nCol;
        }

        this.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(520, 720));

        initMap();
        buttonPanel.add(mapBoard, BorderLayout.SOUTH);
        mapBoard.setLayout(null);
        mapBoard.setBackground(Color.LIGHT_GRAY);
        mapBoard.setPreferredSize(new Dimension(520, 520));

        buttonPanel.add(controlPanel, BorderLayout.NORTH);
        controlPanel.setLayout(null);
        controlPanel.setBackground(Color.LIGHT_GRAY);
        controlPanel.setPreferredSize(new Dimension(520, 150));

        mapBoard.add(map);
        map.setLayout(new GridLayout(nRow, nCol));
        map.setBackground(Color.BLACK);
        map.setSize(width, height);
        map.setLocation((520-width)/2, (540-height)/2);
        map.setVisible(mapIsOpen);
        initText();
    }

    public void initButton(NextStep nextStep) {
        jb = new JButton("下一步");
        jb.addActionListener(nextStep);
        controlPanel.add(jb);
        jb.setBounds(10, 10, 100, 50);

        mapButton = new JButton("寻找方向");
        mapButton.addActionListener(mapControl);
        controlPanel.add(mapButton);
        mapButton.setBounds(120, 10, 100, 50);

        switchButton2 = new JRadioButton("显示地图");
        controlPanel.add(switchButton2);
        switchButton2.addActionListener(cheat2);
        switchButton2.setBounds(230, 10, 100, 50);
        switchButton2.setBackground(Color.LIGHT_GRAY);
    }

    public void expire() {
        isCheat = true;
    }
    
    public void recover() {
        isCheat = false;
    }

    public boolean isCheat() {
        return isCheat;
    }

    public void initText() {
        text1 = new JLabel("");
        text2 = new JLabel("");
        text3 = new JLabel("");
        text4 = new JLabel("");
        text5 = new JLabel("");
        controlPanel.add(text1);
        controlPanel.add(text2);
        controlPanel.add(text3);
        controlPanel.add(text4);
        controlPanel.add(text5);
        text1.setBounds(10, 70, 250, 20);
        text2.setBounds(10, 100, 250, 20);
        text3.setBounds(10, 130, 250, 20);
        text4.setBounds(270, 70, 250, 20);
        text5.setBounds(270, 100, 250, 20);
    }

    public String[] text(double e, node current, int openTime, int attack, int x, int y) {
        String[] s = new String[5];
        s[0] = "e的值为" + e;
        s[1] = "当前坐标为(" + current.getRow() + "," + current.getCol() + ")";
        s[2] = "下一个显示方向的时间为e=" + openTime;
        s[3] = "下一次攻击时间为e=" + attack;
        s[4] = "本次攻击位置为(" + x + "," + y + ")";
        return s;
    }

    public void rewriteText(String t1, String t2, String t3, String t4, String t5) {
        text1.setText(t1);
        text2.setText(t2);
        text3.setText(t3);
        text4.setText(t4);
        text5.setText(t5);
    }

    public void initMap() {
        for(int i = 0; i < nRow; i++) {
            for(int j = 0; j < nCol; j++) {
                Wall wall = new Wall(i, j);
                mapList.add(j+i*nCol, wall);
                map.add(wall);
                if(maze[i][j] == WALL) wall.setBackground(Color.BLACK);
                wall.setOpaque(true);
            }
        }
    }

    public Wall findMapWall(int r, int c) {
        Wall map = mapList.get(c+r*nCol);
        if(map.isWall(r, c)) return map;
        return null;
    }

    public Wall findWall(int r, int c) {
        Wall wall = wallList.get(c+r*nCol);
        if(wall.isWall(r, c)) return wall;
        return null;
    }

    public void move(node currentNode) {
        int row, col;
        Wall posotion;
        if(lastNode != null) {
            row = lastNode.getRow();
            col = lastNode.getCol();
            posotion = findWall(row, col);
            posotion.setBackground(Color.GREEN);
        }
        lastNode = currentNode;
        row = currentNode.getRow();
        col = currentNode.getCol();
        posotion = findWall(row, col);
        posotion.setBackground(Color.CYAN);
    }

    public void attack(List<node> attackList) {
        int row, col;
        Wall wall;
        Wall map;
        for(int i = 0; i < attackList.size(); i ++) {
            node n = attackList.get(i);
            row = n.getRow();
            col = n.getCol();
            wall = findWall(row, col);
            wall.setBackground(Color.BLUE);
            map = findMapWall(row, col);
            map.setBackground(Color.BLACK);
            maze[row][col] = WALL;
        }
    }

    public void attack(int i, int j) {
        int row = i;
        int col = j;
        Wall wall;
        wall = findWall(row, col);
        wall.setBackground(Color.BLUE);
        wall = findMapWall(row, col);
        wall.setBackground(Color.BLACK);
    }

    public boolean mapIsOpen() {
        return mapIsOpen;
    }

    public void refreshNodeList(List<node> nodeList) {
        this.nowPath = nodeList;
    }

    public void openMap() {
        path(nowPath);
        mapIsOpen = true;
        map.setVisible(true);
        refreshButton();
    }

    public void closeMap() {
        mapIsOpen = false;
        refreshButton();
        closePath();
        map.setVisible(false);
    }

    public void refreshButton() {
        if(mapIsOpen) {
            mapButton.setText("关闭");
        } else {
            mapButton.setText("寻找方向");
        }
    }

    public void path(List<node> nodeList) {
        closePath();
        int row, col;
        Wall wall;
        node n;
        // closeOldPath();
        // this.oldPath = nowPath;
        // oldPath();
        this.nowPath = nodeList;
        for(int i = 0; i < this.nowPath.size(); i++) {
            n = this.nowPath.get(i);
            row = n.getRow();
            col = n.getCol();
            wall = findMapWall(row, col);
            wall.setBackground(Color.RED);
        }
    }

    // public void oldPath() {
    //     closeOldPath();
    //     int row, col;
    //     Wall wall;
    //     node n;
    //     for(int i = 0; i < this.oldPath.size(); i++) {
    //         n = this.oldPath.get(i);
    //         row = n.getRow();
    //         col = n.getCol();
    //         wall = findMapWall(row, col);
    //         if(!wall.getBackground().equals(Color.BLACK)) wall.setBackground(Color.PINK);
    //     }
    // }

    public void closePath() {
        int row, col;
        Wall wall;
        node n;
        for(int i = 0; i < this.nowPath.size(); i++) {
            n = this.nowPath.get(i);
            row = n.getRow();
            col = n.getCol();
            wall = findMapWall(row, col);
            if(!wall.getBackground().equals(Color.BLACK)) wall.setBackground(Color.WHITE);
        }
    }

    // public void closeOldPath() {
    //     int row, col;
    //     Wall wall;
    //     node n;
    //     for(int i = 0; i < this.oldPath.size(); i++) {
    //         n = this.oldPath.get(i);
    //         row = n.getRow();
    //         col = n.getCol();
    //         wall = findMapWall(row, col);
    //         if(!wall.getBackground().equals(Color.BLACK)) wall.setBackground(Color.WHITE);
    //     }
    // }
}
