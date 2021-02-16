package com.hicx.filestats.files;

import com.hicx.filestats.io.FileContent;

import java.io.IOException;
import java.util.Optional;


public class FileContentReaderFactory {

    /**
     * TODO probably better to move checking if can handle to FileContentReader with some #canHandle(FileContent) method
     */
    public Optional<FileContentReader> createFileContentReader(FileContent fileContent) throws IOException {
        String fileName = fileContent.getFileName();
        if (fileName.endsWith(".txt")) {
            return Optional.of(new TxtFileContentReader(fileContent));
        }

        return Optional.empty();
    }
}
