package com.hicx.filestats.io.localfilesystem;

import com.hicx.filestats.io.FileContent;
import com.hicx.filestats.io.WatchRegisterer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class LocalFilesystemWatchRegisterer implements WatchRegisterer {

    public void registerWatchForLocalDirectory(Path sourceDir, Consumer<FileContent> contentConsumer) throws IOException {
        LocalWatchDirectory localWatchDirectory = new LocalWatchDirectory(sourceDir, contentConsumer);
        localWatchDirectory.consumeAlreadyExistingFiles();
        localWatchDirectory.watchDirectoryForNewFiles();
    }


}
