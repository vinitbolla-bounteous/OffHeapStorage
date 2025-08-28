package io.vinit.offheap;

import io.vinit.offheap.serializer.Serializers;

public class App {
    public static void main(String[] args) throws Exception {
        // Example with Integer
        try (OffHeapList<Integer> intList = new OffHeapList<>("src/main/java/io/vinit/offheap/storage/data_int.offheap", 1024 * 1024, Serializers.INT)) {

            for (int i = 0; i < 10; i++) {
                intList.add(i * 10);
            }

            for (int i = 0; i < intList.size(); i++) {
                System.out.println("INT Index " + i + " = " + intList.get(i));
            }
        }

        // Example with Long
        try (OffHeapList<Long> longList = new OffHeapList<>("src/main/java/io/vinit/offheap/storage/data_long.offheap", 1024 * 1024, Serializers.LONG)) {

            for (long i = 0; i < 10; i++) {
                longList.add(i * 10000000000L);
            }

            for (int i = 0; i < longList.size(); i++) {
                System.out.println("LONG Index " + i + " = " + longList.get(i));
            }
        }
    }
}