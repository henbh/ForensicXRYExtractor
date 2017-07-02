package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 6/29/2017.
 */
public class MessageConfiguration {
    public HashMap fieldsMap;

    public MessageConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();

        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("identifier");
        arrayList1.add("smsc");
        fieldsMap.put("Name",arrayList1);

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("solan_context_time");
        arrayList4.add("timestamp");
        fieldsMap.put("Time",arrayList4);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("solan_path");
        arrayList2.add("folder");
        fieldsMap.put("Folder", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("solan_text_content");
        arrayList3.add("text");
        fieldsMap.put("Text",arrayList3);

        ArrayList arrayList7 = new ArrayList();
        arrayList7.add("identifier_name");
        arrayList7.add("name");
        fieldsMap.put("Name (Matched)",arrayList7);

        ArrayList arrayList8 = new ArrayList();
        arrayList8.add("uid");
        fieldsMap.put("Unique ID",arrayList8);

        ArrayList arrayList9 = new ArrayList();
        arrayList9.add("status");
        fieldsMap.put("Status",arrayList9);
    }
}
