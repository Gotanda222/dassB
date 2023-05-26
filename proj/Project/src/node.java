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

    public node(){
        
    }

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

    public void setData(node formerNode, double gCost, double epsi){
        double g = formerNode.getG() + gCost;
        setParent(formerNode);
        this.g = g;
        calculateFinalCost(epsi);
    }

    private void calculateFinalCost(double epsi) {
        double finalCost = getG() + epsi * getH();
        this.f = finalCost;
    }

    public boolean refreshG(node childNode, double cost, double epsi){
        double gCost = childNode.getG() + cost;
        if (gCost < getG()) {
            setData(childNode, cost, epsi);
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
        return getRow() + " " + getCol()+" ";
    }

    public boolean compareTo(node node) {
        if(this.col == node.getCol() && this.row == node.getRow())
            return true;
        else return false;
       
    }

}   
