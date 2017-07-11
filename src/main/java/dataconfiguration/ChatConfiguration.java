package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 7/2/2017.
 */
public class ChatConfiguration {
    public HashMap fieldsMap;

    public ChatConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();

        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("solan_subtype");
        arrayList1.add("source");
        fieldsMap.put("Related Application",arrayList1);

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("message_id");
        fieldsMap.put("Unique ID",arrayList4);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("party_id");
        arrayList2.add("identifier");
        fieldsMap.put("Facebook ID", arrayList2);

        ArrayList arrayList21 = new ArrayList();
        arrayList21.add("party_id");
        arrayList21.add("identifier");
        fieldsMap.put("Skype ID", arrayList21);

        ArrayList arrayList22 = new ArrayList();
        arrayList22.add("party_id");
        arrayList22.add("identifier");
        fieldsMap.put("Snapchat ID", arrayList22);

        ArrayList arrayList23 = new ArrayList();
        arrayList23.add("party_id");
        arrayList23.add("identifier");
        fieldsMap.put("Telegram ID", arrayList23);

        ArrayList arrayList24 = new ArrayList();
        arrayList24.add("party_id");
        arrayList24.add("identifier");
        fieldsMap.put("WhatsApp ID", arrayList24);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("party_name");
        fieldsMap.put("Name (Matched)",arrayList3);

        ArrayList arrayList7 = new ArrayList();
        arrayList7.add("timestamp");
        arrayList7.add("solan_context_time");
        fieldsMap.put("Time",arrayList7);

        ArrayList arrayList8 = new ArrayList();
        arrayList8.add("solan_text_content");
        arrayList8.add("body");
        fieldsMap.put("Text",arrayList8);

        ArrayList arrayList9 = new ArrayList();
        arrayList9.add("related_url");
        fieldsMap.put("Related URL",arrayList9);

        ArrayList arrayList91 = new ArrayList();
        arrayList91.add("longitude");
        fieldsMap.put("Longitude",arrayList91);

        ArrayList arrayList92 = new ArrayList();
        arrayList92.add("latitude");
        fieldsMap.put("Latitude",arrayList92);

        ArrayList arrayList93 = new ArrayList();
        arrayList93.add("group_name");
        fieldsMap.put("Group Name",arrayList93);

        ArrayList arrayList933 = new ArrayList();
        arrayList933.add("Path");
        fieldsMap.put("Path",arrayList933);

    }
}
