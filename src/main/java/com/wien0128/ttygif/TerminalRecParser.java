package com.wien0128.ttygif;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class TerminalRecParser {

    public static class Frame {
        public long timestamp;
        public String content;

        public Frame(long timestamp, String content) {
            this.timestamp = timestamp;
            this.content = content;
        }
    }

    public List<Frame> parse(String ttyRecPath) throws IOException {
        List<Frame> frames = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(ttyRecPath)) {
            ByteBuffer buffer = ByteBuffer.allocate(12);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return frames;
    }
}
