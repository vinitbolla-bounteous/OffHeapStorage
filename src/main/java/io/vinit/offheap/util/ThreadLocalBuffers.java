package io.vinit.offheap.util;


import java.nio.ByteBuffer;


public final class ThreadLocalBuffers {
    private ThreadLocalBuffers() {}


    private static final ThreadLocal<ByteBuffer> TL = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(0));


    public static ByteBuffer get(int minCapacity) {
        ByteBuffer buf = TL.get();
        if (buf.capacity() < minCapacity || !buf.isDirect()) {
            buf = ByteBuffer.allocateDirect(minCapacity);
            TL.set(buf);
        }
        buf.clear();
        buf.limit(minCapacity);
        return buf;
    }
}