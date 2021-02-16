package com.hicx.filestats.statistics;


public interface StatisticProcessor {

    void processFileChunk(String fileChunk);

    String getStatistics();
}
