package com.sociit.app.sociit.helpers;

import com.sociit.app.sociit.entities.RssItem;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Lazaro on 4/25/16.
 */
public class RssReader {

    private String rssUrl;

    public RssReader(String rssUrl){
        this.rssUrl = rssUrl;
    }

    public List<RssItem> getItems() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        RssParseHandler handler = new RssParseHandler();
        saxParser.parse(rssUrl, handler);
        return handler.getItems();
    }
}
