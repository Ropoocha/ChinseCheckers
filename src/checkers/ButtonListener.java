package checkers;

import checkers.Button.Player;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class ButtonListener implements MouseListener {

  Button first, second;

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    Button that = (Button) e.getSource();
    if (first == null && second == null) {
      first = that;
      first.setSelected(true);
      first.repaint();
    } else if (first == null) {
      first = that;
      second = null;
      first.setSelected(true);
      first.repaint();
    } else if (second == null) {
      second = that;
      first.setSelected(false);
      second.setSelected(false);
      Player temp = first.getPlayer();
      first.setPlayer(Player.CLEAR);
      second.setPlayer(temp);
      first.repaint();
      second.repaint();
    } else {
      first = that;
      second = null;
      first.setSelected(true);
      first.repaint();
    }

    if (first != null && second != null) {
      first.repaint();
      second.repaint();
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  public String getMove() {
    return Objects.requireNonNullElse(first, new Button(null, -1)).getIndex() + " " + Objects.requireNonNullElse(second, new Button(null, -1)).getIndex();
  }
}
