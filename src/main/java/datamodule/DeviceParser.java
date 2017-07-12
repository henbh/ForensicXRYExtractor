package datamodule;

import configuration.ConfigurationManager;
import dataconfiguration.CallConfiguration;
import dataconfiguration.DeviceConfiguration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DeviceParser extends XryParser {
    public DeviceParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().device_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        try {
            _jsonDocument.put("solan_type", "device");
            _jsonDocument.put("report_type", "cell");
        } catch (Exception e) {
            _logger.error(e);
        }
    }

    @Override
    public ArrayList Parse() {
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            HashMap deviceJsonDoc = extractDevice(fileTextContent);
            saveDocToDB(new JSONObject(deviceJsonDoc).toString());
        }

        return result;
    }

    private HashMap extractDevice(String device) {
        HashMap jsonDevice = new HashMap(_jsonDocument);
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration();
        String deviceContent = textArragment(device);
        ArrayList<String> jsonFields = null;
        ArrayList<String> propList = new ArrayList<>(Arrays.asList(deviceContent.split("#")));

        for (String item : propList) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item.split("@")));

            if (line.size() > 1) {
                String data;
                String attribute;
                try {
                    ArrayList<String> dataArr = new ArrayList<>(Arrays.asList(line.get(1).split("::")));
                    data = dataArr.get(1);
                    ArrayList<String> attArr = new ArrayList<>(Arrays.asList(line.get(2).split("::")));
                    attribute = attArr.get(1);
                    attribute = attribute.substring(0, attribute.length() - 1);
                } catch (Exception e) {
                    continue;
                }

                jsonFields = (ArrayList) deviceConfiguration.fieldsMap.get(attribute);

                if (jsonFields != null) {
                    for (String item1 : jsonFields) {
                        if (attribute.contains("Time")) {
                            String format = "MM/dd/yyyy hh:mm:ss a z";
                            DateTime date = DateTime.parse(data, DateTimeFormat.forPattern(format));

                            jsonDevice.put(item1, date.toString());
                        } else {
                            jsonDevice.put(item1, data);
                        }
                    }
                }


            }
        }

        jsonDevice.put("solan_inserted_timestamp", DateTime.now().toString());
        jsonDevice.put("solan_context_timestamp", DateTime.now().toString());

        System.out.println(jsonDevice);
        return jsonDevice;
    }

    private String textArragment(String text) {
        String result = null;

        try {
            result = text.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace("\r\n", "@");
            result = result.replace(" (Device)", "");
            result = result.replace("@@Device-General Information", "");
        } catch (Exception ex) {
            _logger.error(ex);
        }

        return result;
    }
}
