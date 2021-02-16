package com.hicx.filestats.statistics;


/**
 * Statistics processors abstraction, as its receives file contents in chunks it is stateful - not thread safe.
 *
 * Usual scenario for usage would be ({@link StatisticProcessorsFactory}:
 *
 * 1. create with {@link StatisticProcessorsFactory#createProcessors()}
 * 2. process with {@link StatisticProcessor#processFileChunk}
 * 3. get calculated statistics with {@link StatisticProcessor#getStatistics}
 *
 */
public interface StatisticProcessor {

    /**
     * Handler method for processing this statistics for given file chunk
     *
     * @param fileChunk
     */
    void processFileChunk(String fileChunk);

    /**
     * @return calculated statistics so far
     */
    String getStatistics();
}
