package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 7/4/2017.
 */
public class LocationConfiguration {
    public HashMap fieldsMap;

    public LocationConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();

        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("pos_coordinates_longitude");
        fieldsMap.put("Longitude",arrayList1);

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("pos_coordinates_latitude");
        fieldsMap.put("Latitude",arrayList4);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("Category");
        arrayList2.add("category");
        fieldsMap.put("Related Application", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("connection_type");
        arrayList3.add("solan_text_content");
        fieldsMap.put("Connection Type",arrayList3);

        ArrayList arrayList31 = new ArrayList();
        arrayList31.add("solan_context_timestamp");
        arrayList31.add("timestamp");
        fieldsMap.put("Time",arrayList31);

        ArrayList arrayList311 = new ArrayList();
        arrayList311.add("mac_address");
        fieldsMap.put("MAC Address",arrayList311);

        ArrayList arrayList3111 = new ArrayList();
        arrayList3111.add("name");
        fieldsMap.put("Location Name",arrayList3111);
    }
}
