package com.chtisuisse.opml.utils;

import com.chtisuisse.opml.com.chtisuisse.opml.parser.OPMLSaxHandler;
import com.chtisuisse.opml.domain.Outline;
import com.chtisuisse.opml.domain.OutlineStatus;
import com.chtisuisse.opml.engine.impl.EngineImpl;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * A simple runner from the command line
 * Created by Christophe on 21.06.2015.
 */
public class EngineRunner {

    /**
     * Prevent instantiation
     */
    private EngineRunner(){

    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        //Create a "parser factory" for creating SAX parsers
        SAXParserFactory spfac = SAXParserFactory.newInstance();
        //Now use the parser factory to create a SAXParser object
        SAXParser sp = spfac.newSAXParser();
        //Create the handler
        OPMLSaxHandler handler = new OPMLSaxHandler();
        //Finally, tell the parser to parse the input and notify the handler
        File file = new File(args[0]);
        sp.parse(file,handler);

        List<Outline> outlines = handler.opmlOutlines;
        List<OutlineStatus> result = new EngineImpl().processOPML(outlines);
        Collections.sort(result, (o1, o2) -> {
            if (o1.getLastUpdated() == null){
                return -1;
            } else if (o2.getLastUpdated() == null) {
                return +1;
            } else return -o1.getLastUpdated().compareTo(o2.getLastUpdated());
        });
        for (OutlineStatus outlineStatus : result) {
            System.out.println(outlineStatus.toString());
        }
    }
}
