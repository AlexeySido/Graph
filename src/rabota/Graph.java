package rabota;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//класс графического предстваления графа
public class Graph {

    //создаём список и  массив в котором будут помещаться узлы графов
    ArrayList<Node[]> graphList;
    Node[] graph;
    Node current;
    int r;

    public void setGraphList(ArrayList<Node[]> graphList){
        this.graphList=graphList;
    }

    public ArrayList<Node[]> getGraphList() {
        return graphList;
    }

    //метод построение графа
    public void graphCreation (ArrayList<Node[]> nodes, int r) {
        this.r=r;
        //задаём форму для построения графа
        JFrame frame_3 = new JFrame("Граф");//создаём новую форму
        frame_3.setSize(900, 900);
        frame_3.setVisible(true);

        //создаём список массивов, содержащий 20 первых узлов
        graphList = new ArrayList<Node[]>();

        int counter = 0;
        do {
            for (Node[] node : nodes) {
                counter = counter + node.length;
                graphList.add(node);
                if(counter>=20){break;}
                }
        }while (counter < 20);


//задаём координаты узлов
        int index;
        int x_offset=350;
        int y_offset=50;
        graphList.get(0)[0].x=650;
        graphList.get(0)[0].y=140;
        for (int i = 0; i < graphList.size(); i++) {
            index=0;
            for (int j = 0; j < graphList.get(i).length; j++) {
                current = graphList.get(i)[j];
                if (current.child == 1 && i!=graphList.size()-1) {
                    graphList.get(i + 1)[index].x = current.x;
                    graphList.get(i + 1)[index].y = current.y+y_offset;
                    index++;
                } else if (current.child > 1&&i!=graphList.size()-1) {
                    for (int k = 0; k < 2; k++) {
                        if (k == 0) {
                            graphList.get(i + 1)[index].x = current.x - x_offset;
                            graphList.get(i + 1)[index].y = current.y+y_offset;
                            index++;
                        } else {
                            graphList.get(i + 1)[index].x = current.x + x_offset;
                            graphList.get(i + 1)[index].y = current.y+y_offset;
                            index++;
                        }
                    }
                    }
                }
        //offset отвечат за расстояние между двумя соседними узлами, имеющими одного предка. Будет изменяться
        //только в том случае если один из узлов массива уровня имеет 2 потомка в противном случае offset не изменяется
                if (i!=graphList.size()-1&&index==graphList.get(i+1).length) {
                for (Node node:graphList.get(i)) {
                    if(node.child==2) {
                        x_offset = x_offset / 2;
                        break;
                    }
                }
            }

        }
        DrawingObjects objects = new DrawingObjects();
        frame_3.add(objects);
        objects.diagram.setGraphList(graphList);
    }

    //внутрениий класс для рисования объёктов
    public class DrawingObjects extends JPanel {
        Graph diagram = new Graph();

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            int y=140;

            //строим сетку уровней графа
            for (int i = 0; i < diagram.getGraphList().size(); i++) {
                Stroke dash = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5},0);
                ((Graphics2D) g).setStroke(dash);
                g.setColor(Color.black);
                g.drawLine(50, 150 + 50 * i, 1300, 150 + 50 * i);
                g.drawString(i + "", 1300, 150 + 50 * i);

            }

            //рисуем линии связи узлов
            int index=0;
            g2d.setStroke(new BasicStroke((float)1));
            for (int i = 0; i < graphList.size(); i++) {
                index = 0;
                for (int j = 0; j < graphList.get(i).length; j++) {
                    current = graphList.get(i)[j];
                    if(current.index==20){break;}
                    if (current.child == 1 && i!=graphList.size()-1&&diagram.getGraphList().get(i + 1)[index].index<=20) {
                      g2d.drawLine(current.x+10, current.y+10, diagram.getGraphList().get(i + 1)[index].x+10, diagram.getGraphList().get(i + 1)[index].y+10);
                      index++;
                    }else if (current.child > 1&&i!=graphList.size()-1) {
                        for (int k = 0; k < 2; k++) {
                            if (k == 0&&diagram.getGraphList().get(i + 1)[index].index<=20) {
                                g2d.drawLine(current.x+10, current.y+10, diagram.getGraphList().get(i + 1)[index].x+10, diagram.getGraphList().get(i + 1)[index].y+10);
                                index++;
                            } else if (k==1&&diagram.getGraphList().get(i + 1)[index].index<=20) {
                                g2d.drawLine(current.x+10, current.y+10, diagram.getGraphList().get(i + 1)[index].x+10, diagram.getGraphList().get(i + 1)[index].y+10);
                                index++;
                            }
                        }
                    }
                }
            }
           //рисуем узлы графа
            for (Node[]nodes: diagram.getGraphList()){
                for(int i=0; i<nodes.length; i++){
                    g2d.setColor(Color.blue);
                    if (nodes[i].child==0){
                        g2d.setColor(Color.orange);
                    }
                    g2d.fillOval(nodes[i].x, nodes[i].y, 20, 20 );
                    g2d.setColor(Color.black);
                    g.drawString(nodes[i].index+"-"+nodes[i].parent, nodes[i].x-20, nodes[i].y);
                    if(nodes[i].index==20){break;}
                }
                y=y+50;
            }
            g2d.setColor(Color.orange);
            g2d.fillOval(60,80,20,20);
            g2d.setColor(Color.black);
            g2d.drawString(" - висячие узлы.", 80, 95);
            g2d.drawString("Графическое представление первых 20  узлов графа № "+ r, 500, 50);
        }
    }
}
