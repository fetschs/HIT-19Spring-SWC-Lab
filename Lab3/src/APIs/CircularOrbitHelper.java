package APIs;

import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbit;
import circularOrbit.SocialNetworkCircle;
import circularOrbit.TrackGame;
import circularOrbit.TrackGameOther.RandomStrategy;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import javax.swing.JFrame;
import javax.swing.JPanel;
import physicalObject.CentralUser;
import physicalObject.Nucleus;
import relation.Relation;
import track.Track;

public class CircularOrbitHelper {

  public static class Shapes<L, E> extends JPanel {

    private int rad;
    private CircularOrbit<L, E> circularOrbit;

    public Shapes(int thisrad, CircularOrbit thisSocial) {
      this.rad = thisrad;
      this.circularOrbit = thisSocial;
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      int width = getWidth();
      int height = getHeight();
      g.drawOval(width / 2 - rad, height / 2 - rad, rad * 2, rad * 2);
      Font font = new Font("Serif", Font.PLAIN, 13);
      g.setFont(font);
      String cent = "";
      if (!(circularOrbit instanceof TrackGame)) {
        L temp = circularOrbit.getCentralPoint().getCentralObjects().get(0);
        if (temp instanceof CentralUser) {
          cent = ((CentralUser) temp).getName();
        } else if (temp instanceof Nucleus) {
          cent = ((Nucleus) temp).getName();
        }
      } else {
        cent = "EMPTY";
      }
      g.drawString(cent, width / 2 - rad / 2 - rad / 4, height / 2 + rad / 3);

      Map<E, Pair<Integer, Integer>> ePosition = new HashMap<>();
      int cnt = 0;
      for (Track i : circularOrbit.getTracks()) {
        cnt++;
        g.drawOval(width / 2 - (rad + cnt * 50), height / 2 - (rad + cnt * 50),
            2 * (rad + cnt * 50), 2 * (rad + cnt * 50));
        double angel = 0;
        List<E> theObejcts = circularOrbit.getTrackContent().get(i);
        int eNumber = theObejcts.size();
        for (int j = 0; j < eNumber; j++) {
          double r1 = rad + cnt * 50;
          double r2 = 20;
          double acx = (double) width / 2 + r1 * Math.cos(angel) - r2;
          double acy = (double) height / 2 + r1 * Math.sin(angel) - r2;
          g.drawOval((int) Math.round(acx), (int) Math.round(acy), 40, 40);
          angel = angel + (2 * Math.PI) / eNumber;
          ePosition.put(theObejcts.get(j),
              new Pair(Integer.valueOf((int) (Math.round(acx)) + (int)
                  Math.round(r2)), Integer.valueOf((int) Math.round(acy) + (int) Math.round(r2))));
        }
      }
      for (E j : ePosition.keySet()) {
        if (!(circularOrbit instanceof SocialNetworkCircle)) {
          continue;
        }
        List<Relation<E, E>> nowRelation = circularOrbit.getT2tRelations().get(j);
        g.setColor(Color.green);
        for (Relation<E, E> k : nowRelation) {
          g.drawLine(ePosition.get(j).getKey(), ePosition.get(j).getValue(),
              ePosition.get(k.getTo()).getKey(),
              ePosition.get(k.getTo()).getValue());
        }
        g.setColor(Color.red);
        for (Relation<L, E> relation : circularOrbit.getC2tRelations()) {
          if (!(circularOrbit instanceof SocialNetworkCircle)) {
            continue;
          }
          if (relation.getTo().equals(j)) {
            g.drawLine(width / 2, height / 2, ePosition.get(j).getKey(),
                ePosition.get(j).getValue());
          }
        }
      }
    }
  }

  public static void draw(CircularOrbit circularOrbit) {
    Shapes panel = new Shapes(50, circularOrbit);
    JFrame application = new JFrame();
    application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    application.add(panel);
    application.setSize(1000, 1000);
    application.setVisible(true);
  }

  public static void main(String[] args) {
    SocialNetworkCircle now1 = SocialNetworkCircle.createInstanceFromClient("data/SocialNetworkCircle_my.txt");
    draw(now1);
    TrackGame now2 = TrackGame.creatInstanceFromClient("data/TrackGame.txt", new RandomStrategy());
    for (CircularOrbit circularOrbit : now2.getGroups()) {
      draw(circularOrbit);
    }
    AtomStructure now3 = AtomStructure.createInstanceFromClient("data/AtomicStructure.txt");
    draw(now3);
  }
}
