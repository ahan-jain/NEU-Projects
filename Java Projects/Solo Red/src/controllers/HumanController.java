import TTModel;
import TTGUIView;

/**
 * Holds necessary behaviors for a human player to function. Sends info back to the super class.
 */
public class HumanController extends AbstractController {

  /**
   * A constructor that takes in the necessary params to create this controller.
   * @param model the model of the game
   * @param view the respective view for this controller
   * @param player the player that this controller is for
   */
  public HumanController(TTModel model, TTGUIView view, PlayerActions player) {
    super(model, view, player);
  }

  @Override
  public void handleTurn() {
    // a stub to ensure functionality. This method is overridden in AIController.
  }


}
