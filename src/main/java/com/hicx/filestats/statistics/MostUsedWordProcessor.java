package com.hicx.filestats.statistics;

import java.util.*;

class MostUsedWordProcessor implements StatisticProcessor {

    private Map<String, Integer> wordUsage = new HashMap<>();

    @Override
    public void processFileChunk(String fileChunk) {
        StringTokenizer tokens = new StringTokenizer(fileChunk);
        while (tokens.hasMoreTokens()) {
            String word = tokens.nextToken();
            wordUsage.compute(word, (k, v) -> (v == null) ? 1 : v + 1);
        }
    }

    @Override
    public String getStatistics() {
        if (wordUsage.isEmpty()) {
            return "Most used word stat not applicable";
        }

        return "Most used word: " + getMostUsedWord();
    }

    private String getMostUsedWord() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordUsage.entrySet());
        list.sort((c1, c2) -> {
            int valueCompare = c2.getValue().compareTo(c1.getValue());
            if (valueCompare == 0) {
                return c1.getKey().compareTo(c2.getKey());
            }
            return valueCompare;
        });
        String mostUsedWord = list.iterator().next().getKey();
        return mostUsedWord;
    }
}
