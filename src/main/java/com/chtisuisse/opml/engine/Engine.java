package com.chtisuisse.opml.engine;

import com.chtisuisse.opml.domain.Outline;
import com.chtisuisse.opml.domain.OutlineStatus;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Christophe on 20.06.2015.
 */
@FunctionalInterface
public interface Engine {

    /**
     * Process the OPML and provided back a list of source with status and dates
     * @param tobeChecked the list of all Outline (feeds) to be checked
     * @return checked outlines
     */
    List<OutlineStatus> processOPML(List<Outline> tobeChecked) throws ExecutionException, InterruptedException;
}
