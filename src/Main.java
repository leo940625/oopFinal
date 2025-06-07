import ui.HomeFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import util.FontUtil;

public class Main {
    public static void main(String[] args) {
        // 載入 Noto-SansTC 的兩個字體
        Font regular = FontUtil.getRegular(18f);
        Font bold = FontUtil.getBold(18f);

        // 迭代 UIManager 所有字體項目，根據原本 weight 替換字型
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keys = defaults.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = defaults.get(key);

            if (value instanceof Font) {
                Font oldFont = (Font) value;
                Font newFont;

                if (oldFont.isBold()) {
                    newFont = bold.deriveFont(oldFont.getSize2D());
                } else {
                    newFont = regular.deriveFont(oldFont.getSize2D());
                }

                UIManager.put(key, newFont);
            }
        }

        // 啟動主畫面
        SwingUtilities.invokeLater(() -> new HomeFrame());
    }
}
//北上必有左營 南下必有南港