package helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CallDirection {
    public String direction = null;
    public String contactName = null;
    public String contactTel = null;

    public void fillCallDirection(ArrayList<String> call) {
        HashMap hashMap = callToHash(call);

        if(hashMap.get("Direction") != null)
        {
            if (hashMap.get("Direction").toString().contains("Incoming")) {
                direction = "Incoming";
                if(hashMap.get("FromTel") != null)
                {
                    contactTel = hashMap.get("FromTel").toString();
                }
                if(hashMap.get("Name (Matched)") != null)
                {
                    contactName =hashMap.get("Name (Matched)").toString();
                }
            }
            if (hashMap.get("Direction").toString().contains("Outgoing")) {
                direction = "Outgoing";
                if(hashMap.get("FromTel") != null)
                {
                    contactTel = hashMap.get("FromTel").toString();
                }
                if(hashMap.get("Name (Matched)") != null)
                {
                    contactName =hashMap.get("Name (Matched)").toString();
                }
            }
        }
    }

    private HashMap callToHash(ArrayList<String> call) {
        HashMap result = new HashMap();

        for (String item : call) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(item.split("::")));

            if (line.size() > 1) {
                String field = line.get(0);
                String value = line.get(1);

                result.put(field, value);
            }
        }

        return result;
    }
}
