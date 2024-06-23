package com.wien0128.ttygif;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface LibC extends Library {
    LibC INSTANCE = Native.load(Platform.isWindows() ? "msvcrt" : "c", LibC.class);

    int open(String path, int flags, int mode);
    int close(int fd);
    int write(int fd, byte[] buffer, int count);
}
