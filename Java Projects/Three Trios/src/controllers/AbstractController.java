import Player;
import TTModel;
import TTGUIView;

/**
 * An abstract controller that holds the necessary behaviors
 * for a controller, human or AI, to function.
 */
public abstract class AbstractController implements TTModelFeatures, TTViewFeatures {
  protected final TTModel model;
  protected final TTGUIView view;
  protected final PlayerActions player;
  protected int selectedCardIndex = -1;

  /**
   * Creates a new controller with the following params.
   *
   * @param model the model of the game
   * @param view this controller's respective view
   * @param player the player this controller belongs to
   */
  public AbstractController(TTModel model, TTGUIView view, PlayerActions player) {
    this.model = model;
    this.view = view;
    this.player = player;

    model.addModelListener(this);
    view.addViewListener(this);
  }

  @Override
  public void onTurn(Player currentPlayer) {
    if (currentPlayer == player.getPlayer()) {
      view.showError("It is your turn!");
      handleTurn();
    }
    view.updateView();
  }

  @Override
  public void onGameOver(Player winner) {
    view.showGameOver(winner);
  }

  @Override
  public void selectCard(int cardIdx) {
    selectedCardIndex = cardIdx;
  }


  @Override
  public void selectPosition(int row, int col) {
    if (selectedCardIndex == -1) {
      System.out.println("No card selected!");
      return;
    }
    try {
      model.placeCard(selectedCardIndex, row, col);
      selectedCardIndex = -1;
      view.updateView();
    } catch (IllegalStateException | IllegalArgumentException e) {
      view.showError(e.getMessage());
    }
  }

  /**
   * Handles the turn logic specific to the controller type (Human or AI).
   */
  public abstract void handleTurn();
}
