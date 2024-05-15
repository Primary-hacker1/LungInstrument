package com.just.machine.util;


import android.util.Log;

import java.nio.ByteBuffer;

/**
 * 美灵思心电算法
 */
public class ByteBuffUtils {

    public static synchronized void addBytes(ByteBuffer packetBuff, byte[] bytes) {

        if (bytes != null) {
            addBytes(packetBuff, bytes, bytes.length);
        }
    }

    public static synchronized void addBytes(ByteBuffer packetBuff, byte[] addBytes, int len) {
        if (addBytes != null) {
            packetBuff.compact();

            if (len > packetBuff.remaining()) {
                Log.e("ByteBuffUtils",String.format("Buffer is Overflow remaining:%d  addlen:%d ",
                        packetBuff.remaining(), len));
                packetBuff.clear();
            }

            packetBuff.put(addBytes, 0, len);
            packetBuff.flip();
        }
    }

    public static synchronized int getSize(ByteBuffer packetBuff) {
        return packetBuff.remaining();
    }

    public static synchronized byte[] getBytes(ByteBuffer packetBuff, int getSize) {
        byte[] bs = null;
        if (packetBuff.remaining() >= getSize) {
            bs = new byte[getSize];
            packetBuff.get(bs);
        }
        return bs;
    }

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);


        byte[] bytes = new byte[9];
        addBytes(buffer, bytes);
        addBytes(buffer, bytes);
        addBytes(buffer, bytes);
    }
}
