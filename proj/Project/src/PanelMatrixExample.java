import javax.swing.*;
import java.awt.*;

public class PanelMatrixExample {
    public static void main(String[] args) {
        int rows = 100;
        int columns = 150;

        JFrame frame = new JFrame("Panel Matrix Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JPanel panel = new JPanel();
                if(i%2 == 0)
                    panel.setBackground(Color.BLACK);
                
                if(j%2 == 1)
                    panel.setBackground(Color.BLACK);
                frame.add(panel);
            }
        }

        frame.pack();
        frame.setVisible(true);
    }
}