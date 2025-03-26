import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import cs3500.model.Card;
import cs3500.model.Player;

/**
 * Holds behaviors in common between the Grid and Side panels to minimize code duplication.
 */
public abstract class AbstractPanel extends JPanel implements TTPanel {
  protected Graphics2D g2d;

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g2d = (Graphics2D) g.create();
  }

  public abstract int getCellHeight();

  public abstract int getCellWidth();

  protected void paintCard(Graphics2D g2d, Card card) {
    Color color = card.getPlayer() == Player.RED ? Color.RED : Color.CYAN;
    g2d.setColor(color);
    g2d.fillRect(0, 0, getCellWidth(), getCellHeight());
    highlightCard(g2d, card, color);
    g2d.setFont(new Font("Arial", Font.BOLD, getFontSize()));
    g2d.setColor(Color.BLACK);

    FontMetrics metrics = g2d.getFontMetrics();
    int centerX = getCellWidth() / 2;
    int centerY = getCellHeight() / 2;

    String north = card.getNorth().getStringValue();
    String south = card.getSouth().getStringValue();
    String east = card.getEast().getStringValue();
    String west = card.getWest().getStringValue();

    int northX = centerX - metrics.stringWidth(north) / 2;
    int northY = (int) (getCellHeight() * 0.275) + metrics.getAscent() / 2;
    g2d.drawString(north, northX, northY);

    int southX = centerX - metrics.stringWidth(south) / 2;
    int southY = getCellHeight() - (int) (getCellHeight() * 0.25) + metrics.getAscent() / 2;
    g2d.drawString(south, southX, southY);

    int eastX = (int) (getCellWidth() * 0.75) - metrics.stringWidth(east) / 2;
    int eastY = centerY + metrics.getAscent() / 2;
    g2d.drawString(east, eastX, eastY);

    int westX = (int) (getCellWidth() * 0.25) - metrics.stringWidth(west) / 2;
    int westY = centerY + metrics.getAscent() / 2;
    g2d.drawString(west, westX, westY);
  }


  private int getFontSize() {
    return Math.min((int) (getCellHeight() * 0.275), (int) (getCellWidth() * 0.325));
  }


  public abstract void highlightCard(Graphics2D g2d, Card card, Color originalColor);
}
