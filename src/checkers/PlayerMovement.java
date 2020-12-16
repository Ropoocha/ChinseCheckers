package checkers;

public class PlayerMovement {
  private final Integer playerID;
  private final Integer currentPosition;
  private final Integer followingPosition;

  public PlayerMovement(Integer playerID, Integer currentPosition, Integer followingPosition) {
    this.playerID = playerID;
    this.currentPosition = currentPosition;
    this.followingPosition = followingPosition;
  }

  public Integer getPlayerID() {
    return playerID;
  }

  public Integer getCurrentPosition() {
    return currentPosition;
  }

  public Integer getFollowingPosition() {
    return followingPosition;
  }
}