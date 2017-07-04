package datamodule;

import configuration.ConfigurationManager;
import dataconfiguration.EmailConfiguration;
import dataconfiguration.LocationConfiguration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by henb on 7/4/2017.
 */
public class LocationParser extends XryParser {
    public LocationParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().location_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        try {
            _jsonDocument.put("solan_type", "location");
            _jsonDocument.put("solan_subtype", "phone_location");
        } catch (Exception e) {
            _logger.error(e);
        }
    }

    @Override
    public ArrayList Parse() {
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> mailList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));

            for (String item : mailList) {
                if (item.contains("Related Application")) {
                    HashMap contactJsonDoc = extractMail(item);
                    saveDocToDB(new JSONObject(contactJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private HashMap extractMail(String contact) {
        HashMap jsonContact = new HashMap(_jsonDocument);
        String mailText = textArragment(contact);
        ArrayList<String> mailLines = new ArrayList<>(Arrays.asList(mailText.split("~")));
        LocationConfiguration locationConfiguration = new LocationConfiguration();

        ArrayList<Float> pos = new ArrayList();

        for (String item1 : mailLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));
            try {
                if (line.size() > 1) {
                    String field = line.get(0);
                    String value = line.get(1);

                    ArrayList<String> jsonFields = (ArrayList) locationConfiguration.fieldsMap.get(field);

                    if (jsonFields != null) {
                        for (String item : jsonFields) {
                            if (field.contains("Time")) {
                                String format = "MM/dd/yyyy hh:mm:ss a z";
                                DateTime date = DateTime.parse(value, DateTimeFormat.forPattern(format));
                                jsonContact.put(item.toString(), date.toString());
                            } else if (field.contains("Longitude") || field.contains("Latitude")) {
                                    pos.add(Float.parseFloat(value));
                            } else {
                                jsonContact.put(item, value);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        jsonContact.put("solan_inserted_timestamp", DateTime.now().toString());
        jsonContact.put("solan_position", pos);

        System.out.println(jsonContact);
        return jsonContact;
    }

    private String textArragment(String text) {
        String result = null;

        try {
            result = text.replace("\n", "~");
            result = result.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace("\r", "");
            result = result.replace(" (Device)", "");
        } catch (Exception ex) {
            _logger.error(ex);
        }

        return result;
    }
}
