package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 7/5/2017.
 */
public class AppConfiguration {
    public HashMap fieldsMap;

    public AppConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();
        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("name");
        arrayList1.add("solan_text_content");
        fieldsMap.put("Name", arrayList1);

        ArrayList arrayList11 = new ArrayList();
        arrayList11.add("value");
        arrayList11.add("job");
        fieldsMap.put("Version", arrayList11);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("lastWritten");
        arrayList2.add("collectionTime");
        fieldsMap.put("Purchased", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("type");
        fieldsMap.put("Category",arrayList3);
    }
}
