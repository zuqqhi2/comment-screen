package org.comment_screen;

public class Comment implements Cloneable {
  public static final int DEFAULT_FONT_SIZE = 28;
  public static final String DEFAULT_COLOR = "0x00FF00";

  private String text;
  private int size;
  private String color;
  private int dummy;

  public Comment(Comment cmt) {
    this.text = cmt.getText();
    this.size = cmt.getSize();
    this.color = cmt.getColor();
  }

  public Comment(String text, int size, String color) {
    this.text = text;
    this.size = size;
    this.color = color;

    // Set font size
//if (fontSize == 0) this.fontSize = DEFAULT_FONT_SIZE;
//else this.fontSize = fontSize;

// Set color
//if (colorCode.equals("")) this.colorCode = DEFAULT_COLOR;
//else this.colorCode = colorCode;


  }

  public String getText() { return this.text; }
  public int getSize() { return this.size; }
  public String getColor() { return this.color; }

  @Override
  public String toString() {
    return String.format("Comment{'text=%s','size=%d','color=%s'}",
      text, size, color);
  }
}
