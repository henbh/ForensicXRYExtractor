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
                if (item.contains("Time")) {
                    HashMap jsonCall = new HashMap(jsonObject);

                    item = item.replace("\n", "@");
                    item = item.replace("\t\t\t", ":");
                    item = item.replace("\t\t", ":");
                    item = item.replace("\t", ":");
                    item = item.replace("\r", "");
                    ArrayList<String> callLineList = new ArrayList<>(Arrays.asList(item.split("@")));
                    for (String item1 : callLineList) {
                        ArrayList<String> rowValue = new ArrayList<>(Arrays.asList(item1.split("::")));
                        if (rowValue.size() > 1) {
                            String field = rowValue.get(0);
                            String value = rowValue.get(1);

                            if (field.contains("Related Application")) {
                                jsonCall.put("source", value);
                            } else if (field.contains("Call Type")) {
                                if (value.contains("Dialed")) {
                                    jsonCall.put("solan_subtype", "Outgoing");
                                } else if (value.contains("Received")) {
                                    jsonCall.put("solan_subtype", "Incoming");
                                } else {
                                    //missed
                                    jsonCall.put("solan_subtype", value);
                                }
                            } else if (field.contains("Time")) {
                                jsonCall.put("solan_context_time", value.replace(" (Device)", ""));
                            } else if (field.contains("Duration")) {
                                jsonCall.put("duration", value);
                            } else if (field.contains("Network Type")) {
                                jsonCall.put("network_type", value);
                            } else if (field.contains("Network Type")) {
                                jsonCall.put("network_type", value);
                            } else if (field.contains("Tel")) {
                                jsonCall.put("number", value);
                            } else if (field.contains("Country")) {
                                jsonCall.put("country", value);
                            } else if (field.contains("Name")) {
                                jsonCall.put("identifier", value);
                                jsonCall.put("name", value);
                            } else if (field.contains("Viber")) {
                                jsonCall.put("call_id", value);
                            } else if (field.contains("WeChat ID")) {
                                jsonCall.put("call_id", value);
                            } else if (field.contains("Related Account")) {
                                jsonCall.put("related_account", value);
                            }
                        }
                    }

                    if (jsonCall.get("solan_subtype") != null && jsonCall.get("solan_subtype") != "") {
                        //insert elastic
                    }
                }
            }
        }

        //ins
    }

}
