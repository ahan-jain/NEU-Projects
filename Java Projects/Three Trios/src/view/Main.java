import cs3500.controllers.AIController;
import cs3500.controllers.AbstractController;
import cs3500.controllers.HumanController;
import cs3500.controllers.PlayerActions;
import cs3500.model.AIPlayer;
import cs3500.model.AIPlayerStrategy;
import cs3500.model.CheckCorners;
import cs3500.model.HumanPlayer;
import cs3500.model.Player;
import cs3500.model.TTModel;
import cs3500.model.ThreeTriosModel;
import cs3500.model.ViewModel;

import java.io.File;

/**
 * represents the main class which is responsible for executing the configuration files,
 * and display the game.
 */
public class Main {

  /**
   * represents the main method.
   *
   * @param args the input, if any
   */
  public static void main(String[] args) {
    TTModel model = new ThreeTriosModel();
    String cardPath1 = "docs" + File.separator + "card1.txt";
    String gridPath1 = "docs" + File.separator + "grid1.txt";

    model.startGame(gridPath1, cardPath1);
    ViewModel viewModel = new ViewModel(model);
    PlayerActions player1 = new HumanPlayer(Player.RED);
    GUIView viewRed = new GUIView(viewModel, player1);
    TTGUIView hintView = new HintGUIView(viewRed);
    AbstractController controllerRed = new HumanController(model, viewRed, player1);
    AIPlayerStrategy strat = new CheckCorners(Player.BLUE, model);
    PlayerActions player2 = new AIPlayer(Player.BLUE, strat);
    GUIView viewBlue = new GUIView(viewModel, player2);
    AbstractController controllerBlue = new AIController(model, viewBlue, player2);

    hintView.setVisible(true);
    viewBlue.setVisible(true);

  }
}