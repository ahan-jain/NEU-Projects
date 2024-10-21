package cs3500.solored;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * The tests for the playGame public method in the controller for the Solo Red card game.
 */
public class SoloRedTextControllerTest {

  @Test
  public void testInstantQuit() {
    StringReader input = new StringReader("q");
    StringBuilder output = new StringBuilder();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    SoloRedGameModel model = new SoloRedGameModel();
    List<Cards> deck = model.getAllCards();

    controller.playGame(model, deck, false, 2, 5);

    Assert.assertTrue(output.toString().contains("Game quit!"));
    Assert.assertTrue(output.toString().contains("State of game when quit:"));
  }

  @Test
  public void testInvalidCommand() {
    StringReader input = new StringReader("invalid q");
    StringBuilder output = new StringBuilder();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    SoloRedGameModel model = new SoloRedGameModel();
    List<Cards> deck = model.getAllCards();

    controller.playGame(model, deck, false, 2, 5);

    Assert.assertTrue(output.toString().contains("Invalid command. Try again."));
  }

  @Test
  public void testPlayToPalette() {
    StringReader input = new StringReader("palette 1 1 q");
    StringBuilder output = new StringBuilder();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    SoloRedGameModel model = new SoloRedGameModel();
    List<Cards> deck = model.getAllCards();


    controller.playGame(model, deck, false, 2, 5);

    Assert.assertTrue(output.toString().contains("Number of cards in deck:"));
  }

  @Test
  public void testInvalidMove() {
    StringReader input = new StringReader("palette 999 999 q");
    StringBuilder output = new StringBuilder();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    SoloRedGameModel model = new SoloRedGameModel();
    List<Cards> deck = model.getAllCards();


    controller.playGame(model, deck, false, 2, 5);

    Assert.assertTrue(output.toString().contains("Invalid move. Try again."));
  }

  @Test
  public void testDeck() {
    StringReader input = new StringReader("palette 1 1 q");
    StringBuilder output = new StringBuilder();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    SoloRedGameModel model = new SoloRedGameModel();
    List<Cards> deck = model.getAllCards();

    controller.playGame(model, deck, false, 2, 5);
    Assert.assertTrue(output.toString().contains("Number of cards in deck: 28"));
  }

  @Test
  public void testQuitMidGame() {
    StringReader input = new StringReader("palette 1 1 q");
    StringBuilder output = new StringBuilder();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    SoloRedGameModel model = new SoloRedGameModel();
    List<Cards> deck = model.getAllCards();


    controller.playGame(model, deck, false, 2, 5);

    Assert.assertTrue(output.toString().contains("State of game when quit:"));
  }

  @Test
  public void testGameOver() {
    StringReader input = new StringReader("palette 2 1 canvas 4");
    StringBuilder output = new StringBuilder();
    SoloRedTextController controller = new SoloRedTextController(input, output);

    SoloRedGameModel model = new SoloRedGameModel();
    List<Cards> deck = model.getAllCards();


    controller.playGame(model, deck, false, 2, 5);

    Assert.assertTrue(output.toString().contains("Game won.") ||
            output.toString().contains("Game lost."));
  }
}

