package project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class Decryptor extends Tesbouncy{
	
	public Decryptor(String keys) {
        byte[] salt = {};
        this.key = getKey(keys, salt, KEY_LENGTH);;
	}

	void Decrypting(File file, String _desDir) {
		
		String desDir = _desDir + "\\" + file.getName();
		if (file.isFile()) {
			
			System.out.println("From " + file.getPath());
			System.out.println("To: " + desDir);
			
			File direc = new File(_desDir);
			if (! direc.exists()) {
				direc.mkdir();
			}
			File dec_file  = new File(desDir);
			
			try {
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				FileInputStream inFile = new FileInputStream(file);
				byte[] oldhmac = new byte[32];
				byte[] iv = new byte[16];
				
				raf.seek(file.length() - 32);
				raf.read(oldhmac, 0, 32);
				raf.close();
				
				byte[] newhmac = genHMac(inFile, this.key,32);	
				for(int i=0 ; i<oldhmac.length ; i++) {
					if (oldhmac[i]!=newhmac[i]) {
						JOptionPane.showMessageDialog(frame, "File "+file.getPath()+" bị lỗi!","Thông báo",JOptionPane.ERROR_MESSAGE);
						inFile.close();
						return;
					}
				}
				inFile.close();
				inFile = new FileInputStream(file);
				inFile.read(iv);
			    ParametersWithIV parameterIV =  new ParametersWithIV(key , iv);
			    FileOutputStream outFile = new FileOutputStream(dec_file);
				BufferedBlockCipher decryptCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
				decryptCipher.init(false, parameterIV);
				int readed = 0;
				byte[] curData = new byte[READ_BLOCK];
				
				int curReaded;
				int ret1 = 0;
				byte[] output = new byte [decryptCipher.getOutputSize(READ_BLOCK)];
		        while ((curReaded = inFile.read(curData)) >= 0) {
		        	
		        	if (readed + curReaded > file.length() - 32) {
		        		curReaded = (int) (file.length() - 32 - readed);
		        		if (curReaded==0) break;
		        	}
		        	ret1 = decryptCipher.processBytes(curData, 0, curReaded, output, 0);
		        	if (curReaded==READ_BLOCK)
		        		outFile.write(output);
		        	readed += curReaded;
		        }
		        int ret2 = decryptCipher.doFinal(output, ret1);
		        byte[] output2 = new byte[ret1+ret2];
		        System.arraycopy(output, 0, output2, 0, output2.length);
		        outFile.write(output2);
		        outFile.flush();
				inFile.close();
				outFile.close();
			} catch (IOException | DataLengthException | IllegalStateException | InvalidCipherTextException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(frame, "File "+file.getPath()+" bị lỗi!","Thông báo",JOptionPane.ERROR_MESSAGE);
			} 
		}
		else {//is Directory
			File[] pathnames;
			pathnames = file.listFiles();
	        for (File subFile : pathnames) {
	            //System.out.println(pathname.getName());
	        	this.decrypting(subFile, desDir);
	        }
			
		}
	}

	public byte[] decryptBytes(byte[] input) {
		BufferedBlockCipher decryptCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
		decryptCipher.init(false, this.key);
		System.out.println(input.length);
        byte[] output = new byte [decryptCipher.getOutputSize(input.length)];
        int ret1 = decryptCipher.processBytes(input, 0, input.length, output, 0);
		int ret2 = 0;{
		try {
			ret2 = decryptCipher.doFinal(output, ret1);
		} catch (DataLengthException | IllegalStateException | InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			byte[] err = new byte[3];
			err[0] = err[1] = err[2] = -1;
			return err;
		}
	    byte[] result = new byte[ret1 + ret2];
	    System.arraycopy(output, 0, result, 0, result.length);
	    return result;
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	void decrypting(File file, String _desDir) {
		
		String desDir = _desDir + "\\" + file.getName();
		if (file.isFile()) {
			
			System.out.println("From " + file.getPath());
			System.out.println("To: " + desDir);
			
			File direc = new File(_desDir);
			if (! direc.exists()) {
				direc.mkdir();
			}
			File dec_file  = new File(desDir);
			
			try {
				FileInputStream inFile = new FileInputStream(file);
				byte[] oldhmac = new byte[32];
				byte[] iv = new byte[16];
				inFile.read(oldhmac);
				byte[] newhmac = genHMac(inFile, this.key,0);	
				for(int i=0 ; i<oldhmac.length ; i++) {
					if (oldhmac[i]!=newhmac[i]) {
						JOptionPane.showMessageDialog(frame, "File "+file.getPath()+" bị lỗi!","Thông báo",JOptionPane.ERROR_MESSAGE);
						inFile.close();
						return;
					}
				}
				inFile.close();
				inFile = new FileInputStream(file);
				inFile.read(oldhmac);
				inFile.read(iv);
			    ParametersWithIV parameterIV =  new ParametersWithIV(key , iv);
			    FileOutputStream outFile = new FileOutputStream(dec_file);
				BufferedBlockCipher decryptCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
				decryptCipher.init(false, parameterIV);
				byte[] curData = new byte[16];
				
				int curReaded;
				int flag = 0;
				byte[] output = new byte [decryptCipher.getOutputSize(16)];
		        while ((curReaded = inFile.read(curData)) >= 0) {
		        	
		        	int ret1 = decryptCipher.processBytes(curData, 0, curReaded, output, 0);
		        	if (flag==1)
		        		for(int i=0 ; i<16 ; i++)
		        			outFile.write((char)output[i]);
		        	flag = 1;
		        }
		        int ret2 = decryptCipher.doFinal(output, 0);
	        	for(int i=0 ; i<ret2 ; i++)
	        		outFile.write((char)output[i]);
	        	outFile.flush();
				inFile.close();
				outFile.close();
			} catch (IOException | DataLengthException | IllegalStateException | InvalidCipherTextException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(frame, "File "+file.getPath()+" bị lỗi!","Thông báo",JOptionPane.ERROR_MESSAGE);
			} 
		}
		else {//is Directory
			File[] pathnames;
			pathnames = file.listFiles();
	        for (File subFile : pathnames) {
	            //System.out.println(pathname.getName());
	        	this.decrypting(subFile, desDir);
	        }
			
		}
	}
	
}
