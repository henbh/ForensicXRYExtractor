package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceConfiguration {
    public HashMap fieldsMap;

    public DeviceConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();

        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("full_name");
        fieldsMap.put("Device Name",arrayList1);

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("device_type");
        fieldsMap.put("Device Family",arrayList4);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("detected_manufacture");
        fieldsMap.put("Manufacturer", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("revision");
        fieldsMap.put("Revision",arrayList3);

        ArrayList arrayList7 = new ArrayList();
        arrayList7.add("detected_model");
        arrayList7.add("selected_model");
        fieldsMap.put("Model",arrayList7);

        ArrayList arrayList8 = new ArrayList();
        arrayList8.add("serial_number");
        fieldsMap.put("Serial Number",arrayList8);

        ArrayList arrayList9 = new ArrayList();
        arrayList9.add("imei");
        fieldsMap.put("Mobile Id (IMEI)",arrayList9);

        ArrayList arrayList10 = new ArrayList();
        arrayList10.add("device_id");
        fieldsMap.put("Unique Device Id",arrayList10);

        ArrayList arrayList11 = new ArrayList();
        arrayList11.add("iccid");
        fieldsMap.put("SIM Identification (ICCID)",arrayList11);

        ArrayList arrayList12 = new ArrayList();
        arrayList12.add("phone_number");
        fieldsMap.put("Number",arrayList12);

        ArrayList arrayList13 = new ArrayList();
        arrayList13.add("owner_name");
        fieldsMap.put("Owner Name",arrayList13);
    }
}
