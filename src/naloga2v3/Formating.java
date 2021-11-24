package naloga2v3;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class Formating {

	public static int bytesLittleEndianToInt32(byte[] array, int i) {
		return (array[i] & 0xff) | ((array[i + 1] & 0xff) << 8) | ((array[i + 2] & 0xff) << 16)
				| ((array[i + 3] & 0xff) << 24);
	}

	public static void int32ToLittleEndianBytes(byte[] input, int number, int offset) {
		input[offset] = (byte) (number);
		input[++offset] = (byte) (number >>> 8);
		input[++offset] = (byte) (number >>> 16);
		input[++offset] = (byte) (number >>> 24);
	}

	public static void printStateAsHex(int[] state) {
		for (int i = 0; i < state.length; i++) {
			if (i % 4 == 0) {
				System.out.println();
			}
			System.out.print(Integer.toHexString(state[i]) + " ");

		}
	}

	public static byte[] hexStr2Byte(String hexStr) {
		int len = hexStr.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexStr.charAt(i), 16) << 4)
					+ Character.digit(hexStr.charAt(i + 1), 16));
		}
		return data;
	}

	final protected static char[] hexArray = "0123456789abcdef".toCharArray();

	public static String byte2HexStr(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String stringToHex(String arg) {
		return String.format("%02x", new BigInteger(1, arg.getBytes(StandardCharsets.UTF_8)));
	}
}
