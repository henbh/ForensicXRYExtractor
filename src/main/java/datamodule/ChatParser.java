package datamodule;

import configuration.ConfigurationManager;
import dataconfiguration.ChatConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by henb on 7/2/2017.
 */
public class ChatParser extends XryParser {
    public ChatParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().chat_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        try {
            _jsonDocument.put("solan_type", "chat");
        } catch (Exception e) {
            _logger.error(e);
        }
    }

    @Override
    public ArrayList Parse() {
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> chatList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));

            for (String item : chatList) {
                if (item.contains("Related Application")) {
                    HashMap msgJsonDoc = extractMessage(item);

                    ArrayList<String> paths = new ArrayList<>();
                    paths = (ArrayList<String>) msgJsonDoc.get("attachment_full_path");

                    String doc_id = msgJsonDoc.get("doc_id").toString();
                    String chat_id = msgJsonDoc.get("chat_id").toString();

                    if (paths.size() > 0) {
                        ArrayList<String> pathToTika = tikaPaths(doc_id, chat_id);

                        for (String item1 : pathToTika) {

                            result.add(item1);
                        }
                    }

                    saveDocToDB(new JSONObject(msgJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private ArrayList<String> tikaPaths(String doc_id, String folderName) {
        ArrayList<String> result = new ArrayList<>();

        String chatFolder = createFolderPath(folderName);

        File folder = new File(chatFolder);
        if(folder.exists()) {
            for (File file : folder.listFiles()) {
                String tika = String.format("%s@%s", file.getPath(), doc_id);
                result.add(tika);
                System.out.println("Send File To Tika :: "+tika);
            }
        }
        return result;
    }

    private String createFolderPath(String folderName) {
        String res = null;

        File f = new File(_filePath);
        String extension = FilenameUtils.getExtension(f.getName());
        String folderType = f.getName().replace(extension,"").replace(".","")+" #";
        res = _filePath.replace("."+extension, "") + "//" + folderType+folderName + "//";

        return res;
    }

    private HashMap extractMessage(String msg) {
        UUID uuid = UUID.randomUUID();
        String doc_id = uuid.toString();
        HashMap jsonMsg = new HashMap(_jsonDocument);
        String msgtText = textArragment(msg);
        ArrayList<String> msgLines = new ArrayList<>(Arrays.asList(msgtText.split("%")));
        ChatConfiguration chatConfiguration = new ChatConfiguration();

        ArrayList relatedURL = new ArrayList();
        ArrayList filePaths = new ArrayList();

        for (String item1 : msgLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));

            try {
                if (line.size() > 1) {
                    String field = line.get(0);
                    String value = line.get(1);

                    ArrayList<String> jsonFields = (ArrayList) chatConfiguration.fieldsMap.get(field);

                    if (jsonFields != null) {
                        for (String item : jsonFields) {
                            if (field.contains("Time")) {
                                String format = "MM/dd/yyyy hh:mm:ss a z";
                                DateTime date = DateTime.parse(value, DateTimeFormat.forPattern(format));
                                jsonMsg.put(item.toString(), date.toString());
                            } else if (field.contains("Related URL")) {
                                relatedURL.add(value);
                            } else if (field.contains("Path")) {
                                filePaths.add(value);
                            } else {
                                jsonMsg.put(item, value);
                            }
                        }
                    }
                } else {
                    if (item1.contains(":")) {
                        String chatId = item1.replace(":", "");
                        chatId = chatId.replace(":", chatId);
                        if (StringUtils.isNumeric(chatId)) {
                            jsonMsg.put("chat_id", chatId);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String extraData = extractContactInfo(msgLines);
        if (extraData != null) {
            jsonMsg.put("party_id", extraData);
            jsonMsg.put("identifier", extraData);
        }

        jsonMsg.put("related_url", relatedURL);
        jsonMsg.put("attachment_full_path", filePaths);
        jsonMsg.put("solan_inserted_timestamp", DateTime.now().toString());
        jsonMsg.put("doc_id", doc_id);
        System.out.println(jsonMsg);
        return jsonMsg;
    }

    private String textArragment(String text) {
        String result = null;

        try {
            result = text.replace("From\r\n", "From");
            result = result.replace("To\r\n", "To");
            result = result.replace("\r\n", "%");
            result = result.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace(": ", "::");
            result = result.replace("\r", "");
            result = result.replace(" (Device)", "");
            result = result.replace(" (Network)", "");
        } catch (Exception ex) {
            _logger.error(ex);
        }

        return result;
    }

    private String extractContactInfo(ArrayList<String> chat) {

        HashMap hashMap = new HashMap();
        String result = null;
        try {
            for (String item : chat) {
                ArrayList<String> line = new ArrayList<>(Arrays.asList(item.split("::")));

                if (line.size() > 1) {
                    String field = line.get(0);
                    String value = line.get(1);

                    if (field.contains("Direction")) {
                        hashMap.put("Direction", value);
                    }

                    if (field.contains("ID") && field.contains("From")) {
                        hashMap.put("from_id", value);
                    }

                    if (field.contains("ID") && field.contains("To")) {
                        hashMap.put("to_id", value);
                    }
                }
            }

            if (hashMap.get("Direction") != null) {
                if (hashMap.get("Direction").toString().contains("Incoming")) {
                    hashMap.remove("to_id");
                    if (hashMap.get("from_id") != null) {
                        result = hashMap.get("from_id").toString();
                    }
                }
                if (hashMap.get("Direction").toString().contains("Outgoing")) {
                    hashMap.remove("from_id");
                    if (hashMap.get("to_id") != null) {
                        result = hashMap.get("to_id").toString();
                    }
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
            result = null;
        }
        return result;
    }

}
