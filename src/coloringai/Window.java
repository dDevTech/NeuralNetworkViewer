package coloringai;

import ai.Dataset.Dataset;
import ai.neuralnetwork.activation.Logistic;
import ai.neuralnetwork.initializer.RandomNormal;
import ai.neuralnetwork.optimizer.Momentum;
import ai.neuralnetwork.architecture.NeuralNetwork;
import ai.editor.View;
import ai.neuralnetwork.optimizer.SGD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Window extends JPanel {
    NeuralNetwork network;
    public ArrayList<Point>points= new ArrayList<Point>();
    public float[][]colors;
    public long prevTime;
    boolean firstTime=true;
    public Window(){
        View view = new View(800,600);
        view.setVisible(true);
        network = new NeuralNetwork(2,1, new int[]{15,15,15},new RandomNormal(),new Logistic());

        network.init();
        view.core.setNeuralNetwork(network);
        setBackground(Color.white);
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                synchronized (points) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        points.add(new Point(e.getX(), e.getY(), 1f));
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        points.add(new Point(e.getX(), e.getY(), 0f));
                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        points.add(new Point(e.getX(), e.getY(), 0.5f));
                    }
                }
            }
        });
        prevTime=System.currentTimeMillis();
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);


                if(e.getKeyCode()== KeyEvent.VK_A) {
                    randomSpawnPoints();
                }
                if(e.getKeyCode()== KeyEvent.VK_B) {
                    Thread t = new Thread(new Runnable() {
                        boolean done=false;
                        @Override
                        public void run() {


                            while(true){
                                done=false;
                                Dataset set = new Dataset();
                            synchronized (points) {


                                for (Point p : points) {
                                    set.getData().add(new float[]{p.getX() / (float) getWidth(), p.getY() / (float) getHeight()});
                                    set.getDesired().add(new float[]{p.getColor()});


                                }
                            }
                                Thread thread = network.train(set, 0.05f, 0.01f, 4000, 8, new SGD());
                                while (!done) {
                                    if (!thread.isAlive()) {
                                        if(firstTime){
                                            colors = new float[getWidth()][getHeight()];
                                            firstTime=false;
                                        }
                                        done = true;
                                        for (int i = 0; i < getWidth(); i++) {
                                            for (int j = 0; j < getHeight(); j++) {
                                                network.feedForward(new float[]{i / (float) getWidth(), j / (float) getHeight()});
                                                float[] output = network.getOutputData();
                                                colors[i][j] = output[0];
                                            }

                                        }
                                    }
                                }
                            }






                        }
                    });
                    t.start();
                }


            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    repaint();
                }
            }
        });
        t.start();
    }
    public void randomSpawnPoints(){
        int n=25;
        Random r= new Random();
        for(int i=0;i<n;i++){
            int x=(int)(getWidth()*r.nextFloat());
            int y=(int)(getHeight()*r.nextFloat());
            float c=0f;
            if(r.nextGaussian()>0f){
                c=1f;
            }

            points.add(new Point(x,y,c));
        }
    }
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2= (Graphics2D)g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);


        if(colors!=null){
                for (int i = 0; i < getWidth(); i++) {
                    for (int j = 0; j <  (int)(getHeight()); j++) {
                        g.setColor(new Color(0f,   colors[i][j]/2f+0.5f, 1-colors[i][j]));
                        g.fillRect(i, j, 1, 1);
                    }

                }
        }




        for(Point point:points){
            //g.setColor(new Color(0f,point.getColor()/4f+0.5f,1f-point.getColor()));
            //g.setColor(new Color(point.getColor(),point.getColor(),1f-point.getColor()));
            g.setColor(new Color(0f,point.getColor()/2f+0.5f,1f-point.getColor()));
            g.fillOval(point.getX()-8,point.getY()-8,16,16);
            g.setColor(Color.black);
            g.drawOval(point.getX()-8,point.getY()-8,16,16);
        }

    }
}
