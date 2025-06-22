/**
 *  Features needed for the view to interact with the controller.
 */
public interface TTViewFeatures {
  /**
   * Called when the user selects a card.
   * @param cardIdx the index of the selected card
   */
  void selectCard(int cardIdx);

  /**
   * Called when the user selects a position on the grid.
   * @param row the row of the selected position
   * @param col the column of the selected position
   */
  void selectPosition(int row, int col);
}
