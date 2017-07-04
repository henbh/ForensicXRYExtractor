package datamodule;

import configuration.ConfigurationManager;
import org.apache.log4j.Logger;

/**
 * Created by henb on 7/4/2017.
 */
public class LogParser extends XryParser {
    public LogParser(String filePath, Logger logger) {
        super(filePath, logger);
        _jsonDocument = readJsonObject(ConfigurationManager.getInstance().location_json_path);
        _jsonDocument = fillSolanJason(_jsonDocument, false);
        try {
            _jsonDocument.put("solan_type", "location");
            _jsonDocument.put("solan_subtype", "phone_location");
        } catch (Exception e) {
            _logger.error(e);
        }
    }
}
