package datamodule;

import configuration.ConfigurationManager;
import dataconfiguration.CalendarConfiguration;
import dataconfiguration.EmailConfiguration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by henb on 7/4/2017.
 */
public class EmailParser extends XryParser {
    public EmailParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().email_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        try {
            _jsonDocument.put("solan_type", "mail");
            _jsonDocument.put("solan_subtype", "msg");
        } catch (Exception e) {
            _logger.error(e);
        }
    }

    @Override
    public ArrayList Parse() {
        ArrayList result = new ArrayList();

        String fileTextContent = readFileText(new File(_filePath));

        if (fileTextContent != null) {
            ArrayList<String> mailList = new ArrayList<>(Arrays.asList(fileTextContent.split("#")));

            for (String item : mailList) {
                if (item.contains("MIME Type")) {
                    HashMap contactJsonDoc = extractMail(item);
                    saveDocToDB(new JSONObject(contactJsonDoc).toString());
                }
            }
        }

        return result;
    }

    private HashMap extractMail(String contact) {
        HashMap jsonContact = new HashMap(_jsonDocument);
        String mailText = textArragment(contact);
        ArrayList<String> mailLines = new ArrayList<>(Arrays.asList(mailText.split("~")));
        EmailConfiguration emailConfiguration = new EmailConfiguration();

        ArrayList toEmails = new ArrayList();
        ArrayList fromEmails = new ArrayList();

        for (String item1 : mailLines) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item1.split("::")));
            try {
                if (line.size() > 1) {
                    String field = line.get(0);
                    String value = line.get(1);

                    ArrayList<String> jsonFields = (ArrayList) emailConfiguration.fieldsMap.get(field);

                    if (jsonFields != null) {
                        for (String item : jsonFields) {
                            if (field.contains("Received") || field.contains("Sent")) {
                                String format = "MM/dd/yyyy hh:mm:ss a z";
                                DateTime date = DateTime.parse(value, DateTimeFormat.forPattern(format));
                                jsonContact.put(item.toString(), date.toString());
                            } else if (jsonFields.contains("test")) {
                                if (field.contains("From")) {
                                    fromEmails.add(value);
                                } else {
                                    toEmails.add(value);
                                }
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
        jsonContact.put("from", fromEmails);
        jsonContact.put("to", toEmails);

        String subject = jsonContact.get("subject").toString();
        if (subject != null) {
            subject = subject.replace("RE:","").replace("FWD:","").toLowerCase();
            try {
                jsonContact.put("thread_topic", signSHA(subject));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        System.out.println(jsonContact);
        return jsonContact;
    }

    private String textArragment(String text) {
        String result = null;

        try {
            result = text.replace("From\r\n", "From");
            result = result.replace("To\r\n", "To");
            result = result.replace("\n", "~");
            result = result.replace("\t\t\t", ":");
            result = result.replace("\t\t", ":");
            result = result.replace("\t", ":");
            result = result.replace("\r", "");
        } catch (Exception ex) {
            _logger.error(ex);
        }

        return result;
    }
}
