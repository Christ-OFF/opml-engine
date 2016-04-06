package com.chtisuisse.opml.domain;

/**
 * This class will hold Outline that need to be tested
 * Created by Christophe on 20.06.2015.
 */
public class Outline {

    private String text;
    private String title;
    private String xmlURl;
    private String htmlUrl;

    /**
     * We muste at least have an URL (valid not null)
     *
     * @param xmlURl the compulsory url
     */
    public Outline(String xmlURl) {
        if (xmlURl == null) {
            throw new IllegalArgumentException("Null URL are not supported !");
        }
        this.xmlURl = xmlURl;
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

    public String getXmlURl() {
        return xmlURl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

}
