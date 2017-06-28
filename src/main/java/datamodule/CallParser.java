package datamodule;

import configuration.CallConfiguration;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class CallParser extends XryParser {
    public CallParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject("call.json");
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        _jsonDocument.put("solan_type", "call");
    }

    @Override
    public ArrayList Parse() {
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> callsList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));

            for (String item : callsList) {
                if (item.contains("Time")) {
                    HashMap callJsonDoc = extractCall(item);
                    saveToES(new JSONObject(callJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private HashMap extractCall(String call) {
        HashMap jsonCall = new HashMap(_jsonDocument);
        String callText = textArragment(call);
        ArrayList<String> callLines = new ArrayList<>(Arrays.asList(callText.split("@")));
        CallConfiguration callConfiguration = new CallConfiguration();

        for (String item1 : callLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));

            if (line.size() > 1) {
                String field = line.get(0);
                String value = line.get(1);

                ArrayList<String> jsonFields = (ArrayList) callConfiguration.fieldsMap.get(field);

                if (jsonFields != null) {
                    for (String item: jsonFields) {

                        if (item.toString() == "solan_subtype") {
                            if (value.contains("Dialed")) {
                                jsonCall.put(item.toString(), "Outgoing");
                            } else if (value.contains("Received")) {
                                jsonCall.put(item.toString(), "Incoming");
                            } else {
                                //missed
                                jsonCall.put(item.toString(), value);
                            }
                        } else if(field.contains("Time")){
                            String format = "MM/dd/yyyy hh:mm:ss a z";
                            DateTime date = DateTime.parse(value, DateTimeFormat.forPattern(format));

//                            String date = value.replaceAll("[^\\d./: ]", "");
//                            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");

                            jsonCall.put(item.toString(),date.toString());
                        }else {
                            jsonCall.put(item.toString(), value);
                        }
                    }
                }
            }
        }

        jsonCall.put("solan_inserted_timestamp", DateTime.now().toString());

        return jsonCall;
    }

    private String textArragment(String text) {
        String result = null;

        try {
            result = text.replace("\n", "@");
            result = result.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace("\r", "");
            result = result.replace(" (Device)","");
        } catch (Exception ex) {
            _logger.error(ex);
        }

        return result;
    }
}
