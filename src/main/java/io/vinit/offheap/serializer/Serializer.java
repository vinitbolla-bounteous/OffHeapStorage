package io.vinit.offheap.serializer;


import java.nio.ByteBuffer;


/**
 * Fixed-size serializer for MVP.
 * Implementations MUST:
 * - return a constant {@code fixedSize()}.
 * - read/write using absolute 0..fixedSize region of the given buffer.
 */
public interface Serializer<E> {
    int fixedSize();
    void serialize(E element, ByteBuffer buffer);
    E deserialize(ByteBuffer buffer);
}