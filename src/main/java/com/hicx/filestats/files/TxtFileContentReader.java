package com.hicx.filestats.files;

import com.hicx.filestats.io.FileContent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

class TxtFileContentReader implements FileContentReader {

    private final InputStream inputStream;

    TxtFileContentReader(FileContent fileContent) throws IOException {
        this.inputStream = fileContent.getContent();
    }

    @Override
    public void consumeFileInChunks(Consumer<String> fileChunkConsumer) throws IOException {
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                fileChunkConsumer.accept(scanner.nextLine());
            }
        }
    }
}
