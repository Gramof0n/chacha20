package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FilenameUtils;

import naloga2v3.ChaCha;
import naloga2v3.Formating;

public class GUI {

	private JFrame frame;
	private JTextField txtKey;
	private JTextField txtNonce;
	private JSeparator separator;
	private JButton btnGenerate;
	private JButton btnEncrypt;
	private JButton btnDecrypt;
	private JLabel lblNewLabel_1;
	private JButton btnUpload;
	private JLabel lblNewLabel_2;
	private JTextField txtFilePath;
	private JSeparator separator_1;
	private JLabel lblEncCompleted;
	private JButton btnSaveEncrypted;

	private ChaCha cha;
	private byte[] fileBytes;
	private byte[] encrypted;
	private byte[] decrypted;
	private File selectedFile;
	private JButton btnSaveDecrypted;
	private JLabel lblDecryptionComplete;
	private JButton btnSaveToFile;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JButton btnLoadFromFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 556, 282);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Key:");
		lblNewLabel.setBounds(10, 94, 64, 13);
		frame.getContentPane().add(lblNewLabel);

		txtKey = new JTextField();
		txtKey.setToolTipText("64 bit hex or 32 bit string");
		txtKey.setBounds(65, 91, 467, 19);
		frame.getContentPane().add(txtKey);
		txtKey.setColumns(10);

		txtNonce = new JTextField();
		txtNonce.setColumns(10);
		txtNonce.setBounds(65, 120, 467, 19);
		frame.getContentPane().add(txtNonce);

		JLabel lblGeneratedNonce = new JLabel("Nonce:");
		lblGeneratedNonce.setBounds(10, 123, 64, 13);
		frame.getContentPane().add(lblGeneratedNonce);

		separator = new JSeparator();
		separator.setBounds(10, 83, 522, 2);
		frame.getContentPane().add(separator);

		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(162, 149, 113, 21);
		frame.getContentPane().add(btnGenerate);

		btnEncrypt = new JButton("Encrypt and save");
		btnEncrypt.setBounds(162, 197, 142, 21);
		frame.getContentPane().add(btnEncrypt);

		btnDecrypt = new JButton("Decrypt and save");
		btnDecrypt.setBounds(339, 197, 142, 21);
		frame.getContentPane().add(btnDecrypt);

		lblNewLabel_1 = new JLabel("Choose file:");
		lblNewLabel_1.setBounds(10, 14, 123, 13);
		frame.getContentPane().add(lblNewLabel_1);

		btnUpload = new JButton("Browse");
		btnUpload.setBounds(162, 10, 370, 21);
		frame.getContentPane().add(btnUpload);

		lblNewLabel_2 = new JLabel("File path: ");
		lblNewLabel_2.setBounds(10, 53, 99, 13);
		frame.getContentPane().add(lblNewLabel_2);

		txtFilePath = new JTextField();
		txtFilePath.setEditable(false);
		txtFilePath.setBounds(162, 50, 370, 19);
		frame.getContentPane().add(txtFilePath);
		txtFilePath.setColumns(10);

		separator_1 = new JSeparator();
		separator_1.setBounds(10, 185, 522, 2);
		frame.getContentPane().add(separator_1);

		btnSaveToFile = new JButton("Save to file");
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSaveToFile.setBounds(287, 149, 113, 21);
		frame.getContentPane().add(btnSaveToFile);

		lblNewLabel_3 = new JLabel("Key, nonce operations: ");
		lblNewLabel_3.setBounds(10, 153, 142, 13);
		frame.getContentPane().add(lblNewLabel_3);

		lblNewLabel_4 = new JLabel("Encrypt and decrypt:");
		lblNewLabel_4.setBounds(10, 201, 123, 13);
		frame.getContentPane().add(lblNewLabel_4);

		btnLoadFromFile = new JButton("Load from file");
		btnLoadFromFile.setBounds(412, 149, 113, 21);
		frame.getContentPane().add(btnLoadFromFile);

		generateKeyAndNonce(btnGenerate, txtKey, txtNonce);
		saveKeyNonceToFile(btnSaveToFile);
		loadKeyNonceFromFile(btnLoadFromFile, txtKey, txtNonce);

		openFilePicker(btnUpload, txtFilePath);
		encrypt(btnEncrypt);
		decrypt(btnDecrypt);

	}

	public void generateKeyAndNonce(JButton btn, JTextField key, JTextField nonce) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				key.setText(ChaCha.generateKey());
				nonce.setText(ChaCha.generateNonce());

			}

		});
	}

	public void openFilePicker(JButton btn, JTextField txt) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				int r = jfc.showOpenDialog(null);

				if (r == JFileChooser.APPROVE_OPTION) {
					selectedFile = jfc.getSelectedFile();
					txt.setText(selectedFile.getAbsolutePath());
					try {
						fileBytes = Files.readAllBytes(selectedFile.toPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}

		});
	}

	public void encrypt(JButton btn) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileBytes == null) {
					JOptionPane.showMessageDialog(frame, "Upload file to encrypt", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!validateKeyNonce())
					return;

				String key = txtKey.getText();
				String nonce = txtNonce.getText();

				if (key.length() == 32) {
					key = Formating.stringToHex(key);
				}

				if (nonce.length() == 12 || nonce.length() == 8) {
					nonce = Formating.stringToHex(nonce);
				}

				System.out.println("=================IZ ENCRYPTA=================");
				System.out.println("KLJUC: " + key);
				System.out.println("NONCE: " + nonce);
				System.out.println("=============================================");

				byte[] keyBytes = Formating.hexStr2Byte(key);
				byte[] nonceBytes = Formating.hexStr2Byte(nonce);

				cha = new ChaCha(keyBytes, nonceBytes, 0);

				long pre = System.nanoTime();
				encrypted = cha.encrypt(fileBytes, keyBytes, nonceBytes, 0);
				long post = System.nanoTime();

				double timePassedInS = (double) (post - pre) / 1000000000.0;
				double cyclesPerByteQuestionmark = timePassedInS / fileBytes.length;

				System.out.println("Encryption time in seconds: " + timePassedInS);
				System.out.println("Ciklusi po bajtu?: " + cyclesPerByteQuestionmark);

				JOptionPane.showMessageDialog(frame, "File encrypted  successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);

				saveEncrypted();
			}

		});
	}

	public void decrypt(JButton btn) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileBytes == null) {
					JOptionPane.showMessageDialog(frame, "Upload file to encrypt", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!validateKeyNonce())
					return;

				String key = txtKey.getText();
				String nonce = txtNonce.getText();

				if (key.length() == 32) {
					key = Formating.stringToHex(key);
				}

				if (nonce.length() == 12 || nonce.length() == 8) {
					nonce = Formating.stringToHex(nonce);
				}

				System.out.println("=================IZ DECRYPTA=================");
				System.out.println("KLJUC: " + key);
				System.out.println("NONCE: " + nonce);
				System.out.println("=============================================");

				byte[] keyBytes = Formating.hexStr2Byte(key);
				byte[] nonceBytes = Formating.hexStr2Byte(nonce);

				cha = new ChaCha(keyBytes, nonceBytes, 0);

				long pre = System.nanoTime();
				decrypted = cha.encrypt(fileBytes, keyBytes, nonceBytes, 0);
				long post = System.nanoTime();

				double timePassedInS = (double) (post - pre) / 1000000000.0;
				double cyclesPerByteQuestionmark = timePassedInS / fileBytes.length;

				System.out.println("Decryption time in seconds: " + timePassedInS);
				System.out.println("Ciklusi po bajtu?: " + cyclesPerByteQuestionmark);

				JOptionPane.showMessageDialog(frame, "File decrypted successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);

				saveDecrypted();
			}

		});
	}

	public void saveKeyNonceToFile(JButton btn) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String key = txtKey.getText();
				String nonce = txtNonce.getText();

				if (!validateKeyNonce())
					return;

				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

				jfc.showSaveDialog(null);

				try {
					PrintWriter writer = new PrintWriter(jfc.getSelectedFile().toString() + ".txt");
					writer.println(key);
					writer.println(nonce);
					writer.close();
				} catch (FileNotFoundException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

			}

		});

	}

	public void loadKeyNonceFromFile(JButton btn, JTextField key, JTextField nonce) {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.addChoosableFileFilter(filter);

				int f = jfc.showOpenDialog(null);

				if (f == JFileChooser.APPROVE_OPTION) {
					try {
						List<String> lines = Files.readAllLines(jfc.getSelectedFile().toPath());

						if (lines.size() != 2) {
							JOptionPane.showMessageDialog(frame, "Wrong file", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}

						key.setText(lines.get(0));

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		});
	}

	public void saveEncrypted() {

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int f = jfc.showSaveDialog(null);

		if (f == JFileChooser.CANCEL_OPTION) {
			return;
		}

		String fileExtension = FilenameUtils.getExtension(txtFilePath.getText());
		System.out.println("======================IZ SAVE ENCRYPTEDA=================");
		System.out.println("Extension: " + fileExtension);
		System.out.println("=========================================================");

		System.out.println(selectedFile.getAbsolutePath());
		File outputFile = new File(jfc.getSelectedFile().toString() + "." + fileExtension);
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			outputStream.write(encrypted);
			JOptionPane.showMessageDialog(frame, "File saved to: " + jfc.getSelectedFile().getAbsolutePath(), "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void saveDecrypted() {

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		int f = jfc.showSaveDialog(null);

		if (f == JFileChooser.CANCEL_OPTION) {
			return;
		}

		String fileExtension = FilenameUtils.getExtension(txtFilePath.getText());
		System.out.println("======================IZ SAVE ENCRYPTEDA=================");
		System.out.println("Extension: " + fileExtension);
		System.out.println("=========================================================");

		File outputFile = new File(jfc.getSelectedFile().toString() + "." + fileExtension);
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
			outputStream.write(decrypted);

			JOptionPane.showMessageDialog(frame, "File saved to: " + jfc.getSelectedFile().getAbsolutePath(), "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public boolean validateKeyNonce() {
		if (txtKey.getText().length() < 1 || txtNonce.getText().length() < 1) {
			JOptionPane.showMessageDialog(frame, "Key or nonce is empty	", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		System.out.println(txtKey.getText().length());
		System.out.println(txtKey.getText().length() != 64 && txtKey.getText().length() != 32);
		if (txtKey.getText().length() != 64 && txtKey.getText().length() != 32) {
			JOptionPane.showMessageDialog(frame, "Wrong key size (should be 32 bit string or 64 bit hex", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (txtNonce.getText().length() != 24 && txtNonce.getText().length() != 12 && txtNonce.getText().length() != 16
				&& txtNonce.getText().length() != 8) {
			JOptionPane.showMessageDialog(frame, "Wrong nonce size (should be 8 or 12 bit string or 16 or 24 bit hex",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (txtKey.getText().length() == 64) {
			if (!txtKey.getText().matches("-?[0-9a-fA-F]+")) {
				JOptionPane.showMessageDialog(frame, "Key of this length should be hexadecimal", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

		}

		if (txtNonce.getText().length() == 24 || txtNonce.getText().length() == 16) {
			if (!txtNonce.getText().matches("-?[0-9a-fA-F]+")) {
				JOptionPane.showMessageDialog(frame, "Nonce of this length should be hexadecimal", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

		}
		return true;
	}

}
