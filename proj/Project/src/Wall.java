import java.awt.Color;

import javax.swing.JButton;

public class Wall extends JButton {
    private int x = 0;
    private int y = 0;

    public Wall(int r, int c) {
        this.x = r;
        this.y = c;
        setBackground(Color.WHITE);
    }

    public Boolean isWall(int r, int c) {
        if((r == this.x) && (c == this.y)) {
            return true;
        }
        return false;
    }

    public Wall getWall(int r, int c) {
        if(isWall(r, c)) {
            return this;
        }
        return null;
    }
}
