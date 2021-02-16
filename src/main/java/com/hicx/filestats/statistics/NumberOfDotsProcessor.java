package com.hicx.filestats.statistics;

public class NumberOfDotsProcessor implements StatisticProcessor {

    private int numberOfDots = 0;

    @Override
    public void processFileChunk(String fileChunk) {
        if (fileChunk == null) {
            return;
        }

        for (int i = 0; i < fileChunk.length(); i++) {
            if (fileChunk.charAt(i) == '.') {
                numberOfDots++;
            }
        }
    }

    @Override
    public String getStatistics() {
        return "Number of dots: " + numberOfDots;
    }
}
