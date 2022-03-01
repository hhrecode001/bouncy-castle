package project1;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.SecureRandom;

import javax.swing.JFrame;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class Tesbouncy {
	protected static final int IV_LENGTH = 16;
	protected static final int KEY_LENGTH = 128;
	protected KeyParameter key = null;
	protected BlockCipher engine = new AESEngine();
	protected ParametersWithIV parameterIV = null;
	protected byte[] iv = null;
	protected JFrame frame = new JFrame();
	protected final int READ_BLOCK = 16*1024;
	
    public KeyParameter getKey(String password, byte[] salt, int keySize) {
    	   char [] charArr = password.toCharArray();
    	   PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
    	   generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(charArr),salt, 1024);
    	   KeyParameter key = (KeyParameter)generator.generateDerivedParameters(keySize);
    	   return key;
       }
    
	public byte[] genHMac(FileInputStream inFile, KeyParameter key, int skip_header) {
		HMac hmac = new HMac(new SHA256Digest());
		hmac.init(key);
		byte[] byteData = new byte[128];
		int bytesReaded;
		try {
			if (skip_header>0) {
				byte[] header = new byte[skip_header];
				bytesReaded = inFile.read(header);
			}
			bytesReaded = inFile.read(byteData);
	        while (bytesReaded != -1) {
	        	hmac.update(byteData, 0, bytesReaded);
	            bytesReaded = inFile.read(byteData);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        byte[] result = new byte[hmac.getMacSize()]; 
		hmac.doFinal(result, 0);
		return result;
	}
}