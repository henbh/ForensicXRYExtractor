package datamodule;

import Interfaces.IXryParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import enums.eParser;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class XryParser implements IXryParser {
    public String _filePath;
    public Logger _logger;

    public XryParser(String filePath, Logger logger) {
        _filePath = filePath;
        _logger = logger;
    }

    @Override
    public void Parse() {

    }

    public String readFileText(File file) {
        String fileContent = null;

        if (file.exists()) {
            try {
                String content = FileUtils.readFileToString(file, "utf-8");
                fileContent = content;
            } catch (Exception ex) {
                _logger.error("Error in readFileText Method", ex);
                fileContent = null;
            }
        } else {
            fileContent = null;
            _logger.info("%s File not exist!");
        }
        return fileContent;
    }

    public HashMap readJsonObject(String fileName) {
        HashMap result = new HashMap();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
            String contenetFile = readFileText(file);
            HashMap map = new ObjectMapper().readValue(contenetFile, HashMap.class);
            result = map;
        } catch (Exception ex) {
            _logger.error("Error in readJsonObject Method :: ", ex);
            result = null;
        }

        return result;
    }
}
