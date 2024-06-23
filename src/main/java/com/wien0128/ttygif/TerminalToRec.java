package com.wien0128.ttygif;

import java.io.IOException;

public class TerminalToRec {
    private static final int O_WRONLY = 0x1;
    private static final int O_CREAT = 0x40;
    private static final int O_TRUNC = 0x200;
    private static final int S_IRUSR = 0x100;
    private static final int S_IWUSR = 0x80;

    private int fd;

    public TerminalToRec(String path) throws IOException {
        fd = LibC.INSTANCE.open(path, O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
        if (fd < 0) {
            throw new IOException("Failed to open file");
        }
    }

    public void write(String data) throws IOException {
        byte[] buffer = data.getBytes();
        int result = LibC.INSTANCE.write(fd, buffer, buffer.length);
        if (result < 0) {
            throw new IOException("Failed to write to file");
        }
    }

    public void close() throws IOException {
        int result = LibC.INSTANCE.close(fd);
        if (result < 0) {
            throw new IOException("Failed to close file");
        }
    }
}