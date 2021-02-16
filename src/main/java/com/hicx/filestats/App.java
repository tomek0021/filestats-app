package com.hicx.filestats;


import com.hicx.filestats.files.FileContentReaderFactory;
import com.hicx.filestats.io.WatchRegisterer;
import com.hicx.filestats.io.localfilesystem.LocalFilesystemWatchRegisterer;
import com.hicx.filestats.statistics.StatisticProcessorsFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App {

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("There must be argument with directory passed");
        }

        Path sourceDir = Paths.get(args[0]);

        StatisticProcessorsFactory statisticProcessorsFactory = new StatisticProcessorsFactory();
        FileContentReaderFactory fileContentReaderFactory = new FileContentReaderFactory();
        WatchRegisterer watchRegisterer = new LocalFilesystemWatchRegisterer();

        FileStatsService fileStatsService = new FileStatsService(
                statisticProcessorsFactory,
                fileContentReaderFactory,
                watchRegisterer
        );

        fileStatsService.logFileStats(sourceDir);
    }
}
