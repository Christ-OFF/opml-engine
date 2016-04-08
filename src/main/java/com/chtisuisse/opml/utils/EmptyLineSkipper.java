package com.chtisuisse.opml.utils;

import com.chtisuisse.opml.domain.OutlineStatus;
import com.jayway.jsonpath.internal.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Some feeds have empty lines so let's skip them
 * Created by Christophe on 08.04.2016.
 */
public class EmptyLineSkipper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutlineStatus.class);

    public static InputStream skipEmptyLines(InputStream is) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line = null;
        try {
            while((line = in.readLine()) != null) {
                if (!line.isEmpty()){
                    out.write(line.getBytes());
                    out.write(System.lineSeparator().getBytes());
                }
            }
        } catch (IOException e) {
            LOGGER.warn("We had a problem reading the stream. We output a partial response",e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
