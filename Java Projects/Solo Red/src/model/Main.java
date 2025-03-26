import java.io.File;
import java.io.IOException;

import cs3500.controllers.AbstractController;
import cs3500.controllers.HumanController;
import cs3500.controllers.PlayerActions;
import cs3500.providers.model.ICard;
import cs3500.providers.model.PlayerType;
import cs3500.providers.view.graphics.ThreeTriosGraphicsViewImpl;
import cs3500.providers.view.text.ThreeTriosView;
import cs3500.providers.view.text.ThreeTriosViewImpl;
import GUIView;
import ViewAdapter;
import ViewFeaturesAdapter;

/**
 * A main class to run the game.
 */
public class Main {
  /**
   * The main method that actually runs the game.
   *
   * @param args the args
   */
  public static void main(String[] args) {
    renderGUI();
  }

  private static void renderTextual() {
    TTModel model = new ThreeTriosModel();
    String cardPath1 = "docs" + File.separator + "card1.txt";
    String gridPath1 = "docs" + File.separator + "grid1.txt";
    Appendable ap = new StringBuilder();
    cs3500.providers.model.ThreeTriosModel<ICard> modelAdapter = new TTModelAdapter(
        model, cardPath1, gridPath1);
    ThreeTriosView textualView = new ThreeTriosViewImpl(modelAdapter, ap);
    modelAdapter.startGame();
    modelAdapter.playToCell(3, 1, 1);
    try {
      textualView.render();
    } catch (IOException ignore) {
      // does nothing
    }
    System.out.println(ap);
  }

  private static void renderGUI() {
    TTModel model = new ThreeTriosModel();
    String cardPath1 = "docs" + File.separator + "card1.txt";
    String gridPath1 = "docs" + File.separator + "grid1.txt";

    model.startGame(gridPath1, cardPath1);
    ViewModel viewModel = new ViewModel(model);
    PlayerActions player1 = new HumanPlayer(Player.RED);
    GUIView viewRed = new GUIView(viewModel, player1);
    AbstractController controllerRed = new HumanController(model, viewRed, player1);
    ThreeTriosView providerView = new ThreeTriosGraphicsViewImpl(
        new TTModelAdapter(model, cardPath1, gridPath1));
    PlayerActions player2 = new HumanPlayer(Player.BLUE);
    AbstractController controllerBlue = new HumanController(
        model, new ViewAdapter(providerView, viewModel), player2);
    providerView.addFeatureListener(new ViewFeaturesAdapter(controllerBlue, PlayerType.BLUE));

    viewRed.setVisible(true);
    try {
      providerView.render();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
