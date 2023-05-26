import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cheat implements ActionListener {
    private GameFrame gf;
    private boolean b = true;

    @Override
    public void actionPerformed(ActionEvent e) {
        gf.openMap();
        if(b == false) {
            b = true;
            gf.recover();
        } else {
            b = false;
            gf.expire();
        }
    }

    public Cheat(GameFrame gf) {
        this.gf = gf;
    }
    
}
