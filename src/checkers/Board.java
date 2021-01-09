package checkers;

import checkers.Button.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel {

  private final int size;
  private final int players;
  ArrayList<Button> buttons = new ArrayList<>();
  ButtonListener listener;


  public Board(int side, int size, int players) {
    this.size = size;
    this.players = players;

    setBackground(new Color(97, 202, 31));
    setPreferredSize(new Dimension(side, side));
    listener = new ButtonListener();
    Button centerButton = new Button(listener, 0);
    buttons.add(centerButton);
    add(centerButton);
    int global = 1;
    for (int r = 1; r <= 2 * this.size; ++r) {
      for (int b = 1; b <= 6; ++b) {
        for (int ball = 1; ball <= r; ++ball) {
          Button button;
          if (players == 2 && b == 2 && r > this.size) {
            button = new Button(Player.RED, listener, global);
          } else if (players == 2 && b == 5 && r > this.size) {
            button = new Button(Player.BLUE, listener, global);
          } else if (players == 3 && b == 2 && r > this.size) {
            button = new Button(Player.RED, listener, global);
          } else if (players == 3 && b == 4 && r > this.size) {
            button = new Button(Player.BLUE, listener, global);
          } else if (players == 3 && b == 6 && r > this.size) {
            button = new Button(Player.GREEN, listener, global);
          } else if (players == 4 && b == 1 && r > this.size) {
            button = new Button(Player.RED, listener, global);
          } else if (players == 4 && b == 3 && r > this.size) {
            button = new Button(Player.BLUE, listener, global);
          } else if (players == 4 && b == 4 && r > this.size) {
            button = new Button(Player.GREEN, listener, global);
          } else if (players == 4 && b == 6 && r > this.size) {
            button = new Button(Player.YELLOW, listener, global);
          } else if (players == 6 && b == 1 && r > this.size) {
            button = new Button(Player.RED, listener, global);
          } else if (players == 6 && b == 2 && r > this.size) {
            button = new Button(Player.BLUE, listener, global);
          } else if (players == 6 && b == 3 && r > this.size) {
            button = new Button(Player.GREEN, listener, global);
          } else if (players == 6 && b == 4 && r > this.size) {
            button = new Button(Player.YELLOW, listener, global);
          } else if (players == 6 && b == 5 && r > this.size) {
            button = new Button(Player.MAGENTA, listener, global);
          } else if (players == 6 && b == 6 && r > this.size) {
            button = new Button(Player.CYAN, listener, global);
          } else {
            button = new Button(listener, global);
          }
          button.setVisible(false);
          buttons.add(button);
          add(button);
          global++;
        }
      }
    }
  }

  public Board(int side) {
    this(side, 3, 2);
  }

  public Button getBall(int index) throws IndexOutOfBoundsException {
    return buttons.get(index);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    int shorterSide = Math.min(getWidth(), getHeight());
    int panelSide = Math.max(shorterSide, 600);
    int panelCenter = panelSide / 2;
    int boardHeight = 9 * panelSide / 10;
    int triangleHeight = boardHeight / 4;
    int triangleSide = (int) (2 * Math.sqrt(3) * triangleHeight / 3);

    int x1 = panelCenter;
    int y1 = (panelSide - boardHeight) / 2;

    int[] xcoords = new int[]{
        x1,                         //         top
        x1 + triangleSide / 2,      // hexagon top-right
        x1 + triangleSide * 3 / 2,  //         top-right
        x1 + triangleSide,          // hexagon right
        x1 + triangleSide * 3 / 2,  //         bottom-right
        x1 + triangleSide / 2,      // hexagon bottom-right
        x1,                         //         bottom
        x1 - triangleSide / 2,      // hexagon bottom-left
        x1 - triangleSide * 3 / 2,  //         bottom-left
        x1 - triangleSide,          // hexagon left
        x1 - triangleSide * 3 / 2,  //         top-left
        x1 - triangleSide / 2       // hexagon top-left
    };

    int[] ycoords = new int[]{
        y1,                         //         top
        y1 + triangleHeight,        // hexagon top-right
        y1 + triangleHeight,        //         top-right
        y1 + 2 * triangleHeight,    // hexagon right
        y1 + 3 * triangleHeight,    //         bottom-right
        y1 + 3 * triangleHeight,    // hexagon bottom-right
        y1 + 4 * triangleHeight,    //         bottom
        y1 + 3 * triangleHeight,    // hexagon bottom-left
        y1 + 3 * triangleHeight,    //         bottom-left
        y1 + 2 * triangleHeight,    // hexagon left
        y1 + triangleHeight,        //         top-left
        y1 + triangleHeight,        // hexagon top-left
    };
    Polygon hexagram = new Polygon(xcoords, ycoords, 12);
    g2d.setColor(new Color(150, 111, 51));
    g2d.fillPolygon(hexagram);

    int miniX = triangleSide / (size + 1);
    int miniY = triangleHeight / (size + 1);

    int padding = 1;
    int radius = triangleHeight / (2 * size + 1) - padding;
    int ballSize = radius * 2;

    buttons.get(0).setVisible(true);
    buttons.get(0).setSize(ballSize, ballSize);
    buttons.get(0).setLocation(panelCenter - radius, panelCenter - radius);

    int xPos, yPos;
    int global = 1;
    for (int ring = 1; ring <= size * 2; ++ring) {
      xPos = panelCenter - miniX * ring;
      yPos = panelCenter;

      for (int ball = 1; ball <= ring * 6; ++ball) {
        int xSign = 1, ySign = -1, denominator = 2;
        if (ball >= 3 * ring + 1 && ball <= ring * 6) {
          xSign = -1;
        }

        if ((ball >= ring + 1 && ball <= ring * 2) || (ball >= ring * 4 + 1
            && ball <= ring * 5)) {
          denominator = 1;
          ySign = 0;
        } else if (ball >= ring * 2 + 1 && ball <= ring * 4) {
          ySign = 1;
        }
        if (ring <= size || (ball % ring >= ring - size + 1 && ball % ring <= size + 1) || (
            ball % ring == 0 && ring == size + 1)) {
          buttons.get(global).setVisible(true);
          buttons.get(global).setSize(ballSize, ballSize);
          buttons.get(global).setLocation(xPos - radius, yPos - radius);
        }
        xPos += xSign * miniX / denominator;
        yPos += ySign * miniY;
        global++;
      }
    }
  }

  public int getBoardSize() {
    return size;
  }

  public int getPlayers() {
    return players;
  }

  public ButtonListener getListener() {
    return listener;
  }

  public synchronized void setMove(int first, int second) {
    if (first == -1 || second == -1) {
      return;
    }
    Player temp = buttons.get(first).getPlayer();
    System.out.println(temp);
    buttons.get(first).setPlayer(Player.CLEAR);
    buttons.get(first).repaint();
    buttons.get(second).setPlayer(temp);
    buttons.get(second).repaint();
  }
}
