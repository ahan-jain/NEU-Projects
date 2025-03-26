import cs3500.model.Card;
import cs3500.model.CardCell;
import cs3500.model.Grid;
import cs3500.model.Hole;
import cs3500.model.TTReadOnlyModel;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

/**
 * Represents the grid panel view in the GUI. Initializes and continuously updates the panel to
 * match the game state.
 */
public class GridPanel extends AbstractPanel {
  private final TTReadOnlyModel model;
  private final int rows;
  private final int cols;
  boolean showHints;
  private Card selectedCard;

  /**
   * A constructor that initializes the grid to a given width and height.
   * @param model the model to represent in this GUI
   * @param gridWidth the initial width of the grid
   * @param gridHeight the initial height
   */
  public GridPanel(TTReadOnlyModel model, int gridWidth, int gridHeight) {
    this.model = model;
    this.rows = model.getGrid().getGridRows();
    this.cols = model.getGrid().getGridCols();
    this.showHints = false;
    this.selectedCard = null;

    setPreferredSize(new Dimension(gridWidth, gridHeight));
    setLayout(new GridLayout(rows, cols));
    initializeGrid();
  }

  private void initializeGrid() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        JPanel cell = createCell(row, col);
        add(cell);
      }
    }
  }

  private JPanel createCell(int row, int col) {
    JPanel cell = new TTViewGridCell(row, col);
    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    cell.setPreferredSize(new Dimension(getCellWidth(), getCellHeight()));
    return cell;
  }

  private void renderCell(Graphics2D g2d, int row, int col) {
    if (model.getGrid().getGrid()[row][col] instanceof Hole) {
      g2d.setColor(Color.GRAY);
      g2d.fillRect(0, 0, getCellWidth(), getCellHeight());
    } else if (model.getGrid().getGrid()[row][col].isEmpty()) {
      g2d.setColor(Color.YELLOW);
      g2d.fillRect(0, 0, getCellWidth(), getCellHeight());
      if (this.showHints) {
        paintHint(g2d, row, col);
      }
    } else {
      paintCard(g2d, model.getContentsAt(row, col));
    }
  }

  protected void setSelectedCard(Card card) {
    this.selectedCard = card;
    repaint();
  }

  protected void paintHint(Graphics2D g2d, int row, int col) {
    if (selectedCard != null) {
      g2d.setColor(Color.red);
      Grid grid = model.getGrid();
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          if (grid.getGrid()[i][j] instanceof CardCell && grid.getGrid()[i][j].isEmpty()) {
            int flips = grid.countFlip(i, j, selectedCard, true);
            g2d.drawString(String.valueOf(flips), 5, getCellHeight() - 5);
          }
        }
        }
      }
    }


  @Override
  public int getCellHeight() {
    return this.getHeight() / rows;
  }

  @Override
  public int getCellWidth() {
    return this.getWidth() / cols;
  }

  @Override
  public void highlightCard(Graphics2D g2d, Card card, Color originalColor) {
    // A stub method that does nothing as a grid panel cannot be highlighted
  }

  /**
   * Updates the grid to the new width and height and refreshes the game.
   */
  protected void updateGrid() {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        JPanel cell = (JPanel) getComponent(row * cols + col);
        cell.setPreferredSize(new Dimension(getCellWidth(), getCellHeight()));
        cell.repaint();
      }
    }
  }

  protected void outputCellInfo(int clickedRow, int clickedColumn) {
    System.out.println("Cell clicked at row index " + clickedRow + ", column index "
            + clickedColumn);
    JOptionPane.showMessageDialog(this, "cell: " + clickedRow + "col: " + clickedColumn);
  }

  /**
   * Toggles hints on and off.
   */
  public void toggleHints() {
    this.showHints = !this.showHints;
  }

  /**
   * A class to represent a grid cell in the TTView.
   */
  protected class TTViewGridCell extends JPanel {
    private int row;
    private int col;

    protected TTViewGridCell(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      setSize(getCellWidth(), getCellHeight());
      Graphics2D g2d = (Graphics2D) g;
      renderCell(g2d, row, col);
    }
  }
}
