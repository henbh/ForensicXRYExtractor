package datamodule;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CallParser extends XryParser {
    public CallParser(String filePath, Logger logger) {
        super(filePath, logger);
    }

    @Override
    public void Parse() {
        HashMap jsonObject = readJsonObject("call.json");
        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));
            for (String item : arrayList) {
                if(item.contains("Index"))
                {
                    ArrayList<String>callList = new ArrayList<>(Arrays.asList(item.split("\n")));
                }
                System.out.println(item);
            }
        }
    }

}
