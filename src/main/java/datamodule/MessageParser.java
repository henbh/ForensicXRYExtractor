package datamodule;

import configuration.ConfigurationManager;
import dataconfiguration.MessageConfiguration;
import helpers.CallDirection;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by henb on 6/29/2017.
 */
public class MessageParser extends XryParser {
    public MessageParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().message_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        try {
            _jsonDocument.put("solan_type", "sms_message");
        } catch (Exception e) {
            _logger.error(e);
        }
    }

    @Override
    public ArrayList Parse() {
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> msgList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));

            for (String item : msgList) {
                if (item.contains("Related Application")) {
                    HashMap msgJsonDoc = extractMessage(item);
                    saveDocToDB(new JSONObject(msgJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private HashMap extractMessage(String msg) {
        CallDirection callDirection = new CallDirection();
        HashMap jsonMsg = new HashMap(_jsonDocument);
        String msgtText = textArragment(msg);
        ArrayList<String> msgLines = new ArrayList<>(Arrays.asList(msgtText.split("%")));
        MessageConfiguration messageConfiguration = new MessageConfiguration();

        callDirection.fillCallDirection(msgLines);

        for (String item1 : msgLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));

            try {
                if (line.size() > 1) {
                    String field = line.get(0);
                    String value = line.get(1);

                    ArrayList<String> jsonFields = (ArrayList) messageConfiguration.fieldsMap.get(field);

                    if (jsonFields != null) {
                        for (String item : jsonFields) {
                            if (field.contains("Time")) {
                                String format = "MM/dd/yyyy hh:mm:ss a z";
                                DateTime date = DateTime.parse(value, DateTimeFormat.forPattern(format));
                                jsonMsg.put(item.toString(), date.toString());
                            } else {
                                jsonMsg.put(item, value);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(callDirection.direction != null)
        {
            jsonMsg.put("number",callDirection.contactTel);
            jsonMsg.put("identifier",callDirection.contactTel);
            jsonMsg.put("smsc",callDirection.contactTel);
            jsonMsg.put("name", callDirection.contactName);
            jsonMsg.put("identifier_name", callDirection.contactName);
        }

        jsonMsg.put("solan_inserted_timestamp", DateTime.now().toString());

        System.out.println(jsonMsg);
        return jsonMsg;
    }

    private String textArragment(String text) {
        String result = null;

        try {
            result = text.replace("\n", "%");
            result = result.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace("\r", "");
            result = result.replace(" (Device)", "");
            result = result.replace("%From%Tel", "%FromTel");
            result = result.replace("%To%Tel", "%ToTel");
        } catch (Exception ex) {
            _logger.error(ex);
        }

        return result;
    }

}
