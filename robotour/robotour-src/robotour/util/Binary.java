package robotour.util;

import java.util.List;

/**
 *
 * @author Kotuc
 */
public class Binary {

    public static byte[] toArray(List<Byte> buffer) {
        byte[] bytes = new byte[buffer.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = buffer.get(i);
        }
        return bytes;
    }

    public static short toInt16(byte[] bytes, int pos) {
        return toInt16(bytes[pos + 0], bytes[pos + 1]);
    }

    public static short toInt16Little(byte[] bytes, int pos) {
        return toInt16(bytes[pos + 1], bytes[pos + 0]);
    }

    public static short toInt16(byte high, byte low) {
        return (short) (((0xFF & high) << 8) | ((0xFF & low) << 0));
    }

    public static int toInt32(byte[] bytes, int pos) {
        return toInt32(bytes[pos + 0], bytes[pos + 1], bytes[pos + 2], bytes[pos + 3]);
    }

    public static int toInt32Little(byte[] bytes, int pos) {
        return toInt32(bytes[pos + 3], bytes[pos + 2], bytes[pos + 1], bytes[pos + 0]);
    }

    public static int toInt32(byte b3, byte b2, byte b1, byte b0) {
        return ((0xFF & b3) << 24) | ((0xFF & b2) << 16) | ((0xFF & b1) << 8) | ((0xFF & b0) << 0);
    }

    public static byte highByte(int word) {
        return (byte) ((word >> 8) & 0xFF);
    }

    public static byte lowByte(int word) {
        return (byte) (0xFF & word);
    }

    public static byte getByte(int word, int b) {
        return (byte) ((word >> (8 * b)) & 0xFF);
    }
}
