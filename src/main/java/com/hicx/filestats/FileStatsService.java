package com.hicx.filestats;

import com.hicx.filestats.files.FileContentReader;
import com.hicx.filestats.files.FileContentReaderFactory;
import com.hicx.filestats.io.FileContent;
import com.hicx.filestats.io.WatchRegisterer;
import com.hicx.filestats.statistics.StatisticProcessor;
import com.hicx.filestats.statistics.StatisticProcessorsFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class FileStatsService {

    private final StatisticProcessorsFactory statisticProcessorsFactory;
    private final FileContentReaderFactory fileContentReaderFactory;
    private final WatchRegisterer watchRegisterer;

    public FileStatsService(StatisticProcessorsFactory statisticProcessorsFactory,
                            FileContentReaderFactory fileContentReaderFactory,
                            WatchRegisterer watchRegisterer) {
        this.statisticProcessorsFactory = statisticProcessorsFactory;
        this.fileContentReaderFactory = fileContentReaderFactory;
        this.watchRegisterer = watchRegisterer;
    }

    public void logFileStats(Path sourceDir) {
        Consumer<FileContent> contentConsumer = fileContent -> {
            try {
                List<StatisticProcessor> processors = statisticProcessorsFactory.createProcessors();
                Optional<FileContentReader> contentReaderOptional = fileContentReaderFactory.createFileContentReader(fileContent);
                if (contentReaderOptional.isPresent()) {
                    logFileStats(processors, contentReaderOptional.get());
                }

            } catch (IOException e) {
                handleIOException(e);
            }
        };

        try {
            watchRegisterer.registerWatchForLocalDirectory(sourceDir, contentConsumer);
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void logFileStats(List<StatisticProcessor> processors, FileContentReader fileContentReader) throws IOException {
        fileContentReader.consumeFileInChunks(fileChunk ->
                processors.forEach(processor -> processor.processFileChunk(fileChunk))
        );

        processors.forEach(p -> System.out.println(p.getStatistics()));
    }

    private void handleIOException(IOException e) {
        e.printStackTrace();
    }
}
