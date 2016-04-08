package com.chtisuisse.opml.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Christophe on 08.04.2016.
 */
public class EmptyLineSkipperTest {

    @Test
    public void empty_lines_should_be_skipped() {
        InputStream is = ClassLoader.getSystemResourceAsStream("file_with_empty_lines.txt");
        InputStream is_short = EmptyLineSkipper.skipEmptyLines(is);
        BufferedReader br=new BufferedReader(new InputStreamReader(is_short));
        long lineNo=br.lines().count();
        Assert.assertEquals(3,lineNo);
    }

}