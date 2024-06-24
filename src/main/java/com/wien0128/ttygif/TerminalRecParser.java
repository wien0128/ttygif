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

            while (fis.read(buffer.array()) != -1) {
                long seconds = buffer.getInt(0);
                long microseconds = buffer.getInt(4);
                int length = buffer.getInt(8);

                byte[] data = new byte[length];
                fis.read(data);

                long timestamp = seconds * 1000 + microseconds / 1000;
                String content = new String(data);

                frames.add(new Frame(timestamp, content));
                buffer.clear();
            }
        }
        return frames;
    }
}
