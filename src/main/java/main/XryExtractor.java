package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.ConfigurationManager;
import datamodule.*;
import enums.eParser;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class XryExtractor {
    private String _filePath;
    private Logger _logger;
    private XryParser _xryParser;

    public XryExtractor(String filePath, Logger logger) {
        _filePath = filePath;
        _logger = logger;
    }

    public ArrayList execute() {
        ArrayList<String> arrayList = new ArrayList<>();
        boolean isParser = createNewParser();
        if (isParser) {
            arrayList = _xryParser.Parse();
        } else {
            arrayList.add("");
        }

        return arrayList;
    }

    private boolean createNewParser() {
        StringBuilder jsonFileContent = new StringBuilder("");
        HashMap<String, String> map = null;
        String fileName = null;
        boolean result = true;
        eParser parserType = eParser.NONE;

        try {
            fileName = new File(_filePath).getName();
            //System.out.println(ConfigurationManager.getInstance().parsers_types_json_path);
//            ClassLoader classLoader = this.getClass().getClassLoader();
            File file = new File(ConfigurationManager.getInstance().parsers_types_json_path);

            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        jsonFileContent.append(line).append("\n");
                    }

                    scanner.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                }

                //System.out.println("---------------" + jsonFileContent);
                //_logger.info("---------------" + jsonFileContent);

                map = new ObjectMapper().readValue(jsonFileContent.toString(), HashMap.class);
            } else {
                _logger.info("parsers_types.json not exist!!!!!!!");
            }
        } catch (Exception ex) {
            map = null;
            result = false;
            _logger.error("Error in createNewParser Method :: ", ex);
        }

        if (map != null) {
            parserType = enums.eParser.valueOf(map.get(fileName));
        }

        switch (parserType) {
            case CALLS:
                _xryParser = new CallParser(_filePath, _logger);
                break;
            case CONTACTS:
                _xryParser = new ContactParser(_filePath, _logger);
                break;
            case DEVICE_INFO:
                _xryParser = new DeviceParser(_filePath, _logger);
                break;
            case SMS:
                _xryParser = new MessageParser(_filePath, _logger);
                break;
            case CHATS:
                _xryParser = new ChatParser(_filePath, _logger);
                break;
            case CALENDAR_EVENTS:
                _xryParser = new CalendarParser(_filePath, _logger);
                break;
            case WEB_HISTORY:
                _xryParser = new BrowseHistoryParser(_filePath, _logger);
                break;
            case EMAILS:
                _xryParser = new EmailParser(_filePath, _logger);
                break;
            case NONE:
                result = false;
                break;
        }

        return result;
    }
}
