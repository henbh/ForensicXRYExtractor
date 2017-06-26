package datamodule;

import Interfaces.IXryParser;
import enums.eParser;
import org.apache.log4j.Logger;

public class XryParser implements IXryParser{
    public String _filePath;
    public Logger _logger;
    public eParser _parserType;

    public XryParser(String filePath, Logger logger) {
        _filePath = filePath;
        _logger = logger;
    }

    @Override
    public void Parse() {

    }
}
