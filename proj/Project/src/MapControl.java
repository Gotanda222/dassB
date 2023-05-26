import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapControl implements ActionListener {
    private GameFrame gf;

    public MapControl(GameFrame gf) {
        this.gf = gf;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!gf.isCheat()) {
            if(gf.mapIsOpen()) {
                gf.closeMap();
            } else {
                gf.openMap();
            }
        }
    }
}
