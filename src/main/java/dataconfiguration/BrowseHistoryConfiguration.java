package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 7/3/2017.
 */
public class BrowseHistoryConfiguration {
    public HashMap fieldsMap;

    public BrowseHistoryConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();
        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("url");
        arrayList1.add("solan_text_content");
        fieldsMap.put("Web Address", arrayList1);

        arrayList1.add("url");
        arrayList1.add("solan_text_content");
        fieldsMap.put("Related URL", arrayList1);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("browser");
        fieldsMap.put("Related Application", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("lastAccessed");
        arrayList3.add("solan_context_timestamp");
        fieldsMap.put("Accessed",arrayList3);

        ArrayList arrayList99 = new ArrayList();
        arrayList99.add("lastAccessed");
        fieldsMap.put("Last Visit",arrayList99);

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("lastAccessed");
        fieldsMap.put("Time",arrayList4);

        ArrayList arrayList5 = new ArrayList();
        arrayList5.add("visitCount");
        fieldsMap.put("Access Count",arrayList5);

        ArrayList arrayList51 = new ArrayList();
        arrayList51.add("lastAccessed");
        fieldsMap.put("Created",arrayList51);

        ArrayList arrayList111 = new ArrayList();
        arrayList111.add("url");
        arrayList111.add("solan_text_content");
        fieldsMap.put("Domain", arrayList111);
    }
}
