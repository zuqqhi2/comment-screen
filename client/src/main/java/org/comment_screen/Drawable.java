package org.comment_screen;

import java.awt.Graphics;

interface Drawable {
  public void forward();
  public boolean isDead();
  public void draw(Graphics g);
}
