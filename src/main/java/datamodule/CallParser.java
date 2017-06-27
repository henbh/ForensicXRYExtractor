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
        jsonObject = fillSolanJason(jsonObject, false);

        if (fileTextContent != null) {
            ArrayList<String> callList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));
            for (String item : callList) {
                if (item.contains("Index")) {
                    item = item.replace("\n","@");
                    item = item.replace("\t\t\t",":");
                    item = item.replace("\t\t",":");
                    item = item.replace("\t",":");
                    item = item.replace("\r","");
                    ArrayList<String> callLineList = new ArrayList<>(Arrays.asList(item.split("@")));
                    for (String item1 : callLineList) {
                        ArrayList<String> rowValue = new ArrayList<>(Arrays.asList(item1.split("::")));
                        if (rowValue.size() > 1) {
                            String field = rowValue.get(0);
                            String value = rowValue.get(1);

                            if (field.contains("Related Application")) {
                                jsonObject.put("source", value);
                            } else if (field.contains("Call Type")) {
                                if(value.contains("Dialed"))
                                {
                                    jsonObject.put("solan_subtype", "Outgoing");
                                }else if(value.contains("Received")) {
                                    jsonObject.put("solan_subtype", "Incoming");
                                }else {
                                    //missed
                                    jsonObject.put("solan_subtype", value);
                                }
                            } else if (field.contains("Time")) {
                                jsonObject.put("solan_context_time", value.replace(" (Device)",""));
                            } else if (field.contains("Duration")) {
                                jsonObject.put("duration", value);
                            } else if (field.contains("Network Type")) {
                                jsonObject.put("network_type", value);
                            }else if (field.contains("Network Type")) {
                                jsonObject.put("network_type", value);
                            }else if (field.contains("Tel")) {
                                jsonObject.put("number", value);
                            }else if (field.contains("Country")) {
                                jsonObject.put("country", value);
                            }
                            else if (field.contains("Name")) {
                                jsonObject.put("identifier", value);
                                jsonObject.put("name", value);
                            }
                        }
                    }

                    if(jsonObject.get("solan_subtype")!= null && jsonObject.get("solan_subtype") != "")
                    {
                        //insert elastic
                    }
                }
            }
        }

        //ins
    }

}
