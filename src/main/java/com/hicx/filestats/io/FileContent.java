package com.hicx.filestats.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * File content abstraction allowing reading data from any file (eg.: local, s3 etc.)
 */
public interface FileContent {

    /**
     * @return
     * @throws IOException
     */
    InputStream getContent() throws IOException;

    /**
     * @return
     */
    String getFileName();
}
