package naloga2v3;

public class ChaChaOperations {

	public static int L_ROTATE(int x, int d) {
		return (x << d) | (x >>> 32 - d);
	}

	public static void QUARTERROUND(int[] x, int a, int b, int c, int d) {
		x[a] += x[b];
		x[d] = L_ROTATE(x[d] ^ x[a], 16);
		x[c] += x[d];
		x[b] = L_ROTATE(x[b] ^ x[c], 12);
		x[a] += x[b];
		x[d] = L_ROTATE(x[d] ^ x[a], 8);
		x[c] += x[d];
		x[b] = L_ROTATE(x[b] ^ x[c], 7);
	}
}
