package com.hicx.filestats;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.hicx.filestats.ThreadUtils.wrapInThread;
import static org.assertj.core.api.Assertions.assertThat;

class AppIntegrationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void logsFileContentOfExistingAndNewFiles(@TempDir Path tempDir) throws IOException {
        // given
        Files.write(tempDir.resolve("file0.ttt"), "file0Content".getBytes(StandardCharsets.UTF_8));
        Files.write(tempDir.resolve("file1.txt"), "file1 file1 content.".getBytes(StandardCharsets.UTF_8));
        Files.write(tempDir.resolve("file2.txt"), "file2 file2 content..".getBytes(StandardCharsets.UTF_8));

        // when
        Thread thread = wrapInThread(() ->
                App.main(new String[] {tempDir.toString()})
        );
        thread.start();

        // then check existing
        WaitUtils.wait10Seconds(() -> assertThat(outContent.toString()).isEqualTo(
                "Number of words: 3\nNumber of dots: 1\nMost used word: file1\n" +
                "Number of words: 3\nNumber of dots: 2\nMost used word: file2\n"
        ));

        // then check new files
        Files.write(tempDir.resolve("file3.txt"), "file3 file3 content...".getBytes(StandardCharsets.UTF_8));
        WaitUtils.wait10Seconds(() -> assertThat(outContent.toString()).isEqualTo(
                "Number of words: 3\nNumber of dots: 1\nMost used word: file1\n" +
                "Number of words: 3\nNumber of dots: 2\nMost used word: file2\n" +
                "Number of words: 3\nNumber of dots: 3\nMost used word: file3\n"
        ));
        assertThat(tempDir)
                .isDirectoryNotContaining(path -> path.getFileName().toString().endsWith("file0.ttt"))
                .isDirectoryNotContaining(path -> path.getFileName().toString().endsWith("file1.txt"))
                .isDirectoryNotContaining(path -> path.getFileName().toString().endsWith("file2.txt"))
                .isDirectoryNotContaining(path -> path.getFileName().toString().endsWith("file3.txt"));
        assertThat(tempDir.resolve("processed"))
                .isDirectoryContaining(path -> path.getFileName().toString().endsWith("file0.ttt"))
                .isDirectoryContaining(path -> path.getFileName().toString().endsWith("file1.txt"))
                .isDirectoryContaining(path -> path.getFileName().toString().endsWith("file2.txt"))
                .isDirectoryContaining(path -> path.getFileName().toString().endsWith("file3.txt"));

        thread.interrupt();
    }
}