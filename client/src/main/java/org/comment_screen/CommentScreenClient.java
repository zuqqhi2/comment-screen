package org.comment_screen;

// Window Part
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.Point;
import java.awt.Color;

// Socket.io
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;

// JSON
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;


public class CommentScreenClient {
  public static String serverUrl;
  public static int commentStayTime;
  public static int numVisibleComment;
  public static int commentAreaHeight;

  private static final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

  public static int getFrameWidth() {
    GraphicsDevice device = env.getScreenDevices()[0];

    return device.getDefaultConfiguration().getBounds().width;
  }

  public static int getFrameHeight() {
    GraphicsDevice device = env.getScreenDevices()[0];

    return device.getDefaultConfiguration().getBounds().height;
  }

  public static void main(String args[]) {
    // Read properties
    Properties properties = new Properties();
    String jarPath = System.getProperty("java.class.path");
    String dirPath = jarPath.substring(0, jarPath.lastIndexOf(File.separator)+1);
    String confFilePath = dirPath + "application.properties";

    try {
      properties.load(new FileInputStream(confFilePath));
      serverUrl = properties.getProperty("server_url");
      commentStayTime = Integer.parseInt(properties.getProperty("comment_stay_time"));
      numVisibleComment = Integer.parseInt(properties.getProperty("num_visible_comment"));
      commentAreaHeight = Integer.parseInt(properties.getProperty("comment_area_height"));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    // Create frame
    JFrame frame = new JFrame("Comment Screen Client");

    //JFrame.setDefaultLookAndFeelDecorated(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setUndecorated(true);

    // Set transparency and other setting
    frame.setBackground(new Color(0, 0, 0, 0));
    frame.setSize(getFrameWidth(), getFrameHeight() - 10);
    Point newLocation = new Point(0, 0);
    frame.setLocation(newLocation);

    // Always Foreground
    frame.setAlwaysOnTop(true);

    // Add Panel
    MainPanel panel = new MainPanel();
    frame.getContentPane().add(panel);

    // Show the window
    frame.setVisible(true);
  }
}


class MainPanel extends JPanel implements Runnable {
  public static final int SLEEP_TIME = 10;

  private java.util.List<Drawable> drawnObjects;

  public MainPanel() {
    drawnObjects = new ArrayList<Drawable>();

    setBackground(new Color(0, 0, 0, 0));
    setOpaque(false);
    Thread refresh = new Thread(this);
    refresh.start();
  }

  public void paintComponent(Graphics g) {
    // Draw basic setting
    super.paintComponent(g);

    // Update message
    for (int i = 0; i < drawnObjects.size(); i++) {
      // Move
      drawnObjects.get(i).forward();

      // Dead check
      if (drawnObjects.get(i).isDead()) {
        drawnObjects.remove(i);
        i -= 1;
        continue;
      }

      // Draw
      drawnObjects.get(i).draw(g);


      // Will I control display number?
      //if (isInScreen()) x = x + vx;
      //int counter = 0;

      //for (int i = 0; i < msgs.size(); i++) {
      //  if (msgs.get(i).isInScreen()) counter += 1;
      //}
      //if (isInScreen() || counter <= NUM_VISIBLE_TEXT) x = x + vx;

      // Position Control ?
      /*
      this.x = CommentScreenClient.getFrameWidth() + rnd.nextInt(300);
      this.y = decideY();
      boolean isReposition = false;
      for (int k = 0; isReposition | k < NUM_REPOSITION; k++) {
        isReposition = false;
        for (int i = 0; i < msgs.size(); i++) {
          Rectangle rect = new Rectangle(msgs.get(i).getX(), msgs.get(i).getY(), msgs.get(i).getWidth(), msgs.get(i).getHeight());

          Point ul = new Point(this.x, this.y);
          Point ur = new Point(this.x + getWidth(), this.y);
          Point ll = new Point(this.x, this.y + getHeight());
          Point lr = new Point(this.x + getWidth(), this.y + getHeight());

          if (rect.contains(ul) || rect.contains(ur)) {
            this.x += rnd.nextInt(300);
            moveY((int)(rect.getY() + rect.getHeight() - this.y + rnd.nextInt(300) - 150));
            isReposition = true;
          } else if (rect.contains(ll) || rect.contains(lr)) {
            this.x += rnd.nextInt(300);
            moveY(-(int)(this.y + this.getHeight() - rect.getY() + 150 + rnd.nextInt(300) - 150));
            isReposition = true;
          }
        }
      }
      */

    }
  }


  private void handleSocketIO() throws URISyntaxException {
    MainPanel that = this;
    final Socket socket = IO.socket(CommentScreenClient.serverUrl);

    socket.on("message_from_server", new Emitter.Listener() {
      @Override
      public void call(Object... messages) {
        String jsonComment = messages[0].toString();

        Gson gson = new Gson();
        Comment cmt;
        try {
          // JSON parse
          cmt = gson.fromJson(jsonComment, Comment.class);

          // Add comment to the draw target list
          DrawableComment dcmt = new DrawableComment(cmt);
          drawnObjects.add(dcmt);
        } catch (JsonParseException e) {
          System.err.println(String.format("Input Json: %s", jsonComment));
          e.printStackTrace();
        }
      }
    });

    socket.connect();
  }

  public void run() {
    // Socket.io
    try {
      this.handleSocketIO();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    // Draw loop
    while (true) {
      repaint();
      try {
        Thread.sleep(SLEEP_TIME);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
