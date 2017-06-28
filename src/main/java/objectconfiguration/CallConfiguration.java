package objectconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 6/28/2017.
 */
public class CallConfiguration {
    public HashMap fieldsMap;

    public CallConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();
        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("solan_subtype");
        fieldsMap.put("Call Type", arrayList1);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("source");
        fieldsMap.put("Related Application", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("timestamp");
        arrayList3.add("solan_context_time");
        fieldsMap.put("Time",arrayList3);


        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("duration");
        fieldsMap.put("Duration",arrayList4);

        ArrayList arrayList5 = new ArrayList();
        arrayList5.add("network_type");
        fieldsMap.put("Network Type",arrayList5);

        ArrayList arrayList6 = new ArrayList();
        arrayList6.add("identifier");
        fieldsMap.put("Tel",arrayList6);

        ArrayList arrayList7 = new ArrayList();
        arrayList7.add("country");
        fieldsMap.put("Country",arrayList7);

        ArrayList arrayList8 = new ArrayList();
        arrayList8.add("name");
        fieldsMap.put("Name",arrayList8);

        ArrayList arrayList9 = new ArrayList();
        arrayList9.add("call_id");
        fieldsMap.put("Viber ID",arrayList9);

        ArrayList arrayList10 = new ArrayList();
        arrayList10.add("call_id");
        fieldsMap.put("WeChat ID",arrayList10);

        ArrayList arrayList11 = new ArrayList();
        arrayList11.add("related_account");
        fieldsMap.put("Related Account",arrayList11);
    }
}
