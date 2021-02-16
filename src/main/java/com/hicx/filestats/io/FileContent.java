package com.hicx.filestats.io;

import java.io.IOException;
import java.io.InputStream;

public interface FileContent {

    InputStream getContent() throws IOException;

    String getFileName();
}
