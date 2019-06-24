package robjkezor.github.com;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *      Реализация гамма алгоритма
 */

public class gammaAlgorithm {

    public ArrayList<edge> resultEdges;
    public ArrayList<edge> notPlanarEdges;
    public graph gammaGraph;
    public int numVertex;
    public int numEdges;
    public edge[] resEdges;
    public int circleCount;
    public ArrayList<verge> verges;
    public ArrayList<segment> segments;
    public boolean isourGraphPlanar;
    gammaAlgorithm(graph graph, int numV, int numE)
    {
        resultEdges = new ArrayList<>();
        notPlanarEdges = new ArrayList<>();
        //circleCount = 0;
        gammaGraph = graph;
        numVertex = numV;
        numEdges = numE;
        resEdges = makeSimpleCircle();
        for(int i = 0; i< resEdges.length;i++)
        {
            resultEdges.add(resEdges[i]);
        }
    }

    gammaAlgorithm(graph graph, int numV, int numE,boolean isPlanar)
    {
        int globalEliteCount = 0;
        int currPoint;
        resultEdges = new ArrayList<>();
        notPlanarEdges = new ArrayList<>();
        //circleCount = 0;
        gammaGraph = graph;
        numVertex = numV;
        numEdges = numE;
        /**
         *  Создаем цикл
         */
        resEdges = makeSimpleCircle();
        for(int i = 0; i< resEdges.length;i++)
        {
            resultEdges.add(resEdges[i]);
        }
        /**
         *  Создаем Грани
         */
        ArrayList<Integer> vertexForVerge= new ArrayList<>();
        vertexForVerge.add(resultEdges.get(0).Ne);
        vertexForVerge.add(resultEdges.get(0).Ke);
        ArrayList<edge> tempVector = new ArrayList<>();
        for(int i = 1;i<resultEdges.size();i++)
        {
            tempVector.add(resultEdges.get(i));
        }
        currPoint = resultEdges.get(0).Ke;
        //int oldPoint = resultEdges.get(0).Ke;
        for(int i = 2; i<resultEdges.size();i++)
        {
            for(int j = 0; j< tempVector.size();j++) {
                if ((currPoint == tempVector.get(j).Ne) ||
                        (currPoint == tempVector.get(j).Ke)) {
                    if (currPoint == tempVector.get(j).Ne && tempVector.get(j).Ke!=resultEdges.get(0).Ne)
                    {
                        vertexForVerge.add(tempVector.get(j).Ke);
                        currPoint = tempVector.get(j).Ke;
                        tempVector.remove(tempVector.get(j));
                        //System.out.print(" hey ");
                        continue;
                    }
                    if (currPoint == tempVector.get(j).Ke && tempVector.get(j).Ne!=resultEdges.get(0).Ne)
                    {
                        vertexForVerge.add(tempVector.get(j).Ne);
                        currPoint = tempVector.get(j).Ne;
                        tempVector.remove(tempVector.get(j));
                        //System.out.print(" hey ");
                        continue;
                    }

                }
            }
            //System.out.print(temp.Ne+" "+temp.Ke+" || ");
        }

        verges = new ArrayList<>();
        verges.add(new verge(circleCount+2,vertexForVerge));
        verges.add(new verge(circleCount+2,vertexForVerge));
        for (int i = 0;i<verges.size();i++)
        {
            System.out.print(" verge " + i + " : ");
            for (int j = 0;j<verges.get(i).contactVertexList.size();j++)
            {
                System.out.print(" "+verges.get(i).contactVertexList.get(j)+" ");
            }
            System.out.println();
        }

        /**
         *  Making segments
         */
        circle []example = new circle[numVertex];
        for(int i = 0;i< numVertex;i++)
        {
            example[i] = new circle();
        }

        for(int i = 0;i< numVertex;i++)
        {
            for (int j = 0; j < vertexForVerge.size(); j++) {
                if (i + 1 == vertexForVerge.get(j)) {
                    example[i].num = 0;
                }
            }
        }
        System.out.println();
        ArrayList<edge> edgesWithoutCircle = new ArrayList<>();
        ArrayList<edge> edgesGr = new ArrayList<>();
        for(int  i = 0; i< numEdges;i++)
        {
            edgesGr.add(graph.edgesMass[i]);
        }
        int flagIf = 0;
        for(int i = 0; i< edgesGr.size();i++)
        {
            for(int j = 0; j< resultEdges.size();j++)
            {
                if(edgesGr.get(i).Ne == resultEdges.get(j).Ne &&
                        edgesGr.get(i).Ke == resultEdges.get(j).Ke)
                {
                    flagIf = 0;
                    break;
                }
                else flagIf = 1;
            }
            if(flagIf == 1) edgesWithoutCircle.add(edgesGr.get(i));
        }
        int segmentCount = 1;
        for(int i = 0;i<vertexForVerge.size();i++)
        {
            int flagzero = 0;
            if(example[vertexForVerge.get(i)-1].num == 0)
            {
                for(int j = 0; j< edgesWithoutCircle.size();j++)
                {
                    if(edgesWithoutCircle.get(j).Ne == vertexForVerge.get(i))
                    {
                        example[vertexForVerge.get(i)-1].num = segmentCount;
                        example[edgesWithoutCircle.get(j).Ke-1].num = segmentCount;
                        flagzero = 1;
                    }
                    if(edgesWithoutCircle.get(j).Ke == vertexForVerge.get(i))
                    {
                        example[vertexForVerge.get(i)-1].num = segmentCount;
                        example[edgesWithoutCircle.get(j).Ne-1].num = segmentCount;
                        flagzero = 1;
                    }
                }
                int flag = 0;
                while(flag !=-1)
                {
                    flag = -1;
                    for(int k = 0; k<numVertex;k++)
                    {
                        if(example[k].num == segmentCount)
                        {
                            if(example[k].myFather != 1)
                            {
                                for(int w = 0; w < edgesWithoutCircle.size();w++)
                                {
                                    if(edgesWithoutCircle.get(w).Ne == k+1)
                                    {
                                        example[edgesWithoutCircle.get(w).Ke-1].num = segmentCount;
                                        flag = 0;
                                    }
                                    if(edgesWithoutCircle.get(w).Ke == k+1)
                                    {
                                        example[edgesWithoutCircle.get(w).Ne-1].num = segmentCount;
                                        flag = 0;
                                    }
                                }
                                example[k].myFather = 1;
                            }
                        }
                    }
                }
                if(flagzero != 0) segmentCount++;
            }
            //segmentCount++;
        }
        int maxFlag;
        ArrayList<Integer> maxValue = new ArrayList<>();
        for(int  i = 0; i<numVertex;i++)
        {
            maxFlag = 0;
            for(int j = 0; j<maxValue.size();j++)
            {
                if(example[i].num == maxValue.get(j))
                    maxFlag =1;
            }
            if(example[i].num != 0&& example[i].num!= -1&& maxFlag == 0)
                maxValue.add(example[i].num);
        }
        int max_Value = maxValue.size();
        segments = new ArrayList<>();
        for(int j = 0; j < max_Value;j++)
        {
            ArrayList<Integer> contactVertexCurrentSegment = new ArrayList<>();
            ArrayList<Integer> vertexCurrentSegment = new ArrayList<>();
            ArrayList<edge> edgesCurrentSegment = new ArrayList<>();
            ArrayList<verge> acceptedVerge = new ArrayList<>();
            for(int k = 0;k<numVertex;k++)
            {
                if(example[k].num == maxValue.get(j))//if(example[k].num == j+1)
                {
                    vertexCurrentSegment.add(k+1);
                    for(int l = 0; l <vertexForVerge.size();l++)
                    {
                        if((k+1) == vertexForVerge.get(l)) contactVertexCurrentSegment.add(k+1);
                    }
                    for(int p = 0; p<edgesWithoutCircle.size();p++)
                    {
                        if(edgesWithoutCircle.get(p).Ne == k+1) edgesCurrentSegment.add(edgesWithoutCircle.get(p));
                        if(edgesWithoutCircle.get(p).Ke == k+1) edgesCurrentSegment.add(edgesWithoutCircle.get(p));
                    }
                }
            }
            ArrayList<edge> edgesCurrentSegment2 = new ArrayList<>();
            Iterator iterator = edgesCurrentSegment.iterator();
            while (iterator.hasNext())
            {
                edge o = (edge) iterator.next();
                if(!edgesCurrentSegment2.contains(o)) edgesCurrentSegment2.add(o);
            }
            edgesCurrentSegment=edgesCurrentSegment2;
            segments.add(new segment(contactVertexCurrentSegment,vertexCurrentSegment,edgesCurrentSegment,acceptedVerge));
        }
        for(int i = 0; i < segments.size();i++)
        {
            for(int j = 0;j<segments.get(i).edges.size() - 1;j++)
            {
                for(int k = j+1; k < segments.get(i).edges.size();k++)
                {
                    if((segments.get(i).edges.get(k).Ne == segments.get(i).edges.get(j).Ne)&&
                            (segments.get(i).edges.get(k).Ke == segments.get(i).edges.get(j).Ke))
                    {
                        segments.get(i).edges.remove(k);
                    }
                }
            }
        }
        boolean firstStep = true;
        /**
         *  Основной цикл алгоритма
         */
        while(segments.isEmpty() != true) {
            /** Переопределяем множество сегментов!
             *  т.е. находим в сегменте ребро из двух контактных вершин и создаем новый сегмент
             */
            if (firstStep == false) {
                //выкинем ребра из сегментов, если они уже лежат в результирующих just in case we fucked up
                for (int i = 0; i < segments.size(); i++) {
                    ArrayList<edge> deletedEdges = new ArrayList<>();
                    for (int j = 0;j<segments.get(i).edges.size();j++) {
                        for (int k = 0;k<resultEdges.size();k++) {
                            if (segments.get(i).edges.get(j).Ne == resultEdges.get(k).Ne &&
                                    segments.get(i).edges.get(j).Ke == resultEdges.get(k).Ke)
                            {
                                deletedEdges.add(segments.get(i).edges.get(j));
                                //segments.get(i).edges.remove(segments.get(i).edges.get(j));
                            }
                        }

                    }
                    segments.get(i).edges.removeAll(deletedEdges);
                }
                //тоже самое со списками всех вершин
                for (int i = 0; i < segments.size(); i++) {
                    ArrayList<Integer> deletedVertex = new ArrayList<>();
                    for (int k = 0;k<segments.get(i).allVertexList.size();k++) {
                        int flag =0;
                        for (int j = 0;j<segments.get(i).edges.size();j++) {

                            if (segments.get(i).edges.get(j).Ne == segments.get(i).allVertexList.get(k) ||
                                    segments.get(i).edges.get(j).Ke == segments.get(i).allVertexList.get(k))
                            {

                                flag = 1;
                                //segments.get(i).edges.remove(segments.get(i).edges.get(j));
                            }
                        }
                        if(flag == 0)
                        {
                            deletedVertex.add(segments.get(i).allVertexList.get(k));
                        }

                    }
                    segments.get(i).allVertexList.removeAll(deletedVertex);
                }
                //и контактными
                ArrayList<Integer> emptyVertex = new ArrayList<>();
                for (int i = 0; i < segments.size(); i++) {
                    emptyVertex.clear();
                    for (int j = 0; j < segments.get(i).contactVertexList.size(); j++) {
                        int flag5 = 0;
                        for (int k = 0; k < segments.get(i).edges.size(); k++) {
                            if (segments.get(i).contactVertexList.get(j) == segments.get(i).edges.get(k).Ne ||
                                    segments.get(i).contactVertexList.get(j) == segments.get(i).edges.get(k).Ke) {
                                flag5 = 1;
                            }
                        }
                        if (flag5 == 0) {
                            emptyVertex.add(segments.get(i).contactVertexList.get(j));
                        }
                    }
                    for (int j = 0; j < segments.get(i).contactVertexList.size(); j++) {
                        for (int w = 0; w < emptyVertex.size(); w++) {
                            if (segments.get(i).contactVertexList.get(j) == emptyVertex.get(w)) {
                                segments.get(i).contactVertexList.remove(j);
                            }
                        }
                    }
                }
                newPoint:
                //проходим по списку сегментов
                for (int i = 0; i < segments.size(); i++) {
                    //если сегмент состоит из 1 ребра, он нас не интересует
                    if (segments.get(i).edges.size() == 1) continue;
                    //проходим по списку ребер данного сегмента
                    for (int j = 0; j < segments.get(i).edges.size(); j++) {
                        //фиксируем первую контактную вершину
                        for (int k = 0; k < segments.get(i).contactVertexList.size(); k++) {
                            //фиксируем вторую контактную вершину
                            for (int l = 0; l < segments.get(i).contactVertexList.size(); l++) {
                                //если мы нашли ребро в сегменте из двух контактных вершин
                                //то мы его выносим в новый сегмент
                                if ((l != k && segments.get(i).edges.get(j).Ne == segments.get(i).contactVertexList.get(k) &&
                                        segments.get(i).edges.get(j).Ke == segments.get(i).contactVertexList.get(l)) ||
                                        l != k && segments.get(i).edges.get(j).Ke == segments.get(i).contactVertexList.get(k) &&
                                                segments.get(i).edges.get(j).Ne == segments.get(i).contactVertexList.get(l)) {
                                    //создали новый сегмент, добавили в список
                                    ArrayList<Integer> contactVertexCurrentSegment = new ArrayList<>();
                                    contactVertexCurrentSegment.add(segments.get(i).contactVertexList.get(l));
                                    contactVertexCurrentSegment.add(segments.get(i).contactVertexList.get(k));
                                    ArrayList<Integer> vertexCurrentSegment = new ArrayList<>();
                                    vertexCurrentSegment.add(segments.get(i).contactVertexList.get(l));
                                    vertexCurrentSegment.add(segments.get(i).contactVertexList.get(k));
                                    ArrayList<edge> edgesCurrentSegment = new ArrayList<>();
                                    edgesCurrentSegment.add(segments.get(i).edges.get(j));
                                    ArrayList<verge> acceptedVerge = new ArrayList<>();
                                    segment newSegment = new segment(contactVertexCurrentSegment, vertexCurrentSegment, edgesCurrentSegment, acceptedVerge);
                                    segments.add(newSegment);
                                    //удаляем ребро из текущего сегмента, обновляем параметры сегмента
                                    segments.get(i).edges.remove(segments.get(i).edges.get(j));//could be bad
                                    for (int w = 0; w < segments.get(i).contactVertexList.size(); w++) {
                                        int flagShitty = 0;
                                        for (int z = 0; z < segments.get(i).edges.size(); z++) {
                                            if ((segments.get(i).contactVertexList.get(w) ==
                                                    segments.get(i).edges.get(z).Ne) ||
                                                    (segments.get(i).contactVertexList.get(w) ==
                                                            segments.get(i).edges.get(z).Ke))
                                                flagShitty = 1;
                                        }
                                        if (flagShitty == 0)
                                            segments.get(i).contactVertexList.remove(segments.get(i).contactVertexList.get(w));//could be bad
                                    }
                                    System.out.println(" WE MADE NEW SEGMENT AS A PAIR OF CONTACT");
                                    //удаляем все непринадлежащие сегменту вершины
                                    /*ArrayList<Integer> deletedVertex = new ArrayList<>();
                                    for (int y = 0;k<segments.get(i).allVertexList.size();y++) {
                                        int flag =0;
                                        for (int x = 0;j<segments.get(i).edges.size();x++) {
                                            if (segments.get(i).edges.get(x).Ne == segments.get(i).allVertexList.get(y) ||
                                                    segments.get(i).edges.get(x).Ke == segments.get(i).allVertexList.get(y))
                                            {
                                                flag = 1;
                                                //segments.get(i).edges.remove(segments.get(i).edges.get(j));
                                            }
                                        }
                                        if(flag == 0)
                                        {
                                            deletedVertex.add(segments.get(i).allVertexList.get(y));
                                        }
                                    }
                                    segments.get(i).allVertexList.removeAll(deletedVertex);*/
                                    break newPoint;
                                }

                            }
                        }
                    }
                }
            }
            /** Определяем новые сегменты из текущего
             *  Для этого, нам необходимо взять все неконтактные вершины сегмента,
             *  пройти по ним и посмотреть, не являются ли все смежные с ними вершины - контактными
             *  Если это так, то мы можем вынести данную неконтактную вершину со всеми её ребрами
             *  в новый сегмент, или все остальные ребра вынести в новыый сегмент.
             *  Если же у нас не нашлось сегментов из 1 неконтактной вершины, мы начинаем создавать
             *  список из неконтактных вершин, в который будут входить все неконтактные вершины,
             *  связанные между собой не контактными вершинами.
             *  Если очередная контактная вершина не имеет других неконтактных смежных вершин, кроме уже
             *  имеющихся в списке, но при этом в сегменте остались неконтактные вершины, то мы
             *  переопределяем новый сегмент, в который отправляются все несвязные с данным списком
             *  неконтактные вершины, + все инцидентные им ребра.
             *  Таким образом мы переопределим множество всех сегментов графа.
             */
            if (firstStep == false)
            {
                //проверяем, нет ли у нас пустого сегмента
                somePoint:
                //проходим по списку сегментов
                for (int  i = 0; i<segments.size();i++)
                {
                    //для каждого сегмента определяем список неконтактных вершин
                    ArrayList<Integer> nonContactVertex = new ArrayList<>();
                    //проходим по списку всех вершин сегмента, добавляем не контактные в список
                    for (int j = 0; j< segments.get(i).allVertexList.size();j++)
                    {
                        //если вершина не содержится в контактных данного сегмента
                        if (!segments.get(i).contactVertexList.contains(segments.get(i).allVertexList.get(j)))
                        {
                            //мы ее добавляем в список неконтактных
                            nonContactVertex.add(segments.get(i).allVertexList.get(j));
                        }
                        //проверим, не напиздели ли мы со списком не контактных вершин
                    }
                    //проверим, не напиздели ли мы со списком не контактных вершин
                    for (int j = 0;j<nonContactVertex.size();j++) {
                        int flag = 0;
                        for (int k = 0; k < segments.get(i).edges.size(); k++) {
                            if(nonContactVertex.get(j) == segments.get(i).edges.get(k).Ne ||
                                    nonContactVertex.get(j) == segments.get(i).edges.get(k).Ke)
                            {
                                flag = 1;
                            }
                        }
                        if(flag == 0)
                        {
                            nonContactVertex.remove(nonContactVertex.get(j));
                        }
                    }
                    if(nonContactVertex.size() > 1) {
                        //проходим по списку неконтактных вершин, смотрим инцидентные им вершины
                        for (int j = 0; j < nonContactVertex.size(); j++) {
                            //создали список ребер инцидентных данной неконтактной вершине
                            ArrayList<edge> nonContactIncidentEdges = new ArrayList<>();
                            int isHereAnotherNonContactVertex = 0;
                            //проходим по всем ребрам сегмента
                            for (int k = 0; k < segments.get(i).edges.size(); k++) {
                                //если нашли нашу неконтактную вершину
                                if (segments.get(i).edges.get(k).Ne == nonContactVertex.get(j)) {
                                    //смотрим кто ей инцидентен
                                    if (segments.get(i).contactVertexList.contains(segments.get(i).edges.get(k).Ke)) {//если ей инцидентна контактная вершина, то мы добавляем данное ребро в список
                                        nonContactIncidentEdges.add(segments.get(i).edges.get(k));
                                    } else {//в противном случае нам не интересна данная вершина
                                        isHereAnotherNonContactVertex = 1;
                                        break;
                                    }
                                }
                                //если нашли нашу неконтактную вершину
                                if (segments.get(i).edges.get(k).Ke == nonContactVertex.get(j)) {
                                    //смотрим кто ей инцидентен
                                    if (segments.get(i).contactVertexList.contains(segments.get(i).edges.get(k).Ne)) {//если ей инцидентна контактная вершина, то мы добавляем данное ребро в список
                                        nonContactIncidentEdges.add(segments.get(i).edges.get(k));
                                    } else {//в противном случае нам не интересна данная вершина
                                        isHereAnotherNonContactVertex = 1;
                                        break;
                                    }
                                }
                            }
                            //если мы не нашли другую не контактную вершину, инцидентную данной,
                            //а только контактные, то мы выносим эти ребра и вершины в новый сегмент
                            if (isHereAnotherNonContactVertex == 0) {//создаем новый сегмент
                                //определяем список контактных вершин нового сегмента
                                ArrayList<Integer> contactVertexCurrentSegment = new ArrayList<>();
                                for (int k = 0; k < segments.get(i).contactVertexList.size(); k++) {
                                    //проходим по всем ребрам нашего нового сегмента, смотрим какие вершины из
                                    // данного списка ребер контактные в старом сегменте
                                    for (int w = 0; w < nonContactIncidentEdges.size(); w++) {
                                        //и добавляем их в новый сегмент
                                        if ((segments.get(i).contactVertexList.get(k) == nonContactIncidentEdges.get(w).Ne ||
                                                segments.get(i).contactVertexList.get(k) == nonContactIncidentEdges.get(w).Ke)
                                                && !contactVertexCurrentSegment.contains(segments.get(i).contactVertexList.get(k)))
                                            contactVertexCurrentSegment.add(segments.get(i).contactVertexList.get(k));
                                    }
                                }
                                //определяем список вообще всех вершин нового сегмента
                                //то есть добавляем из списка контактных 1 неконтактную
                                ArrayList<Integer> vertexCurrentSegment = new ArrayList<>();
                                //добавили все контактные
                                /*for (int k = 1; k < contactVertexCurrentSegment.size(); k++) {
                                    vertexCurrentSegment.add(contactVertexCurrentSegment.get(k));
                                }*/
                                //и добавили единственную не контактную
                                vertexCurrentSegment.add(nonContactVertex.get(j));
                                // добавили все контактные
                                for (int k = 0; k < segments.get(i).edges.size(); k++) {
                                    if (segments.get(i).edges.get(k).Ne == nonContactVertex.get(j) &&
                                            !vertexCurrentSegment.contains(segments.get(i).edges.get(k).Ke)) {
                                        vertexCurrentSegment.add(segments.get(i).edges.get(k).Ke);
                                        if (!contactVertexCurrentSegment.contains(segments.get(i).edges.get(k).Ke)) {
                                            contactVertexCurrentSegment.add(segments.get(i).edges.get(k).Ke);
                                        }
                                    }
                                    if (segments.get(i).edges.get(k).Ke == nonContactVertex.get(j) &&
                                            !vertexCurrentSegment.contains(segments.get(i).edges.get(k).Ne)) {
                                        vertexCurrentSegment.add(segments.get(i).edges.get(k).Ne);
                                        if (!contactVertexCurrentSegment.contains(segments.get(i).edges.get(k).Ne)) {
                                            contactVertexCurrentSegment.add(segments.get(i).edges.get(k).Ne);
                                        }
                                    }
                                }
                                //определяем все ребра нового сегмента
                                ArrayList<edge> edgesCurrentSegment = new ArrayList<>();
                                //т.е. добавляем все ребра, что мы определили как инцидентные данной
                                //не контактной вершине
                                nonContactIncidentEdges.clear();
                                for (int k = 0; k < segments.get(i).edges.size(); k++) {
                                    if (vertexCurrentSegment.contains(segments.get(i).edges.get(k).Ne) &&
                                            vertexCurrentSegment.contains(segments.get(i).edges.get(k).Ke)) {
                                        nonContactIncidentEdges.add(segments.get(i).edges.get(k));
                                    }
                                }
                                for (int k = 0; k < nonContactIncidentEdges.size(); k++) {
                                    edgesCurrentSegment.add(nonContactIncidentEdges.get(k));
                                }
                                ArrayList<verge> acceptedVerge = new ArrayList<>();
                                segment newSegment = new segment(contactVertexCurrentSegment, vertexCurrentSegment, edgesCurrentSegment, acceptedVerge);
                                //добавили в конец списка новый сегмент
                                segments.add(newSegment);
                                System.out.println("We made new segment with only 1 nonContactVertex in");
                                //удаляем все, что принадлежит новому сегменту из текущего
                                //удаляем все, кроме ребра из текущего сегмента, обновляем параметры сегмента
                                //удалили все ребра, что пошли в новый сегмент
                                //segments.get(i).edges.removeAll(nonContactIncidentEdges);
                                for (int k = 0; k < nonContactIncidentEdges.size(); k++) {
                                    if (segments.get(i).edges.contains(nonContactIncidentEdges.get(k))) {
                                        segments.get(i).edges.remove(nonContactIncidentEdges.get(k));
                                    }
                                }
                                //удаляем вершину, что была неконтактной
                                segments.get(i).allVertexList.remove(nonContactVertex.get(j));
                                //segments.get(i).contactVertexList.removeAll(segments.get(segments.size()-1).contactVertexList);
                                //проходим по всем оставшимся ребрам сегмента
                                ArrayList<Integer> tempDel = new ArrayList<>();
                                for (int w = 0; w < segments.get(i).contactVertexList.size(); w++) {
                                    //если вершина не встретилась ни в одном ребре, то мы удаляем ее из списка
                                    int flagForCheckNonIncidentVertex = 0;
                                    for (int k = 0; k < segments.get(i).edges.size(); k++) {
                                        if (segments.get(i).edges.get(k).Ne == segments.get(i).contactVertexList.get(w)
                                                || segments.get(i).edges.get(k).Ke == segments.get(i).contactVertexList.get(w)) {
                                            flagForCheckNonIncidentVertex = 1;
                                        }
                                    }
                                    if (flagForCheckNonIncidentVertex == 0) {
                                        tempDel.add(segments.get(i).contactVertexList.get(w));
                                    }
                                }
                                //удаляем все вершины, которые мы определили как непринадлежащие данному сегменту
                                //segments.get(i).contactVertexList.removeAll(tempDel);
                                for (int k = 0; k < tempDel.size(); k++) {
                                    if (segments.get(i).contactVertexList.contains(tempDel.get(k))) {
                                        segments.get(i).contactVertexList.remove(tempDel.get(k));
                                    }
                                }
                                //проходим по всем оставшимся ребрам сегмента
                                ArrayList<Integer> tempDelForAllVertex = new ArrayList<>();
                                for (int w = 0; w < segments.get(i).allVertexList.size(); w++) {
                                    //если вершина не встретилась ни в одном ребре, то мы удаляем ее из списка
                                    int flagForCheckNonIncidentVertex = 0;
                                    for (int k = 0; k < segments.get(i).edges.size(); k++) {
                                        if (segments.get(i).edges.get(k).Ne == segments.get(i).allVertexList.get(w)
                                                || segments.get(i).edges.get(k).Ke == segments.get(i).allVertexList.get(w)) {
                                            flagForCheckNonIncidentVertex = 1;
                                        }
                                    }
                                    if (flagForCheckNonIncidentVertex == 0) {
                                        tempDelForAllVertex.add(segments.get(i).allVertexList.get(w));
                                    }
                                }
                                //удаляем все вершины, которые мы определили как непринадлежащие данному сегменту
                                segments.get(i).allVertexList.removeAll(tempDelForAllVertex);
                                break somePoint;
                            }
                            //теперь мы убедились, что в сегменте больше нет сегментов из 1 неконтактной вершины
                        }
                    }

                }
            }

            if (firstStep == false)
            {

                for (int  i = 0; i<segments.size();i++) {
                    //для каждого сегмента определяем список неконтактных вершин
                    ArrayList<Integer> nonContactVertex = new ArrayList<>();
                    //проходим по списку всех вершин сегмента, добавляем не контактные в список
                    for (int j = 0; j < segments.get(i).allVertexList.size(); j++) {
                        //если вершина не содержится в контактных данного сегмента
                        if (!segments.get(i).contactVertexList.contains(segments.get(i).allVertexList.get(j))) {
                            //мы ее добавляем в список неконтактных
                            nonContactVertex.add(segments.get(i).allVertexList.get(j));
                        }
                        //проверим, не напиздели ли мы со списком не контактных вершин
                    }
                    //проверим, не напиздели ли мы со списком не контактных вершин
                    for (int j = 0; j < nonContactVertex.size(); j++) {
                        int flag = 0;
                        for (int k = 0; k < segments.get(i).edges.size(); k++) {
                            if (nonContactVertex.get(j) == segments.get(i).edges.get(k).Ne ||
                                    nonContactVertex.get(j) == segments.get(i).edges.get(k).Ke) {
                                flag = 1;
                            }
                        }
                        if (flag == 0) {
                            nonContactVertex.remove(nonContactVertex.get(j));
                        }
                    }
                    if(nonContactVertex.size() > 3)
                    {
                        ArrayList<edge> nonContactEdges = new ArrayList<>();
                        //определяем множество ребер, состоящих из неконтактных вершин
                        for (int j = 0; j<segments.get(i).edges.size();j++)
                        {
                            //если обе вершине принаддежат списку неконтактных
                            if(nonContactVertex.contains(segments.get(i).edges.get(j).Ne) &&
                                    nonContactVertex.contains(segments.get(i).edges.get(j).Ke))
                            {
                                //до мы добавляем это ребро
                                nonContactEdges.add(segments.get(i).edges.get(j));
                            }
                        }
                        // теперь мы проходим по всем этим ребрам, и определяем, сколько кокмпонент связности мы имеем
                        // среди неконтактных вершин, если мы определим, что есть более 1 компоненты, мы её вынесем  в
                        // новый сегмент
                        circle[] nonContactVertexInc = new circle[nonContactVertex.size()];
                        for(int j = 0;j<nonContactVertex.size();j++)
                        {
                            nonContactVertexInc[j] = new circle();
                        }
                        for(int j = 0;j<nonContactVertex.size();j++)
                        {
                            nonContactVertexInc[j].num = nonContactVertex.get(j);
                            nonContactVertexInc[j].myFather = (-1)*nonContactVertexInc[j].num;
                        }
                        //проходим по всем ребрам столько раз, сколько у нас этих ребер
                        for (int j = 0; j< nonContactEdges.size();j++)
                        {
                            for (int l=0;l<nonContactVertexInc.length;l++)
                            {
                                for(int k = 0;k<nonContactEdges.size();k++) {
                                    //фиксируем первую вершину
                                    if (nonContactEdges.get(k).Ne == nonContactVertexInc[l].num)
                                    {
                                        for (int n=0;n<nonContactVertexInc.length;n++)
                                        {
                                            //фиксируем вторую вершину ребра
                                            if (nonContactEdges.get(k).Ke == nonContactVertexInc[n].num &&
                                                    nonContactEdges.get(k).Ne == nonContactVertexInc[l].num)
                                            {
                                                if (nonContactVertexInc[n].myFather != nonContactVertexInc[l].myFather)
                                                {
                                                    if (nonContactVertexInc[n].myFather > nonContactVertexInc[l].myFather)
                                                    {
                                                        nonContactVertexInc[n].myFather = nonContactVertexInc[l].myFather;
                                                    }
                                                    else {
                                                        nonContactVertexInc[l].myFather = nonContactVertexInc[n].myFather;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (nonContactEdges.get(k).Ke == nonContactVertexInc[l].num)
                                    {
                                        {
                                            for (int n=0;n<nonContactVertexInc.length;n++)
                                            {
                                                //фиксируем вторую вершину ребра
                                                if (nonContactEdges.get(k).Ne == nonContactVertexInc[n].num &&
                                                        nonContactEdges.get(k).Ke == nonContactVertexInc[l].num)
                                                {
                                                    if (nonContactVertexInc[n].myFather != nonContactVertexInc[l].myFather)
                                                    {
                                                        if (nonContactVertexInc[n].myFather > nonContactVertexInc[l].myFather)
                                                        {
                                                            nonContactVertexInc[n].myFather = nonContactVertexInc[l].myFather;
                                                        }
                                                        else {
                                                            nonContactVertexInc[l].myFather = nonContactVertexInc[n].myFather;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ArrayList<Integer> numberOfComponents = new ArrayList<>();
                        //считаем количество компонент смежности
                        for (int k = 0;k<nonContactVertexInc.length;k++)
                        {
                            if(!numberOfComponents.contains(nonContactVertexInc[k].myFather))
                            {
                                numberOfComponents.add(nonContactVertexInc[k].myFather);
                            }
                        }
                        System.out.println(" Число компонент равно "+ numberOfComponents.size());
                        //System.out.println("I WAS HERE");
                        //теперь мы знаем сколько компонент смежности образуют неконтактные вершины,
                        //а значит если их более двух, то мы можем вынести все, кроме 1ой в новый сегмент!
                        if(numberOfComponents.size() > 1)
                        {
                            ArrayList<Integer> nonContactNotThisSegment = new ArrayList<>();
                            //определяем первую компоненту, которую мы оставим в сегменте
                            for (int k = 0; k < nonContactVertexInc.length;k++)
                            {
                                if(nonContactVertexInc[k].myFather == numberOfComponents.get(1))
                                {
                                    //добавили в список неконтактные вершины, которые не останутся в данном сегменте
                                    nonContactNotThisSegment.add(nonContactVertexInc[k].num);
                                }
                            }
                            //теперь ищем для них все смежные контактные и определяем, равно ли их число хотя бы 2
                            ArrayList<Integer> contactNotThisSegment = new ArrayList<>();
                            for (int l = 0;l< nonContactNotThisSegment.size();l++)
                            {
                                for (int k = 0;k< segments.get(i).edges.size();k++)
                                {
                                    if(segments.get(i).edges.get(k).Ne == nonContactNotThisSegment.get(l)&&
                                            segments.get(i).contactVertexList.contains(segments.get(i).edges.get(k).Ke)&&
                                            !contactNotThisSegment.contains(segments.get(i).edges.get(k).Ke))
                                    {
                                        contactNotThisSegment.add(segments.get(i).edges.get(k).Ke);
                                    }
                                    if(segments.get(i).edges.get(k).Ke == nonContactNotThisSegment.get(l)&&
                                            segments.get(i).contactVertexList.contains(segments.get(i).edges.get(k).Ne)&&
                                            !contactNotThisSegment.contains(segments.get(i).edges.get(k).Ne))
                                    {
                                        contactNotThisSegment.add(segments.get(i).edges.get(k).Ne);
                                    }
                                }
                            }
                            //если число контактных вершин получилось меньше 2, то мы не будем создавать новый сегмент
                            if(contactNotThisSegment.size() < 2) break;
                            //если же есть х.б. 2 контактные, то мы образуем новый сегмент
                            //определяем список всех вершин нового сегмента
                            ArrayList<Integer> vertexCurrentSegment = nonContactNotThisSegment;
                            for(int  k = 0; k< contactNotThisSegment.size();k++)
                            {
                                vertexCurrentSegment.add(contactNotThisSegment.get(k));
                            }
                            //определеяем все ребра в новом сегменте
                            ArrayList<edge> edgesCurrentSegment = new ArrayList<>();
                            for(int  k = 0;k<segments.get(i).edges.size();k++)
                            {
                                if(vertexCurrentSegment.contains(segments.get(i).edges.get(k).Ne)&&
                                        !edgesCurrentSegment.contains(segments.get(i).edges.get(k)))
                                {
                                    edgesCurrentSegment.add(segments.get(i).edges.get(k));
                                }
                                if(vertexCurrentSegment.contains(segments.get(i).edges.get(k).Ke)&&
                                        !edgesCurrentSegment.contains(segments.get(i).edges.get(k)))
                                {
                                    edgesCurrentSegment.add(segments.get(i).edges.get(k));
                                }
                            }
                            ArrayList<verge> acceptedVerge = new ArrayList<>();
                            segment newSegment = new segment(contactNotThisSegment, vertexCurrentSegment, edgesCurrentSegment, acceptedVerge);
                            //добавили в конец списка новый сегмент
                            segments.add(newSegment);
                            //теперь удаляем все ребра, которые мы определили в новый сегмент из текущего

                            for (int k = 0; k < edgesCurrentSegment.size(); k++) {
                                if (segments.get(i).edges.contains(edgesCurrentSegment.get(k))) {
                                    segments.get(i).edges.remove(edgesCurrentSegment.get(k));
                                }
                            }
                            ArrayList<Integer> tempDel = new ArrayList<>();
                            for (int w = 0; w < segments.get(i).contactVertexList.size(); w++) {
                                //если вершина не встретилась ни в одном ребре, то мы удаляем ее из списка
                                int flagForCheckNonIncidentVertex = 0;
                                for (int k = 0; k < segments.get(i).edges.size(); k++) {
                                    if (segments.get(i).edges.get(k).Ne == segments.get(i).contactVertexList.get(w)
                                            || segments.get(i).edges.get(k).Ke == segments.get(i).contactVertexList.get(w)) {
                                        flagForCheckNonIncidentVertex = 1;
                                    }
                                }
                                if (flagForCheckNonIncidentVertex == 0) {
                                    tempDel.add(segments.get(i).contactVertexList.get(w));
                                }
                            }
                            //удаляем все вершины, которые мы определили как непринадлежащие данному сегменту
                            for (int k = 0; k < tempDel.size(); k++) {
                                if (segments.get(i).contactVertexList.contains(tempDel.get(k))) {
                                    segments.get(i).contactVertexList.remove(tempDel.get(k));
                                }
                            }
                            //проходим по всем оставшимся ребрам сегмента
                            ArrayList<Integer> tempDelForAllVertex = new ArrayList<>();
                            for (int w = 0; w < segments.get(i).allVertexList.size(); w++) {
                                //если вершина не встретилась ни в одном ребре, то мы удаляем ее из списка
                                int flagForCheckNonIncidentVertex = 0;
                                for (int k = 0; k < segments.get(i).edges.size(); k++) {
                                    if (segments.get(i).edges.get(k).Ne == segments.get(i).allVertexList.get(w)
                                            || segments.get(i).edges.get(k).Ke == segments.get(i).allVertexList.get(w)) {
                                        flagForCheckNonIncidentVertex = 1;
                                    }
                                }
                                if (flagForCheckNonIncidentVertex == 0) {
                                    tempDelForAllVertex.add(segments.get(i).allVertexList.get(w));
                                }
                            }
                            //удаляем все вершины, которые мы определили как непринадлежащие данному сегменту
                            segments.get(i).allVertexList.removeAll(tempDelForAllVertex);

                            System.out.println(" WE MADE NEW SEGMENT AS A NON CONTACT COMPONENT!!!!");
                        }
                    }
                }
            }


            //переопределяем список контактных вершин каждого сегмента через принадлежность результирующим ребрам
            /*for(int  i = 0; i < segments.size();i++)
            {
                for(int k = 0;k< resultEdges.size();k++)
                {
                    if(segments.get(i).allVertexList.contains(resultEdges.get(k).Ne)&&
                            !segments.get(i).contactVertexList.contains(resultEdges.get(k).Ne))
                    {
                        segments.get(i).contactVertexList.add(resultEdges.get(k).Ne);
                    }
                    if(segments.get(i).allVertexList.contains(resultEdges.get(k).Ke)&&
                            !segments.get(i).contactVertexList.contains(resultEdges.get(k).Ke))
                    {
                        segments.get(i).contactVertexList.add(resultEdges.get(k).Ke);
                    }
                }
            }*/
            System.out.println(" Lets check contact vertex here!!! ");
            for(int i=0;i<segments.size();i++)
            {
                if(segments.get(i).edges.isEmpty() == true)
                {
                    segments.remove(i);
                }
            }
            if(segments.isEmpty() == true)
            {
                isourGraphPlanar = true;
                System.out.print(" Our graph is planar! ");
                return;
            }
            if (segments.get(0).contactVertexList.isEmpty()==true)
            {
                System.out.println(" still dont have any contact vertex !!! ");
            }
            //проверяем, нет ли у нас пустого сегмента
            for(int  i = 0; i< segments.size();i++)
            {
                System.out.print("segment: "+i+" : ");
                for(int j = 0; j< segments.get(i).contactVertexList.size();j++)
                {
                    System.out.print(" "+ segments.get(i).contactVertexList.get(j)+" ");
                }
                System.out.println();
            }

            /**
             * Определеяем множество допустимых граней
             */

            ArrayList<Integer> emptyVertex = new ArrayList<>();
            for (int i = 0; i < segments.size(); i++) {
                emptyVertex.clear();
                for (int j = 0; j < segments.get(i).contactVertexList.size(); j++) {
                    int flag5 = 0;
                    for (int k = 0; k < segments.get(i).edges.size(); k++) {
                        if (segments.get(i).contactVertexList.get(j) == segments.get(i).edges.get(k).Ne ||
                                segments.get(i).contactVertexList.get(j) == segments.get(i).edges.get(k).Ke) {
                            flag5 = 1;
                        }
                    }
                    if (flag5 == 0) {
                        emptyVertex.add(segments.get(i).contactVertexList.get(j));
                    }
                }
                for (int j = 0; j < segments.get(i).contactVertexList.size(); j++) {
                    for (int w = 0; w < emptyVertex.size(); w++) {
                        if (segments.get(i).contactVertexList.get(j) == emptyVertex.get(w)) {
                            segments.get(i).contactVertexList.remove(j);
                        }
                    }
                }
            }
            ArrayList<Integer> currContVert = new ArrayList<>();
            ArrayList<Integer> currContVert2 = new ArrayList<>();
            int segment_flag2, another_flag;
            for (int i = 0; i < segments.size(); i++) {
                segments.get(i).acceptedVerge.clear();
            }
            ArrayList<Integer> acceptedVergeIndex = new ArrayList<>();
            acceptedVergeIndex.clear();
            for (int i = 0; i < segments.size(); i++) {
                for (int j = 0; j < verges.size(); j++) {
                    //currContVert = segments.get(i).contactVertexList;
                    currContVert.addAll(segments.get(i).contactVertexList);
                    //currContVert2 = segments.get(i).contactVertexList;
                    currContVert2.addAll(segments.get(i).contactVertexList);
                    segmentCount = 0;
                    another_flag = 1;
                    for (int w = 0; w < currContVert.size(); w++) {
                        segment_flag2 = 0;
                        for (int k = 0; k < verges.get(j).contactVertexList.size(); k++) {
                            if (currContVert.get(w) == verges.get(j).contactVertexList.get(k))
                            {
                                currContVert2.remove(currContVert2.size() - 1);
                                segment_flag2 = 1;
                            }
                        }
                        if (segment_flag2 == 0) {
                            another_flag = 0;
                        }
                    }
                    if(currContVert2.contains(currContVert.get(0)))
                    {
                        //System.out.println("u piece of shit its acceptable");
                    }
                    if (another_flag == 1) {
                        segments.get(i).acceptedVerge.add(verges.get(j));
                        if (i == 0) {
                            acceptedVergeIndex.add(j);
                        }
                    }
                    currContVert.clear();
                }
            }
            //новая проверка на допустимые грани
            //проходим по всем сегментам
            if(gammaGraph.numberNodes > 100){
                for (int i = 0; i < segments.size(); i++)
                {
                    //проходим по всем граням, в каждой проверяем
                    for(int j = 0;j<verges.size();j++)
                    {
                        int flagForAccept = 0;
                        for(int k = 0;k<segments.get(i).contactVertexList.size();k++)
                        {
                            if(!verges.get(j).contactVertexList.contains(segments.get(i).contactVertexList.get(k)))
                            {
                                flagForAccept = 1;
                            }
                        }
                        if(flagForAccept == 0)
                        {
                            segments.get(i).acceptedVerge.add(verges.get(j));
                            if (i == 0) {
                                acceptedVergeIndex.add(j);
                            }
                        }
                    }
                }
            }
            for(int i = 0;i<segments.size();i++)
            {
                if(segments.get(i).acceptedVerge.isEmpty() == true&& segments.get(i).contactVertexList.isEmpty() != true)//could be rly bad
                {
                    for(int j = 0; j< segments.get(i).edges.size();j++)
                    {
                        notPlanarEdges.add(segments.get(i).edges.get(j));
                    }
                    System.out.print("Our Graph is not planar!!!");
                    isourGraphPlanar = false;
                    return;
                }
            }
            //int first_contact_point = -1;
            int last_contact_point = -1;
            //boolean is_last_contact = false;
            //int prev_point;
            ArrayList<Integer> averagePoint = new ArrayList<>();
            /*for (int i = 0; i < segments.get(0).edges.size(); i++) {
                for (int j = 0; j < segments.get(0).contactVertexList.size(); j++) {
                    if (segments.get(0).edges.get(i).Ke == segments.get(0).contactVertexList.get(j)) {
                        first_contact_point = segments.get(0).edges.get(i).Ke;
                        break;
                    }
                    if (segments.get(0).edges.get(i).Ne == segments.get(0).contactVertexList.get(j)) {
                        first_contact_point = segments.get(0).edges.get(i).Ne;
                        break;
                    }
                }
            }*/
            //экспериментальный кусок кода
            /*if(segments.get(0).contactVertexList.size() == 1&& segments.get(0).edges.size()==1) {
                resultEdges.add(segments.get(0).edges.get(0));
                segments.get(0).edges = new ArrayList<>();
                segments.remove(0);
                continue;
            }*/
            //новый способ определения альфа цепи
            GraphForSegment theGraph = new GraphForSegment();
            //добавили вершины
            for(int i= 0;i<segments.get(0).allVertexList.size();i++)
            {
                theGraph.addVertex(segments.get(0).allVertexList.get(i));
            }
            //добавили ребра
            for(int i = 0;i<segments.get(0).edges.size();i++)
            {
                int firstNe = 0;
                int secondKe = 0;
                for(int j = 0;j<segments.get(0).allVertexList.size();j++)
                {
                    if(theGraph.vertexList[j].label == segments.get(0).edges.get(i).Ne)
                    {
                        firstNe = j;
                    }
                    if(theGraph.vertexList[j].label == segments.get(0).edges.get(i).Ke)
                    {
                        secondKe = j;
                    }
                }
                theGraph.addEdge(firstNe,secondKe);
            }
            ArrayList<Integer> path = new ArrayList<>();
            int firstContact = 0;
            int lastContact = 0;
            for(int j = 0;j<segments.get(0).allVertexList.size();j++) {
                if (theGraph.vertexList[j].label == segments.get(0).contactVertexList.get(0)) {
                    firstContact = j;
                }
            }
            //ищем путь из первой контактной сегмента
            theGraph.bfs(path,firstContact);
            System.out.println("works");
            //получили в path список смежности, теперь можем определить промежуточные вершины
            //проходим по списку и ищем вторую контактную вершину
            for(int i = 1;i<path.size();i++)
            {
                /*if(path.get(i) == segments.get(0).contactVertexList.get(1))
                {
                    //нашли позицию второй контактной вершины
                    lastContact = i;
                    System.out.println("last contact is: "+lastContact+" and it is: "+ path.get(lastContact)+" ");
                }*/
                if(segments.get(0).contactVertexList.contains(path.get(i)))
                {
                    lastContact = i;
                    last_contact_point = path.get(lastContact);
                    System.out.println("last contact is: "+lastContact+" and it is: "+ path.get(lastContact)+" ");
                }
            }
            //теперь мы должны добавить в average все промежуточные вершины между двумя контактными
            //проходим вновь по path от второй контактной к первой, и добавляем все вершины
            //с которыми имеет инцидентность

            while(lastContact > 1)
            {
                int flag = 0;
                for(int j = 0;j<segments.get(0).edges.size();j++)
                {
                    if(segments.get(0).edges.get(j).Ne == path.get(lastContact) &&
                            segments.get(0).edges.get(j).Ke == path.get(lastContact-1)
                            || (segments.get(0).edges.get(j).Ke == path.get(lastContact) &&
                            segments.get(0).edges.get(j).Ne == path.get(lastContact-1)))
                    {
                        flag = 1;
                    }
                }
                if(flag == 1)
                {
                    averagePoint.add(0,path.get(lastContact -1));
                    path.remove(lastContact);
                    lastContact--;
                }else
                {
                    path.remove(lastContact-1);
                    lastContact--;
                }
            }
            System.out.println("making progress!!!!");
            //добавляем новую альфа цепь
            doTheNewAlphaChain(segments.get(0).contactVertexList.get(0),
                    last_contact_point,acceptedVergeIndex.get(0),
                    averagePoint,segments.get(0));

            //WARNING!!!
            //переопределяем список контактных вершин каждого сегмента
            for(int  i = 0; i < segments.size();i++)
            {
                for(int k = 0;k< verges.size();k++)
                {
                    for (int j = 0; j < segments.get(i).allVertexList.size();j++)
                    {
                        if(verges.get(k).contactVertexList.contains(segments.get(i).allVertexList.get(j))&&
                                !segments.get(i).contactVertexList.contains(segments.get(i).allVertexList.get(j)))
                        {
                            segments.get(i).contactVertexList.add(segments.get(i).allVertexList.get(j));
                        }
                    }
                }
            }
            System.out.println(" New iteration begins!!! ");
            globalEliteCount++;
            firstStep = false;
            for(int i=0;i<segments.size();i++)
            {
                if(segments.get(i).edges.isEmpty() == true)
                {
                    segments.remove(i);
                }
            }
            if(segments.isEmpty() == true)
            {
                isourGraphPlanar = true;
                System.out.print(" Our graph is planar! ");
            }
        }
    }

    public void doTheNewAlphaChain(int first_contact_point,int last_contact_point, int verge_num, ArrayList<Integer> average,segment currentSegment)
    {
        //System.out.print("i was here u moron");
        ArrayList<Integer> temp = new ArrayList<>();
        if(average.isEmpty() != true)
        {
            //experimental part of my code
            //experimental part of my code
            System.out.println("first_contact_point: "+first_contact_point);
            System.out.print("average_points: ");
            for(int i = 0;i<average.size();i++)
            {
                System.out.print(" "+average.get(i)+" ");
            }
            System.out.println();
            System.out.println("last_contact_point: "+last_contact_point);
            int firstCheckedInd = -1;
            int secondCheckedInd = -1;
            for(int i = 0; i < verges.get(verge_num).contactVertexList.size();i++)
            {
                if(verges.get(verge_num).contactVertexList.get(i) == first_contact_point) firstCheckedInd = i;
                if(verges.get(verge_num).contactVertexList.get(i) == last_contact_point) secondCheckedInd = i;
            }
            /*if(firstCheckedInd > secondCheckedInd)
            {
                temp.add(last_contact_point);
                temp.add(first_contact_point);
            }
            if(firstCheckedInd < secondCheckedInd)
            {
                temp.add(first_contact_point);
                temp.add(last_contact_point);
            }*/
            //the end of experimental part of code
            //the end of experimental part of code
            int curr_point;
            for(int j = 0;j<segments.get(0).edges.size();j++)
            {
                if(segments.get(0).edges.get(j).Ne == first_contact_point &&
                        segments.get(0).edges.get(j).Ke ==average.get(0))
                {
                    //System.out.println("YA DOBAVIL VERSHINU V CONTACT ");
                    segments.get(0).contactVertexList.add(average.get(0));
                    resultEdges.add(segments.get(0).edges.get(j));
                    temp.add(first_contact_point); //the end of experimental part of code
                    temp.add(segments.get(0).edges.get(j).Ke);
                    segments.get(0).edges.remove(j);
                    break;
                }
                if(segments.get(0).edges.get(j).Ke == first_contact_point &&
                        segments.get(0).edges.get(j).Ne ==average.get(0))
                {
                    //System.out.println("YA DOBAVIL VERSHINU V CONTACT ");
                    segments.get(0).contactVertexList.add(average.get(0));
                    resultEdges.add(segments.get(0).edges.get(j));
                    temp.add(first_contact_point); //the end of experimental part of code
                    temp.add(segments.get(0).edges.get(j).Ne);
                    segments.get(0).edges.remove(j);
                    break;
                }
            }
            curr_point = average.get(0);
            ArrayList<edge> deletedEdges = new ArrayList<>();
            for(int i = 1;i<average.size();i++)
            {
                for(int j = 0;j<segments.get(0).edges.size();j++)
                {
                    if(segments.get(0).edges.get(j).Ne == curr_point &&
                            segments.get(0).edges.get(j).Ke ==average.get(i))
                    {
                        segments.get(0).contactVertexList.add(average.get(i));
                        resultEdges.add(segments.get(0).edges.get(j));
                        temp.add(segments.get(0).edges.get(j).Ke);
                        deletedEdges.add(segments.get(0).edges.get(j));
                        break;
                    }
                    if(segments.get(0).edges.get(j).Ke == curr_point &&
                            segments.get(0).edges.get(j).Ne ==average.get(i))
                    {
                        segments.get(0).contactVertexList.add(average.get(i));
                        resultEdges.add(segments.get(0).edges.get(j));
                        temp.add(segments.get(0).edges.get(j).Ne);
                        deletedEdges.add(segments.get(0).edges.get(j));
                        break;
                    }
                }
                curr_point = average.get(i);
            }
            for(int  i = 0; i< segments.get(0).edges.size();i++)
            {
                for(int j = 0 ;j<deletedEdges.size();j++)
                {
                    if(segments.get(0).edges.get(i).Ne == deletedEdges.get(j).Ne &&
                            segments.get(0).edges.get(i).Ke == deletedEdges.get(j).Ke)
                    {
                        segments.get(0).edges.remove(segments.get(0).edges.get(i));//could be bad
                        //segments.get(0).edges.remove(i);
                        break;
                    }
                }
            }
            for(int j = 0; j<segments.get(0).edges.size();j++)
            {
                if(segments.get(0).edges.get(j).Ne == last_contact_point &&
                        segments.get(0).edges.get(j).Ke == curr_point)
                {
                    resultEdges.add(segments.get(0).edges.get(j));
                    temp.add(last_contact_point); //the end of experimental part of code
                    segments.get(0).edges.remove(j);
                    break;
                }
                if(segments.get(0).edges.get(j).Ke == last_contact_point &&
                        segments.get(0).edges.get(j).Ne == curr_point)
                {
                    resultEdges.add(segments.get(0).edges.get(j));
                    temp.add(last_contact_point); //the end of experimental part of code
                    segments.get(0).edges.remove(j);
                    break;
                }
            }
            ArrayList<Integer> tempDel = new ArrayList<>();
            int firstInd = -1;int lastInd= -1;
            for(int i = 0; i < verges.get(verge_num).contactVertexList.size();i++)
            {
                if(verges.get(verge_num).contactVertexList.get(i) == first_contact_point) firstInd = i;
                if(verges.get(verge_num).contactVertexList.get(i) == last_contact_point) lastInd = i;
            }
            int flagForCorrectAdditing = 0;
            if(firstInd > lastInd)
            {
                int tmp;
                tmp = firstInd;
                firstInd = lastInd;
                lastInd = tmp;
                flagForCorrectAdditing = 1;
                //System.out.println("  WE SHOULD MAKE SWOP FIRST AND LAST!!!!");
            }
            for(int i = 0; i< verges.get(verge_num).contactVertexList.size();i++)
            {
                if(i>firstInd && i<lastInd)
                {
                    //если первая контактная вершина находится в грани после последней
                    //контактной вершины, то добавляем в обратном порядке от обычного
                    /*if(first_contact_point == verges.get(verge_num).contactVertexList.get(lastInd)
                            && flagForCorrectAdditing == 1)//experimental part of code
                    {
                        tempDel.add(verges.get(verge_num).contactVertexList.get(i));
                    }//the end of experimental part of code
                    else {*/
                    tempDel.add(0, verges.get(verge_num).contactVertexList.get(i));
                    //}
                    //tempDel.add(verges.get(verge_num).contactVertexList.get(i));//AGHTUNG
                }
            }
            ArrayList<Integer> newhuynya = new ArrayList<>();
            for(int i = 0; i< verges.get(verge_num).contactVertexList.size();i++)
            {
                newhuynya.add(verges.get(verge_num).contactVertexList.get(i));
            }
            newhuynya.subList(firstInd+1,lastInd).clear();
            verges.get(verge_num).contactVertexList = newhuynya;
            //verges.get(verge_num).contactVertexList.subList(firstInd+1,lastInd).clear();
            for(int i = 1;i<temp.size()-1;i++)
            {
                //добавляет в оба списка и verge_num и второй
                //verges.get(verge_num).contactVertexList.add(firstInd+1,temp.get(i));
                ArrayList<Integer> newarrayhuynya = new ArrayList<>();
                for(int j = 0; j< verges.get(verge_num).contactVertexList.size();j++)// verges.get(verge_num).contactVertexList;
                {
                    newarrayhuynya.add(verges.get(verge_num).contactVertexList.get(j));
                }
                newarrayhuynya.add(firstInd+1,temp.get(i));
                //newarrayhuynya.add(firstInd+1,temp.get(i));
                //if(flagForCorrectAdditing == 0) firstInd++;
                verges.get(verge_num).contactVertexList = newarrayhuynya;
                verges.get(verge_num).numberVertex++;
                firstInd++;//could be rly fucking bad
                if(firstCheckedInd > secondCheckedInd) firstInd--;//could be rly fucking bad at all
                //System.out.print("ADDSHIT");
            }
            //в новую грань пиздим все, что между контактными вершинами включая их, т.е.
            ArrayList<Integer> newTemp = new ArrayList<>();
            for(int i = 0; i < verges.get(verge_num).contactVertexList.size();i++)
            {
                if(verges.get(verge_num).contactVertexList.get(i) == first_contact_point) firstInd = i;
                if(verges.get(verge_num).contactVertexList.get(i) == last_contact_point) lastInd = i;
            }
            if(firstInd > lastInd)
            {
                int tmp;
                tmp = firstInd;
                firstInd = lastInd;
                lastInd = tmp;
            }
            //проходим по всем вершинам нашей измененной грани
            for (int i = 0;i<verges.get(verge_num).contactVertexList.size();i++)
            {
                //если это контактная или между ними, то мы ее берем в рабство
                if (i >= firstInd && i <= lastInd)
                {
                    newTemp.add(verges.get(verge_num).contactVertexList.get(i));
                }
            }
            //эта хуета не работает
            //verges.add(new verge(average.size()+2,temp));
            //а вот эта должна
            verges.add(new verge(average.size()+2,newTemp));
            /*for(int i = tempDel.size()-1; i >= 0;i--)//could be really bad
            {
                verges.get(verges.size()-1).contactVertexList.add(tempDel.get(i));
            }*/

            for(int i = 0; i< tempDel.size();i++)
            {
                verges.get(verges.size()-1).contactVertexList.add(tempDel.get(i));
            }
            System.out.println();
            for(int j = 0; j < verges.size();j++)
            {
                System.out.print(" verge "+j+" ");
                for(int i = 0; i< verges.get(j).contactVertexList.size();i++)
                {
                    System.out.print(" "+verges.get(j).contactVertexList.get(i));
                }
                System.out.println();
            }
            ArrayList<Integer> emptyVertexCurSegment1 = new ArrayList<>();
            emptyVertexCurSegment1.clear();
            for(int j = 0; j< segments.get(0).contactVertexList.size();j++)
            {
                int flag5 = 0;
                for(int k = 0;k< segments.get(0).edges.size();k++)
                {
                    if(segments.get(0).contactVertexList.get(j) == segments.get(0).edges.get(k).Ne ||
                            segments.get(0).contactVertexList.get(j) == segments.get(0).edges.get(k).Ke)
                    {
                        flag5 = 1;
                    }
                }
                if(flag5 == 0)
                {
                    emptyVertexCurSegment1.add(segments.get(0).contactVertexList.get(j));
                }
            }
            for(int j = 0; j<segments.get(0).contactVertexList.size();j++)
            {
                for(int w = 0; w< emptyVertexCurSegment1.size();w++)
                {
                    if(segments.get(0).contactVertexList.get(j) == emptyVertexCurSegment1.get(w))
                    {
                        segments.get(0).contactVertexList.remove(j);
                    }
                }
            }
            segments.get(0).contactVertexList.removeAll(emptyVertexCurSegment1);
            emptyVertexCurSegment1.clear();
            //тоже самое для всех вершин
            for(int j = 0; j< segments.get(0).allVertexList.size();j++)
            {
                int flag5 = 0;
                for(int k = 0;k< segments.get(0).edges.size();k++)
                {
                    if(segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ne ||
                            segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ke)
                    {
                        flag5 = 1;
                    }
                    /*if (segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ne
                            && !emptyVertexCurSegment1.contains(segments.get(0).allVertexList.get(j)))
                    {
                        emptyVertexCurSegment1.add(segments.get(0).allVertexList.get(j));
                    }
                    if (segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ke
                            && !emptyVertexCurSegment1.contains(segments.get(0).allVertexList.get(j)))
                    {
                        emptyVertexCurSegment1.add(segments.get(0).allVertexList.get(j));
                    }*/
                }
                if(flag5 == 0)
                {
                    emptyVertexCurSegment1.add(segments.get(0).allVertexList.get(j));
                }
            }
            /*for(int j = 0; j<segments.get(0).allVertexList.size();j++)
            {
                for(int w = 0; w< emptyVertexCurSegment1.size();w++)
                {
                    if(segments.get(0).allVertexList.get(j) == emptyVertexCurSegment1.get(w))
                    {
                        segments.get(0).allVertexList.remove(emptyVertexCurSegment1.get(w));
                    }
                }
            }*/
            segments.get(0).allVertexList.removeAll(emptyVertexCurSegment1);
            /*segments.get(0).allVertexList.clear();
            for (int j = 0 ;j<emptyVertexCurSegment1.size();j++)
            {
                segments.get(0).allVertexList.add(emptyVertexCurSegment1.get(j));
            }*/

        }
        else
        {
            //experimental part of my code
            int firstCheckedInd = -1;
            int secondCheckedInd = -1;
            for(int i = 0; i < verges.get(verge_num).contactVertexList.size();i++)
            {
                if(verges.get(verge_num).contactVertexList.get(i) == first_contact_point) firstCheckedInd = i;
                if(verges.get(verge_num).contactVertexList.get(i) == last_contact_point) secondCheckedInd = i;
            }
            if(firstCheckedInd > secondCheckedInd)
            {
                temp.add(last_contact_point);
                temp.add(first_contact_point);
            }
            if(firstCheckedInd < secondCheckedInd)
            {
                temp.add(first_contact_point);
                temp.add(last_contact_point);
            }
            //the end of experimental part of code
            //experimental part of my code
            /*int firstCheckedInd = -1;
            int secondCheckedInd = -1;
            for(int i = 0; i < verges.get(verge_num).contactVertexList.size();i++)
            {
                if(verges.get(verge_num).contactVertexList.get(i) == first_contact_point) firstCheckedInd = i;
                if(verges.get(verge_num).contactVertexList.get(i) == last_contact_point) secondCheckedInd = i;
            }
            if(firstCheckedInd > secondCheckedInd)
            {
                int tmp = first_contact_point;
                first_contact_point = last_contact_point;
                last_contact_point = tmp;
            }*/
            //the end of experimental part of code
            //temp.add(first_contact_point);
            //temp.add(last_contact_point);
            for(int j = 0; j<segments.get(0).edges.size();j++)
            {
                if(segments.get(0).edges.get(j).Ne == first_contact_point &&
                        segments.get(0).edges.get(j).Ke == last_contact_point)
                {
                    resultEdges.add(segments.get(0).edges.get(j));
                    segments.get(0).edges.remove(j);
                    break;
                }
                if(segments.get(0).edges.get(j).Ne == last_contact_point &&
                        segments.get(0).edges.get(j).Ke == first_contact_point)
                {
                    resultEdges.add(segments.get(0).edges.get(j));
                    segments.get(0).edges.remove(j);
                    break;
                }
            }
            //создаем новую грань, изменяем текущую
            ArrayList<Integer> tempDel = new ArrayList<>();
            int firstInd = -1;int lastInd= -1;
            for(int i = 0; i < verges.get(verge_num).contactVertexList.size();i++)
            {
                if(verges.get(verge_num).contactVertexList.get(i) == first_contact_point) firstInd = i;
                if(verges.get(verge_num).contactVertexList.get(i) == last_contact_point) lastInd = i;
            }
            //int flagForCorrectAdditing = 0;
            if(firstInd>lastInd)
            {
                int tmp;
                tmp = firstInd;
                firstInd = lastInd;
                lastInd = tmp;
                //flagForCorrectAdditing = 1;
            }
            for(int i = 0; i< verges.get(verge_num).contactVertexList.size();i++)
            {
                if(i>firstInd&& i<lastInd)
                {
                    tempDel.add(verges.get(verge_num).contactVertexList.get(i));
                    //tempDel.add(0,verges.get(verge_num).contactVertexList.get(i));
                }
            }
            ArrayList<Integer> newhuynya = new ArrayList<>();
            for(int i = 0; i< verges.get(verge_num).contactVertexList.size();i++)
            {
                newhuynya.add(verges.get(verge_num).contactVertexList.get(i));
            }
            newhuynya.subList(firstInd+1,lastInd).clear();
            verges.get(verge_num).contactVertexList = newhuynya;

            for(int i = 1;i<temp.size()-1;i++)
            {
                //добавляет в оба списка и verge_num и второй
                //verges.get(verge_num).contactVertexList.add(firstInd+1,temp.get(i));
                ArrayList<Integer> newarrayhuynya = new ArrayList<>();
                for(int j = 0; j< verges.get(verge_num).contactVertexList.size();j++)// verges.get(verge_num).contactVertexList;
                {
                    newarrayhuynya.add(verges.get(verge_num).contactVertexList.get(j));
                }
                newarrayhuynya.add(firstInd+1,temp.get(i));
                //if(flagForCorrectAdditing == 0) firstInd++;
                verges.get(verge_num).contactVertexList = newarrayhuynya;
                verges.get(verge_num).numberVertex++;
                //System.out.print("ADDSHIT");
            }
            //cоздали новую грань
            verges.add(new verge(average.size()+2,temp));
            //вставили промежуточные вершины на свои месте без изменений
            int newPosition = 1;
            for(int i = 0; i< tempDel.size();i++)
            {
                verges.get(verges.size()-1).contactVertexList.add(newPosition,tempDel.get(i));//could be bad
                //verges.get(verges.size()-1).contactVertexList.add(tempDel.get(i));
                newPosition++;
            }
            System.out.println();
            for(int j = 0; j < verges.size();j++)
            {
                System.out.print(" verge "+j+" ");
                for(int i = 0; i< verges.get(j).contactVertexList.size();i++)
                {
                    System.out.print(" "+verges.get(j).contactVertexList.get(i));
                }
                System.out.println();
            }
            ArrayList<Integer> emptyVertexCurSegment1 = new ArrayList<>();
            emptyVertexCurSegment1.clear();
            for(int j = 0; j< segments.get(0).contactVertexList.size();j++)
            {
                int flag5 = 0;
                for(int k = 0;k< segments.get(0).edges.size();k++)
                {
                    if(segments.get(0).contactVertexList.get(j) == segments.get(0).edges.get(k).Ne||
                            segments.get(0).contactVertexList.get(j) == segments.get(0).edges.get(k).Ke)
                    {
                        flag5 = 1;
                    }
                }
                if(flag5 == 0)
                {
                    emptyVertexCurSegment1.add(segments.get(0).contactVertexList.get(j));
                }
            }
            for(int j = 0; j<segments.get(0).contactVertexList.size();j++)
            {
                for(int w = 0; w< emptyVertexCurSegment1.size();w++)
                {
                    if(segments.get(0).contactVertexList.get(j) == emptyVertexCurSegment1.get(w))
                    {
                        segments.get(0).contactVertexList.remove(j);
                    }
                }
            }
            segments.get(0).contactVertexList.removeAll(emptyVertexCurSegment1);
            emptyVertexCurSegment1.clear();
            //тоже самое для всех вершин
            for(int j = 0; j< segments.get(0).allVertexList.size();j++)
            {
                int flag5 = 0;
                for(int k = 0;k< segments.get(0).edges.size();k++)
                {
                    if(segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ne ||
                            segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ke)
                    {
                        flag5 = 1;
                    }
                    /*if (segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ne
                            && !emptyVertexCurSegment1.contains(segments.get(0).allVertexList.get(j)))
                    {
                        emptyVertexCurSegment1.add(segments.get(0).allVertexList.get(j));
                    }
                    if (segments.get(0).allVertexList.get(j) == segments.get(0).edges.get(k).Ke
                            && !emptyVertexCurSegment1.contains(segments.get(0).allVertexList.get(j)))
                    {
                        emptyVertexCurSegment1.add(segments.get(0).allVertexList.get(j));
                    }*/
                }
                if(flag5 == 0)
                {
                    emptyVertexCurSegment1.add(segments.get(0).allVertexList.get(j));
                }
            }
            /*for(int j = 0; j<segments.get(0).allVertexList.size();j++)
            {
                for(int w = 0; w< emptyVertexCurSegment1.size();w++)
                {
                    if(segments.get(0).allVertexList.get(j) == emptyVertexCurSegment1.get(w))
                    {
                        segments.get(0).allVertexList.remove(emptyVertexCurSegment1.get(w));
                    }
                }
            }*/
            segments.get(0).allVertexList.removeAll(emptyVertexCurSegment1);
            /*segments.get(0).allVertexList.clear();
            for (int j = 0 ;j<emptyVertexCurSegment1.size();j++)
            {
                segments.get(0).allVertexList.add(emptyVertexCurSegment1.get(j));
            }*/
        }
    }

    public edge[] makeSimpleCircle()
    {
        int firstPoint,lastPoint,currentPoint,tmp,flag,count;
        edge[] resultE = new edge[0];
        circle[] example = new circle[numVertex];
        ArrayList<edge> exampleEdges = new ArrayList<>();
        for(int i = 0;i<numVertex;i++)
        {
            example[i] = new circle();
        }
        example[0].num = 0;
        example[0].myFather = 0;
        firstPoint = 1;
        currentPoint = 1;
        tmp = 1;
        flag = 0;
        count = 0;
        lastPoint = -1;
        while(flag!=1)//while not find intersepcion
        {
            for (int i = 0; i < numEdges; i++) {
                if (gammaGraph.edgesMass[i].Ne == currentPoint) {
                    if ((example[gammaGraph.edgesMass[i].Ke - 1].num != -1) &&
                            (example[gammaGraph.edgesMass[i].Ne - 1].myFather !=
                                    gammaGraph.edgesMass[i].Ke))
                    {
                        lastPoint = gammaGraph.edgesMass[i].Ke;
                        firstPoint = example[lastPoint-1].myFather;
                        //firstPoint = gammaGraph.edgesMass[i].Ke;
                        //firstPoint = gammaGraph.edgesMass[i].Ne;//cbbad
                        flag = 1;
                        break;
                    }
                    if (example[gammaGraph.edgesMass[i].Ne - 1].myFather == gammaGraph.edgesMass[i].Ke) continue;
                    example[gammaGraph.edgesMass[i].Ke - 1].num = tmp;
                    example[gammaGraph.edgesMass[i].Ke - 1].myFather = currentPoint;
                }
                if (gammaGraph.edgesMass[i].Ke == currentPoint) {
                    if ((example[gammaGraph.edgesMass[i].Ne - 1].num != -1) &&
                            (example[gammaGraph.edgesMass[i].Ke - 1].myFather
                                    != gammaGraph.edgesMass[i].Ne)) {
                        lastPoint = gammaGraph.edgesMass[i].Ne;
                        firstPoint = example[lastPoint-1].myFather;//cbbad
                        flag = 1;
                        break;
                    }
                    if (example[gammaGraph.edgesMass[i].Ke - 1].myFather == gammaGraph.edgesMass[i].Ne) continue;
                    example[gammaGraph.edgesMass[i].Ne - 1].num = tmp;
                    example[gammaGraph.edgesMass[i].Ne - 1].myFather = currentPoint;
                }
            }
            if (flag == 0) {
                for (int i = 0; i < numVertex; i++) {
                    if (example[i].num == tmp) {
                        currentPoint = i + 1;
                        tmp++;
                        count++;
                        break;
                    }
                }
            }
        }
        if(flag == 1)
        {
            edge[] newEdges = new edge[count+2];
            for(int i  = 0;i<count+2;i++)
            {
                newEdges[i] = new edge();
            }
            newEdges[0].Ke = lastPoint;
            newEdges[0].Ne = firstPoint;
            newEdges[1].Ke = currentPoint;
            newEdges[1].Ne = lastPoint;
            exampleEdges.add(newEdges[0]);
            exampleEdges.add(newEdges[1]);
            int newcurpos = currentPoint;
            for(int i = 2;i<count+2;i++)
            {
                newEdges[i].Ke = currentPoint;
                newEdges[i].Ne = example[currentPoint-1].myFather;
                currentPoint = example[currentPoint-1].myFather;
            }
            while(newcurpos != firstPoint)
            {
                exampleEdges.add(newEdges[exampleEdges.size()]);
                newcurpos = example[newcurpos-1].myFather;
            }
            //resultE = newEdges;
            resultE = new edge[exampleEdges.size()];
            for(int i = 0;i< exampleEdges.size();i++)
            {
                resultE[i] = exampleEdges.get(i);
            }
        }
        circleCount = count;
        for(int i = 0;i<numEdges;i++)
        {
            for(int j = 0; j< resultE.length;j++)
            {
                if(gammaGraph.edgesMass[i].Ne == resultE[j].Ke && gammaGraph.edgesMass[i].Ke == resultE[j].Ne)
                {
                    resultE[j].Ne = gammaGraph.edgesMass[i].Ne;
                    resultE[j].Ke = gammaGraph.edgesMass[i].Ke;
                    continue;
                }
                if(gammaGraph.edgesMass[i].Ke == resultE[j].Ne && gammaGraph.edgesMass[i].Ne == resultE[j].Ke)
                {
                    resultE[j].Ne = gammaGraph.edgesMass[i].Ke;
                    resultE[j].Ke = gammaGraph.edgesMass[i].Ne;
                    continue;
                }
            }
        }
        return resultE;
    }

    public ArrayList<edge> getResultEdges()
    {
        return resultEdges;
    }
}

class circle
{
    public int num;
    public int myFather;
    circle()
    {
        num = -1;
        myFather = -1;
    }
}

class verge
{
    public int numberVertex;
    public ArrayList<Integer> contactVertexList;
    verge(int num, ArrayList<Integer> cont)
    {
        numberVertex = num;
        contactVertexList = cont;
    }
}

class segment
{
    public ArrayList<Integer> contactVertexList;
    public ArrayList<Integer> allVertexList;
    public ArrayList<edge> edges;
    public ArrayList<verge> acceptedVerge;
    segment(ArrayList<Integer> cV, ArrayList<Integer> v, ArrayList<edge> edg, ArrayList<verge> accV)
    {
        contactVertexList = cV;
        allVertexList = v;
        edges = edg;
        acceptedVerge = accV;
    }

}

class neighbor
{
    public int name;
    public ArrayList<Integer> nonContactNeighbors;
    neighbor(int nm)
    {
        name = nm;
        nonContactNeighbors = new ArrayList<>();
    }
}

class Queue {
    private final int SIZE = 1120;
    private int[] queArray;
    private int front;
    private int rear;

    public Queue() {
        queArray = new int[SIZE];
        front = 0;
        rear = -1;
    }

    public void insert(int j) // put item at rear of queue
    {
        if (rear == SIZE - 1)
            rear = -1;
        queArray[++rear] = j;
    }

    public int remove() // take item from front of queue
    {
        int temp = queArray[front++];
        if (front == SIZE)
            front = 0;
        return temp;
    }

    public boolean isEmpty() // true if queue is empty
    {
        return (rear + 1 == front || (front + SIZE - 1 == rear));
    }
}

class Vertex {
    public int label; // label
    public boolean wasVisited;

    public Vertex(int l) {
        label = l;
        wasVisited = false;
    }
}

class GraphForSegment {
    private final int MAX_VERTS = 1120;
    public Vertex vertexList[]; // list of vertices
    private int adjMat[][]; // adjacency matrix
    private int nVerts; // current number of vertices
    private Queue theQueue;

    public GraphForSegment() {
        vertexList = new Vertex[MAX_VERTS];
        // adjacency matrix
        adjMat = new int[MAX_VERTS][MAX_VERTS];
        nVerts = 0;
        for (int j = 0; j < MAX_VERTS; j++)
            // set adjacency
            for (int k = 0; k < MAX_VERTS; k++)
                // matrix to 0
                adjMat[j][k] = 0;
        theQueue = new Queue();
    }

    public void addVertex(int l) {
        vertexList[nVerts++] = new Vertex(l);
    }

    public void addEdge(int start, int end) {
        adjMat[start][end] = 1;
        adjMat[end][start] = 1;
    }

    public void displayVertex(int v) {
        System.out.print(vertexList[v].label+" ");

    }

    // breadth-first search
    public void bfs(ArrayList<Integer> path, int firtsContact)
    { // begin at vertex 0
        vertexList[firtsContact].wasVisited = true; // mark it
        displayVertex(firtsContact); // display it
        path.add(vertexList[firtsContact].label);
        theQueue.insert(firtsContact); // insert at tail
        int v2;

        while (!theQueue.isEmpty()) // until queue empty,
        {
            int v1 = theQueue.remove(); // remove vertex at head
            // until it has no unvisited neighbors
            while ((v2 = getAdjUnvisitedVertex(v1)) != -1) { // get one,
                vertexList[v2].wasVisited = true; // mark it
                displayVertex(v2); // display it
                path.add(vertexList[v2].label);
                theQueue.insert(v2); // insert it
            }
        }
        // queue is empty, so we're done
        for (int j = 0; j < nVerts; j++)
            // reset flags
            vertexList[j].wasVisited = false;
    }

    // returns an unvisited vertex adj to v
    public int getAdjUnvisitedVertex(int v) {
        for (int j = 0; j < nVerts; j++)
            if (adjMat[v][j] == 1 && vertexList[j].wasVisited == false)
                return j;
        return -1;
    }
}
