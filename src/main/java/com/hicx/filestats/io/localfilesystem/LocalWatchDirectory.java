package com.hicx.filestats.io.localfilesystem;

import com.hicx.filestats.io.FileContent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * Solution from:
 * https://docs.oracle.com/javase/tutorial/essential/io/notification.html
 */
class LocalWatchDirectory {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;

    private final Path sourceDirPath;
    private final Path targetDirPath;

    private final Consumer<FileContent> contentConsumer;

    LocalWatchDirectory(Path sourceDir, Consumer<FileContent> contentConsumer) throws IOException {
        this.contentConsumer = contentConsumer;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.sourceDirPath = sourceDir;
        this.targetDirPath = sourceDir.resolve("processed");
        File targetDir = targetDirPath.toFile();
        if (targetDir.exists()) {
            targetDir.mkdirs();
        }

        WatchKey key = sourceDirPath.register(watcher, ENTRY_CREATE);
        keys.put(key, sourceDirPath);
    }

    void consumeAlreadyExistingFiles() throws IOException {
        for (File file : sourceDirPath.toFile().listFiles()) {
            notifyConsumerOnFile(file.toPath());
        }
    }

    private void notifyConsumerOnFile(Path sourceFilePath) throws IOException {
        if (Files.isDirectory(sourceFilePath)) {
            return;
        }

        contentConsumer.accept(new FileContent() {

            @Override
            public InputStream getContent() throws IOException {
                return Files.newInputStream(sourceFilePath);
            }

            @Override
            public String getFileName() {
                return sourceFilePath.getFileName().toString();
            }
        });

        Path targetFilePath = targetDirPath.resolve(sourceFilePath.getFileName().toString());
        Files.move(sourceFilePath, targetFilePath);
    }

    /**
     * Process all events for keys queued to the watcher
     *
     * @throws IOException
     */
    void watchDirectoryForNewFiles() throws IOException {
        for (; ; ) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                notifyConsumerOnNewFile(dir, (WatchEvent<Path>) event);
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    private void notifyConsumerOnNewFile(Path dir, WatchEvent<Path> event) throws IOException {
        // Context for directory entry event is the file name of entry
        @SuppressWarnings("unchecked")
        WatchEvent<Path> ev = event;
        Path name = ev.context();
        Path sourceFilePath = dir.resolve(name);

        notifyConsumerOnFile(sourceFilePath);
    }
}
