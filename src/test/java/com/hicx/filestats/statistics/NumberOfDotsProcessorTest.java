package com.hicx.filestats.statistics;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class NumberOfDotsProcessorTest {

    private NumberOfDotsProcessor numberOfDotsProcessor = new NumberOfDotsProcessor();

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            ";0",
            "word;0",
            "word\t12,[];0",
            ".;1",
            "..;2",
            "...;3",
            "word1.word2.word3;2",
            "...word1.word2.word3...;8"
    })
    public void countDots(String text, int expectedNumberOfWords) {
        // given text

        // when
        numberOfDotsProcessor.processFileChunk(text);

        // then
        assertThat(numberOfDotsProcessor.getStatistics())
                .isEqualTo("Number of dots: " + expectedNumberOfWords);
    }

    @Test
    public void countDotsOnMultipleInvocations() {
        // given
        String text1 = "word", text2 = ".", text3 = "word. word", text4 = ". word...word.";

        // when
        numberOfDotsProcessor.processFileChunk(text1);
        numberOfDotsProcessor.processFileChunk(text2);
        numberOfDotsProcessor.processFileChunk(text3);
        numberOfDotsProcessor.processFileChunk(text4);

        // then
        assertThat(numberOfDotsProcessor.getStatistics())
                .isEqualTo("Number of dots: 7");
    }
}