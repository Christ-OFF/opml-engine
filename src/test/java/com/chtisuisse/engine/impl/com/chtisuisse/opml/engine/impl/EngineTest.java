package com.chtisuisse.engine.impl.com.chtisuisse.opml.engine.impl;

import com.chtisuisse.opml.TestBase;
import com.chtisuisse.opml.domain.Outline;
import com.chtisuisse.opml.domain.OutlineStatus;
import com.chtisuisse.opml.engine.Engine;
import com.chtisuisse.opml.engine.impl.EngineImpl;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Tests are put in an order driven by TDD : going deeper
 * Created by Christophe on 20.06.2015.
 */

public class EngineTest extends TestBase {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    /**
     * The one that is tested
     */
    private Engine testedEngine;

    @Before
    public void setup(){
        testedEngine = new EngineImpl();
    }

    @Test
    public void should_handle_emptylist() {
        List<Outline> input = new ArrayList<>(0);
        List<OutlineStatus> output = testedEngine.processOPML(input);
        Assert.assertNotNull(output);
        Assert.assertTrue(output.size() == 0);
    }

    @Test
    public void should_retrieve_same_outline() throws MalformedURLException {
        // Mock web
        stubFor(get(urlEqualTo("/feed"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(VALID_RSS_SAMPLE)));
        List<Outline> input = new ArrayList<>(1);
        Outline checked = new Outline("http://localhost:8089/feed");
        input.add(checked);
        List<OutlineStatus> output = testedEngine.processOPML(input);
        Assert.assertTrue(output.size() == 1);
        Assert.assertEquals(output.get(0).getFeed(),checked);
    }

}
