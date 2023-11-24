package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    MainFrame(){
        this.setTitle("My Notes");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(420, 420);
        this.setMinimumSize(new Dimension(400, 300));
        this.setVisible(true);
    }
}
