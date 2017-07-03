package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 7/3/2017.
 */
public class CalendarConfiguration {
    public HashMap fieldsMap;

    public CalendarConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();
        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("subject");
        fieldsMap.put("Subject", arrayList1);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("source");
        fieldsMap.put("Related Application", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("start");
        arrayList3.add("solan_context_time");
        arrayList3.add("start_date");
        fieldsMap.put("Start",arrayList3);


        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("repeat_type");
        fieldsMap.put("Categories",arrayList4);

        ArrayList arrayList5 = new ArrayList();
        arrayList5.add("end");
        fieldsMap.put("End",arrayList5);

        ArrayList arrayList6 = new ArrayList();
        arrayList6.add("subject");
        arrayList6.add("solan_text_content");
        fieldsMap.put("Subject",arrayList6);

        ArrayList arrayList7 = new ArrayList();
        arrayList7.add("description");
        fieldsMap.put("Description",arrayList7);

        ArrayList arrayList8 = new ArrayList();
        arrayList8.add("repeat_every");
        fieldsMap.put("Repeat",arrayList8);
    }
}
