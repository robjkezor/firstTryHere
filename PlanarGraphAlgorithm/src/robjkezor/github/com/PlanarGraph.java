package robjkezor.github.com;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import java.net.*;

/**
 * @version 0.2a
 * @author Mikhail Silin
 */

public class PlanarGraph {
    public static void main(String[] args) {
        EventQueue.invokeLater(() ->
        {
            JFrame frame = new DrawFrame();
            frame.setSize(1280,720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            //frame.setIconImage(new ImageIcon(getClass().getResource("/src/imagesPackage/vertex3.png")).getImage());
        });
    }
}
/*
    Основной фрейм приложения
 */
class DrawFrame extends JFrame
{
    public static final String DEFAULT_TITLE = "Planar Graph";
    public JRadioButtonMenuItem defaultMode;
    public JRadioButtonMenuItem addVertexMode;
    public JRadioButtonMenuItem addEdgeMode;
    public JRadioButtonMenuItem deleteEdgeMode;
    public JRadioButtonMenuItem deleteVertexMode;
    public JMenuBar menuBar;
    public JMenu fileMenu;
    public JMenu editMenu;
    public JMenu viewMenu;
    public JMenu changeModeMunu;
    public JMenu gammaMenu;
    public JMenuItem openItem;
    public JMenuItem openItem2;
    public JMenuItem openItem3;
    public JMenuItem openItem4;
    public JMenuItem editItem1;
    public JMenuItem viewItem1;
    public JMenuItem gammaItem1;
    public JMenuItem gammaItem2;
    public static boolean isEdgeDrawingMode;
    public static boolean isVertexDrawingMode;
    public static boolean isDefaultMode;
    public static boolean isEdgeDeletingMode;
    public static boolean isVertexDeletingMode;
    public DrawComponent MainComp;
    public static boolean getEdgeDrawingMode() {return isEdgeDrawingMode;}
    public static boolean getVertexDrawingMode() {return isVertexDrawingMode;}
    public static boolean getEdgeDeletingMode(){return isEdgeDeletingMode;}
    public static boolean getVertexDeletingMode() {return isVertexDeletingMode;}

