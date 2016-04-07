package com.chtisuisse.opml.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class will hold Outline that need to be tested
 * Created by Christophe on 20.06.2015.
 */
public class Outline {

    private String text;
    private String title;
    private String xmlURL;
    private String htmlUrl;
    /**
     * This parameter will hold the real XmlURL as the orgiginal one may be redirected a few times
     */
    private String redirectedXmlUrl;

    /**
     * We muste at least have an URL (valid not null)
     *
     * @param xmlURL the compulsory url
     */
    public Outline(String xmlURL) {
        if (xmlURL == null) {
            throw new IllegalArgumentException("Null URL are not supported !");
        }
        this.xmlURL = xmlURL;
        this.redirectedXmlUrl = xmlURL;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
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

    public String getXmlURL() {
        return xmlURL;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getRedirectedXmlUrl() {
        return redirectedXmlUrl;
    }

    public void setRedirectedXmlUrl(String redirectedXmlUrl) {
        this.redirectedXmlUrl = redirectedXmlUrl;
    }

}
