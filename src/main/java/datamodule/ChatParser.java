package datamodule;

import configuration.ConfigurationManager;
import dataconfiguration.ChatConfiguration;
import dataconfiguration.MessageConfiguration;
import helpers.CallDirection;
import helpers.ChatDirection;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
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
                    saveDocToDB(new JSONObject(msgJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private HashMap extractMessage(String msg) {
        UUID uuid = UUID.randomUUID();
        String doc_id = uuid.toString();
        ChatDirection chatDirection = new ChatDirection();
        HashMap jsonMsg = new HashMap(_jsonDocument);
        String msgtText = textArragment(msg);
        ArrayList<String> msgLines = new ArrayList<>(Arrays.asList(msgtText.split("%")));
        ChatConfiguration chatConfiguration = new ChatConfiguration();

        chatDirection.fillChatDirection(msgLines);
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
                    String chatId = item1.replace(":", "");
                    if (StringUtils.isNumeric(chatId)) {
                        jsonMsg.put("message_id", item1.replace(":", chatId));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (chatDirection.direction != null) {
            jsonMsg.put("number", chatDirection.contactTel);
            jsonMsg.put("identifier", chatDirection.contactTel);
            jsonMsg.put("smsc", chatDirection.contactTel);
            jsonMsg.put("name", chatDirection.contactName);
            jsonMsg.put("identifier_name", chatDirection.contactName);
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
            result = text.replace("\n", "%");
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
