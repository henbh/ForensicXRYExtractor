package datamodule;

import Interfaces.IXryParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.ConfigurationManager;
import enums.eParser;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class XryParser implements IXryParser {
    public String _filePath;
    public Logger _logger;
    public HashMap _jsonDocument;

    public XryParser(String filePath, Logger logger) {
        _filePath = filePath;
        _logger = logger;
        _jsonDocument = new HashMap();
    }

    @Override
    public ArrayList Parse() {
        return null;
    }

    public void saveDocToDB(String doc) {
        if (ConfigurationManager.getInstance().is_logstach) {
            saveToLogStash(doc);
        } else {
            saveToElastic(doc);
        }
    }

    private void saveToLogStash(String doc) {
        try {
            System.out.println("Entered sendPostTest at " + LocalDateTime.now().toString());
            CloseableHttpClient httpclient = HttpClients.createDefault();

            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(ConfigurationManager.getInstance().ls_ip)
                    .setPort(Integer.parseInt(ConfigurationManager.getInstance().ls_port))
                    .build();

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(
                    new StringEntity(
                            doc,
                            ContentType.create("application/json", Consts.UTF_8)));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }

        } catch (Exception ex) {
            _logger.error("Save doc to LS - Failled!!!!", ex);
        }
    }

    private void saveToElastic(String doc) {
        try {
            URL url = new URL(
                    String.format(
                            "%s/%s/%s",
                            ConfigurationManager.getInstance().es_url,
                            ConfigurationManager.getInstance().es_index,
                            ConfigurationManager.getInstance().es_type
                    )
            );
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(doc);
            out.close();
            if (httpCon.getResponseCode() != 200 && httpCon.getResponseCode() != 201)
                System.out.printf(
                        "Failed to post document to ES. URL: %s\n document: %s\n",
                        url.toString(),
                        doc
                );
        } catch (Exception ex) {
            _logger.error(ex);
        }
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
            _logger.info(file.getPath().toString()+" File not exist!");
        }
        return fileContent;
    }

    public HashMap readJsonObject(String fileName) {
        HashMap result = new HashMap();

        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(fileName);
        if(file.exists()) {
            try {
                String contenetFile = readFileText(file);
                HashMap map = new ObjectMapper().readValue(contenetFile, HashMap.class);
                result = map;
            } catch (Exception ex) {
                _logger.error("Error in readJsonObject Method :: ", ex);
                result = null;
            }
        }
        else{
            _logger.error("Calls json file not exist!");
        }

        return result;
    }

    public HashMap<String, String> solanBaseStringAttributes(String realPath) {
        HashMap<String, String> attr = new HashMap<>();
        try {
            String path = realPath;

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

    public HashMap fillSolanJason(HashMap json, boolean isFile) {
        HashMap<String, String> attr = solanBaseStringAttributes(_filePath);

        if (attr != null && attr.size() > 0) {
            attr.put("solan_device", attr.get("solan_device"));
            attr.put("solan_case", attr.get("solan_case"));
            attr.put("solan_poi", attr.get("solan_poi"));

            if (isFile) {
                attr.put("solan_path", attr.get("solan_path"));
            }
        }

        json.put("solan_device", attr.get("solan_device"));
        json.put("solan_case", attr.get("solan_case"));
        json.put("solan_poi", attr.get("solan_poi"));

        return json;
    }
}
