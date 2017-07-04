package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 7/4/2017.
 */
public class EmailConfiguration {
    public HashMap fieldsMap;

    public EmailConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();

        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("subject");
        fieldsMap.put("Subject",arrayList1);

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("solan_text_content");
        fieldsMap.put("Text",arrayList4);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("source");
        fieldsMap.put("Related Application", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("mail_timestamp");
        arrayList3.add("solan_context_time");
        fieldsMap.put("Sent",arrayList3);

        ArrayList arrayList31 = new ArrayList();
        arrayList31.add("mail_timestamp");
        arrayList31.add("solan_context_time");
        fieldsMap.put("Received",arrayList31);

        ArrayList arrayList311 = new ArrayList();
        arrayList311.add("test");
        fieldsMap.put("FromEmail",arrayList311);

        ArrayList arrayList3111 = new ArrayList();
        arrayList3111.add("test");
        fieldsMap.put("ToEmail",arrayList3111);
    }
}
