package com.chtisuisse.opml.engine.impl;

import com.chtisuisse.opml.domain.Outline;
import com.chtisuisse.opml.domain.OutlineStatus;
import com.chtisuisse.opml.engine.Engine;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The processing engine : serial for now must be paralyzed
 * Created by Christophe on 20.06.2015.
 */
public class EngineImpl implements Engine {

    private static final Logger logger = LoggerFactory.getLogger(EngineImpl.class);

    @Override
    public List<OutlineStatus> processOPML(List<Outline> tobeChecked) {
        // Checks
        if (tobeChecked == null) {
            throw new IllegalArgumentException("List to be checked cannot be null ");
        }
        // Process
        logger.debug("Processing : add");
        // No parallel here (useless)
        List<OutlineStatus> prepare = tobeChecked.parallelStream()
                .map(OutlineStatus::new)   // transform List of Outline to List of OutlineStatus
                .distinct()
                .collect(Collectors.toList());
        logger.debug("Processing : check");
        // We will parallelize the processing
        // Please read : http://stackoverflow.com/questions/21163108/custom-thread-pool-in-java-8-parallel-stream
        List<OutlineStatus> checked = prepare.stream()
                                    .map(OutlineStatus::check)
                                    .collect(Collectors.toList());
        logger.debug("Processing : filter");
        return checked.stream()
                .filter(toBeStatusFiltered -> toBeStatusFiltered.getHttpStatus() == HttpStatus.SC_OK) // filter out non ok
                .filter(toBeDateTested -> toBeDateTested.getLastUpdated() != null)
                .collect(Collectors.toList());
    }
}
