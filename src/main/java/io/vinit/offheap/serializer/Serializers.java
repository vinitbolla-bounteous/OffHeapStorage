package io.vinit.offheap.serializer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class Serializers {
    private Serializers() {}

    public static final Serializer<Integer> INT = new Serializer<>() {
        @Override public int fixedSize() { return Integer.BYTES; }

        public void serialize(Integer v, ByteBuffer b) {
            b.order(ByteOrder.LITTLE_ENDIAN);
            b.putInt(v);
        }

        public Integer deserialize(ByteBuffer b) {
            b.order(ByteOrder.LITTLE_ENDIAN);
            return b.getInt();
        }
    };

    public static final Serializer<Long> LONG = new Serializer<>() {
        @Override public int fixedSize() { return Long.BYTES; }

        @Override
        public void serialize(Long v, ByteBuffer b) {
            b.order(ByteOrder.LITTLE_ENDIAN);
            b.putLong(v);
        }

        @Override
        public Long deserialize(ByteBuffer b) {
            b.order(ByteOrder.LITTLE_ENDIAN);
            return b.getLong();
        }
    };
}
