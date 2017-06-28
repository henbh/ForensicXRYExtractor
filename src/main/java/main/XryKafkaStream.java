package main;

import org.apache.log4j.Logger;
import streams.BasicStreamTwoWays;

import java.util.ArrayList;

public class XryKafkaStream extends BasicStreamTwoWays {
    public static void main(String[] args) {
        BasicStreamTwoWays basicStreamTwoWay = new XryKafkaStream();

        try {
            basicStreamTwoWay.streamMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList run(String str, Logger logger) {
        ArrayList arrayList = new ArrayList();

        try {
            XryExtractor xryExtractor = new XryExtractor(str, logger);
            arrayList = xryExtractor.execute();
        } catch (Exception ex) {
            logger.error("Error in run method :: %s", ex);
        } finally {
            arrayList.add("");
        }

        return arrayList;
    }
}
