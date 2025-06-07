package util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtil {
    private static final Map<String, Font> fontCache = new HashMap<>();

    public static Font loadFont(String path) {
        try {
            if (fontCache.containsKey(path)) {
                return fontCache.get(path);
            }

            InputStream is = FontUtil.class.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new RuntimeException("字體檔案找不到：" + path);
            }

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            fontCache.put(path, baseFont);
            return baseFont;
        } catch (FontFormatException | java.io.IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, 14);
        }
    }

    public static Font getRegular(float size) {
        return loadFont("fonts/NotoSansTC-Regular.ttf").deriveFont(size);
    }

    public static Font getBold(float size) {
        return loadFont("fonts/NotoSansTC-Bold.ttf").deriveFont(size);
    }
}
