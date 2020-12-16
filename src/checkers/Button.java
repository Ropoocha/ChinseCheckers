package checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

public class Button extends JPanel {

  public enum Player {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    CYAN,
    MAGENTA,
    CLEAR
  }

  private Color color = Color.BLACK;
  private final Color baseColor = new Color(105, 70, 56);
  private Player player = Player.CLEAR;
  private int index;

  private boolean selected = false;

  public Button(Player player, ButtonListener listener, int index) {
    this.index = index;
    this.player = player;
    setOpaque(false);
    addMouseListener(listener);
  }

  public Button(ButtonListener listener, int index) {
    this(Player.CLEAR, listener, index);
  }

  private int getDiameter() {
    return Math.min(getWidth(), getHeight());
  }


  @Override
  public boolean contains(int x, int y) {
    int radius = getDiameter() / 2;
    return Point2D.distance(x, y, getWidth() / 2.0, getHeight() / 2.0) < radius;
  }

  @Override
  protected void paintComponent(Graphics g) {
    switch (player) {
      case RED:
        color = new Color(230, 79, 48);
        break;
      case BLUE:
        color = Color.BLUE;
        break;
      case GREEN:
        color = Color.GREEN;
        break;
      case CYAN:
        color = Color.CYAN;
        break;
      case YELLOW:
        color = Color.YELLOW;
        break;
      case MAGENTA:
        color = Color.MAGENTA;
        break;
      case CLEAR:
        color = baseColor;
    }
    int diameter = getDiameter();
    int radius = diameter / 2;
    int miniDiameter = 2 * diameter / 3;
    int miniRadius = miniDiameter / 2;

    if (selected) {
      g.setColor(Color.RED);
      g.fillOval(0, 0, diameter, diameter);
      g.setColor(color);
      g.fillOval(radius - miniRadius, radius - miniRadius, miniDiameter, miniDiameter);
    } else {
      g.setColor(color);
      g.fillOval(0, 0, diameter, diameter);
    }
  }

  public int getIndex() {
    return index;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
