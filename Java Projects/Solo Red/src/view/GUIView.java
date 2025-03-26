import cs3500.controllers.PlayerActions;
import cs3500.controllers.TTViewFeatures;
import cs3500.model.Player;
import cs3500.model.ViewModel;
import cs3500.model.Card;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A GUI view class that represents the model as a board. There are two panels
 * to represent each hand respectively and one to represent the grid. The game
 * operates on mouse inputs.
 */
public class GUIView extends JFrame implements TTGUIView {
  private boolean shown;
  private final ViewModel model;
  private final SidePanel redPanel;
  private final SidePanel bluePanel;
  protected final GridPanel gridPanel;
  private final JLabel currentPlayerLabel;
  private final PlayerActions player;

  private final List<TTViewFeatures> listeners;

  /**
   * A constructor that takes in a model and initializes the rest to default values.
   *
   * @param model the model to represent
   */
  public GUIView(ViewModel model, PlayerActions player) {
    this.model = model;
    this.player = player;
    this.listeners = new ArrayList<>();
    shown = false;
    setTitle("Game Board");
    setSize(800, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    currentPlayerLabel = new JLabel("Current player: " + model.getTurn(),
            SwingConstants.CENTER);
    add(currentPlayerLabel, BorderLayout.NORTH);

    redPanel = new SidePanel(Player.RED, getSidePanelWidth(), this.getHeight(), model);
    bluePanel = new SidePanel(Player.BLUE, getSidePanelWidth(), this.getHeight(), model);

    TTMouseListener redMouseListener = new TTMouseListener();
    redPanel.addMouseListener(redMouseListener);
    add(redPanel, BorderLayout.WEST);
    TTMouseListener blueMouseListener = new TTMouseListener();
    bluePanel.addMouseListener(blueMouseListener);
    add(bluePanel, BorderLayout.EAST);

    gridPanel = new GridPanel(model, getGridPanelWidth(), this.getHeight());
    TTMouseListener gridMouseListener = new TTMouseListener();
    gridPanel.addMouseListener(gridMouseListener);
    add(gridPanel, BorderLayout.CENTER);
    addMouseListeners();
    updatePanels();
    updateView();
  }

  @Override
  public void addViewListener(TTViewFeatures listener) {
    listeners.add(listener);
  }

  @Override
  public void removeViewListener(TTViewFeatures listener) {
    listeners.remove(listener);
  }

  private int getSidePanelWidth() {
    return (int) (this.getWidth() * 0.2);
  }

  private int getGridPanelWidth() {
    return (this.getWidth() - 2 * getSidePanelWidth());
  }

  @Override
  public void updateView() {
    currentPlayerLabel.setText("Current player: " + model.getTurn());
    gridPanel.updateGrid();
    redPanel.clearHighlight();
    bluePanel.clearHighlight();
    redPanel.updatePanel(model.getHand(Player.RED));
    bluePanel.updatePanel(model.getHand(Player.BLUE));
  }

  @Override
  public void addMouseListeners() {
    redPanel.addMouseListener(new TTMouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (model.getTurn() == Player.RED) {
          int selectedIdx = e.getY() / redPanel.getCellHeight();
          notifyCardSelected(selectedIdx);
          redPanel.handleMouseClick(selectedIdx);
        } else {
          JOptionPane.showMessageDialog(redPanel, "It's not RED's turn!",
                  "Invalid Action", JOptionPane.WARNING_MESSAGE);
        }
      }
    });

    bluePanel.addMouseListener(new TTMouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (model.getTurn() == Player.BLUE) {
          int selectedIdx = e.getY() / bluePanel.getCellHeight();
          notifyCardSelected(selectedIdx);
          bluePanel.handleMouseClick(selectedIdx);
        } else {
          JOptionPane.showMessageDialog(bluePanel, "It's not BLUE's turn!",
                  "Invalid Action", JOptionPane.WARNING_MESSAGE);
        }
      }
    });


    (this.player.getPlayer() == Player.RED ? redPanel : bluePanel).addSelectedCardListener(
            new SidePanel.SelectedCardListener() {
              @Override
              public void onCardSelected(Card selectedCard) {
                gridPanel.setSelectedCard(selectedCard); // Update GridPanel with selected card
              }

              @Override
              public void onCardDeselected() {
                gridPanel.setSelectedCard(null); // Clear selection in GridPanel
              }
            });

    gridPanel.addMouseListener(new TTMouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (model.getTurn() == player.getPlayer()) {
          int clickedRow = e.getY() / gridPanel.getCellHeight();
          int clickedCol = e.getX() / gridPanel.getCellWidth();

          notifyPositionSelected(clickedRow, clickedCol);
        }
      }
    });
  }

  /**
   * Updates the panels for the current game state.
   */
  public void updatePanels() {
    redPanel.updatePanel(model.getHand(Player.RED));
    bluePanel.updatePanel(model.getHand(Player.BLUE));
  }

  private void notifyCardSelected(int cardIdx) {
    for (TTViewFeatures listener : listeners) {
      listener.selectCard(cardIdx);
    }
  }

  private void notifyPositionSelected(int row, int col) {
    for (TTViewFeatures listener : listeners) {
      listener.selectPosition(row, col);
    }
    updateView();
  }

  @Override
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void showGameOver(Player winner) {
    if (!shown) {
      if (winner != null) {
        JOptionPane.showMessageDialog(this, "Game Over! Winner: " + winner);
      } else {
        JOptionPane.showMessageDialog(this, "Game Over! It's a tie!");
      }
    }
    shown = true;
  }

  /**
   * Gets the grid panel.
   *
   * @return the grid panel
   */
  public GridPanel getGridPanel() {
    return this.gridPanel;
  }

}

