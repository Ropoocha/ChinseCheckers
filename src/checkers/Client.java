package checkers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class Client {

  // client-server variables
  private final String serverAddress;
  private Scanner in;
  private PrintWriter out;
  private int id;

  // swing variables
  private final JFrame frame = new JFrame("Chinese checkers");
  private Board board;
  private int playerCount;
  private int boardSize;


  public Client(String serverAddress) {
    this.serverAddress = serverAddress;
  }

  private void run() throws IOException {
    try {
      Socket socket = new Socket(serverAddress, 62626);
      in = new Scanner(socket.getInputStream());
      out = new PrintWriter(socket.getOutputStream(), true);

      while (in.hasNextLine()) {
        String line = in.nextLine();
        if (line.startsWith("SETTINGS")) {
          Object[] noOfPlayers = {2, 3, 4, 6};
          Object[] possibleSizes = {3, 4, 5, 6, 7};
          int players = (int) JOptionPane
              .showInputDialog(null, "Select the number of players", "Players",
                  JOptionPane.QUESTION_MESSAGE, null, noOfPlayers, 2);
          int size = (int) JOptionPane
              .showInputDialog(null, "Select the size of the board", "Board size",
                  JOptionPane.QUESTION_MESSAGE, null, possibleSizes, 3);
          out.println(players + " " + size);
        } else if (line.startsWith("TOOMANYPLAYERS")) {
          System.out.println("Currently there isn't enough slots on the server.");
        } else if (line.startsWith("VARIABLES")) {
          String vars = line.substring(10);
          try {
            playerCount = Integer.parseInt(vars.split(" ")[0]);
            boardSize = Integer.parseInt(vars.split(" ")[1]);
            board = new Board(600, boardSize, playerCount);
            frame.getContentPane().add(new JScrollPane(board));
            frame.pack();
          } catch (NumberFormatException e) {
            throw new NumberFormatException("Server passed variables in a wrong format (How???)");
          }
        } else if (line.startsWith("MAKEMOVE")) {
          out.println(board.getListener().getMove());
        } else if (line.startsWith("RETURNMOVE")) {
          out.println(board.getListener().getMove());
        } else if (line.startsWith("ID")) {
          id = Integer.parseInt(line.substring(3));
        } else if (line.startsWith("SYNC")) {
          String move = line.substring(5);
          if (board != null) {
            board.setMove(Integer.parseInt(move.split(" ")[0]), Integer.parseInt(move.split(" ")[1]));
          }
        }
      }
    } finally {
      frame.setVisible(false);
      frame.dispose();
    }

  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("The first command line argument should be the server IP address");
      return;
    }
    Client client = new Client(args[0]);
    client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    client.frame.setVisible(true);
    client.run();
  }
}
