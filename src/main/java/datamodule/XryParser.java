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

    public HashMap<String, String> solanBaseStringAttributes(String realPath) {
        HashMap<String, String> attr = new HashMap<>();
        try {
            String path = realPath;

            //path = path.substring(path.indexOf("\\storage")).replace("\\", "/");


            String solanCase = path.substring(path.indexOf("/cases/") + 7);
            String caseName = solanCase.substring(0, solanCase.indexOf("/"));

            String solanDevice = path.substring(path.indexOf("/devices/") + 9);

            String deviceName = solanDevice.substring(solanDevice.indexOf("/") + 1);
            deviceName = deviceName.substring(0, deviceName.indexOf("/"));

            String poi = "Device Owner";
            attr.put("solan_device", deviceName);
            attr.put("solan_case", caseName);
            attr.put("solan_poi", poi);
            attr.put("solan_path", path);

        } catch (Exception e) {
            System.out.println("Solan Base attribute extracting error ");
        }

        return attr;
    }

    public HashMap fillSolanJason(HashMap json, boolean isFile){
        HashMap<String, String> attr = solanBaseStringAttributes(_filePath);

        if(attr != null && attr.size()>0)
        {
            attr.put("solan_device", attr.get("solan_device"));
            attr.put("solan_case", attr.get("solan_case"));
            attr.put("solan_poi", attr.get("solan_poi"));

            if(isFile)
            {
                attr.put("solan_path", attr.get("solan_path"));
            }
        }

        return json;
    }
}
