import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.comment_screen.Message;

public class MessageTest {
  @Test public void testGetContent() {
    String comment = "test";
    Message msg = new Message(comment, new java.util.ArrayList(), "FFFFFF", 10);

    assertEquals(comment, msg.getContent());
  }
}
