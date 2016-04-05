package com.chtisuisse.opml.com.chtisuisse.opml.parser;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Simple test for the xml node handler
 * Created by Christophe on 21.06.2015.
 */
public class OPMLSaxHandlerTest {

    private static final String THE_TITLE = "the title";
    private static final String THE_TEXT = "the text";
    private static final String HTML_URL = "http://www.perdu.com/vraiment";
    private static final String XML_URL = "http://www.perdu.com";

    @Test
    public void should_accept_standard_node() throws SAXException {
        OPMLSaxHandler handler = new OPMLSaxHandler();
        AttributesImpl attributes = new org.xml.sax.helpers.AttributesImpl();
        attributes.addAttribute("uri","localName","type","String","rss");
        attributes.addAttribute("uri","localName","xmlUrl","String", XML_URL);
        //
        handler.startElement("uri","localname","outline",attributes);
        handler.endElement("uri","localname","outline");
        //
        Assert.assertEquals(1,handler.opmlOutlines.size());
    }

    @Test
    public void should_filterout_non_rss() throws SAXException {
        OPMLSaxHandler handler = new OPMLSaxHandler();
        AttributesImpl attributes = new org.xml.sax.helpers.AttributesImpl();
        attributes.addAttribute("uri","localName","xmlUrl","String", XML_URL);
        //
        handler.startElement("uri","localname","outline",attributes);
        handler.endElement("uri","localname","outline");
        //
        Assert.assertEquals(0,handler.opmlOutlines.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_reject_invalid_node() throws SAXException {
        OPMLSaxHandler handler = new OPMLSaxHandler();
        AttributesImpl attributes = new org.xml.sax.helpers.AttributesImpl();
        attributes.addAttribute("uri","localName","type","String","rss");
        attributes.addAttribute("uri","localName","xmlUrl","String",null);
        //
        handler.startElement("uri","localname","outline",attributes);
        handler.endElement("uri","localname","outline");
    }

    @Test
    public void should_set_all_exepcted_atributes() throws SAXException {
        OPMLSaxHandler handler = new OPMLSaxHandler();
        AttributesImpl attributes = new org.xml.sax.helpers.AttributesImpl();
        attributes.addAttribute("uri","localName","type","String","rss");
        attributes.addAttribute("uri","localName","title","String", THE_TITLE);
        attributes.addAttribute("uri","localName","text","String", THE_TEXT);
        attributes.addAttribute("uri","localName","htmlUrl","String", HTML_URL);
        attributes.addAttribute("uri","localName","xmlUrl","String", XML_URL);
        //
        handler.startElement("uri","localname","outline",attributes);
        handler.endElement("uri","localname","outline");
        //
        Assert.assertEquals(THE_TITLE,handler.opmlOutlines.get(0).getTitle());
        Assert.assertEquals(THE_TEXT,handler.opmlOutlines.get(0).getText());
        Assert.assertEquals(HTML_URL,handler.opmlOutlines.get(0).getHtmlUrl().toString());
        Assert.assertEquals(XML_URL,handler.opmlOutlines.get(0).getXmlURl().toString());
    }
}
