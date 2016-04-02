package com.chtisuisse.opml;

/**
 * Created by Christophe on 28.06.2015.
 */
public class TestBase {
    /**
     * Constants for testing
     */
    protected static final String LAST_PUBDATE_OK = "Fri, 19 Jun 2015 20:25:27 GMT";
    /**
     * Constants for testing
     */
    protected static final String VALID_RSS_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss xmlns:media=\"http://search.yahoo.com/mrss/\" version=\"2.0\">\n" +
            "<channel>\n" +
            "<title>Big Picture</title>\n" +
            "<description>News Stories in Photographs from the Boston Globe.</description>\n" +
            "<language>en-us</language>\n" +
            "<copyright>Copyright 2014</copyright>\n" +
            "<generator>Methode</generator>\n" +
            "<item xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
            "<title>Shooting at Charleston church</title>http:<description>Nine people were killed Wednesday night when authorities say Dylan Storm Roof, 21, fired upon a prayer meeting inside the Emanuel AME Church in Charleston, S.C.  Roof was captured on Thursday after an intense manhunt. \n" +
            " -- \n" +
            "By Leanne Burden Seidel&lt;br /&gt;<![CDATA[<img src=http://c.o0bg.com/rf/image_960w/Boston/2011-2020/2015/06/19/BostonGlobe.com/BigPicture/Images/477719910.jpg>]]>&lt;br /&gt;Kearston Farr comforts her daughter, Taliyah Farr,5, as they stand in front of the Emanuel African Methodist Episcopal Church where the nine people were killed, June 19,  A 21-year-old white gunman is suspected of killing nine people during a prayer meeting in the church, which is one of the nation's oldest black churches in Charleston.\n" +
            "  (Joe Raedle/Getty Images)&lt;br /&gt;&lt;br /&gt;</description>\n" +
            "<link>http://www.bostonglobe.com/news/bigpicture/2015/06/18/shooting-charleston-church/qVpT1iW2U9I1T1McubMLbJ/story.html</link>\n" +
            "<guid>http://www.bostonglobe.com/news/bigpicture/2015/06/18/shooting-charleston-church/qVpT1iW2U9I1T1McubMLbJ/story.html</guid>\n" +
            "<categories/>\n" +
            "<pubDate>" + LAST_PUBDATE_OK + "</pubDate>\n" +
            "</item>\n" +
            "<item xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
            "<title>Children of the Moon</title>http:<description>Alabaster-skinned people born on a sun-scorched group of islands off Panama’s Caribbean coast are venerated as “Children of the Moon”. Albinos make up between 5 and 10 percent of the roughly 80,000 indigenous Gunas who live on the mainland of the Guna Yala region and its islands. With their sensitive skin and eyes, young Guna albinos must be shuttled to and from school, avoiding the baking heat, while they watch their friends play in the streets. International Albinism Awareness Day was June 13th. \n" +
            " -- \n" +
            "By Reuters&lt;br /&gt;<![CDATA[<img src=http://c.o0bg.com/rf/image_960w/Boston/2011-2020/2015/06/16/BostonGlobe.com/BigPicture/Images/2015-06-11T111428Z_1391130308_GF10000123836_RTRMADP_3_PANAMA-ALBINOS-WIDERIMAGE.jpg>]]>&lt;br /&gt;Delyane Avila, 6, who is part of the albino or \"Children of the Moon\" group in the Guna Yala indigenous community, drew on her notebook next to neighbors on Ailigandi Island in the Guna Yala region, Panama. \n" +
            "\n" +
            "  (Carlos Jasso/Reuters)&lt;br /&gt;&lt;br /&gt;</description>\n" +
            "<link>http://www.bostonglobe.com/news/bigpicture/2015/06/16/children-moon/uzxuqBuOTxgg8rCvvJzU2N/story.html</link>\n" +
            "<guid>http://www.bostonglobe.com/news/bigpicture/2015/06/16/children-moon/uzxuqBuOTxgg8rCvvJzU2N/story.html</guid>\n" +
            "<categories/>\n" +
            "<pubDate>Wed, 17 Jun 2015 02:27:15 GMT</pubDate>\n" +
            "</item>\n" +
            "</channel>\n" +
            "</rss>";

    private static final String LAST_PUDATE_ROME_KO = "Tue, 19 May 2015 18:30:40 +0100";

