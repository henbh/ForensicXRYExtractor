package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 7/4/2017.
 */
public class LogConfiguration {
    public HashMap fieldsMap;

    public LogConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();

        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("solan_text_content");
        arrayList1.add("solan_subtype");
        fieldsMap.put("Package Name",arrayList1);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("source");
        fieldsMap.put("Related Application", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("log_time");
        arrayList3.add("solan_context_timestamp");
        fieldsMap.put("Time",arrayList3);
    }
}
