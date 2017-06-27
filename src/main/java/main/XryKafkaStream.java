package main;

import org.apache.log4j.Logger;
import streams.BasicStreamTwoWays;

import java.util.ArrayList;

public class XryKafkaStream extends BasicStreamTwoWays {
    public static void main(String[] args) {
        BasicStreamTwoWays basicStreamTwoWay = new XryKafkaStream();

        try {
            //basicStreamTwoWay.streamMain();
            XryKafkaStream xryKafkaStream = new XryKafkaStream();
            xryKafkaStream.run("C:\\Dev\\XDR\\Solan\\Files\\iPhone4----AppleiPhone4GSM(A1332)-file\\rdisk0s2\\Calls.txt", null);
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
