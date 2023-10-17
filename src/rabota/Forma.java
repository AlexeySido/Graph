package rabota;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Forma {

    JFrame frame; //Объявляем форму

    private JTextField textField_m;//текстовое поле для ввода количество потомков для узлов
    private JTextField textField_N;//текстовое поле для ввода общего числа вершин графа
    private JTextField textField_R;//текстовое поле для ввода количества генерируемых графов
    private JCheckBox checkBox_A;//поле галочки выбора способа остановки формирования графа
    private JCheckBox checkBox_B;//поле галочки выбора способа остановки формирования графа
    private JTextArea textArea;//текстовое поле для вывода результата - индексов графов
    ArrayList<Double> alpha;//список структурных параметров
    JScrollPane scroller;
    JPanel panel_B;

    public static void main(String[] args){

        Forma frame = new Forma();
        frame.create();
    }

    public void create(){
        frame = new JFrame("Расчёт и постороение графа");//вводим форму

        JPanel panel_A = new JPanel();//задаём панель на которой разместим поля ввода и кнопку
        Listener_Button pushButton = new Listener_Button();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem clearMenuItem = new JMenuItem("Очистить форму");
        JMenuItem outputMenuItem = new JMenuItem("Записать результат в файл");
        clearMenuItem.addActionListener(new ClearMenuListener());
        outputMenuItem.addActionListener(new OutputMenuItem());
        fileMenu.add(clearMenuItem);
        fileMenu.add(outputMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        //надписи около полей ввода
        JLabel label_1 = new JLabel("Введите значения и нажмите кнопку \"Выполнить расчёт\"");
//        JLabel label_m = new JLabel("m = 3");
        JLabel label_N = new JLabel("N = ");
        JLabel label_R = new JLabel("R = ");
        JLabel label_rule_A = new JLabel("Правило остановки:");

        //поля ввода и флажковые окошки
        textField_m = new JTextField(7);
        textField_N = new JTextField(7);
        textField_R = new JTextField(7);
        //задаём окошко флажка для правила останова А
        checkBox_A = new JCheckBox("A", true);//флажок установлен по умолчанию
        checkBox_A.addActionListener(new Listener_CheckBox_A());//при выборе флажка A, флажок B сбрасывается

        //задаём окошко флажка для правила останова B
        checkBox_B = new JCheckBox("B", false);
        checkBox_B.addActionListener(new Listener_CheckBox_B());//при выборе флажка B, флажок A сбрасывается

        JButton button = new JButton("Выполнить расчёт"); //кнопка выполнения вычислений
        button.addActionListener(new Listener_Button());

        //размещение элементов на панели
        panel_A.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));//пустые поля вокруг панели
        panel_A.add(label_1);
//      panel_A.add(label_m);
//      panel_A.add(textField_m);
        panel_A.add(label_N);
        panel_A.add(textField_N);
        panel_A.add(label_R);
        panel_A.add(textField_R);
        panel_A.add(label_rule_A);
        panel_A.add(checkBox_A);
        panel_A.add(checkBox_B);
        panel_A.add(button);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(BorderLayout.NORTH, panel_A);
        frame.setSize(300,300);
        frame.setVisible(true);
        alpha = new ArrayList<Double>();
    }

    //внутренний класс Listener_Button для кнопки
    public class Listener_Button implements  ActionListener{

        int count; //счётчик висячих вершин
        Tree tree = new Tree();
        @Override
        public void actionPerformed(ActionEvent pushButton) {

            panel_B = new JPanel();//задаём панель
            panel_B.setLayout(new BorderLayout());

            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(new Font("Serif", Font.ITALIC, 16));
            textArea.setText("Вывод результата\n");

            scroller = new JScrollPane(textArea);
            scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

            panel_B.add(scroller);

            frame.getContentPane().add(BorderLayout.CENTER, panel_B);

            int m = 2;
            int n = Integer.parseInt(textField_N.getText());
            int r = Integer.parseInt(textField_R.getText());

//проверка правильности ввода исходных данных
            if(m<2||r<1||n<30){
                JOptionPane.showMessageDialog(frame,
                        "Вводимые данные должны удовлетворять следующим требованиям:\n" +
                                "Целое число\n Количество узлов N>=30\n Число генерируемых графов R>0\n" +
                                "Нажмите ОК и повторите ввод данных!",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int counter = 1;
//остановка формирование графа по правилу А
            if (checkBox_A.isSelected()) {
                while (counter <= r) {
                    for (int i = 0; i < r; i++) {
                        tree.creation_A(n, m);
                        //выводим на экран таблицу вершин графа
                        if (tree.nodesList.size() == 0) {
                            break;
                        }
                        tree.nodesList.removeAll(Collections.singleton(null));//удаление null ячеек списка если имеются
                        Node[] localNode = tree.nodesList.get(tree.nodesList.size() - 1);
                        Boolean check = false;
                        for (Node node : localNode) {
                            if (node.index == n) {
                                check = true;
                                break;
                            }
                        }
                        if (check == true) {
                            textArea.setVisible(true);
                            textArea.append("\nТаблица вершин случайного графа N_" + counter);
                            for (Node[] node_1 : tree.nodesList) {///////
                                String s = "";
                                String t;

                                for (Node node_2 : node_1) {/////////////
                                    if (node_2 == null) {
                                        t = "";
                                    } else if (node_2 != node_1[0]) {
                                        t = ", " + Integer.toString(node_2.index) + "-" + Integer.toString(node_2.parent);
                                    } else {
                                        t = Integer.toString(node_2.index) + "-" + Integer.toString(node_2.parent);
                                    }
                                    s = s + t;
                                }
                                textArea.append("\n" + s);
                                frame.setVisible(true);
                            }
                            textArea.append("\n\nТаблица висячих вершин случайного графа N_" + counter + "\n");
                            textArea.append(dangling_vertex(tree.nodesList));
                            textArea.append("\n\nЧисло уровней иерархии графа = " + tree.nodesList.size());
                            Node [] lastArray = tree.nodesList.get(tree.nodesList.size()-1);
                            lastArray = nullRemove(lastArray);
                            int q = lastArray[lastArray.length-1].index;
                            textArea.append("\nОбщее количество вершин = " + q) ;
                            textArea.append("\nЧисло путей (для дерева соответствует количеству висячих вершин) = " + count);
                            double a = Double.valueOf(n) / Double.valueOf(count);
                            alpha.add(a);
                            textArea.append("\nСтруктурный параметр " + '\u03B1' + " = " + String.format("%.2f", a) + "\n");
                            frame.setVisible(true);
                            counter = counter + 1;
                            if (counter > r) {
                                break;
                            }
                        }
                    }
                }
            }

 //Остановка формирования графа по методу B
            if (checkBox_B.isSelected()) {
                counter=1;
                while (counter <= r) {
                    for (int i = 0; i < r; i++) {
                        tree.creation_B(n, m);
                        //выводим на экран таблицу вершин графа
                        if (tree.nodesList.size() == 0) {
                            break;
                        }
                        tree.nodesList.removeAll(Collections.singleton(null));
                        Node[] localNode = tree.nodesList.get(tree.nodesList.size() - 1);
                        Boolean check = false;
                        for (Node node : localNode) {
                            if (node.index == n) {
                                check = true;
                                break;
                            }
                        }
                        if (check == true) {
                            textArea.append("\nТаблица вершин случайного графа N_" + counter);
                            for (Node[] node_1 : tree.nodesList) {///////
                                String s = "";
                                String t;
                                for (Node node_2 : node_1) {/////////////
                                    if (node_2 == null) {
                                        t = "";
                                    } else if (node_2 != node_1[0]) {
                                        t = ", " + Integer.toString(node_2.index) + "-" + Integer.toString(node_2.parent);
                                    } else {
                                        t = Integer.toString(node_2.index) + "-" + Integer.toString(node_2.parent);
                                    }
                                    s = s + t;
                                }
                                textArea.append("\n" + s);
                                frame.setVisible(true);
                            }
                            textArea.append("\n\nТаблица висячих вершин случайного графа N_" + counter + "\n");
                            textArea.append(dangling_vertex(tree.nodesList));
                            textArea.append("\n\nЧисло уровней иерархии графа = " + tree.nodesList.size());
                            Node [] lastArray = tree.nodesList.get(tree.nodesList.size()-1);
                            //lastArray=nullRemove(lastArray);
                            int q = lastArray[lastArray.length-1].index;
                            textArea.append("\nОбщее количество вершин = " + q) ;
                            textArea.append("\nЧисло путей (для дерева соответствует количеству висячих вершин) = " + count);
                            double a = Double.valueOf(n) / Double.valueOf(count);
                            alpha.add(a);
                            textArea.append("\nСтруктурный параметр " + '\u03B1' + " = " + String.format("%.2f", a) + "\n");
                            frame.setVisible(true);
                            counter = counter + 1;
                            if (counter > r) {
                                break;
                            }
                            textArea.setVisible(true);
                        }
                    }
                }
            }

                int number=1;
                textArea.append("\n\n Общий список структурных параметров " + '\u03B1' + ":\n");
                for (Double b : alpha) {
                    if (number == 1) {
                        textArea.append(number + ") " + String.format("%.2f", b));
                    } else {
                        textArea.append("; " + number + ") " + String.format("%.2f", b));
                    }
                    number++;
                }
                alpha.clear();
                //построение гистограммы
                Histogram histogram = new Histogram();
                histogram.histogramCreation(tree.nodesList, m, r);

        //построение графическое графа, только для случаев когда m-1 = 2 (что соответствует условию задания)
            if(m==2) {
                Graph graph = new Graph();
                graph.graphCreation(tree.nodesList, r);
            }
        }

        //метод определения висячих вершин
            public String dangling_vertex (ArrayList < Node[]>nodesList) {
                Tree vertex = new Tree();
                vertex.nodesList = tree.nodesList;
                String s = "";
                String t;
                count = 0;
                for (Node[] localArray : vertex.nodesList) {
                    for (Node localNode : localArray) {
                        if (localNode == null) {
                            t = "";
                        } else if (localNode.child == 0) {
                            if (count != 0) {
                                t = "; " + Integer.toString(localNode.index) + "-" + Integer.toString(localNode.parent);
                            } else {
                                t = Integer.toString(localNode.index) + "-" + Integer.toString(localNode.parent);
                            }
                            s = s + t;
                            count = count + 1;
                        }
                    }
                }
                if (s == "") {
                    return ("Весячие вершины отсутствуют, все вершины имеют потомков!");
                } else {
                    return s;
                }
            }
//метод удаления null ячеек для последнего массива списка
        public Node[] nullRemove(Node[] nodesArray) {
            ArrayList<Node> list = new ArrayList<Node>();
            for(Node s : nodesArray) {
                if(s != null) {
                    list.add(s);
                }
            }
            return list.toArray(new Node[list.size()]);
        }
        }
//проверка активации CheckBox ячеек
//при выборе ячейки А, ячейка В деактивируется
    public class Listener_CheckBox_A implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            checkBox_B.setSelected(false);
            checkBox_A.setSelected(true);

        }
    }
    //при выборе ячейки В, ячейка А деактивируется
    public class Listener_CheckBox_B implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            checkBox_A.setSelected(false);
            checkBox_B.setSelected(true);

        }
    }
//кнопка очистки поля до исходного состояния
    public class ClearMenuListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //очищаем форму до исходного состояния
            textField_m.setText(null);
            textField_N.setText(null);
            textField_R.setText(null);
            panel_B.remove(scroller);
            frame.repaint();
        }
    }
//вызов диалогового окна перед записью в файл
    public class OutputMenuItem implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            //запись результата в файл
            JFileChooser textWrite = new JFileChooser();
            textWrite.showSaveDialog(frame);
            createFile(textWrite.getSelectedFile());
        }
    }
//метод записи текста в файл
    private void createFile(File file){
          try (BufferedWriter fileOut = new BufferedWriter(new FileWriter(file))) {
          textArea.write(fileOut);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}


