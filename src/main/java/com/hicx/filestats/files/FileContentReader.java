package com.hicx.filestats.files;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public interface FileContentReader {

    void consumeFileInChunks(Consumer<String> fileChunkConsumer) throws IOException;
}
