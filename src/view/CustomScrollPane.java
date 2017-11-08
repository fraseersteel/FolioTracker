package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CustomScrollPane extends JScrollPane {

    public CustomScrollPane(String title){
        this.setLayout(new ScrollPaneLayout());
        this.setFocusable(true);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black), title);
        border.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(border);
        this.setVisible(true);
    }
}
