public class node {
    private int g;
    private int h;
    private int f;
    private int col;
    private int row;
    private boolean isBlock;
    private boolean set;
    private node parent;

    public void setPosition(int row, int col){
        this.row = row;
        this.col =col;
    }

    public void setIsBlock(boolean isBlock){
        this.isBlock = isBlock;
    }

    public node setParent(node parent){
        return this.parent = parent;
    }

    public int getCol(){
        return this.col;
    }

    public int getRow(){
        return this.row;
    }

    public boolean getIsBlock(){
        return this.isBlock;
    }

    public node getParent(){
        return this.parent;
    }

}   
