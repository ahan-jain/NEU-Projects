import TTModel;
import TTGUIView;

/**
 * A controller that handles the necessary behaviors for an AI player to function. Takes in an AI
 * player.
 */
public class AIController extends AbstractController {
  /**
   * A constructor that takes in the necessary params for this controller.
   *
   * @param model  the model of the game
   * @param view   the view to display to
   * @param player a player.
   */
  public AIController(TTModel model, TTGUIView view, PlayerActions player) {
    super(model, view, player);
  }

  @Override
  public void handleTurn() {
    player.makeMove();
    view.updateView();
  }
}
