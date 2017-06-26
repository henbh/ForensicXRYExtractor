package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import datamodule.XryParser;
import org.apache.log4j.Logger;
import streams.BasicStreamTwoWays;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
        createNewParser();
        return arrayList;
    }

    private XryParser createNewParser() {
        XryParser xryParser = null;

        try {
            String jsonFilePath = new java.io.File("/configuration/file_name.json").getCanonicalPath();
            HashMap<String, Object> result =
                    new ObjectMapper().readValue(readFile(jsonFilePath), HashMap.class);
        } catch (Exception ex) {
            _logger.error("Error in createNewParser Method :: ", ex);
        }

        return xryParser;
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
