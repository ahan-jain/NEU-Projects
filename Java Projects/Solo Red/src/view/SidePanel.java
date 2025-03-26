import cs3500.model.Card;
import cs3500.model.Player;
import cs3500.model.TTReadOnlyModel;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a side panel in the game UI that displays the cards held by a player.
 * Each player's side panel has a color-coded background (red or cyan) and displays each card
 * vertically.
 */
public class SidePanel extends AbstractPanel {
  private final List<Card> hand;
  protected int highlightedCardIndex;
  private final List<SelectedCardListener> listeners;

  /**
   * A constructor to initialize the side panel with a player's hand and
   * the dimensions of the panel.
   *
   * @param player          the player whose hand is being displayed
   * @param sidePanelWidth  the width of the side panel
   * @param sidePanelHeight the height of the side panel
   * @param model           the model to represent in this GUI
   */
  public SidePanel(Player player, int sidePanelWidth, int sidePanelHeight, TTReadOnlyModel model) {
    Color panelColor = player == Player.RED ? Color.RED : Color.CYAN;
    this.hand = model.getHand(player);
    listeners = new ArrayList<>();
    this.highlightedCardIndex = -1;

    setPreferredSize(new Dimension(sidePanelWidth, sidePanelHeight));
    setLayout(new GridLayout(hand.size(), 1));
    setBackground(panelColor);
  }

  protected interface SelectedCardListener {
    void onCardSelected(Card selectedCard);
    void onCardDeselected();
  }

  protected void addSelectedCardListener(SelectedCardListener listener) {
    listeners.add(listener);
  }

  private void notifyCardSelected(Card card) {
    for (SelectedCardListener listener : listeners) {
      listener.onCardSelected(card);
    }
  }

  private void notifyCardDeselected() {
    for (SelectedCardListener listener : listeners) {
      listener.onCardDeselected();
    }
  }

  /**
   * Handles mouse clicks on the panel.
   *
   * @param clickedIndex the index of the clicked card
   */
  public void handleMouseClick(int clickedIndex) {
    if (clickedIndex < hand.size()) {
      if (highlightedCardIndex == clickedIndex) {
        highlightedCardIndex = -1; // Deselect card
        notifyCardDeselected();
      } else {
        highlightedCardIndex = clickedIndex; // Select new card
        notifyCardSelected(hand.get(clickedIndex));
      }
      repaint();
    }
  }


  protected void updatePanel(List<Card> cards) {
    removeAll();
    setLayout(new GridLayout(cards.size(), 1));
    for (Card card : cards) {
      JPanel cell = new TTViewSidePanelCell(card);
      cell.paintComponents(g2d);
      cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      cell.setPreferredSize(new Dimension(getCellWidth(), getCellHeight()));
      add(cell);
    }
    revalidate();
    repaint();
  }

  @Override
  public int getCellHeight() {
    if (this.hand.isEmpty()) {
      return this.getHeight();
    }
    return this.getHeight() / this.hand.size();
  }

  @Override
  public int getCellWidth() {
    return this.getWidth();
  }

  /**
   * Handles mouse clicks on the panel.
   */

  @Override
  public void highlightCard(Graphics2D g2d, Card card, Color originalColor) {
    if (hand.indexOf(card) == highlightedCardIndex) {
      g2d.setColor(!g2d.getColor().equals(Color.GREEN) ? Color.GREEN : originalColor);
      g2d.fillRect(0, 0, getCellWidth(), getCellHeight());
    }
  }

  /**
   * A class to represent a side panel cell in the TTView.
   */
  protected class TTViewSidePanelCell extends JPanel {
    private final Card card;

    protected TTViewSidePanelCell(Card card) {
      this.card = card;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      setSize(getCellWidth(), getCellHeight());
      Graphics2D g2d = (Graphics2D) g;
      paintCard(g2d, card);
    }
  }

  public void clearHighlight() {
    this.highlightedCardIndex = -1;
    repaint();
  }

}
