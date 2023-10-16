package rabota;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//класс рисования гистограммы распределения количества потомков узлов
public class Histogram {

    //public ArrayList<Integer> histogram;//список для хранения количества случайно выпавших потомков узла
    public int[] child;
    int r;//номер графа для которого строится гистограмма (всегда последний граф)

   public int[] child(ArrayList<Node[]> nodes, int m){
       child = new int[m+1];
        for (Node[] nodeArray : nodes) {
           for (Node node : nodeArray) {
               if(node!=null) {
                   int number = node.child;
                   child[number] = child[number] + 1;
               }
           }
       }
       return child;
   }

    //метод формирования гистограммы
    public void histogramCreation (ArrayList<Node[]> nodes, int m, int r) {

        //создаём общий массив с разбивкой по количеству сгенерированных потомков
        DrawingLines lines = new DrawingLines();
        lines.histogram.child(nodes,m);
        JFrame frame_2 = new JFrame("Гистограмма сгенерированных потомков");//создаём новую форму
        frame_2.setSize(600,700);
        frame_2.getContentPane().add(lines);
        frame_2.setVisible(true);
        this.r=r;
    }

    public class DrawingLines extends JPanel {
        Histogram histogram = new Histogram();
        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;

            //строим сетку (шкалы) гистограммы
            for (int i = 2; i < 10; i++) {
                g.drawLine(50, 80 + 50 * i, 450, 80 + 50 * i);
                int vs = 90 - i * 10;
                int hs = 9 - i;
                g.drawString(vs + "", 30, 80 + 50 * i);
                g.drawString(hs + "", 520-50*i, 545);
                g.drawString("Количество потомков", 170, 565);
                g.drawString("Суммарное количество потомков каждого типа для графа № "+r, 100, 50);
            }
            g2d.drawLine(50, 80, 50, 530);
            int i=0;
            int j=70;
            for (int q: histogram.child) {
                int red = (int) (Math.random() * 255);
                int green = (int) (Math.random() * 255);
                int blue = (int) (Math.random() * 255);
                Color color = new Color(red,green,blue);
               g2d.setColor(color);
               g2d.fillRect(50+i, 530-q*5, 50, q*5);
               g.drawString(q+"", j, 530-q*5-10);
               i=i+50;
               j=j+50;
           }
        }
        }
    }

