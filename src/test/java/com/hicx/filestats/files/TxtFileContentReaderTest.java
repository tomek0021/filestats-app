package com.hicx.filestats.files;

import com.hicx.filestats.io.FileContent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class TxtFileContentReaderTest {

    FileContent fileContent = mock(FileContent.class);

    @Test
    void notifyConsumerPerEachLine() throws IOException {
        // given
        String text = "chunk1\nchunk2\nchunk3";
        InputStream targetStream = new ByteArrayInputStream(text.getBytes());
        when(fileContent.getContent()).thenReturn(targetStream);
        TxtFileContentReader txtFileContentReader = new TxtFileContentReader(fileContent);
        Consumer<String> fileChunkConsumer = mock(Consumer.class);

        // when
        txtFileContentReader.consumeFileInChunks(fileChunkConsumer);

        // then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(fileChunkConsumer, times(3)).accept(captor.capture());
        assertThat(captor.getAllValues()).containsExactly("chunk1", "chunk2", "chunk3");
    }
}