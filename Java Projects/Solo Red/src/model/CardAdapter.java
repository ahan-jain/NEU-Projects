import cs3500.providers.model.AttackValue;
import cs3500.providers.model.ICard;
import cs3500.providers.model.IPlayer;
import cs3500.providers.model.PlayerType;

/**
 * An adapter class that adapts a Card to an ICard.
 */
public class CardAdapter implements ICard {
  private final Card card;

  public CardAdapter(Card card) {
    this.card = card;
  }

  @Override
  public String getIdentifier() {
    return card.getCardName();
  }

  @Override
  public AttackValue getNorth() {
    return convertAttackValue(card.getNorth());
  }

  @Override
  public AttackValue getSouth() {
    return convertAttackValue(card.getSouth());
  }

  @Override
  public AttackValue getEast() {
    return convertAttackValue(card.getEast());
  }

  @Override
  public AttackValue getWest() {
    return convertAttackValue(card.getWest());
  }

  /**
   * Converts a card of type ICard to a new Card object. Also sets the card's player
   * to the given IPlayer.
   *
   * @return a Card object with the same info as the given ICard
   */
  public static Card convertICardToCard(ICard that, IPlayer player) {
    String name = that.getIdentifier();
    name += " " + that.getNorth().toString();
    name += " " + that.getSouth().toString();
    name += " " + that.getEast().toString();
    name += " " + that.getWest().toString();
    Card created = new Card(name);
    if (player.getType() == PlayerType.BLUE) {
      created.flip();
    }

    return created;
  }

  private AttackValue convertAttackValue(AttackValue av) {
    switch (av.getValue()) {
      case 1:
        return AttackValue.ONE;
      case 2:
        return AttackValue.TWO;
      case 3:
        return AttackValue.THREE;
      case 4:
        return AttackValue.FOUR;
      case 5:
        return AttackValue.FIVE;
      case 6:
        return AttackValue.SIX;
      case 7:
        return AttackValue.SEVEN;
      case 8:
        return AttackValue.EIGHT;
      case 9:
        return AttackValue.NINE;
      case 10:
        return AttackValue.A;
      default:
        throw new IllegalArgumentException("invalid attack value!");
    }
  }

  @Override
  public String toString() {
    return this.getIdentifier() + " " + this.getNorth()
            + " " + this.getSouth() + " " + this.getEast() + " " + this.getWest();
  }
}
