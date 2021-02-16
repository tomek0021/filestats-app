package com.hicx.filestats.statistics;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MostUsedWordProcessorTest {

    private MostUsedWordProcessor mostUsedWordProcessor = new MostUsedWordProcessor();

    @Test
    public void forEmptyTextReturnsNA() {
        String emptyText = "";

        mostUsedWordProcessor.processFileChunk(emptyText);

        assertThat(mostUsedWordProcessor.getStatistics())
                .isEqualTo("Most used word stat not applicable");
    }

    @Test
    public void whenMoreThanOneMostUsedReturnInAlphabetical() {
        String text = "wordb worda worda wordb wordc";

        mostUsedWordProcessor.processFileChunk(text);

        assertThat(mostUsedWordProcessor.getStatistics())
                .isEqualTo("Most used word: worda");
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "word;word",
            "word word word1;word",
            "word word word word1 word1;word",
            "word word1 word word1 word word2;word",
    })
    public void findMostUsedWord(String text, String expectedMostUsedWord) {
        // given text

        // when
        mostUsedWordProcessor.processFileChunk(text);

        // then
        assertThat(mostUsedWordProcessor.getStatistics())
                .isEqualTo("Most used word: " + expectedMostUsedWord);
    }
}