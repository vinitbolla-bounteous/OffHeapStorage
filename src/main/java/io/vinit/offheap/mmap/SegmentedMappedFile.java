package io.vinit.offheap.mmap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class SegmentedMappedFile implements AutoCloseable {

    private static final long MAX_SEGMENT_SIZE = Integer.MAX_VALUE;
    private final FileChannel channel;
    private final List<MappedByteBuffer> segments = new ArrayList<>();
    private final long totalSize;
    private final int segmentCount;

    public SegmentedMappedFile(String path, long size) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            file.createNewFile();
        }

        this.totalSize = size;
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(size);
        this.channel = raf.getChannel();
        this.segmentCount = (int) ((size + MAX_SEGMENT_SIZE - 1) / MAX_SEGMENT_SIZE);

        for (int i = 0; i < segmentCount; i++) {
            long offset = i * MAX_SEGMENT_SIZE;
            long remaining = size - offset;
            long segSize = Math.min(remaining, MAX_SEGMENT_SIZE);
            MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE, offset, segSize);
            segments.add(buf);
        }
    }

//    private MappedByteBuffer bufferFor(long pos) {
//        int segIndex = (int) (pos / MAX_SEGMENT_SIZE);
//        int segOffset = (int) (pos % MAX_SEGMENT_SIZE);
//        MappedByteBuffer buf = segments.get(segIndex);
//        buf.position(segOffset);
//        return buf;
//    }

//    public void putByte(long pos, byte b) {
//        bufferFor(pos).put(b);
//    }
//
//    public byte getByte(long pos) {
//        return bufferFor(pos).get();
//    }
//
//    public void putInt(long pos, int value) {
//        bufferFor(pos).putInt(value);
//    }
//
//    public int getInt(long pos) {
//        return bufferFor(pos).getInt();
//    }
//
//    public void putLong(long pos, long value) {
//        bufferFor(pos).putLong(value);
//    }
//
//    public long getLong(long pos) {
//        return bufferFor(pos).getLong();
//    }
//
//    public void putBytes(long pos, byte[] data) {
//        bufferFor(pos).put(data);
//    }
//
//    public void getBytes(long pos, byte[] dest) {
//        bufferFor(pos).get(dest);
//    }

    public long size() {
        return totalSize;
    }

    public ByteBuffer slice(long offset, int size) {
        int segIndex = (int) (offset / MAX_SEGMENT_SIZE);
        int segOffset = (int) (offset % MAX_SEGMENT_SIZE);
        MappedByteBuffer segment = segments.get(segIndex).duplicate();
        segment.position(segOffset);
        segment.limit(segOffset + size);
        return segment.slice();
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}