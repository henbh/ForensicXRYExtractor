package tests;

import main.XryKafkaStream;

/**
 * Created by henb on 6/28/2017.
 */
public class Tester {
    public static void main(String[] args) {
        XryKafkaStream xryKafkaStream = new XryKafkaStream();
        xryKafkaStream.run("C:\\Dev\\XDR\\Solan\\Files\\iPhone4----AppleiPhone4GSM(A1332)-file\\rdisk0s2\\Messages-Emails.txt",null);
    }
}