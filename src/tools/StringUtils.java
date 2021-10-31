package tools;

import physics.Vector2;

import java.awt.*;

public class StringUtils {
    public static void drawCenteredString(Graphics2D g, String text, Vector2 position, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x =(int)( position.x - metrics.stringWidth(text) / 2);

        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y =(int) position.y - ( metrics.getHeight()/ 2) + metrics.getAscent();

        // Set the font
        g.setFont(font);
        // Draw the String

        g.drawString(text, x, y);
    }

}