    public DrawFrame()
    {
        setTitle(DEFAULT_TITLE);
        /*
            Обработчик событий
         */
        ActionListener listener = event ->{
            if(defaultMode.isSelected() ==true)
            {
                isDefaultMode = true;
                isEdgeDrawingMode = false;
                isVertexDrawingMode = false;
                isEdgeDeletingMode = false;
                isVertexDeletingMode = false;
                addEdgeMode.setSelected(false);
                addVertexMode.setSelected(false);
                deleteEdgeMode.setSelected(false);
                deleteVertexMode.setSelected(false);
            }

            if(addVertexMode.isSelected() ==true)
            {
                isDefaultMode = false;
                isEdgeDrawingMode = false;
                isVertexDrawingMode = true;
                isEdgeDeletingMode = false;
                isVertexDeletingMode = false;
                addEdgeMode.setSelected(false);
                defaultMode.setSelected(false);
                deleteEdgeMode.setSelected(false);
                deleteVertexMode.setSelected(false);
            }

            if(addEdgeMode.isSelected() ==true)
            {
                isDefaultMode = false;
                isEdgeDrawingMode = true;
                isVertexDrawingMode = false;
                isEdgeDeletingMode = false;
                isVertexDeletingMode = false;
                defaultMode.setSelected(false);
                addVertexMode.setSelected(false);
                deleteEdgeMode.setSelected(false);
                deleteVertexMode.setSelected(false);
            }

            if(deleteEdgeMode.isSelected() ==true)
            {
                isDefaultMode = false;
                isEdgeDrawingMode = false;
                isVertexDrawingMode = false;
                isEdgeDeletingMode = true;
                isVertexDeletingMode = false;
                addEdgeMode.setSelected(false);
                defaultMode.setSelected(false);
                addVertexMode.setSelected(false);
                deleteVertexMode.setSelected(false);
            }
            if(deleteVertexMode.isSelected() ==true)
            {
                isDefaultMode = false;
                isEdgeDrawingMode = false;
                isVertexDrawingMode = false;
                isEdgeDeletingMode = false;
                isVertexDeletingMode = true;
                addEdgeMode.setSelected(false);
                defaultMode.setSelected(false);
                addVertexMode.setSelected(false);
                deleteEdgeMode.setSelected(false);
            }
        };
        /*
            Обработчик событий от нажатий на кнопки меню
         */
        Action makeNewGraph = new AbstractAction("Сгенерировать произвольный граф с 8 вершинами") {
            public void actionPerformed(ActionEvent event)
            {
                MainComp.circleEdges.clear();
                MainComp.circles.clear();
                MainComp.lines.clear();
                MainComp.curveList.clear();
                MainComp.notPlanarEdges.clear();
                MainComp.mainGraph = new graph(12,8);
                MainComp.firstTry();
            }
        };
        Action makeNewGraph1 = new AbstractAction("Сгенерировать произвольный граф с 10 вершинами") {
            public void actionPerformed(ActionEvent event)
            {
                MainComp.circleEdges.clear();
                MainComp.circles.clear();
                MainComp.lines.clear();
                MainComp.curveList.clear();
                MainComp.notPlanarEdges.clear();
                MainComp.mainGraph = new graph(18,10);
                //MainComp.mainGraph = new graph(13,6);
                MainComp.firstTry();
            }
        };
        Action makeEmptyGraph = new AbstractAction("Очистить окно") {
            public void actionPerformed(ActionEvent event)
            {
                MainComp.circleEdges.clear();
                MainComp.circles.clear();
                MainComp.lines.clear();
                MainComp.curveList.clear();
                MainComp.notPlanarEdges.clear();
                MainComp.mainGraph = new graph();
                MainComp.firstTry();
            }
        };
        Action makeBigGraph = new AbstractAction("Задать параметры графа") {
            public void actionPerformed(ActionEvent event)
            {
                MainComp.circleEdges.clear();
                MainComp.circles.clear();
                MainComp.lines.clear();
                MainComp.curveList.clear();
                MainComp.notPlanarEdges.clear();
                String resultOfUserChoice = JOptionPane.showInputDialog(DrawFrame.this,"Введите" +
                        " количество вершин и ребер в графе");
                String[] results = resultOfUserChoice.split(" ");
                int [] resultsInInt = new int[2];
                resultsInInt[0] = Integer.parseInt(results[0]);
                resultsInInt[1] = Integer.parseInt(results[1]);
                System.out.println(resultsInInt[0] +" "+resultsInInt[1]);
                MainComp.mainGraph = new graph(resultsInInt[1],resultsInInt[0]);
                long startTime = System.currentTimeMillis();
                gammaAlgorithm Algorithm =
                        new gammaAlgorithm(MainComp.mainGraph, MainComp.mainGraph.numberNodes,
                                MainComp.mainGraph.numberEdges,true);
                long finishTime = System.currentTimeMillis();
                System.out.println();
                System.out.print("Время работы алгоритма( в миллисекундах): ");
                System.out.println(finishTime - startTime);
                //MainComp.firstTry();
            }
        };
        Action makeAllEdgesAsALine = new AbstractAction("Отобразить ребра в исходном виде") {
            public void actionPerformed(ActionEvent event)
            {
                for(int i = 0;i<MainComp.curveList.size();i++)
                {
                    MainComp.curveList.get(i).ctrlx = MainComp.curveList.get(i).x1;
                    MainComp.curveList.get(i).ctrly = MainComp.curveList.get(i).y1;
                    repaint();
                }
            }
        };
        Action gammaAlgorithm = new AbstractAction("Запустить гамма Алгоритм") {
            public void actionPerformed(ActionEvent event)
            {
                MainComp.circleEdges.clear();
                MainComp.notPlanarEdges.clear();
                long startTime = System.currentTimeMillis();
                gammaAlgorithm Algorithm =
                        new gammaAlgorithm(MainComp.mainGraph, MainComp.mainGraph.numberNodes,
                                MainComp.mainGraph.numberEdges,true);
                long finishTime = System.currentTimeMillis();
                edge[] drawingCircle = new edge[Algorithm.resultEdges.size()];
                //drawingCircle = Algorithm.makeSimpleCircle();
                for(int  i = 0;i< Algorithm.resultEdges.size();i++) {
                    drawingCircle[i] = Algorithm.resultEdges.get(i);
                }
                MainComp.circleEdges = Algorithm.resultEdges;
                if(Algorithm.notPlanarEdges.size()!= 0)
                    MainComp.notPlanarEdges = Algorithm.notPlanarEdges;
                repaint();

                System.out.println();
                System.out.print("Время работы алгоритма( в миллисекундах): ");
                System.out.println(finishTime - startTime);
                //MainComp.circleEdges = drawingCircle;
                /*for(int i = 0; i< MainComp.mainGraph.edgesMass.length; i++)
                {
                    /*for(int j = 0; j< drawingCircle.length; j++)
                    {
                        if((MainComp.mainGraph.edgesMass[i].Ke == drawingCircle[j].Ke&&
                                MainComp.mainGraph.edgesMass[i].Ne == drawingCircle[j].Ne)||
                                (MainComp.mainGraph.edgesMass[i].Ke == drawingCircle[j].Ne&&
                                        MainComp.mainGraph.edgesMass[i].Ne == drawingCircle[j].Ke))
                        {
                            MainComp.g2.setPaint(Color.GREEN);
                            MainComp.curveList.get(i).setCurve(MainComp.curveList.get(i));
                            repaint();
                        }
                    }
                }*/
                /*for(int i = 0;i<MainComp.curveList.size();i++)
                {
                    MainComp.curveList.get(i).ctrlx = MainComp.curveList.get(i).x1;
                    MainComp.curveList.get(i).ctrly = MainComp.curveList.get(i).y1;
                    repaint();
                }*/
            }
        };
        Action makeSimpleCircle = new AbstractAction("Отобразить простой цикл") {
            public void actionPerformed(ActionEvent event) {
                MainComp.circleEdges.clear();

                gammaAlgorithm Algorithm =
                        new gammaAlgorithm(MainComp.mainGraph, MainComp.mainGraph.numberNodes,
                                MainComp.mainGraph.numberEdges);
                edge[] drawingCircle = new edge[Algorithm.resultEdges.size()];
                for(int  i = 0;i< Algorithm.resultEdges.size();i++) {
                    drawingCircle[i] = Algorithm.resultEdges.get(i);
                }
                MainComp.circleEdges = Algorithm.resultEdges;
                repaint();
            }
        };
        Action exitProgramm = new AbstractAction("Выйти из программы") {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        };

        /* Adding menubar */
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Сгенерировать");
        changeModeMunu = new JMenu("Задать режим");
        editMenu = new JMenu("Выход");
        viewMenu = new JMenu("Вид");
        gammaMenu = new JMenu("Гамма Алгоритм");
        ButtonGroup modeGroup = new ButtonGroup();
        defaultMode = new JRadioButtonMenuItem("Режим просмотра",true);
        defaultMode.addActionListener(listener);
        addVertexMode = new JRadioButtonMenuItem("Режим добавления вершин",false);
        addVertexMode.addActionListener(listener);
        addEdgeMode = new JRadioButtonMenuItem("Режим добавления ребер",false);
        addEdgeMode.addActionListener(listener);
        deleteVertexMode = new JRadioButtonMenuItem("Режим удаления вершин", false);
        deleteVertexMode.addActionListener(listener);
        deleteEdgeMode = new JRadioButtonMenuItem("Режим удаления ребер", false);
        deleteEdgeMode.addActionListener(listener);

        modeGroup.add(defaultMode);
        modeGroup.add(addVertexMode);
        modeGroup.add(addEdgeMode);
        modeGroup.add(deleteEdgeMode);
        modeGroup.add(deleteVertexMode);
        openItem = new JMenuItem("Open graph");
        openItem.setAction(makeNewGraph1);
        openItem2 = new JMenuItem("Make new graph");
        openItem2.setAction(makeNewGraph);
        openItem3 = new JMenuItem("Make empty graph");
        openItem3.setAction(makeEmptyGraph);
        openItem4 = new JMenuItem("Make big graph");
        openItem4.setAction(makeBigGraph);
        editItem1 = new JMenuItem("Exit");
        editItem1.setAction(exitProgramm);
        viewItem1 = new JMenuItem("Make all edges straight");
        viewItem1.setAction(makeAllEdgesAsALine);
        gammaItem1 = new JMenuItem("Make simple circle");
        gammaItem1.setAction(makeSimpleCircle);
        gammaItem2 = new JMenuItem("Gamma Algorithm");
        gammaItem2.setAction(gammaAlgorithm);
        fileMenu.add(openItem);
        fileMenu.add(openItem2);
        fileMenu.add(openItem3);
        fileMenu.add(openItem4);
        editMenu.add(editItem1);
        viewMenu.add(viewItem1);
        changeModeMunu.add(defaultMode);
        changeModeMunu.add(addVertexMode);
        changeModeMunu.add(addEdgeMode);
        changeModeMunu.add(deleteVertexMode);
        changeModeMunu.add(deleteEdgeMode);
        gammaMenu.add(gammaItem1);
        gammaMenu.add(gammaItem2);
        menuBar.add(fileMenu);
        menuBar.add(changeModeMunu);
        menuBar.add(viewMenu);
        menuBar.add(gammaMenu);
        menuBar.add(editMenu);
        URL newURL  = getClass().getResource("/imagesPackage/vertex3.png");
        Image imageIcon = new ImageIcon(newURL).getImage();
        setIconImage(imageIcon);
        add(menuBar, BorderLayout.NORTH);
        MainComp = new DrawComponent();
        add(MainComp);
        pack();
    }
}
/*
    Компонента отрисовки
 */
class DrawComponent extends JComponent {
    private Ellipse2D current;
    private Line2D currentLine;
    public QuadCurve2D currentCurve;
    public ArrayList<Ellipse2D> circles;
    public ArrayList<Line2D> lines;
    public ArrayList<QuadCurve2D.Float> curveList;
    private ArrayList<Line2D> currentLines;
    public ArrayList<edge> circleEdges;
    public ArrayList<edge> notPlanarEdges;
    private boolean isMakingEdge;
    private boolean isDeletingEdge;
    //public int modeGraph = DrawFrame.getMakingNewGraph();
    Graphics2D g2;
    URL url = getClass().getResource("/imagesPackage/vertex2.png");
    public Image vertexImage = new ImageIcon(url).getImage();
    //public Image vertexImage = new ImageIcon("C:\\Users\\Mikhail\\IdeaProjects\\GraphPlanarTest\\src\\imagesPackage\\vertex2.png").getImage();
    public graph mainGraph;// = new graph(11,6);
    //maingraph = new graph(8,5);
    public int[] freePoints= {100,100,400,100,100,400,400,400,250,250,550,250};
    public ArrayList<Integer> freePointsForGraph = new ArrayList<>();
    //public int[] freePoints= {100,100,400,100,100,400,400,400,250,250,550,250};
    Point2D firstPoint;
    Point2D secondPoint;
    public DrawComponent() {
        circles = new ArrayList<>();
        lines = new ArrayList<>();
        curveList = new ArrayList<>();
        current = null;
        currentCurve = null;
        currentLine = null;
        currentLines = null;
        circleEdges = new ArrayList<>();
        notPlanarEdges = new ArrayList<>();
        isMakingEdge = false;
        isDeletingEdge = false;
        firstPoint = new Point2D.Double();
        secondPoint = new Point2D.Double();
        mainGraph = new graph();
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
    }
    /*
         Отображение сгенерированного графа
     */
    public void paintComponent(Graphics g)
    {
        //Graphics2D g2 = (Graphics2D) g;
        g2 = (Graphics2D) g;
        BasicStroke pen1 = new BasicStroke(1); //толщина линии 5
        g2.setStroke(pen1);
        Font f = new Font("TimesRoman", Font.BOLD,13);
        g2.setFont(f);
        for(Line2D k: lines)
        {
            g2.setPaint(Color.LIGHT_GRAY);
            g2.draw(k);

        }
        pen1 = new BasicStroke(4); //толщина линии 5
        g2.setStroke(pen1);
        for(QuadCurve2D K: curveList)
        {
            g2.setPaint(Color.BLACK);
            g2.draw(K);

        }
        if(curveList.size()!=0&&circleEdges!=null){
            for(int i = 0;i<circleEdges.size();i++) {
                for (int j = 0; j<curveList.size();j++)
                {
                    //System.out.print("second HUI");
                    if(mainGraph.edgesMass[j].Ne == circleEdges.get(i).Ne &&
                            mainGraph.edgesMass[j].Ke == circleEdges.get(i).Ke||
                            mainGraph.edgesMass[j].Ne == circleEdges.get(i).Ke &&
                                    mainGraph.edgesMass[j].Ke == circleEdges.get(i).Ne)
                    {
                        g2.setPaint(Color.GREEN);
                        g2.draw(curveList.get(j));
                    }
                }
            }
        }
        if(curveList.size()!=0&&notPlanarEdges!=null){
            for(int i = 0;i<notPlanarEdges.size();i++) {
                for (int j = 0; j<curveList.size();j++)
                {
                    //System.out.print("second HUI");
                    if(mainGraph.edgesMass[j].Ne == notPlanarEdges.get(i).Ne &&
                            mainGraph.edgesMass[j].Ke == notPlanarEdges.get(i).Ke||
                            mainGraph.edgesMass[j].Ne == notPlanarEdges.get(i).Ke &&
                                    mainGraph.edgesMass[j].Ke == notPlanarEdges.get(i).Ne)
                    {
                        g2.setPaint(Color.RED);
                        g2.draw(curveList.get(j));
                    }
                }
            }
        }
        for(Ellipse2D r: circles)
        {
            g2.draw(r);
            g2.setPaint(Color.BLACK);
            g2.fill(r);
            //g2.drawImage(vertexImage,(int)r.getX(),(int)r.getY(),null);
            //g2.drawImage(getClass().getResource("/src/imagePackage/vertex3.png"),(int)r.getX()-4,(int)r.getY()-4,null);
            g2.drawImage(vertexImage,(int)r.getX()-4,(int)r.getY()-4,null);
            String tmp;
            //getClass().getResource("/src/imagesPackage/vertex3.png")
            //g2.drawString("2",(int)r.getCenterX()-6,(int)r.getCenterY()+5);
            for(int i =0; i< mainGraph.edgesMass.length;i++)
            {
                if((r.getCenterY()== mainGraph.edgesMass[i].NeCoord.y)
                        &&(r.getCenterX() == mainGraph.edgesMass[i].NeCoord.x))
                {
                    tmp = String.valueOf(mainGraph.edgesMass[i].Ne);
                    g2.drawString(tmp,(int)r.getCenterX()-6,(int)r.getCenterY()+5);
                }
                if((r.getCenterY()== mainGraph.edgesMass[i].KeCoord.y)
                        &&(r.getCenterX() == mainGraph.edgesMass[i].KeCoord.x))
                {
                    tmp = String.valueOf(mainGraph.edgesMass[i].Ke);
                    g2.drawString(tmp,(int)r.getCenterX()-6,(int)r.getCenterY()+5);
                }
            }
        }
        //g2.setPaint(Color.GREEN);
        //if(curveList.size() != 0) g2.draw(curveList.get(0));
        //g2.draw(new QuadCurve2D.Float(100, 100, 100,200, 200,200));
        // g2.drawImage(vertexImage,50,50,null);
    }
    /*
           Отображение сгенерированного графа
     */
    public void firstTry()
    {
        int count = 0;
        for(int i = 0; i< freePoints.length;i++)
        {
            freePointsForGraph.add(freePoints[i]);
        }
        for(int  i = 100;i<1200;i= i+50)
        {
            for(int j = 450;j<650;j = j+50)
            {
                freePointsForGraph.add(i);
                freePointsForGraph.add(j);
            }
        }
        for(int i = 0;i<mainGraph.edgesMass.length;i++)
        {
            if(mainGraph.edgesMass[i].NeCoord.x == -1)
            {
                mainGraph.edgesMass[i].NeCoord.x = freePointsForGraph.get(count);
                mainGraph.edgesMass[i].NeCoord.y = freePointsForGraph.get(count+1);
                //mainGraph.edgesMass[i].NeCoord.x = freePoints[count];
                //mainGraph.edgesMass[i].NeCoord.y = freePoints[count+1];
                for(int j=0;j<mainGraph.edgesMass.length;j++)
                {
                    if(mainGraph.edgesMass[j].Ne == mainGraph.edgesMass[i].Ne)
                    {
                        mainGraph.edgesMass[j].NeCoord.x = mainGraph.edgesMass[i].NeCoord.x;
                        mainGraph.edgesMass[j].NeCoord.y = mainGraph.edgesMass[i].NeCoord.y;
                    }
                    if(mainGraph.edgesMass[j].Ke == mainGraph.edgesMass[i].Ne)
                    {
                        mainGraph.edgesMass[j].KeCoord.x = mainGraph.edgesMass[i].NeCoord.x;
                        mainGraph.edgesMass[j].KeCoord.y = mainGraph.edgesMass[i].NeCoord.y;
                    }
                }
                count = count+2;
                current = new Ellipse2D.Double();
                current.setFrameFromCenter(mainGraph.edgesMass[i].NeCoord.x,
                        mainGraph.edgesMass[i].NeCoord.y,
                        mainGraph.edgesMass[i].NeCoord.x+10,
                        mainGraph.edgesMass[i].NeCoord.y+10);
                circles.add(current);
            }
            if(mainGraph.edgesMass[i].KeCoord.x == -1)
            {
                mainGraph.edgesMass[i].KeCoord.x = freePointsForGraph.get(count);
                mainGraph.edgesMass[i].KeCoord.y = freePointsForGraph.get(count+1);
                //mainGraph.edgesMass[i].KeCoord.x = freePoints[count];
                //mainGraph.edgesMass[i].KeCoord.y = freePoints[count+1];
                for(int j=0;j<mainGraph.edgesMass.length;j++)
                {
                    if(mainGraph.edgesMass[j].Ne == mainGraph.edgesMass[i].Ke)
                    {
                        mainGraph.edgesMass[j].NeCoord.x = mainGraph.edgesMass[i].KeCoord.x;
                        mainGraph.edgesMass[j].NeCoord.y = mainGraph.edgesMass[i].KeCoord.y;
                    }
                    if(mainGraph.edgesMass[j].Ke == mainGraph.edgesMass[i].Ke)
                    {
                        mainGraph.edgesMass[j].KeCoord.x = mainGraph.edgesMass[i].KeCoord.x;
                        mainGraph.edgesMass[j].KeCoord.y = mainGraph.edgesMass[i].KeCoord.y;
                    }
                }
                count = count+2;
                current = new Ellipse2D.Double();
                current.setFrameFromCenter(mainGraph.edgesMass[i].KeCoord.x,
                        mainGraph.edgesMass[i].KeCoord.y,
                        mainGraph.edgesMass[i].KeCoord.x+10,
                        mainGraph.edgesMass[i].KeCoord.y+10);
                circles.add(current);
            }
        }
        for(int i = 0; i<mainGraph.edgesMass.length;i++)
        {
            currentLine = new Line2D.Double(mainGraph.edgesMass[i].NeCoord.x,
                    mainGraph.edgesMass[i].NeCoord.y,
                    mainGraph.edgesMass[i].KeCoord.x,
                    mainGraph.edgesMass[i].KeCoord.y);
            lines.add(currentLine);
            curveList.add(new QuadCurve2D.Float((float)mainGraph.edgesMass[i].NeCoord.x,
                    (float)mainGraph.edgesMass[i].NeCoord.y, (float)mainGraph.edgesMass[i].NeCoord.x,
                    (float)mainGraph.edgesMass[i].NeCoord.y, (float)mainGraph.edgesMass[i].KeCoord.x,
                    (float)mainGraph.edgesMass[i].KeCoord.y));
        }
        repaint();
    }
    /*
        Функция нахождения вершины
     */
    public Ellipse2D find(Point2D p)
    {
        for(Ellipse2D r: circles)
        {
            if(r.contains(p)) return r;
        }
        return null;
    }