    protected static final String RSS_WITH_PUBDATE_PROBLEM = "<rss version=\"2.0\">\n" +
            "<channel>\n" +
            "<title>OsmAnd blog</title>\n" +
            "<link>http://osmand.net/blog</link>\n" +
            "<description/>\n" +
            "<generator></generator>\n" +
            "\n" +
            "<item>\n" +
            "<title>OsmAnd 2.1</title>\n" +
            "<link>http://osmand.net/blog?id=osmand-osmand-2-1-released</link>\n" +
            "<description>\n" +
            "<p>OsmAnd 2.1 release is out! This time, we focused on improving POI search and making your trips even more interactive through new Wikipedia data sorted by country.</p>\n" +
            "</description>\n" +
            "<pubDate>Tue, 16 June 2015</pubDate>\n" +
            "</item>\n" +
            "\n" +
            "<item>\n" +
            "<title>OsmAnd 1.0.2 (iOS)</title>\n" +
            "<link>http://osmand.net/blog?id=osmand-ios-1.0.2</link>\n" +
            "<description>\n" +
            "\t<p>We are glad to announce that iOS version of OsmAnd is becoming better over time. We put significant efforts and managed to finish lots of useful features in a short period of time. Here is the short list of achievements.</p>\n" +
            "<ul>\n" +
            "\t<li>We significantly improved the responsiveness of the map interaction.</li>\n" +
            "\t<li>OsmAnd Maps now is also available for iPad.</li>\n" +
            "\t<li>We added translations to Russian, German, Danish, French, Spanish and other languages (thanks to our great community). </li>\n" +
            "\t<li>Parking Position and Trip Recording addons were added to the application. Please feel free to use them without charge</li>\n" +
            "\t<li>In the new version you will be able to search a location by simply put the url or enter the coordinantes in the search text box.</li>\n" +
            "\t<li>And lots of other minor and useful improvements.</li>\n" +
            "</ul>\n" +
            "</description>\n" +
            "<pubDate>" + LAST_PUDATE_ROME_KO + "</pubDate>\n" +
            "</item>\n" +
            "\n" +
            "\n" +
            "<item>\n" +
            "<title>OsmAnd 2.0</title>\n" +
            "<link>http://osmand.net/blog?id=osmand-2-0-released</link>\n" +
            "<description>\n" +
            "<p>We are glad to announce that OsmAnd 2.0 is coming to devices. We expect to finish the rollout on this week. This has been a while we had to admit that OsmAnd didn't follow the guidelines and had an inconsistent UI. This major release we fully focused to put the most trending UI, Material Design, in place. Meanwhile we considered dozens interactive patterns to improve the usability of various features in OsmAnd and came up with a Dashboard screen, which provides a balanced solution between Search &amp; Map display. Please help us by giving your feedback to make the product better.</p>\n" +
            "</description>\n" +
            "<pubDate>Tue, 29 Apr 2015 18:30:40 +0100</pubDate>\n" +
            "</item>\n" +
            "\n" +
            "\n" +
            "<item>\n" +
            "<title>OsmAnd for iPhone</title>\n" +
            "<guid isPermaLink=\"true\">http://osmand.net/blog?id=osmand-ios</guid>\n" +
            "<link>http://osmand.net/blog?id=osmand-ios</link>\n" +
            "<description>\n" +
            "<p>We are glad to announce that iPhone version of OsmAnd is released.</p>\n" +
            "<p>This version contains the most used functionality of Android version together with significantly improved user interface, which we believe is a good combination.</p>\n" +
            "\t\t\t\n" +
            "</description>\n" +
            "<pubDate>Sun, 19 Apr 2015 12:30:40 +0100</pubDate>\n" +
            "</item>\n" +
            "\n" +
            "\n" +
            "<item>\n" +
            "<title>Nautical charts</title>\n" +
            "<guid isPermaLink=\"true\">http://osmand.net/blog?id=nautical-charts</guid>\n" +
            "<link>http://osmand.net/blog?id=nautical-charts</link>\n" +
            "<description>\n" +
            "<p>OsmAnd 2.0 is coming soon. One of the new features of the version will be Nautical Charts plugin.</p>\n" +
            "<p>Nautical Chart is a detailed graphical representation of oceans, seas, coastal areas and rivers.</p>\n" +
            "<p>Nautical Charts are made for people who drive any kind of vehicle on water: from professional sailors to people who rented a boat to make a tour over city canals. The charts can contain various information like sailing routes, navigation lights, dangerous areas, areas where it&apos;s allowed or not allowed to sail or dock, etc.</p>\n" +
            "<p>All the professional sailors are obliged to have official nautical charts on their ships. These charts are published by authorized agencies and cost quite some money. Agencies are investing a lot in keeping the charts up to date. They release updates for the charts on regular basis, but because of the fact that reviewing the information and processing the updates takes quite some time the nautical charts are never completely up-to-date. </p>\n" +
            " <a href=\"http://osmand.net/blog?id=nautical-charts\">Read more</a>\n" +
            "</description>\n" +
            "<pubDate>Fri, 20 Mar 2015 18:30:40 +0100</pubDate>\n" +
            "</item>\n" +
            "\n" +
            "</channel>\n" +
            "</rss>";
}
