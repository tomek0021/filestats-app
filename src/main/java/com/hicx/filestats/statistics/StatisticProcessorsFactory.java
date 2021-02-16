package com.hicx.filestats.statistics;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Factory object for {@link StatisticProcessor}
 */
public class StatisticProcessorsFactory {

    public List<StatisticProcessor> createProcessors() {
        return asList(
                new NumberOfWordsProcessor(),
                new NumberOfDotsProcessor(),
                new MostUsedWordProcessor()
        );
    }
}
