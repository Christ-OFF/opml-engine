package com.chtisuisse.opml.domain;

import com.jayway.jsonpath.internal.IOUtils;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.ParsingFeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * The Outline checker
 * Created by Christophe on 20.06.2015.
 */
public class OutlineStatus {

    private static final Logger logger = LoggerFactory.getLogger(OutlineStatus.class);

    private static final String PROBLEM_WHILE_PROCESSING = "Problem while processing ";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0";

    private Outline feed;
    private int httpStatus;
    private LocalDateTime lastUpdated;

    public OutlineStatus(Outline feed) {
        this.feed = feed;
    }

    public Outline getFeed() {
        return feed;
    }

    public void setFeed(Outline feed) {
        this.feed = feed;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * The outline can be checked to get a status
     * The method was returning void
     * But in order to use Streams it returns itself
     */
    public OutlineStatus check() {

        SyndFeed syndicationFeed = null;
        InputStream is = null;
        try {

            SyndFeedInput input = new SyndFeedInput();
            URL url = this.getFeed().getXmlURl();

            logger.debug("Going to call " + this.getFeed().getXmlURl().toString() + " for feed.");

            try {
                System.setProperty("http.agent", USER_AGENT);
                URLConnection openConnection = url.openConnection();
                is = url.openConnection().getInputStream();
                if ("gzip".equals(openConnection.getContentEncoding())) {
                    is = new GZIPInputStream(is);
                }
                InputSource source = new InputSource(is);
                input = new SyndFeedInput();
                syndicationFeed = input.build(source);
                XmlReader reader = new XmlReader(url);
                //
                syndicationFeed = input.build(reader);
                logger.info("Feed " + this.getFeed().getXmlURl().toString() + " replied with " + syndicationFeed.getEntries().size() + " elements ");
                List entries = syndicationFeed.getEntries();
                if (!entries.isEmpty()) {
                    SyndEntry lastEntry = (SyndEntry) entries.get(0);
                    if (lastEntry.getPublishedDate() != null) {
                        lastUpdated = lastEntry.getPublishedDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();
                    } else {
                        logger.warn("Last entry has no update datetime " + this.getFeed().getXmlURl().toString());
                        lastUpdated = null;
                    }
                } else {
                    logger.warn("Feed " + this.getFeed().getXmlURl() + " has no entry at all ");
                    lastUpdated = null;
                }
                httpStatus = HttpStatus.SC_OK;
            } catch (ParsingFeedException pfe) {
                logger.error("Problem while parsing \"" + this.getFeed().getTitle() + "\" with URL " + this.getFeed().getXmlURl().toString() + "reason " + pfe.getLocalizedMessage(), pfe);
                httpStatus = HttpStatus.SC_INTERNAL_SERVER_ERROR;
            } catch (FeedException e) {
                logger.error(PROBLEM_WHILE_PROCESSING + this.getFeed().getXmlURl().toString(), e);
                httpStatus = HttpStatus.SC_INTERNAL_SERVER_ERROR;
            } catch (IOException e) {
                logger.warn(PROBLEM_WHILE_PROCESSING + this.getFeed().getXmlURl().toString(), e);
                logger.warn(PROBLEM_WHILE_PROCESSING + this.getFeed().getXmlURl().toString(), e);
                httpStatus = HttpStatus.SC_BAD_REQUEST;
            }

        } finally {
            IOUtils.closeQuietly(is);
        }

        return this;
    }

    @Override
    public String toString() {
        return "OutlineStatus{" +
                "feed=" + feed +
                ", httpStatus=" + httpStatus +
                ", lastUpdated=" + lastUpdated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutlineStatus that = (OutlineStatus) o;

        return !(feed != null ? !feed.equals(that.feed) : that.feed != null);

    }

    @Override
    public int hashCode() {
        return feed != null ? feed.hashCode() : 0;
    }
}
