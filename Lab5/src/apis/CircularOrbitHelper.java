package apis;

import circularorbit.AtomStructure;
import circularorbit.CircularOrbit;
import circularorbit.SocialNetworkCircle;
import circularorbit.TrackGame;
import circularorbit.iostrategy.ScannerAndFilesIo;
import circularorbit.trackgameother.RandomStrategy;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JPanel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import physicalobject.Athlete;
import physicalobject.CentralUser;
import physicalobject.Nucleus;
import relation.Relation;
import track.Track;

public class CircularOrbitHelper {

  @AllArgsConstructor
  @Getter
  private static class Location {

    int xx;
    int yy;
  }

  static class Shapes<L, E> extends JPanel {

    private final int rad;
    private final CircularOrbit<L, E> circularOrbit;

    private Shapes(int tempRad, CircularOrbit<L, E> tempOrbit) {
      this.rad = tempRad;
      this.circularOrbit = tempOrbit;
    }

    /**
     * paint component.
     *
     * @param g the graphics will paint
     */
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      int width = getWidth();
      int height = getHeight();
      g.drawOval(width / 2 - rad, height / 2 - rad, rad * 2, rad * 2);
      Font font = new Font("Serif", Font.PLAIN, 13);
      g.setFont(font);
      String cent;
      if (!(circularOrbit instanceof TrackGame)) {
        System.out.println(circularOrbit.getClass());
        L temp = circularOrbit.getCentralPoint().getCentralObjects().get(0);
        if (temp instanceof CentralUser) {
          cent = ((CentralUser) temp).getName();
        } else {
          cent = ((Nucleus) temp).getName();
        }
      } else {
        cent = "EMPTY";
      }
      g.drawString(cent, width / 2 - rad / 2 - rad / 4, height / 2 + rad / 3);

      Map<E, Location> positions = new HashMap<>();
      int cnt = 0;
      for (Track track : circularOrbit.getTracks()) {
        cnt++;
        g.drawOval(width / 2 - (rad + cnt * 50), height / 2 - (rad + cnt * 50),
            2 * (rad + cnt * 50), 2 * (rad + cnt * 50));
        double angel = 0;
        List<E> theObjects = circularOrbit.getTrackContent().get(track);
        if (theObjects == null) {
          continue;
        }
        for (E object : theObjects) {
          double r1 = rad + cnt * 50;
          double r2 = 20;
          double acx = (double) width / 2 + r1 * Math.cos(angel) - r2;
          double acy = (double) height / 2 + r1 * Math.sin(angel) - r2;
          g.drawOval((int) Math.round(acx), (int) Math.round(acy), 40, 40);
          angel = angel + (2 * Math.PI) / theObjects.size();
          positions.put(object, new Location((int) (Math.round(acx)) + (int)
              Math.round(r2), (int) Math.round(acy) + (int) Math.round(r2)));
        }
      }
      for (Entry<E, Location> entry : positions.entrySet()) {
        if (!(circularOrbit instanceof SocialNetworkCircle)) {
          break;
        }
        List<Relation<E, E>> nowRelation = circularOrbit.getT2tRelations().get(entry.getKey());
        g.setColor(Color.green);
        for (Relation<E, E> toObject : nowRelation) {
          g.drawLine(entry.getValue().getXx(), entry.getValue().getYy(),
              positions.get(toObject.getTo()).getXx(),
              positions.get(toObject.getTo()).getYy());
        }
        g.setColor(Color.red);
        for (Relation<L, E> relation : circularOrbit.getC2tRelations()) {
          if (relation.getTo().equals(entry.getKey())) {
            g.drawLine(width / 2, height / 2, entry.getValue().getXx(),
                entry.getValue().getYy());
          }
        }
      }
    }
  }

  /**
   * draw a gui for circularOrbit.
   *
   * @param circularOrbit the circularOrbit will draw.
   * @param <L> the central object
   * @param <E> the object in track
   */
  public static <L, E> void draw(CircularOrbit<L, E> circularOrbit) {
    int tempRad = 100;
    Shapes panel = new Shapes<>(tempRad, circularOrbit);
    JFrame application = new JFrame();
    application.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    application.add(panel);
    int width = 1000;
    int height = 1000;
    application.setSize(width, height);
    application.setVisible(true);
  }

  /**
   * the main method to draw the GUI.
   *
   * @param args no use.
   */
  public static void main(String[] args) {
    SocialNetworkCircle now1 = null;
    TrackGame now2;
    AtomStructure now3 = null;
    try {
      String filename = "data/SocialNetworkCircle_my.txt";
      now1 = SocialNetworkCircle
          .createInstanceFromClient(filename, new ScannerAndFilesIo(new File(filename)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    draw(now1);
    try {
      String filename = "data/TrackGame.txt";
      now2 = TrackGame
          .creatInstanceFromClient(filename, new RandomStrategy(),
              new ScannerAndFilesIo(new File(filename)));
      for (CircularOrbit<Athlete, Athlete> circularOrbit : now2.getGroups()) {
        draw(circularOrbit);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      String filename = "data/AtomicStructure.txt";
      now3 = AtomStructure
          .createInstanceFromClient(filename, new ScannerAndFilesIo(new File(filename)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    draw(now3);
  }
}
