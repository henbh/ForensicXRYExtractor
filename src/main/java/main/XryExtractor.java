package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import datamodule.CallParser;
import datamodule.XryParser;
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
            _xryParser.Parse();
        } else {
            arrayList.add("");
        }

        return arrayList;
    }

    private boolean createNewParser() {
        XryParser xryParser = null;
        StringBuilder jsonFileContent = new StringBuilder("");
        HashMap<String, String> map;
        String fileName = null;
        boolean result = true;
        eParser parserType = eParser.NONE;

        try {
            fileName = new File(_filePath).getName();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("parsers_types.json").getFile());

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

            map = new ObjectMapper().readValue(jsonFileContent.toString(), HashMap.class);

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
                //_xryParser = new
                break;
            case NONE:
                result = false;
                break;
        }

        return result;
    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }
}
