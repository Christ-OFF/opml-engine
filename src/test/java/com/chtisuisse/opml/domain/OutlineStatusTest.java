package com.chtisuisse.opml.domain;

import com.chtisuisse.opml.TestBase;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

/**
 * Test for checking feed
 * Created by Christophe on 21.06.2015.
 */
public class OutlineStatusTest extends TestBase {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    @Test(expected = IllegalArgumentException.class)
    public void should_not_accept_null_feed_url() throws MalformedURLException {
        Outline checked = new Outline(null);
    }

    @Test
    public void should_be_KO_if_source_is_not() throws MalformedURLException {
        // Mock web
        stubFor(get(urlEqualTo("/feed"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)));
        // Mock input
        Outline checked = new Outline("http://localhost:8089/feed");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        //
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST,output.getHttpStatus());
    }

    @Test
    public void should_be_OK_if_source_is() throws MalformedURLException {
        // Mock web
        stubFor(get(urlEqualTo("/feed"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(VALID_RSS_SAMPLE)));
        // Mock input
        Outline checked = new Outline("http://localhost:8089/feed");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        //
        Assert.assertEquals(HttpStatus.SC_OK, output.getHttpStatus());
    }

    @Test
    public void should_retrieve_last_update_datetime() throws MalformedURLException {
        // Mock web
        stubFor(get(urlEqualTo("/feed"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(VALID_RSS_SAMPLE)));
        // Mock input
        Outline checked = new Outline("http://localhost:8089/feed");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        // We wan't to parse "Fri, 19 Jun 2015 20:25:27 GMT";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        LocalDateTime expected = LocalDateTime.parse(LAST_PUBDATE_OK,format);
        Assert.assertEquals(expected, output.getLastUpdated());
    }

    /**
     * Rome doesn't seem to parse the pubdate
     * @throws MalformedURLException
     */
    @Test
    public void should_not_retrieve_invalid_last_update_from_rss() throws MalformedURLException {
        // Mock web
        stubFor(get(urlEqualTo("/feed"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(RSS_WITH_PUBDATE_PROBLEM)));
        // Mock input
        Outline checked = new Outline("http://localhost:8089/feed");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        Assert.assertNull("Pubdate should be null", output.getLastUpdated());

    }

    @Test
    /**
     * You may ingore this integration test or update the feed
     * Some feeds reply a 403 when this library aske for the feed
     * (Of course it works in a browser)
     * So this maybe a user agent problem
     * User fiddler as a proxy to see what's happening with those command line arguments
     * -Dhttp.proxyHost=localhost -Dhttp.proxyPort=8888
     */
    public void user_agent_aware_feed_should_not_get_a_403() throws MalformedURLException {
        Outline checked = new Outline("http://blog.getbootstrap.com/feed.xml");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        Assert.assertEquals(HttpStatus.SC_OK,output.getHttpStatus());
    }

}
