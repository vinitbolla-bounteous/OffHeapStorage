package io.vinit.offheap;

import io.vinit.offheap.mmap.SegmentedMappedFile;
import io.vinit.offheap.serializer.Serializer;
import java.nio.ByteBuffer;

public class OffHeapList<E> implements AutoCloseable {
    private final SegmentedMappedFile storage;
    private final Serializer<E> serializer;
    private final int recordSize;
    private long size = 0;

    public OffHeapList(String filePath, long capacityBytes, Serializer<E> serializer) throws Exception {
        this.storage = new SegmentedMappedFile(filePath, capacityBytes);
        this.serializer = serializer;
        this.recordSize = serializer.fixedSize();
    }

    public void add(E element) {
        long offset = size * recordSize;
        ByteBuffer buf = storage.slice(offset, recordSize);
        serializer.serialize(element, buf);
        size++;
    }

    public E get(long index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index);
        }
        long offset = index * recordSize;
        ByteBuffer buf = storage.slice(offset, recordSize);
        return serializer.deserialize(buf);
    }

    public long size() {
        return size;
    }

    @Override
    public void close() throws Exception {
        storage.close();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (long i = 0; i < size; i++) {
            sb.append(get(i));
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
