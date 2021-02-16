package com.hicx.filestats.io.localfilesystem;

import com.hicx.filestats.WaitUtils;
import com.hicx.filestats.io.FileContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.Consumer;

import static com.hicx.filestats.ThreadUtils.wrapInThread;
import static org.assertj.core.api.Assertions.assertThat;

class LocalFilesystemWatchRegistererTest {

    LocalFilesystemWatchRegisterer localFilesystemWatchRegisterer = new LocalFilesystemWatchRegisterer();

    @Test
    void logsFileContentOfExistingAndNewFiles(@TempDir Path tempDir) throws IOException {
        // given
        Files.write(tempDir.resolve("file1"), "file1Content".getBytes(StandardCharsets.UTF_8));
        Files.write(tempDir.resolve("file2"), "file2Content".getBytes(StandardCharsets.UTF_8));
        Collection<String> fileChunks = new ArrayList<>();
        Consumer<FileContent> contentConsumer = fileContent -> fileChunks.add(getContent(fileContent));

        // when
        Thread thread = wrapInThread(() ->
                localFilesystemWatchRegisterer.registerWatchForLocalDirectory(tempDir, contentConsumer)
        );
        thread.start();

        // then check existing
        WaitUtils.wait10Seconds(() -> assertThat(fileChunks.size()).isEqualTo(2));
        assertThat(fileChunks).containsExactlyInAnyOrder("file1Content", "file2Content");

        // then check new files
        Files.write(tempDir.resolve("file3"), "file3Content".getBytes(StandardCharsets.UTF_8));

        WaitUtils.wait10Seconds(() -> assertThat(fileChunks.size()).isEqualTo(3));
        assertThat(fileChunks).containsExactlyInAnyOrder("file1Content", "file2Content", "file3Content");

        thread.interrupt();
    }

    private String getContent(FileContent fileContent) {
        try {
            Scanner s = new Scanner(fileContent.getContent()).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}