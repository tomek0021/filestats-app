package com.hicx.filestats.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface WatchRegisterer {

    void registerWatchForLocalDirectory(Path sourceDir, Consumer<FileContent> contentConsumer) throws IOException;
}
