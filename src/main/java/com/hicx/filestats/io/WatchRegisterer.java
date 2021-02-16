package com.hicx.filestats.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Registerer reading and monitoring directory and passing file content to given consumer
 *
 */
public interface WatchRegisterer {

    /**
     * Registerers implementations should first:
     * 1. Read currently existing files within sourceDir and pass it to contentConsumer
     * 2. After that monitor directory contents and when new file is recognized pass its content to contentConsumer
     *
     * After successfull consumption move file to "processed" subdirectory
     *
     * @param sourceDir directory to monitor
     * @param contentConsumer consumer of file content
     * @throws IOException
     */
    void registerWatchForLocalDirectory(Path sourceDir, Consumer<FileContent> contentConsumer) throws IOException;
}
