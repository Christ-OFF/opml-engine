package com.chtisuisse.opml.domain;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class will hold Outline that need to be tested
 * Created by Christophe on 20.06.2015.
 */
public class Outline {

    private String text;
    private String title;
    private URL xmlURl;
    private URL htmlUrl;

    /**
     * We muste at least have an URL (valid not null)
     * @param xmlURl the compulsory url
     */
    public Outline(String xmlURl) throws MalformedURLException {
        if (xmlURl == null){
            throw new IllegalArgumentException("Null URL are not supported !");
        }
        this.xmlURl = new URL(xmlURl);
    }

    @Override
    public String toString() {
        return "Outline{" +
                "text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", xmlURl='" + xmlURl + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getXmlURl() {
        return xmlURl;
    }

    public URL getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) throws MalformedURLException {
        this.htmlUrl = new URL(htmlUrl);
    }

}
