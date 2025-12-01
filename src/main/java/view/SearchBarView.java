package view;

import javax.swing.*;
import java.awt.*;

public class SearchBarView extends JPanel {
    private final int width;
    private final int height;

    public SearchBarView(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void applyTheme(Color textColor, Color bgColor){
        setBackground(bgColor);
        setForeground(textColor);
    }
}
