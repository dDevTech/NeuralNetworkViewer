package coloringai;

import javax.swing.*;

public class Frame extends JFrame {
    public Window p = new Window();
    public Frame(){
        setSize(1000,750);
        setTitle("Coloring AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(p);
    }
}
