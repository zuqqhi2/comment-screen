package org.comment_screen;

// Draw part
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.RenderingHints;

import java.util.List;
import java.util.Random;


public class DrawableComment extends Comment implements Drawable {
  private static final String FONT_NAME = "Helvetica";
  private static final int DEFAULT_FONT_SIZE = 28;
  private static final String DEFAULT_COLOR = "0x00FF00";

  private int x;
  private int y;
  private int vx;

  private Random rnd;

  // Constructor
  public DrawableComment(Comment cmt) {
    super(cmt);

    rnd = new Random();

    this.x = CommentScreenClient.getFrameWidth() + rnd.nextInt(300);
    this.y = decideY();

    // Calculate speed
    this.vx = -(CommentScreenClient.getFrameWidth() + getWidth())
     / CommentScreenClient.commentStayTime
     / (1000 / MainPanel.SLEEP_TIME);
  }

  // Getter
  public int getX() { return x; }
  public int getY() { return y; }
  public int getWidth() {
    Font font = new Font(FONT_NAME, Font.PLAIN, getSize());
    Canvas c = new Canvas();
    FontMetrics fm = c.getFontMetrics(font);
    return (int)fm.stringWidth(getText());
  }
  public int getHeight() {
    Font font = new Font(FONT_NAME, Font.PLAIN, getSize());
    Canvas c = new Canvas();
    FontMetrics fm = c.getFontMetrics(font);
    return (int)fm.getHeight();
  }

  private int decideY() {
    int startY = CommentScreenClient.getFrameHeight() - CommentScreenClient.commentAreaHeight;
    int endY = CommentScreenClient.getFrameHeight() - getHeight() - 10;

    return rnd.nextInt(endY - startY) + startY;
  }

  private void moveY(int value) {
    int startY = CommentScreenClient.getFrameHeight() - CommentScreenClient.commentAreaHeight;
    int endY = CommentScreenClient.getFrameHeight() - getHeight() - 10;

    this.y += value;
    if (this.y >= endY)      this.y = startY;
    else if (this.y <= startY) this.y = endY;
  }

  public boolean isInScreen() {
    if (x < CommentScreenClient.getFrameWidth() && !isDead()) return true;
    else return false;
  }

  // Animation
  public void forward() {
    x = x + vx;
  }

  public boolean isDead() {
    if (x + getWidth() <= 0) return true;
    else return false;
  }

  // Draw myself
  public void draw(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;

    // Set color
    g2.setColor(Color.decode(getColor()));
    // Set Font size
    g2.setFont(new Font(FONT_NAME, Font.PLAIN, getSize()));
    // Anti-Aliasing
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    // Draw String
    g2.drawString(getText(), x, y);
  }
}
