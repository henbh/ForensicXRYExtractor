package main;

import org.apache.log4j.Logger;
import streams.BasicStreamTwoWays;

import java.util.ArrayList;

public class XryExtractor  extends BasicStreamTwoWays
{
    public static void main(String[] args){
        BasicStreamTwoWays basicStreamTwoWay = new XryExtractor();

        try {
            basicStreamTwoWay.streamMain();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public ArrayList run(String str, Logger logger)
    {
        ArrayList arrayList = new ArrayList();

        //your logic

        return arrayList;
    }
}
