import java.util.ArrayList;
import java.util.List;

import cs3500.providers.model.ICard;
import cs3500.providers.model.IPlayer;
import cs3500.providers.model.PlayerType;

/**
 * An adapter from our Player to the IPlayer interface. Ensures all functionality from the IPlayer
 * interface is carried over and simulated with our model implementation.
 */
public class PlayerAdapter implements IPlayer {
  private final Player color;
  private final TTModel model;

  /**
   * A constructor that gives this adapter a Player to represent and a model to use for
   * getting hand.
   *
   * @param model the model to use for necessary operations, like getHand
   * @param color the Player color of this player
   */
  public PlayerAdapter(TTModel model, Player color) {
    this.model = model;
    this.color = color;
  }


  @Override
  public List<ICard> getHand() {
    List<Card> handInCard = model.getHand(color);
    List<ICard> hand = new ArrayList<>();
    for (Card c : handInCard) {
      hand.add(new CardAdapter(c));
    }
    return hand;
  }

  @Override
  public PlayerType getType() {
    return color == Player.RED ? PlayerType.RED : PlayerType.BLUE;
  }

  @Override
  public ICard getFromHand(int index) {
    return getHand().get(index);
  }

  @Override
  public String toString() {
    return color.name();
  }
}
