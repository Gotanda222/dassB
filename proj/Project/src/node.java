public class node {
    private double g;
    private double h;
    private double f;
    private int col;
    private int row;
    private boolean isBlock;
    private node parent;

    public node(int i, int j){
        this.col = j;
        this.row = i;
    }

    public void setH1(node finalNode) { //曼哈顿
        this.h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
    }

    public void setH2(node finalNode) { //欧几里得
        this.h = Math.sqrt((finalNode.getRow()- getRow())^2+(finalNode.getCol() - getCol())^2);
    }

    public void setH3(node finalNode) { //对角线
        double dx = Math.abs(finalNode.getRow() - getRow());
        double dy = Math.abs(finalNode.getCol() - getCol());
        double min = Math.min(dx,dy);
        this.h = 2.5*(dx+dy+ (Math.sqrt(2)-2) * min);
    }

    public void setF(double f){
        this.f = f;
    }
    public void setG(double g) {
        this.g = g;
    }

    public void setData(node formerNode, double gCost){
        double g = formerNode.getG() + gCost;
        setParent(formerNode);
        setG(g);
        calculateFinalCost();
    }

    private void calculateFinalCost() {
        double finalCost = getG() + getH();
        setF(finalCost);
    }
    public boolean refreshG(node currentNode, double cost){
        double gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setData(currentNode, cost);
            return true;
        }
        return false;
    }

    public void setBlock(boolean Block){
        this.isBlock = Block;
    }

    public node setParent(node parent){
        return this.parent = parent;
    }

    public double getF(){
        return this.f;
    }
    public double getG(){
        return this.g;
    }
    public double getH(){
        return this.h;
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

    @Override
    public String toString() {
        String str ="";
        if(this.getIsBlock())
            str += "1 ";
        else str += "0 ";
        return str;
    }

    public boolean compareTo(node node) {
        if(this.col == node.getCol() && this.row == node.getRow())
            return true;
        else return false;
       
    }

}   
