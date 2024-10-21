package cs3500.solored;

import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.Cards;

/**
 * Test class for the basic SoloRedGameModel.
 */
public class BasicModelTest extends AbstractSoloRedGameModelTest {

  @Override
  protected RedGameModel<Cards> createModel() {
    return new SoloRedGameModel();
  }
}
