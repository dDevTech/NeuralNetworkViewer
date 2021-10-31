package tools;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FontLoader {
    public static boolean alreadyLoaded=false;
    public static void loadFont(){
        try
        {
            ColorPalette.font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("font/NotoSans-Bold.ttf"));
            Log.log("Font loaded successfully");
            alreadyLoaded=true;
        } catch (FileNotFoundException ex)
        {
            Log.log("File not found");
        } catch (FontFormatException | IOException ex)
        {
            Log.log("Font error");
        }
    }
}
