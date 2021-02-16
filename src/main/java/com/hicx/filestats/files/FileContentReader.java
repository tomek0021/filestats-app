package com.hicx.filestats.files;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Abstraction allowing reading contents of various files.
 *
 * Use {@link FileContentReaderFactory} to create concrete instances.
 */
public interface FileContentReader {

    /**
     * Chunk can be any piece of file that can be easily handled by processors - its up to implementation to decide on that
     *
     * @param fileChunkConsumer
     * @throws IOException
     */
    void consumeFileInChunks(Consumer<String> fileChunkConsumer) throws IOException;
}
