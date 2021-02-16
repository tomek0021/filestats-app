package com.hicx.filestats.statistics;

import java.util.StringTokenizer;

class NumberOfWordsProcessor implements StatisticProcessor {

    private int numberOfWords = 0;

    @Override
    public void processFileChunk(String fileChunk) {
        StringTokenizer tokens = new StringTokenizer(fileChunk);
        numberOfWords += tokens.countTokens();
    }

    @Override
    public String getStatistics() {
        return "Number of words: " + numberOfWords;
    }
}
