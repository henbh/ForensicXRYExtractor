package datamodule;

import configuration.ConfigurationManager;
import dataconfiguration.AppConfiguration;
import dataconfiguration.LogConfiguration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by henb on 7/5/2017.
 */
public class AppParser extends XryParser {
    public AppParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().app_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        try {
            _jsonDocument.put("solan_type", "installedApps");
        } catch (Exception e) {
            _logger.error(e);
        }
    }

    @Override
    public ArrayList Parse() {
        System.out.println("------------------Strat Parse------------------------------------------------- ");
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> appList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));

            for (String item : appList) {
                if (item.contains("Package Name")) {
                    HashMap contactJsonDoc = extractLog(item);
                    saveDocToDB(new JSONObject(contactJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private HashMap extractLog(String contact) {
        HashMap jsonContact = new HashMap(_jsonDocument);
        String logText = textArragment(contact);
        ArrayList<String> eventLines = new ArrayList<>(Arrays.asList(logText.split("%")));
        AppConfiguration appConfiguration = new AppConfiguration();

        for (String item1 : eventLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));
            try {
                if (line.size() > 1) {
                    String field = line.get(0);
                    String value = line.get(1);

                    ArrayList<String> jsonFields = (ArrayList) appConfiguration.fieldsMap.get(field);

                    if (jsonFields != null) {
                        for (String item : jsonFields) {
                            if (field.contains("Purchased")) {
                                String date1 = value;
                                if(date1.contains("+"))
                                {
                                    date1 = date1.replace("+08:00","");
                                }

                                String format = "MM/dd/yyyy hh:mm:ss a z";
                                DateTime date = DateTime.parse(date1, DateTimeFormat.forPattern(format));
                                jsonContact.put(item.toString(), date.toString());
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
        jsonContact.put("solan_context_timestamp",DateTime.now().toString());

        System.out.println(jsonContact);
        return jsonContact;
    }

    private String textArragment(String text) {
        String result = null;

        try {
            result = text.replace("\n", "%");
            result = result.replace("\t\t\t\t", ":");
            result = result.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace("\r", "");
            result = result.replace(" (Device)", "");
            result = result.replace(" (Network)", "");
        } catch (Exception ex) {
            _logger.error(ex);
        }

        return result;
    }
}
