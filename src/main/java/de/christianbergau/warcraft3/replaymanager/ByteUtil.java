package de.christianbergau.warcraft3.replaymanager;

import java.nio.charset.StandardCharsets;

class ByteUtil {
    static int ord(String s) {
        return s.length() > 0 ? (s.getBytes(StandardCharsets.UTF_16LE)[0] & 0xff) : 0;
    }

    static int ord(char c) {
        return c < 0x80 ? c : ord(Character.toString(c));
    }

    /**
     * @TODO Is there a native solution?
     */
    static byte[] getByteArrayFromRange(byte[] buffer, int fromIndex, int length) {
        byte[] result = new byte[length];

        for (int i = 0; i < length; i++) {
            result[i] = buffer[i + fromIndex];
        }

        return result;
    }

    /**
     * @TODO Is there a native solution?
     */
    static String getStringFromByteArray(byte[] buffer, int fromIndex, int length) {
        return new String(getByteArrayFromRange(buffer, fromIndex, length), StandardCharsets.UTF_8);
    }
}
