package ai.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class View extends JFrame {

    public Core core;
    public View(int width, int height){

        this.setIconImage(Toolkit.getDefaultToolkit().getImage("network.png"));
        this.setSize(width,height);
        this.setTitle("Neural Network Editor");
        core=new Core();
        this.setContentPane(core);
        this.setLocationRelativeTo(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                core.updateNetwork();
            }
        });
    }

}
