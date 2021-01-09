package checkers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

  private final PlayerDirector playerDirector;
  private static Integer boardSize = -1;
  private static Integer playerAmount = -1;
  private static boolean serverSet = false;
  private static int currentlyConnected = 0;
  private static final ArrayList<PrintWriter> writers = new ArrayList<>();
  private static final ArrayList<Scanner> readers = new ArrayList<>();

  public Server() {
    this.playerDirector = new PlayerDirector(playerAmount, boardSize, this);
  }

  /*
      TODO: The argument of the function is the ID of the player who is currently requested to make the move.
       The function should return the player's movement.
  */
  public PlayerMovement getPlayerMovement(Integer playerID) {
    int first, second;
    while (true) {
      writers.get(playerID).print("RETURNMOVE");
      String move = readers.get(playerID).nextLine();
      first = Integer.parseInt(move.split(" ")[0]);
      second = Integer.parseInt(move.split(" ")[1]);
      if (first != -1 && second != -1) {
        break;
      }
    }
    return new PlayerMovement(playerID, first, second);
  }

  /*
      TODO: This event is triggered when the player made the invalid movement.
  */
  public void invalidMovementEvent() {

  }

  /*
     TODO: This event is triggered when the player (with the ID equals playerID) wins.
      You can use this to inform players that someone has won.
      The other players can continue the game in the fight for more places.
  */
  public void newWinnerEvent(Integer playerID) {
    for (PrintWriter writer : writers) {
      writer.println("Player " + playerID + " wins!");
    }
  }

  /*
      TODO: execute this method when every player launched to the game and you want to start
  */
  public void startGame() {
    playerDirector.startGame();
  }

  /*
      TODO: execute this method to finish the game
  */
  public void finishGame() {
    playerDirector.finishGame();
  }

  /*
      TODO: The client should provide the amount of players.
  */
  public static Integer getPlayerAmount() {
    return serverSet ? playerAmount : -1;
  }

  /*
      TODO: The client should provide the size of board.
  */
  public static Integer getBoardSize() {
    return serverSet ? boardSize : -1;
  }

  public static void main(String[] args) {
    Server server = new Server();
    System.out.println("Server is running...");
    ExecutorService pool = Executors.newFixedThreadPool(10);

    try (ServerSocket listener = new ServerSocket(62626)) {

      while (true) {
        pool.execute(new Agent(listener.accept()));
        System.out.println("Client connected!");
        currentlyConnected++;
        if (currentlyConnected == playerAmount) {
          server.startGame();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class Agent implements Runnable {

    private final Socket client;
    private Scanner in;
    private PrintWriter out;

    public Agent(Socket socket) {
      client = socket;
    }

    @Override
    public void run() {
      try {
        in = new Scanner(client.getInputStream());
        out = new PrintWriter(client.getOutputStream(), true);
        writers.add(out);
        readers.add(in);

        // If server isn't set up request the settings from the first player that connects.
        if (!serverSet) {
          out.println("SETTINGS");
          String settings = in.nextLine();
          if (settings == null) {
            return;
          }
          try {
            playerAmount = Integer.parseInt(settings.split(" ")[0]);
            boardSize = Integer.parseInt(settings.split(" ")[1]);
            serverSet = true;
          } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid settings, should get 'playerCount boardSize'");
          }
        }

        if (currentlyConnected > playerAmount) {
          out.println("TOOMANYPLAYERS");
          currentlyConnected--;
          return;
        }

        // Send playerAmount and boardSize to next clients
        out.println("VARIABLES " + playerAmount + " " + boardSize);

          while (true) {
            out.println("MAKEMOVE");
            String move = in.nextLine();
            for (PrintWriter writer : writers) {
              writer.println("SYNC " + Integer.parseInt(move.split(" ")[0]) + " " + Integer
                  .parseInt(move.split(" ")[1]));
            }
          }

      } catch (IOException ioException) {
        ioException.printStackTrace();
      } finally {
        try {
          client.close();
          System.out.println("Client disconnected!");
        } catch (IOException ignore) {
        }
      }

    }
  }
}
