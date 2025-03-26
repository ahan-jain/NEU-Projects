import cs3500.providers.model.ICard;
import cs3500.providers.model.ICell;
import cs3500.providers.model.IPlayer;

/**
 * A class that adapts a cell to an ICell.
 */
public class CellAdapter implements ICell {
  private final TTModel model;
  private final TTCell cell;

  public CellAdapter(TTModel model, TTCell cell) {
    this.model = model;
    this.cell = cell;
  }

  @Override
  public void playCard(ICard cardToPlay, IPlayer player) {
    cell.placeCard(CardAdapter.convertICardToCard(cardToPlay, player));
  }

  @Override
  public boolean isOccupied() {
    return !cell.isEmpty();
  }

  @Override
  public boolean isHole() {
    return (cell instanceof Hole);
  }

  @Override
  public ICard getCard() {
    return new CardAdapter(cell.getCard());
  }

  @Override
  public IPlayer getOwner() {
    return new PlayerAdapter(model, cell.getCard().getPlayer());
  }

  @Override
  public void flipOwner(IPlayer newOwner) {
    cell.flipCard();
  }

  @Override
  public ICell getCopy() {
    return this;
  }

  /**
   * A static method that converts the given ICell to an object of TTCell type.
   *
   * @param that the ICell to convert
   * @return a new TTCell with the same information as the given ICell
   */
  public static TTCell convertICellToTTCell(ICell that) {
    if (that.isHole()) {
      return new Hole();
    } else if (!that.isOccupied()) {
      return new CardCell();
    } else {
      TTCell cardCell = new CardCell();
      cardCell.placeCard(CardAdapter.convertICardToCard(that.getCard(), that.getOwner()));
      return cardCell;
    }
  }
}
