package dataconfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by henb on 6/28/2017.
 */
public class ContactConfiguration {
    public HashMap fieldsMap;

    public ContactConfiguration() {
        fillFieldsMap();
    }

    private void fillFieldsMap() {
        fieldsMap = new HashMap();

        ArrayList arrayList1 = new ArrayList();
        arrayList1.add("name");
        arrayList1.add("contact_name");
        fieldsMap.put("Name",arrayList1);

        ArrayList arrayList4 = new ArrayList();
        arrayList4.add("created_time");
        arrayList4.add("solan_context_timestamp");
        fieldsMap.put("Created",arrayList4);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("source");
        fieldsMap.put("Related Application", arrayList2);

        ArrayList arrayList3 = new ArrayList();
        arrayList3.add("phone_number");
        fieldsMap.put("Mobile",arrayList3);

        ArrayList arrayList7 = new ArrayList();
        arrayList7.add("phone_number");
        fieldsMap.put("Home",arrayList7);

        ArrayList arrayList8 = new ArrayList();
        arrayList8.add("phone_number");
        fieldsMap.put("Tel",arrayList8);

        ArrayList arrayList9 = new ArrayList();
        arrayList9.add("web");
        fieldsMap.put("Web Address",arrayList9);

        ArrayList arrayList10 = new ArrayList();
        arrayList10.add("email");
        fieldsMap.put("Email",arrayList10);

        ArrayList arrayList11 = new ArrayList();
        arrayList11.add("related_account");
        fieldsMap.put("Related Account",arrayList11);

        ArrayList arrayList12 = new ArrayList();
        arrayList12.add("related_account");
        fieldsMap.put("Line ID",arrayList12);

        ArrayList arrayList13 = new ArrayList();
        arrayList13.add("account_note");
        fieldsMap.put("Note",arrayList13);

        ArrayList arrayList14 = new ArrayList();
        arrayList14.add("account_display_name");
        fieldsMap.put("Display Name",arrayList14);

        ArrayList arrayList15 = new ArrayList();
        arrayList15.add("country");
        fieldsMap.put("Country",arrayList15);

        ArrayList arrayList16 = new ArrayList();
        arrayList16.add("related_account");
        fieldsMap.put("Skype ID",arrayList16);

        ArrayList arrayList17 = new ArrayList();
        arrayList17.add("related_account");
        fieldsMap.put("Skout ID",arrayList17);

        ArrayList arrayList18 = new ArrayList();
        arrayList18.add("related_account");
        fieldsMap.put("Snapchat ID",arrayList18);

        ArrayList arrayList19 = new ArrayList();
        arrayList19.add("related_account");
        fieldsMap.put("Telegram ID",arrayList19);

        ArrayList arrayList20 = new ArrayList();
        arrayList20.add("related_account");
        fieldsMap.put("Twitter ID",arrayList20);

        ArrayList arrayList21 = new ArrayList();
        arrayList21.add("location");
        fieldsMap.put("Location Name",arrayList21);

        ArrayList arrayList22 = new ArrayList();
        arrayList22.add("related_account");
        fieldsMap.put("WeChat ID",arrayList22);

        ArrayList arrayList23 = new ArrayList();
        arrayList23.add("related_account");
        fieldsMap.put("WhatsApp ID",arrayList23);
    }
}
