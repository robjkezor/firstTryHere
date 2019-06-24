package robjkezor.github.com;
import java.lang.Math;
import java.util.*;

/**
 Структура хранения графа
 */
public class graph {
    public int numberEdges;
    public int numberNodes;
    public edge edgesMass[];

    public graph(int numEdges, int numNodes) {
        numberEdges = numEdges;
        numberNodes = numNodes;
        generateGraph();
    }

    public graph() {
        numberEdges = 0;
        numberNodes = 0;
        edgesMass = new edge[numberEdges];
    }

    public void deleteEdge(double x1,double y1,double x2,double y2) {
        edge[] tmp = new edge[numberEdges - 1];
        int count = 0;
        for (int i = 0; i < edgesMass.length; i++)
        {
            if((edgesMass[i].NeCoord.x == x1 && edgesMass[i].NeCoord.y == y1)&&
                    (edgesMass[i].KeCoord.x == x2 && edgesMass[i].KeCoord.y == y2)||
                    (edgesMass[i].NeCoord.x == x2 && edgesMass[i].NeCoord.y == y2)&&
                            (edgesMass[i].KeCoord.x == x1 && edgesMass[i].KeCoord.y == y1)
            )
            {
                //System.out.print(" deleting "+edgesMass[i].Ne+" "+edgesMass[i].Ke);
            }
            else {
                //System.out.print(" tmp+ "+edgesMass[i].Ne+" "+edgesMass[i].Ke);
                tmp[count] = edgesMass[i];
                count++;
            }
        }
        //for(int i = 0;i< ;i++)
        //System.out.print(" _ "+ edgesMass.length+" _ ");
        edgesMass = tmp;
        //System.out.print(" _ "+ edgesMass.length+" _ ");
        numberEdges--;
        if(numberEdges == 0) numberNodes = 0;
    }
    public void addEdge(double x1,double y1,double x2,double y2)
    {
        edge[] tmp = new edge[numberEdges+1];
        for(int i=0;i<numberEdges;i++)
        {
            tmp[i] = edgesMass[i];
        }
        tmp[tmp.length-1] = new edge();
        tmp[tmp.length-1].NeCoord.x = x1;
        tmp[tmp.length-1].NeCoord.y = y1;
        tmp[tmp.length-1].KeCoord.x = x2;
        tmp[tmp.length-1].KeCoord.y = y2;
        for(int i = 0;i<numberEdges;i++)
        {
            if(edgesMass[i].NeCoord.x == tmp[tmp.length-1].NeCoord.x&&
                    edgesMass[i].NeCoord.y == tmp[tmp.length-1].NeCoord.y&&
                    tmp[tmp.length-1].Ne==-1)
            {
                tmp[tmp.length-1].Ne = edgesMass[i].Ne;
                //System.out.print("SHIT HAPPENS ONCE");
                continue;
            }

            if(edgesMass[i].KeCoord.x == tmp[tmp.length-1].KeCoord.x&&
                    edgesMass[i].KeCoord.y == tmp[tmp.length-1].KeCoord.y&&
                    tmp[tmp.length-1].Ke==-1)
            {
                tmp[tmp.length-1].Ke = edgesMass[i].Ke;
                //System.out.print("SHIT HAPPENS TWICE ");
                continue;
            }
        }
        for(int i = 0;i<numberEdges;i++)
        {
            if(edgesMass[i].NeCoord.x == tmp[tmp.length-1].KeCoord.x&&
                    edgesMass[i].NeCoord.y == tmp[tmp.length-1].KeCoord.y&&
                    tmp[tmp.length-1].Ke==-1)
            {
                tmp[tmp.length-1].Ke = edgesMass[i].Ne;
                //System.out.print("SHIT HAPPENS ONCE");
                continue;
            }

            if(edgesMass[i].KeCoord.x == tmp[tmp.length-1].NeCoord.x&&
                    edgesMass[i].KeCoord.y == tmp[tmp.length-1].NeCoord.y&&
                    tmp[tmp.length-1].Ne==-1)
            {
                tmp[tmp.length-1].Ne = edgesMass[i].Ke;
                //System.out.print("SHIT HAPPENS TWICE ");
                continue;
            }
        }
        numberEdges++;
        edgesMass = tmp;

        for(int i=0;i<edgesMass.length;i++)
        {
            int flag = 0;
            if(edgesMass[i].Ke == -1 )
            {
                edgesMass[i].Ke = numberNodes+1;
                /*if(numberNodes!=0)
                {*/
                numberNodes++;
                /*}
                else numberNodes=2;*/
                System.out.print(numberNodes+"  ");
                flag = 1 ;
            }
            if(edgesMass[i].Ne == -1)
            {
                edgesMass[i].Ne = numberNodes+1;
                if(flag == 0)
                {
                    /*if(numberNodes!=0)
                    {*/
                    numberNodes++;
                    /*}else
                        numberNodes = 2;*/
                    System.out.print(numberNodes+"  ");
                }
            }
        }
        int temp=0;
        for(int i = 0; i<edgesMass.length;i++)
        {
            if(edgesMass[i].Ke >  temp)
                temp = edgesMass[i].Ke;
            if(edgesMass[i].Ne > temp)
                temp = edgesMass[i].Ne;
        }
        if(numberNodes < temp)
            numberNodes++;
    }
    public void generateGraph()
    {
        //новый вариант генерации графа
        ArrayList<Integer> mainCircle = new ArrayList<Integer>();
        for (int i = 1; i <= numberNodes; i++) {
            mainCircle.add(i);
        }
        Random r = new Random();

        for (int i = 0; i != mainCircle.size(); i++) {
            int d = mainCircle.size();
            int q = mainCircle.get(i);
            int y = q;
            mainCircle.remove(i);
            int rand = r.nextInt(d);
            if (rand == 0) {
                mainCircle.add(rand, y);
            } else {
                int s = rand - 1;
                mainCircle.add(s, y);
            }
        }
        /*for(int i = 0; i < mainCircle.size();i++)
        {
            System.out.print(" "+mainCircle.get(i)+" ");
        }*/
        // making first n edges!!!
        edgesMass = new edge[numberEdges];
        for(int i = 0;i<edgesMass.length;i++)
        {
            edgesMass[i] = new edge();
        }
        for(int i = 0; i< numberNodes-1;i++)
        {
            edgesMass[i].Ne = mainCircle.get(i);
            edgesMass[i].Ke = mainCircle.get(i+1);
        }
        //edgesMass[numberNodes-1].Ne = mainCircle.get(mainCircle.size()-1);
        //edgesMass[numberNodes-1].Ke = mainCircle.get(0);
        //теперь нам надо задать все остальные ребра случайно
        int count1,count2;
        int randNumber1 = 0;
        int randNumber2 = 0;
        count1 = numberNodes;
        if(numberEdges >= numberNodes)
        {
            for(int i = 0; i<numberEdges - numberNodes+1;i++)
            {
                count2 = -1;
                while(count2 < 0)
                {
                    count2 = -1;
                    randNumber1 = rnd(0,count1-1);
                    randNumber2 = rnd(0,count1-1);
                    if(randNumber1 == randNumber2) continue;
                    for(int j = 0;j<numberNodes+i;j++)
                    {
                        if((edgesMass[j].Ne == mainCircle.get(randNumber1)&&
                                edgesMass[j].Ke == mainCircle.get(randNumber2)) ||
                                (edgesMass[j].Ne == mainCircle.get(randNumber2)&&
                                        edgesMass[j].Ke == mainCircle.get(randNumber1)))
                        {
                            count2 = count2 - numberEdges;
                            break;
                        }
                        else count2++;
                    }
                }
                edgesMass[numberNodes+i-1].Ne = mainCircle.get(randNumber1);
                edgesMass[numberNodes+i-1].Ke = mainCircle.get(randNumber2);
            }
        }
        edgesMass[numberNodes-1].Ne = mainCircle.get(mainCircle.size()-1);
        edgesMass[numberNodes-1].Ke = mainCircle.get(0);

        /*for(int i = 0; i < edgesMass.length;i++)
        {
            System.out.print(" "+edgesMass[i].Ne+" "+edgesMass[i].Ke+" ");
        }*/

    }
    public static int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
/*
    Структура хранения вершины в графе
 */
class vertexPoint
{
    public double x;
    public double y;
    vertexPoint()
    {
        x = -1;
        y = -1;
    }
    vertexPoint(int x1, int y1)
    {
        x = x1;
        y = y1;
    }
    public void setX(int num)
    {
        x = num;
    }
    public void setY(int num)
    {
        y = num;
    }
    public double getX(int num)
    {
        return x;
    }
    public double getY(int num)
    {
        return y;
    }

}
/*
    Структура хранения ребра в графе
 */
class edge
{
    public int Ne;
    public int Ke;
    public vertexPoint NeCoord;
    public vertexPoint KeCoord;
    edge()
    {
        Ne = -1;
        Ke = -1;
        NeCoord = new vertexPoint();
        KeCoord = new vertexPoint();
    }

    @Override
    public boolean equals(Object comparing) {
        if(this == comparing) {
            return true;
        }
        if(!(comparing instanceof edge)){
            return false;
        }
        edge c = (edge) comparing;

        return (Integer.compare(Ne,c.Ne) == 0 && Integer.compare(Ke,c.Ke) == 0);
        //return super.equals(obj);
    }

    public int getKe()
    {
        return Ke;
    }
    public  int getNe()
    {
        return Ne;
    }
}