package com.chtisuisse.opml.com.chtisuisse.opml.parser;

import com.chtisuisse.opml.domain.Outline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * From
 * http://javarevisited.blogspot.ch/2011/12/parse-read-xml-file-java-sax-parser.html
 * Parses a OPML File
 * Created by Christophe on 20.06.2015.
 */
public class OPMLSaxHandler extends DefaultHandler {

    private static final Logger logger = LoggerFactory.getLogger(OPMLSaxHandler.class);

    /**
     * Those are the nodes we are looking for
     */
    private static final String OUTLINE_NODE = "outline";
    /**
     * And of that type
     */
    private static final String OUTLINE_NODE_TYPE = "type";
    private static final String OUTLINE_NODE_TYPE_RSS = "rss";
    private static final String OUTLINE_NODE_TEXT = "text";
    private static final String OUTLINE_NODE_TITLE = "title";
    private static final String OUTLINE_NODE_XMLURL = "xmlUrl";
    private static final String OUTLINE_NODE_HTMLURL = "htmlUrl";

    /**
     * The result
     */
    public final List<Outline> opmlOutlines = new ArrayList<>();
    /**
     * The added outline
     */
    private Outline outline = null;

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        outline = null;
        if (qName.equalsIgnoreCase(OUTLINE_NODE)) {
            String nodeType = attributes.getValue(OUTLINE_NODE_TYPE);
            if (OUTLINE_NODE_TYPE_RSS.equals(nodeType)) {
                String xmlUri = attributes.getValue(OUTLINE_NODE_XMLURL);
                try {
                    outline = new Outline(xmlUri);
                    // Optionnal attributes
                    outline.setTitle(attributes.getValue(OUTLINE_NODE_TITLE));
                    outline.setText(attributes.getValue(OUTLINE_NODE_TEXT));
                    tryToSetHtml(attributes);
                    logger.debug("An outline has been built " + outline.toString());
                } catch (MalformedURLException e) {
                    logger.warn("We have rejected outline without a good " + OUTLINE_NODE_XMLURL, e);
                }
            } else {
                logger.debug("The following outline is not an feed one");
            }
        }
    }

    /**
     * We want to handle problem more nicely
     * @param attributes
     */
    private void tryToSetHtml(Attributes attributes) {
        try  {
            outline.setHtmlUrl(attributes.getValue(OUTLINE_NODE_HTMLURL));
        } catch (MalformedURLException e){
            logger.info("Invalide html url but outline is created " + outline.toString(), e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (outline != null) {
                opmlOutlines.add(outline);
        }
    }

}