    public QuadCurve2D.Float findLine(Point2D p)
    {
        for(int i = 0;i<lines.size();i++)
        {
            Line2D r = lines.get(i);
            if(r.ptLineDist(p)<10)
            {
                if(p.distance(r.getP1())<r.getP1().distance(r.getP2())&&
                        p.distance(r.getP2())<r.getP1().distance(r.getP2())) {
                    //System.out.print(" _  " + i + " _ ");
                    return curveList.get(i);
                }
            }
        }
        return null;
    }
    public ArrayList<Line2D> findLineList(Point2D p)
    {
        ArrayList<Line2D> linesList = new ArrayList<>();
        for(Line2D r: lines)
        {
            if(r.ptLineDist(p)<5)
            {
                linesList.add(r);
            }
        }
        return linesList;
    }
    /*
        Добавление новой вершины
     */
    public void add(Point2D p)
    {
        double x = p.getX();
        double y = p.getY();

        current = new Ellipse2D.Double();
        current.setFrameFromCenter(x,y,x+10,y+10);
        circles.add(current);
        repaint();
    }
    /*
        Функции обработки работы с мышью
     */
    private class MouseHandler extends MouseAdapter
    {
        /*
            Создание новой вершины по клику мыши
         */
        public void mousePressed(MouseEvent event)
        {
            current = find(event.getPoint());
            currentCurve = findLine(event.getPoint());
            //if(currentLine!=null)
            //  System.out.print(" hui "+currentLine.ptLineDist(event.getPoint())+" hui ");

            if(current == null)
            {
                if (DrawFrame.getVertexDrawingMode() == true)
                {
                    add(event.getPoint());

                }
                else
                {

                }
            }
            else
            {
                if(DrawFrame.getVertexDeletingMode() == true)
                {
                    int flag = 0;
                    for(int  i = 0; i < mainGraph.edgesMass.length;i++)
                    {
                        if(mainGraph.edgesMass[i].NeCoord.x != current.getCenterX() &&
                                mainGraph.edgesMass[i].NeCoord.y != current.getCenterY() &&
                                mainGraph.edgesMass[i].KeCoord.x != current.getCenterX() &&
                                mainGraph.edgesMass[i].KeCoord.y != current.getCenterY())
                        { }
                        else flag = 1;
                    }
                    if(flag == 0)
                    {
                        circles.remove(current);
                        repaint();
                    }
                }
                if (DrawFrame.getEdgeDrawingMode() == true)
                {
                    if(isMakingEdge == true)
                    {
                        secondPoint = new Point2D.Double(current.getCenterX(),current.getCenterY());
                        currentLine = new Line2D.Double(firstPoint,secondPoint);
                        mainGraph.addEdge(firstPoint.getX(),firstPoint.getY(),secondPoint.getX(),secondPoint.getY());
                        lines.add(currentLine);
                        curveList.add(new QuadCurve2D.Float((float)firstPoint.getX(),(float)firstPoint.getY(),
                                (float)firstPoint.getX(), (float)firstPoint.getY(), (float)secondPoint.getX(),
                                (float)secondPoint.getY()));
                        firstPoint = new Point2D.Double();
                        secondPoint = new Point2D.Double();
                        isMakingEdge = false;
                        repaint();
                    }
                    else
                    {
                        firstPoint = new Point2D.Double(current.getCenterX(),current.getCenterY());
                        isMakingEdge = true;
                    }
                }
                if(DrawFrame.getEdgeDeletingMode() == true)
                {
                    if(isDeletingEdge == true)
                    {
                        secondPoint = new Point2D.Double(current.getCenterX(),current.getCenterY());
                        int num = -1;
                        for(int i = 0;i<lines.size();i++)
                        {
                            //Line2D r = lines.get(i);
                            if((lines.get(i).getP1().equals(firstPoint)==true && lines.get(i).getP2().equals(secondPoint)==true)||
                                    (lines.get(i).getP2().equals(firstPoint)==true && lines.get(i).getP1().equals(secondPoint)==true))
                            {
                                //System.out.print(" i WAS HERE !!! ");
                                num = i;
                            }
                        }
                        if(num != -1) lines.remove(num);
                        if(num != -1) curveList.remove(num);
                        mainGraph.deleteEdge(firstPoint.getX(),firstPoint.getY(),secondPoint.getX(),secondPoint.getY());


                        firstPoint = new Point2D.Double();
                        secondPoint = new Point2D.Double();
                        isDeletingEdge = false;
                        repaint();
                    }
                    else
                    {
                        firstPoint = new Point2D.Double(current.getCenterX(),current.getCenterY());
                        isDeletingEdge = true;
                    }
                }
            }
        }
        public void mouseClicked(MouseEvent event)
        {
            current = find(event.getPoint());
        }
    }

