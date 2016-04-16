package com.chtisuisse.opml.domain;

import com.chtisuisse.opml.TestBase;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Test for checking feed
 * Created by Christophe on 21.06.2015.
 */
public class OutlineStatusTest extends TestBase {

    /**
     * This is a very common reply body for 301 redirect
     */
    private static final String BODY_PERM_REDIRECT = "<html> <head><title>301 Moved Permanently</title></head> <body bgcolor=\"white\"> <center><h1>301 Moved Permanently</h1></center> <hr><center>nginx</center> </body> </html>";

    /**
     * This is a very common reply body for 302 redirect
     */
    private static final String BODY_TEMP_REDIRECT = "<html> <head><title>302 Moved Temporarily</title></head> <body bgcolor=\"white\"> <center><h1>302 Moved Temporarily</h1></center> <hr><center>nginx</center> </body> </html>";
    private static final String XKCD = "<?xml version=\"1.0\" encoding=\"utf-8\"?><feed xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en\"><title>xkcd.com</title><link href=\"http://xkcd.com/\" rel=\"alternate\"></link><id>http://xkcd.com/</id><updated>2016-04-11T00:00:00Z</updated><entry><title>Brain Upload</title><link href=\"http://xkcd.com/1666/\" rel=\"alternate\"></link><updated>2016-04-11T00:00:00Z</updated><id>http://xkcd.com/1666/</id><summary type=\"html\">&lt;img src=\"http://imgs.xkcd.com/comics/brain_upload.png\" title=\"I just spent 20 minutes deciding whether to start an email with 'Hi' or 'Hey', so I think it transferred correctly.\" alt=\"I just spent 20 minutes deciding whether to start an email with 'Hi' or 'Hey', so I think it transferred correctly.\" /&gt;</summary></entry><entry><title>City Talk Pages</title><link href=\"http://xkcd.com/1665/\" rel=\"alternate\"></link><updated>2016-04-08T00:00:00Z</updated><id>http://xkcd.com/1665/</id><summary type=\"html\">&lt;img src=\"http://imgs.xkcd.com/comics/city_talk_pages.png\" title=\"I don't think the Lakeshore Air Crash Museum really belongs under 'Tourist Attractions.' It's not a museum--it's just an area near the Lake Festival Laser Show where a lot of planes have crashed.\" alt=\"I don't think the Lakeshore Air Crash Museum really belongs under 'Tourist Attractions.' It's not a museum--it's just an area near the Lake Festival Laser Show where a lot of planes have crashed.\" /&gt;</summary></entry><entry><title>Mycology</title><link href=\"http://xkcd.com/1664/\" rel=\"alternate\"></link><updated>2016-04-06T00:00:00Z</updated><id>http://xkcd.com/1664/</id><summary type=\"html\">&lt;img src=\"http://imgs.xkcd.com/comics/mycology.png\" title=\"Conspiracy theory: There's no such thing as corn. Those fields you see are just the stalks of a fungus that's controlling our brains to make us want to spread it.\" alt=\"Conspiracy theory: There's no such thing as corn. Those fields you see are just the stalks of a fungus that's controlling our brains to make us want to spread it.\" /&gt;</summary></entry><entry><title>Garden</title><link href=\"http://xkcd.com/1663/\" rel=\"alternate\"></link><updated>2016-04-04T00:00:00Z</updated><id>http://xkcd.com/1663/</id><summary type=\"html\">&lt;script src=\"http://linden0.xkcd.com/linden.js\" data-endpoint=\"http://linden.xkcd.com/\" data-image-endpoint=\"/1663/art/\" data-title=\"Relax.\"&gt;&lt;/script&gt;</summary></entry></feed>";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    @Test(expected = IllegalArgumentException.class)
    public void should_not_accept_null_feed_url() throws MalformedURLException {
        new Outline(null);
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

    /**
     * Let's test absolute path to better understand URL
     */
    @Test
    public void url_should_handle_absolute_redirection() throws MalformedURLException {
        URL base = new URL("http://www.perdu.com");
        URL next = new URL(base, "/absolute");
        Assert.assertEquals("http://www.perdu.com/absolute",next.toExternalForm());
    }

    /**
     * Let's test relative path to better understand URL
     */
    @Test
    public void url_should_handle_relative_redirection() throws MalformedURLException {
        URL base = new URL("http://www.perdu.com/");
        URL next = new URL(base, "relative");
        Assert.assertEquals("http://www.perdu.com/relative",next.toExternalForm());
    }

    /**
     * Let's test relative path to better understand URL
     */
    @Test
    public void url_should_handle_full_redirection() throws MalformedURLException {
        URL base = new URL("http://www.perdu.com/");
        URL next = new URL(base, "http://www.gagne.com");
        Assert.assertEquals("http://www.gagne.com",next.toExternalForm());
    }

    /**
     * This in a unit test
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Test
    public void should_follow_one_permanent_redirect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Mock web
        stubFor(get(urlEqualTo("/permredir"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_MOVED_PERMANENTLY)
                        .withHeader("Location","http://localhost:8089/destination")
                        .withBody(BODY_PERM_REDIRECT)));
        stubFor(get(urlEqualTo("/destination"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody("BODY")));
        // Create object
        Outline checked = new Outline("http://localhost:8089/permredir");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        Assert.assertEquals("http://localhost:8089/destination",checked.getRedirectedXmlUrl());
    }

    /**
     * This in a unit test
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Test
    public void should_follow_one_temporary_redirect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Mock web
        stubFor(get(urlEqualTo("/tempredir"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_MOVED_TEMPORARILY)
                        .withHeader("Location","http://localhost:8089/destination")
                        .withBody(BODY_TEMP_REDIRECT)));
        stubFor(get(urlEqualTo("/destination"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody("BODY")));
        // Create object
        Outline checked = new Outline("http://localhost:8089/tempredir");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        Assert.assertEquals("http://localhost:8089/destination",checked.getRedirectedXmlUrl());
    }

    /**
     * This in a unit test
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Test
    public void should_follow_three_redirects_only() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Mock web
        stubFor(get(urlEqualTo("/redir1"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_MOVED_TEMPORARILY)
                        .withHeader("Location","http://localhost:8089/redir2")
                        .withBody(BODY_TEMP_REDIRECT)));
        stubFor(get(urlEqualTo("/redir2"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_MOVED_TEMPORARILY)
                        .withHeader("Location","http://localhost:8089/redir3")
                        .withBody(BODY_TEMP_REDIRECT)));
        stubFor(get(urlEqualTo("/redir2"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_MOVED_TEMPORARILY)
                        .withHeader("Location","http://localhost:8089/redir4")
                        .withBody(BODY_TEMP_REDIRECT)));
        stubFor(get(urlEqualTo("/redir4"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_MOVED_TEMPORARILY)
                        .withHeader("Location","http://localhost:8089/redir5")
                        .withBody(BODY_TEMP_REDIRECT)));
        stubFor(get(urlEqualTo("/redir5"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_MOVED_TEMPORARILY)
                        .withHeader("Location","http://localhost:8089/notreached")
                        .withBody(BODY_TEMP_REDIRECT)));
        // Create object
        Outline checked = new Outline("http://localhost:8089/redir1");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        Assert.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR,output.getHttpStatus());
    }

    /**
     * Integration test to see what king of last-update time http://xkcd.com/atom.xml has
     */
    @Test
    public void should_handle_last_update_time_for_xkcd(){
        // Mock web
        stubFor(get(urlEqualTo("/xkcd"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(XKCD)));
        Outline checked = new Outline("http://localhost:8089/xkcd");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        Assert.assertEquals(HttpStatus.SC_OK,output.getHttpStatus());
        Assert.assertNotNull("Last updated should not be null", output.getLastUpdated());

    }

    @Test
    public void should_handle_wordpress_feeds_tricky_redirection() throws MalformedURLException {
        Outline checked = new Outline("http://www.madame-oreille.com/blog/index.php/feed/atom/");
        OutlineStatus output = new OutlineStatus(checked);
        output.check();
        Assert.assertEquals(HttpStatus.SC_OK,output.getHttpStatus());
    }



}
