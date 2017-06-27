package datamodule;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CallParser extends XryParser {
    public CallParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject("call.json");
        _jsonDocument = fillSolanJason(_jsonDocument, false);
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

    private HashMap extractCall(String call)
    {
        HashMap jsonCall = new HashMap(_jsonDocument);
        String callText = textArragment(call);
        ArrayList<String> callLines = new ArrayList<>(Arrays.asList(callText.split("@")));

        for (String item1 : callLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));

            if (line.size() > 1) {
                String field = line.get(0);
                String value = line.get(1);

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

        return jsonCall;
    }

    private String textArragment(String text)
    {
        String result = null;

        try {
            result = text.replace("\n", "@");
            result = result.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace("\r", "");
        }catch (Exception ex)
        {
            _logger.error(ex);
        }

        return result;
    }
}
