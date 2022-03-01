package project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import java.security.SecureRandom;
import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class Encryptor extends Tesbouncy {
	
	public Encryptor(String keys) {
        byte[] salt = {};
		this.key = getKey(keys, salt, KEY_LENGTH);;
		
	  	SecureRandom random = new SecureRandom();
	    byte[] _iv = random.generateSeed(IV_LENGTH);
	    ParametersWithIV _parameterIV =  new ParametersWithIV(key , _iv);
	    this.parameterIV = _parameterIV;
	    this.iv = _iv;
	}
	
	void Encrypting(File file, String _desDir) {
		
		String desDir = _desDir + "\\" + file.getName();
		if (file.isFile()) {
			
			System.out.println("From " + file.getPath());
			System.out.println("To: " + desDir);
			
			File direc = new File(_desDir);
			if (! direc.exists()) {
				direc.mkdir();
			}
			File enc_file  = new File(desDir);
			File tmp_file  = new File(_desDir + "\\tmp.txt");
			try {
				FileInputStream inFile = new FileInputStream(file);
				FileOutputStream tmpFile = new FileOutputStream(tmp_file, true);	
				BufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
				encryptCipher.init(true, this.parameterIV);
				tmpFile.write(iv);
				byte[] curData = new byte[READ_BLOCK];
				
				int curReaded;
				int ret1 = 0;
				byte[] output = new byte [encryptCipher.getOutputSize(READ_BLOCK)];
		        while ((curReaded = inFile.read(curData)) >= 0) {
		        	ret1 = encryptCipher.processBytes(curData, 0, curReaded, output, 0);
		        	if (curReaded==READ_BLOCK)
		        		tmpFile.write(output);
		        }
		        int ret2 = encryptCipher.doFinal(output, ret1);
		        byte[] output2 = new byte[ret1+ret2];
		        System.arraycopy(output, 0, output2, 0, output2.length);
		        tmpFile.write(output2);
		        tmpFile.flush();
		        byte[] hmac = genHMac(inFile, this.key, 0);	
		        tmpFile.write(hmac);
		        tmpFile.flush();
		        tmp_file.renameTo(enc_file);
				inFile.close();
				tmpFile.close();
			} catch (IOException | DataLengthException | IllegalStateException | InvalidCipherTextException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		else {//is Directory
			File[] pathnames;
			pathnames = file.listFiles();
	        for (File subFile : pathnames) {
	            //System.out.println(pathname.getName());
	        	this.encrypting(subFile, desDir);
	        }
			
		}
	}
	
	public byte[] encryptBytes(byte[] input) {
		BufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
		encryptCipher.init(true, this.key);
		
		byte[] output = new byte [encryptCipher.getOutputSize(input.length)];
		int ret1 = encryptCipher.processBytes(input, 0, input.length, output, 0);
		int ret2 = 0;{
		try {
			ret2 = encryptCipher.doFinal(output, 0);
		} catch (DataLengthException | IllegalStateException | InvalidCipherTextException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	void encrypting(File file, String _desDir) {
		
		String desDir = _desDir + "\\" + file.getName();
		if (file.isFile()) {
			
			System.out.println("From " + file.getPath());
			System.out.println("To: " + desDir);
			
			File direc = new File(_desDir);
			if (! direc.exists()) {
				direc.mkdir();
			}
			File enc_file  = new File(desDir);
			File tmp_file  = new File(_desDir + "\\tmp.txt");
			try {
				FileInputStream inFile = new FileInputStream(file);
				FileOutputStream tmpFile = new FileOutputStream(tmp_file);
				tmpFile.close();
				tmpFile = new FileOutputStream(tmp_file, true);	
				BufferedBlockCipher encryptCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
				encryptCipher.init(true, this.parameterIV);
				tmpFile.write(iv);
				byte[] curData = new byte[16];
				
				int curReaded;
				int lastReaded = 0;
				int flag = 0;
				byte[] output = new byte [encryptCipher.getOutputSize(16)];
		        while ((curReaded = inFile.read(curData)) >= 0) {
		        	
		        	encryptCipher.processBytes(curData, 0, curReaded, output, 0);
		        	if (flag==1)
		        		for(int i=0 ; i<16 ; i++)
		        			tmpFile.write((char)output[i]);
		        	flag = 1;
		        	lastReaded = curReaded;
		        }
		        encryptCipher.doFinal(output, 0);
		        if (lastReaded==16)
		        	tmpFile.write(output);
		        else {
		        	for(int i=0 ; i<16 ; i++)
		        		tmpFile.write((char)output[i]);
		        }
		        tmpFile.flush();
		        
		        FileOutputStream outFile = new FileOutputStream(enc_file);
		        outFile.close();
		        outFile = new FileOutputStream(enc_file, true);
		        inFile.close();
		        inFile = new FileInputStream(tmp_file);	
		        byte[] hmac = genHMac(inFile, this.key, 0);		
		        outFile.write(hmac);
		        inFile.close();
		        inFile = new FileInputStream(tmp_file);	
		        byte[] byteData = new byte[16];
		        int bytesReaded = inFile.read(byteData);
		        while (bytesReaded != -1) {
	                outFile.write(byteData);
		            bytesReaded = inFile.read(byteData);
		        }
		        outFile.flush();
				inFile.close();
				tmpFile.close();
				outFile.close();
				
			    if (tmp_file.delete()) { 
			        System.out.println("Deleted the file: " + tmp_file.getName());
			      } else {
			        System.out.println("Failed to delete the file.");
			      }
			} catch (IOException | DataLengthException | IllegalStateException | InvalidCipherTextException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		else {//is Directory
			File[] pathnames;
			pathnames = file.listFiles();
	        for (File subFile : pathnames) {
	            //System.out.println(pathname.getName());
	        	this.encrypting(subFile, desDir);
	        }
			
		}
	}

}
