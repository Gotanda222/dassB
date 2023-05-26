import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextStep implements ActionListener {
    private double e;
    private int p, k;
    private int a = 0;
    private int b = 0;
    private int[] inquiryTime;
    private int[][] attack;
    private ARAStar ss;
    private GameFrame gf;
    private int x;
    private int y;

    public NextStep(double e, int p, int[][] attack, int k, int[] inquiry, ARAStar ss, GameFrame gf) {
        this.e = e;
        this.p = p;
        this.k = k;
        this.ss = ss;
        this.inquiryTime = inquiry;
        this.attack = attack;
        this.gf = gf;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        e = e - 1;
        ss.move();
        gf.move(ss.getCurrentPosition());
        if(e == attack[a][0]) {
            x = attack[a][1];
            y = attack[a][2];
            ss.attack(x, y);
            gf.attack(x, y);
            if(a != p-1) a++;
        }
        if(e == inquiryTime[b]) {
            gf.openMap();
            gf.path(ss.ARAStarFindPath());
            if(b != k-1) b++;
        } else {
            gf.closeMap();
        }
        if(gf.isCheat()) {
            gf.closeMap();
            gf.path(ss.getCurrentPath());
            gf.openMap();
        }
        gf.refreshNodeList(ss.ARAStarFindPath());
        String[] s = gf.text(e, ss.getCurrentPosition(), inquiryTime[b], attack[a][0], x, y);
        gf.rewriteText(s[0], s[1], s[2], s[3], s[4]);
    }
    
}
