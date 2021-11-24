package naloga2v3;

import java.util.Random;

public class ChaCha {

	public static final int KEY_SIZE = 32;

	public static final int NONCE_SIZE = 12;

	private int[] state = new int[16];

	public ChaCha(byte[] key, byte[] nonce, int counter) {
		this.state = initState(key, nonce, counter);
	}

	public int[] initState(byte[] key, byte[] nonce, int counter) {
		int[] initialState = new int[16];

		if (key.length != KEY_SIZE) {
			System.err.println("WRONG KEY SIZE");
			return null;
		}

		// expand 32-byte k konstanta u heksu
		initialState[0] = 0x61707865;
		initialState[1] = 0x3320646e;
		initialState[2] = 0x79622d32;
		initialState[3] = 0x6b206574;

		// kljuc
		for (int i = 0; i < 8; i++) {
			initialState[i + 4] = Formating.bytesLittleEndianToInt32(key, i * 4);
		}

		// Nonce i brojac
		if (nonce.length == NONCE_SIZE) {
			initialState[12] = counter;
			initialState[13] = Formating.bytesLittleEndianToInt32(nonce, 0);
			initialState[14] = Formating.bytesLittleEndianToInt32(nonce, 4);
			initialState[15] = Formating.bytesLittleEndianToInt32(nonce, 8);

			return initialState;
		} else {
			System.err.println("WRONG NONCE SIZE");
			return null;
		}
	}

	public byte[] chachaBlock() {
		int[] workingState = new int[16];
		byte[] out = new byte[64];

		// Kopiraj stejt u working stejt
		for (int i = 0; i < 16; i++) {
			workingState[i] = this.state[i];
		}

		// 10 rundi quraterrounda
		for (int i = 0; i < 10; i++) {
			// Kolone
			ChaChaOperations.QUARTERROUND(workingState, 0, 4, 8, 12);
			ChaChaOperations.QUARTERROUND(workingState, 1, 5, 9, 13);
			ChaChaOperations.QUARTERROUND(workingState, 2, 6, 10, 14);
			ChaChaOperations.QUARTERROUND(workingState, 3, 7, 11, 15);
			// Dijagonale
			ChaChaOperations.QUARTERROUND(workingState, 0, 5, 10, 15);
			ChaChaOperations.QUARTERROUND(workingState, 1, 6, 11, 12);
			ChaChaOperations.QUARTERROUND(workingState, 2, 7, 8, 13);
			ChaChaOperations.QUARTERROUND(workingState, 3, 4, 9, 14);
		}

		// Saberi working state sa pocetnom state matricom
		for (int i = 0; i < 16; i++) {
			workingState[i] += this.state[i];
		}

		// Napuin output buffer
		for (int i = 0; i < 16; i++) {
			Formating.int32ToLittleEndianBytes(out, workingState[i], i * 4);
		}

		return out;

	}

	public byte[] encrypt(byte[] plaintext, byte[] key, byte[] nonce, int counter) {
		this.state = initState(key, nonce, counter);

		int dpos = 0;
		int spos = 0;
		int i = 0;
		byte[] result = new byte[plaintext.length];
		int len = plaintext.length;

		byte[] keystream = new byte[64];
		while (len > 0) {
			keystream = chachaBlock();

			this.state[12] += 1;

			if (len <= 64) {
				for (i = len; i-- > 0;) {
					result[i + dpos] = (byte) (plaintext[i + spos] ^ keystream[i]);
				}
				return result;
			}
			for (i = 64; i-- > 0;) {
				result[i + dpos] = (byte) (plaintext[i + spos] ^ keystream[i]);
			}

			len -= 64;
			spos += 64;
			dpos += 64;
		}

		return null;
	}

	public byte[] decrypt(byte[] ciphertext, byte[] key, byte[] nonce, int counter) {
		this.state = initState(key, nonce, counter);

		byte[] decrypted = encrypt(ciphertext, key, nonce, counter);
		return decrypted;
	}

	public void printState() {
		Formating.printStateAsHex(this.state);
	}

	public static String generateKey() {
		Random r = new Random();
		byte[] key = new byte[KEY_SIZE];
		r.nextBytes(key);

		String keyString = Formating.byte2HexStr(key);
		System.out.println("Key " + keyString);
		return keyString;

	}

	public static String generateNonce() {
		Random r = new Random();
		byte[] nonce = new byte[NONCE_SIZE];
		r.nextBytes(nonce);

		String nonceString = Formating.byte2HexStr(nonce);
		System.out.println(nonceString);
		return nonceString;

	}

}