    private class MouseMotionHandler implements MouseMotionListener
    {
        /*
            Движение мышью
         */
        public void mouseMoved(MouseEvent event)
        {
            /*if(findLine(event.getPoint())== null) {
                setCursor(Cursor.getDefaultCursor());
            }
            else
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }*/
            if(find(event.getPoint())== null) {
                setCursor(Cursor.getDefaultCursor());
            }
            else {
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
        }
        /*
            Движение мышью с зажатой левой кнопкой
         */
        public void mouseDragged(MouseEvent event)
        {
            if((current != null)&&(firstPoint.getX()==0.00))
            {
                //System.out.print(firstPoint.getX());
                int newXcoord = event.getPoint().x;
                int newYcoord = event.getPoint().y;
                Point2D newPoint = new Point2D.Double();
                newPoint.setLocation((int)current.getCenterX(),
                        (int)current.getCenterY());
                for(int i= 0;i<mainGraph.edgesMass.length;i++)
                {
                    if(current.getCenterX() == mainGraph.edgesMass[i].NeCoord.x
                            &&current.getCenterY() == mainGraph.edgesMass[i].NeCoord.y)
                    {
                        mainGraph.edgesMass[i].NeCoord.x = newXcoord;
                        mainGraph.edgesMass[i].NeCoord.y = newYcoord;
                    }
                    if(current.getCenterX() == mainGraph.edgesMass[i].KeCoord.x
                            &&current.getCenterY() == mainGraph.edgesMass[i].KeCoord.y)
                    {
                        mainGraph.edgesMass[i].KeCoord.x = newXcoord;
                        mainGraph.edgesMass[i].KeCoord.y = newYcoord;
                    }
                }
                Point2D[] boolMass = new Point2D[mainGraph.edgesMass.length];
                for(int i = 0; i < curveList.size();i++)
                {

                    if(curveList.get(i).x1==curveList.get(i).ctrlx&&
                            curveList.get(i).y1==curveList.get(i).ctrly)
                        boolMass[i] = new Point2D.Double(0,0);
                    else
                        boolMass[i] = new Point2D.Double(curveList.get(i).ctrlx,curveList.get(i).ctrly);
                }
                lines.clear();
                curveList.clear();
                for(int i = 0; i<mainGraph.edgesMass.length;i++)
                {
                    currentLine = new Line2D.Double(mainGraph.edgesMass[i].NeCoord.x,
                            mainGraph.edgesMass[i].NeCoord.y,
                            mainGraph.edgesMass[i].KeCoord.x,
                            mainGraph.edgesMass[i].KeCoord.y);
                    lines.add(currentLine);
                    if(boolMass[i].getX()!=0&&boolMass[i].getY()!=0)
                    {
                        curveList.add(new QuadCurve2D.Float((float) mainGraph.edgesMass[i].NeCoord.x,
                                (float) mainGraph.edgesMass[i].NeCoord.y, (float) boolMass[i].getX(),
                                (float) boolMass[i].getY(), (float) mainGraph.edgesMass[i].KeCoord.x,
                                (float) mainGraph.edgesMass[i].KeCoord.y));
                    }else {
                        curveList.add(new QuadCurve2D.Float((float) mainGraph.edgesMass[i].NeCoord.x,
                                (float) mainGraph.edgesMass[i].NeCoord.y, (float) mainGraph.edgesMass[i].NeCoord.x,
                                (float) mainGraph.edgesMass[i].NeCoord.y, (float) mainGraph.edgesMass[i].KeCoord.x,
                                (float) mainGraph.edgesMass[i].KeCoord.y));
                    }
                }
                for(int i = 0; i< boolMass.length;i++)
                {
                    boolMass[i] = null;
                }
                current.setFrameFromCenter(newXcoord, newYcoord,
                        newXcoord + 10, newYcoord + 10);
                repaint();
            }
            if(current==null&&currentCurve!=null)
            {
                currentCurve.setCurve(currentCurve.getP1(),event.getPoint(),currentCurve.getP2());
                //curveList.add(currentCurve.Float);
                repaint();
            }
        }
    }
}