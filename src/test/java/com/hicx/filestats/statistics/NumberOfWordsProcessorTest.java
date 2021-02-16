package com.hicx.filestats.statistics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberOfWordsProcessorTest {

    private NumberOfWordsProcessor numberOfWordsProcessor = new NumberOfWordsProcessor();


    @ParameterizedTest
    @ValueSource(strings = {
            "",
            " ",
            "\t",
            " \t",
            " \t \t"
    })
    public void countAsZeroEmptyOrBlankString(String text) {
        // given text

        // when
        numberOfWordsProcessor.processFileChunk(text);

        // then
        assertThat(numberOfWordsProcessor.getStatistics())
                .isEqualTo("Number of words: 0");
    }

    @Test
    public void countAsOneSingleWord() {
        String text = "word";

        numberOfWordsProcessor.processFileChunk(text);

        assertThat(numberOfWordsProcessor.getStatistics())
                .isEqualTo("Number of words: 1");
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "word1 word2;2",
            "word1  word2;2",
            "word1\tword2;2",
            "word1\t\tword2;2"
    })
    public void wordDividerIsSpaceOrTabCharacter(String text, int expectedNumberOfWords) {
        // given text

        // when
        numberOfWordsProcessor.processFileChunk(text);

        // then
        assertThat(numberOfWordsProcessor.getStatistics())
                .isEqualTo("Number of words: " + expectedNumberOfWords);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "word1 word2 word3;3",
            "word1\tword2\tword3;3",
            "word1 word2\tword3;3",
            "word1 word2\tword3 \tword4\t word5;5",
    })
    public void countWordsProperly(String text, int expectedNumberOfWords) {
        // given text

        // when
        numberOfWordsProcessor.processFileChunk(text);

        // then
        assertThat(numberOfWordsProcessor.getStatistics())
                .isEqualTo("Number of words: " + expectedNumberOfWords);
    }

    @Test
    public void countWordsProperlyForMultipleInvocations() {
        // given
        String text1 = "word1 word2", text2 = "word3", text3 = "word4\tword5", text4 = "word6 word7\tword8";

        // when
        numberOfWordsProcessor.processFileChunk(text1);
        numberOfWordsProcessor.processFileChunk(text2);
        numberOfWordsProcessor.processFileChunk(text3);
        numberOfWordsProcessor.processFileChunk(text4);

        // then
        assertThat(numberOfWordsProcessor.getStatistics())
                .isEqualTo("Number of words: 8");
    }
}