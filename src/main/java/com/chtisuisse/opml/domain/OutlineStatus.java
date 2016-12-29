package com.chtisuisse.opml.domain;

import com.chtisuisse.opml.utils.EmptyLineSkipper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.ParsingFeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * The Outline checker
 * Created by Christophe on 20.06.2015.
 */
public class OutlineStatus {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutlineStatus.class);

    private static final String PROBLEM_WHILE_PROCESSING = "Problem while processing ";

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0";
    /**
     * Max time to wait to connect to destination
     */
    private static final int CONNECT_TIMEOUT = 15000;
    /**
     * Max time to open connection
     */
    private static final int TIMEOUT = 15000;
    /**
     * We must protect ourselves of never ending redirections A -> B -> A -> B ....
     */
    private static final int MAX_REDIRECTS = 3;

    private final Outline feed;
    private int httpStatus;
    private LocalDateTime lastUpdated;

    public OutlineStatus(Outline feed) {
        this.feed = feed;
    }

    public Outline getFeed() {
        return feed;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * This private methods will follow redirects
     * We keep this method private to train testing private methods
     *
     * @param startURL the url we start with
     * @return a good redirected connection
     * @throws IOException
     */
    private HttpURLConnection openConnectionWithRedirects(String startURL) throws TooManyRedirectionsException, IOException {


        String url  = startURL;
        for (int currentNbredirects = 0 ; currentNbredirects <= MAX_REDIRECTS ; currentNbredirects++) {

            URL currentURL = new URL(url);
            HttpURLConnection result = getHttpURLConnection(currentURL);
            int currentHttpStatus = result.getResponseCode();
            if (HttpURLConnection.HTTP_MOVED_TEMP == currentHttpStatus
                    || HttpURLConnection.HTTP_MOVED_PERM == currentHttpStatus) {
                String newLocation = result.getHeaderField("Location");
                URL next = new URL(currentURL, newLocation);
                String finalURL = next.toExternalForm();
                LOGGER.info("At the end ,the feed URL " + this.feed.getXmlURL() + " redirects to " + finalURL);
                this.feed.setRedirectedXmlUrl(finalURL);
                url = finalURL;
            } else {
                // We are not redirecting anymore return
                return result;
            }
        }
        throw new TooManyRedirectionsException(startURL);
    }

    /**
     * Build a connection with expected parameters
     *
     * @param url needed to return a connection
     * @return a connection
     * @throws IOException
     */
    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection result = (HttpURLConnection) url.openConnection();
        result.setConnectTimeout(CONNECT_TIMEOUT);
        result.setReadTimeout(TIMEOUT);
        result.setInstanceFollowRedirects(false);           // if true it won't follow http <-> https redirects
        result.setRequestProperty("User-Agent", USER_AGENT);
        return result;
    }

    /**
     * The outline can be checked to get a status
     * The method was returning void
     * But in order to use Streams it returns itself
     */
    public OutlineStatus check() {

        SyndFeed syndicationFeed;

        SyndFeedInput input;
        String feeedURL = this.getFeed().getXmlURL();
        LOGGER.debug("Going to call " + this.getFeed().getXmlURL() + " for feed.");

        System.setProperty("http.agent", USER_AGENT);
        try (
                // is is Closable
                // see : http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
                InputStream is = openConnectionWithRedirects(feeedURL).getInputStream()
        ) {
            InputSource source = new InputSource(EmptyLineSkipper.skipEmptyLines(is));
            input = new SyndFeedInput();
            input.setAllowDoctypes(true);
            syndicationFeed = input.build(source);
            LOGGER.info("Feed " + this.getFeed().getXmlURL() + " replied with " + syndicationFeed.getEntries().size() + " elements ");
            List entries = syndicationFeed.getEntries();
            if (!entries.isEmpty()) {
                SyndEntry lastEntry = (SyndEntry) entries.get(0);
                if (lastEntry.getUpdatedDate() != null) {
                    lastUpdated = lastEntry.getUpdatedDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();
                } else if (lastEntry.getPublishedDate() != null) {
                    lastUpdated = lastEntry.getPublishedDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime();
                } else {
                    LOGGER.warn("Last entry has no update datetime " + this.getFeed().getXmlURL());
                    lastUpdated = null;
                }
            } else {
                LOGGER.warn("Feed " + this.getFeed().getXmlURL() + " has no entry at all ");
                lastUpdated = null;
            }
            httpStatus = HttpStatus.SC_OK;
        } catch (TooManyRedirectionsException tmre) {
            LOGGER.error("We do not allow more than " + MAX_REDIRECTS + " redirections", tmre);
            httpStatus = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        } catch (IllegalArgumentException | ParsingFeedException iae) {
            LOGGER.error("Problem while parsing \"" + this.getFeed().getTitle() + "\" with URL " + this.getFeed().getXmlURL() + "reason " + iae.getLocalizedMessage(), iae);
            httpStatus = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        } catch (FeedException e) {
            LOGGER.error(PROBLEM_WHILE_PROCESSING + this.getFeed().getXmlURL(), e);
            httpStatus = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        } catch (IOException e) {
            LOGGER.warn(PROBLEM_WHILE_PROCESSING + this.getFeed().getXmlURL(), e);
            LOGGER.warn(PROBLEM_WHILE_PROCESSING + this.getFeed().getXmlURL(), e);
            httpStatus = HttpStatus.SC_BAD_REQUEST;
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
