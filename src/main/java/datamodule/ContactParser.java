package datamodule;

import configuration.ConfigurationManager;
import objectconfiguration.CallConfiguration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by henb on 6/28/2017.
 */
public class ContactParser extends XryParser {
    public ContactParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().call_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        _jsonDocument.put("solan_type", "contact");
    }

    @Override
    public ArrayList Parse() {
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> contactsList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));

            for (String item : contactsList) {
                if (item.contains("Related Application")) {
                    HashMap contactJsonDoc = extractContact(item);
                    saveDocToDB(new JSONObject(contactJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private HashMap extractContact(String contact) {
        HashMap jsonContact = new HashMap(_jsonDocument);
        String callText = textArragment(contact);
        ArrayList<String> contactLines = new ArrayList<>(Arrays.asList(callText.split("@")));
        CallConfiguration contactConfiguration = new CallConfiguration();

        for (String item1 : contactLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));

            if (line.size() > 1) {
                String field = line.get(0);
                String value = line.get(1);

                ArrayList<String> jsonFields = (ArrayList) contactConfiguration.fieldsMap.get(field);

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

                            jsonCall.put(item.toString(),date.toString());
                        }else {
                            jsonCall.put(item.toString(), value);
                        }
                    }
                }
            }
        }

        jsonCall.put("solan_inserted_timestamp", DateTime.now().toString());

        System.out.println(jsonCall);
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
