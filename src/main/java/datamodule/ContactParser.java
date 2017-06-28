package datamodule;

import configuration.ConfigurationManager;
import objectconfiguration.CallConfiguration;
import objectconfiguration.ContactConfiguration;
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
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().contact_json_path);
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
        String contactText = textArragment(contact);
        ArrayList<String> contactLines = new ArrayList<>(Arrays.asList(contactText.split("@")));
        ContactConfiguration  contactConfiguration = new ContactConfiguration();

        for (String item1 : contactLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));

            if (line.size() > 1) {
                String field = line.get(0);
                String value = line.get(1);

                ArrayList<String> jsonFields = (ArrayList) contactConfiguration.fieldsMap.get(field);

                if (jsonFields != null) {
                    for (String item: jsonFields) {
                        if(field.contains("Created")){
                            String format = "MM/dd/yyyy hh:mm:ss a z";
                            DateTime date = DateTime.parse(value, DateTimeFormat.forPattern(format));
                            jsonContact.put(item.toString(),date.toString());
                        }else {
                            jsonContact.put(item.toString(), value);
                        }
                    }
                }
            }
        }

        jsonContact.put("solan_inserted_timestamp", DateTime.now().toString());

        System.out.println(jsonContact);
        return jsonContact;
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
