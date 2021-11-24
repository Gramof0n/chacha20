package naloga2v3;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Main {

	public static void main(String[] args) {

		Random rand = new Random();

		byte[] key = new byte[ChaCha.KEY_SIZE];
		rand.nextBytes(key);
		byte[] nonce = new byte[ChaCha.NONCE_SIZE];
		rand.nextBytes(nonce);

		ChaCha cha = new ChaCha(key, nonce, 0);

		System.out.println(" _____ _           _____ _            _____  _____ \r\n"
				+ "/  __ \\ |         /  __ \\ |          / __  \\|  _  |\r\n"
				+ "| /  \\/ |__   __ _| /  \\/ |__   __ _ `' / /'| |/' |\r\n"
				+ "| |   | '_ \\ / _` | |   | '_ \\ / _` |  / /  |  /| |\r\n"
				+ "| \\__/\\ | | | (_| | \\__/\\ | | | (_| |./ /___\\ |_/ /\r\n"
				+ " \\____/_| |_|\\__,_|\\____/_| |_|\\__,_|\\_____/ \\___/ \r\n"
				+ "                                                   \r\n"
				+ "                                                   ");

		System.out.println("Initial state:");
		cha.printState();
		System.out.println("\n=========================================================");
		System.out.println();

		System.out.println("Generated key: ");
		System.out.println(Formating.byte2HexStr(key));
		System.out.println("=========================================================");
		System.out.println();

		System.out.println("Generated nonce: ");
		System.out.println(Formating.byte2HexStr(nonce));
		System.out.println("=========================================================");
		System.out.println();

		String plain = "Ok ovo je plaintext dosta dugacak dosta opasan grrr plejntekst kolje sve pred sobom";

		long pre = System.nanoTime();
		byte[] enc = cha.encrypt(plain.getBytes(), key, nonce, 0);
		long post = System.nanoTime();

		System.out.println("Vrijeme u nanosec: " + (post - pre));

		byte[] dec = cha.decrypt(enc, key, nonce, 0);

		System.out.println("Encrypted plaintext as hex string: ");
		System.out.println(new String(enc, StandardCharsets.UTF_8));
		System.out.println("=========================================================");
		System.out.println();
		System.out.println("Decrypted ciphertext");
		System.out.println(new String(dec, StandardCharsets.UTF_8));

	}

}
