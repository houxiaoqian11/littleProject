package com.sumavision.tvpay.connector.util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

/**
 * <strong>Title : CryptoUtil</strong><br>
 * <strong>Description : 加解密工具类</strong><br>
 * <strong>Create on : 2015-05-04</strong><br>
 * 
 * @author linshangqing@cmbc.com.cn<br>
 */
public abstract class FastPayCryptionUtil {
	/**
	 * 数字签名函数入口
	 * 
	 * @param plainBytes
	 *            待签名明文字节数组
	 * @param privateKey
	 *            签名使用私钥
	 * @param signAlgorithm
	 *            签名算法
	 * @return 签名后的字节数组
	 * @throws Exception
	 */
	public static byte[] digitalSign(byte[] plainBytes, PrivateKey privateKey) throws Exception {
		String signAlgorithm = "SHA1WithRSA";
		try {
			Signature signature = Signature.getInstance(signAlgorithm);
			signature.initSign(privateKey);
			signature.update(plainBytes);
			byte[] signBytes = signature.sign();

			return signBytes;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			throw new Exception(e);
		} catch (SignatureException e) {
			throw new Exception(e);
		}
	}

	/**
	 * 验证数字签名函数入口
	 * 
	 * @param plainBytes
	 *            待验签明文字节数组
	 * @param signBytes
	 *            待验签签名后字节数组
	 * @param publicKey
	 *            验签使用公钥
	 * @param signAlgorithm
	 *            签名算法
	 * @return 验签是否通过
	 * @throws Exception
	 */
	public static boolean verifyDigitalSign(byte[] plainBytes, byte[] signBytes, PublicKey publicKey) throws Exception {
		boolean isValid = false;
		String signAlgorithm = "SHA1WithRSA";
		try {
			Signature signature = Signature.getInstance(signAlgorithm);
			signature.initVerify(publicKey);
			signature.update(plainBytes);
			isValid = signature.verify(signBytes);
			return isValid;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			throw new Exception(e);
		} catch (SignatureException e) {
			throw new Exception(e);
		}
	}

	/**
	 * 获取RSA公钥对象
	 * 
	 * @param keyContent
	 *            RSA公钥内容
	 * @param keyAlgorithm
	 *            密钥算法
	 * @return RSA公钥对象
	 * @throws Exception
	 */
	private static void getRSAPublicKeyByKeyContent(String keyContent, String keyAlgorithm) throws Exception {

		try {
			X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(keyContent));
			KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
			publicKey  = keyFactory.generatePublic(pubX509);

		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 获取RSA私钥对象
	 * 
	 * @param keyContent
	 *            RSA私钥内容
	 * @param password
	 *            RSA私钥保护密钥
	 * @param keyAlgorithm
	 *            密钥算法
	 * @return RSA私钥对象
	 * @throws Exception
	 */
	private static void getRSAPrivateKeyByKeyContent(String keyContent, String password, String keyAlgorithm) throws Exception {

		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(keyContent));
			System.out.println(keyContent.length());
			System.out.println(Base64.decodeBase64(keyContent).length);
			
			KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
			privateKey  = keyFactory.generatePrivate(priPKCS8);
		} catch (Exception e) {
			throw new Exception(e);
		} 
	}

	/**
	 * RSA加密
	 * 
	 * @param plainBytes
	 *            明文字节数组
	 * @param publicKey
	 *            公钥
	 * @param keyLength
	 *            密钥bit长度
	 * @param reserveSize
	 *            padding填充字节数，预留11字节
	 * @param cipherAlgorithm
	 *            加解密算法，一般为RSA/ECB/PKCS1Padding
	 * @return 加密后字节数组，不经base64编码
	 * @throws Exception
	 */
	public static byte[] RSAEncrypt(byte[] plainBytes, PublicKey publicKey) throws Exception {
		int keyLength = 2048;
		int reserveSize = 11;
		String cipherAlgorithm = "RSA/ECB/PKCS1Padding";
		int keyByteSize = keyLength / 8; // 密钥字节数
		int encryptBlockSize = keyByteSize - reserveSize; // 加密块大小=密钥字节数-padding填充字节数
		int nBlock = plainBytes.length / encryptBlockSize;// 计算分段加密的block数，向上取整
		if ((plainBytes.length % encryptBlockSize) != 0) { // 余数非0，block数再加1
			nBlock += 1;
		}

		try {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			// 输出buffer，大小为nBlock个keyByteSize
			ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * keyByteSize);
			// 分段加密
			for (int offset = 0; offset < plainBytes.length; offset += encryptBlockSize) {
				int inputLen = plainBytes.length - offset;
				if (inputLen > encryptBlockSize) {
					inputLen = encryptBlockSize;
				}

				// 得到分段加密结果
				byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
				// 追加结果到输出buffer中
				outbuf.write(encryptedBlock);
			}

			outbuf.flush();
			outbuf.close();
			return outbuf.toByteArray();
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e);
		} catch (NoSuchPaddingException e) {
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			throw new Exception(e);
		} catch (IllegalBlockSizeException e) {
			throw new Exception(e);
		} catch (BadPaddingException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	/**
	 * RSA解密
	 * 
	 * @param encryptedBytes
	 *            加密后字节数组
	 * @param privateKey
	 *            私钥
	 * @param keyLength
	 *            密钥bit长度
	 * @param reserveSize
	 *            padding填充字节数，预留11字节
	 * @param cipherAlgorithm
	 *            加解密算法，一般为RSA/ECB/PKCS1Padding
	 * @return 解密后字节数组，不经base64编码
	 * @throws Exception
	 */
	public static byte[] RSADecrypt(byte[] encryptedBytes, PrivateKey privateKey )
			throws Exception {
		int keyLength = 2048;
		int reserveSize = 11;
		String cipherAlgorithm = "RSA/ECB/PKCS1Padding";
		int keyByteSize = keyLength / 8; // 密钥字节数
		int decryptBlockSize = keyByteSize - reserveSize; // 解密块大小=密钥字节数-padding填充字节数
		int nBlock = encryptedBytes.length / keyByteSize;// 计算分段解密的block数，理论上能整除

		try {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			// 输出buffer，大小为nBlock个decryptBlockSize
			ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlockSize);
			// 分段解密
			for (int offset = 0; offset < encryptedBytes.length; offset += keyByteSize) {
				// block大小: decryptBlock 或 剩余字节数
				int inputLen = encryptedBytes.length - offset;
				if (inputLen > keyByteSize) {
					inputLen = keyByteSize;
				}

				// 得到分段解密结果
				byte[] decryptedBlock = cipher.doFinal(encryptedBytes, offset, inputLen);
				// 追加结果到输出buffer中
				outbuf.write(decryptedBlock);
			}

			outbuf.flush();
			outbuf.close();
			return outbuf.toByteArray();
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e);
		} catch (NoSuchPaddingException e) {
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			throw new Exception(e);
		} catch (IllegalBlockSizeException e) {
			throw new Exception(e);
		} catch (BadPaddingException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	/**
	 * AES加密
	 * 
	 * @param plainBytes
	 *            明文字节数组
	 * @param keyBytes
	 *            密钥字节数组
	 * @param keyAlgorithm
	 *            密钥算法
	 * @param cipherAlgorithm
	 *            加解密算法
	 * @param IV
	 *            随机向量
	 * @return 加密后字节数组，不经base64编码
	 * @throws Exception
	 */
	public static byte[] AESEncrypt(byte[] plainBytes, byte[] keyBytes) throws Exception {
		String keyAlgorithm = "AES";
		String cipherAlgorithm = "AES/ECB/PKCS5Padding";
		try {
			// AES密钥长度为128bit、192bit、256bit，默认为128bit
			if (keyBytes.length % 8 != 0 || keyBytes.length < 16 || keyBytes.length > 32) {
				throw new Exception("AES密钥长度不合法");
			}

			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			SecretKey secretKey = new SecretKeySpec(keyBytes, keyAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] encryptedBytes = cipher.doFinal(plainBytes);

			return encryptedBytes;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception(e);
		} catch (NoSuchPaddingException e) {
			throw new Exception(e);
		} catch (InvalidKeyException e) {
			throw new Exception(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new Exception(e);
		} catch (BadPaddingException e) {
			throw new Exception(e);
		} catch (IllegalBlockSizeException e) {
			throw new Exception(e);
		}
	}

	/**
	 * AES解密
	 * 
	 * @param encryptedBytes
	 *            密文字节数组，不经base64编码
	 * @param keyBytes
	 *            密钥字节数组
	 * @param keyAlgorithm
	 *            密钥算法
	 * @param cipherAlgorithm
	 *            加解密算法
	 * @param IV
	 *            随机向量
	 * @return 解密后字节数组
	 * @throws Exception
	 */
	public static byte[] AESDecrypt(byte[] encryptedBytes, byte[] keyBytes) throws Exception {
		String keyAlgorithm = "AES";
		String cipherAlgorithm = "AES/ECB/PKCS5Padding";
		try {
			// AES密钥长度为128bit、192bit、256bit，默认为128bit
			if (keyBytes.length % 8 != 0 || keyBytes.length < 16 || keyBytes.length > 32) {
				throw new Exception("AES密钥长度不合法");
			}

			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			SecretKey secretKey = new SecretKeySpec(keyBytes, keyAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);

			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			return decryptedBytes;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("NoSuchAlgorithmException");
		} catch (NoSuchPaddingException e) {
			throw new Exception("NoSuchPaddingException");
		} catch (InvalidKeyException e) {
			throw new Exception("InvalidKeyException");
		} catch (InvalidAlgorithmParameterException e) {
			throw new Exception("InvalidAlgorithmParameterException");
		} catch (BadPaddingException e) {
			throw new Exception("BadPaddingException");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("IllegalBlockSizeException");
		}
	}

	/**
	 * 字符数组转16进制字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] string2bytes(String str, int radix) {
		byte[] srcBytes = str.getBytes();

		// 2个16进制字符占用1个字节，8个二进制字符占用1个字节
		int size = 2;
		if (radix == 2) {
			size = 8;
		}

		byte[] tgtBytes = new byte[srcBytes.length / size];
		for (int i = 0; i < srcBytes.length; i = i + size) {
			String tmp = new String(srcBytes, i, size);
			tgtBytes[i / size] = (byte) Integer.parseInt(tmp, radix);
		}
		return tgtBytes;
	}

	/**
	 * 字符数组转16进制字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2string(byte[] bytes, int radix) {
		// 2个16进制字符占用1个字节，8个二进制字符占用1个字节
		int size = 2;
		if (radix == 2) {
			size = 8;
		}
		StringBuilder sb = new StringBuilder(bytes.length * size);
		for (int i = 0; i < bytes.length; i++) {
			int integer = bytes[i];
			while (integer < 0) {
				integer = integer + 256;
			}
			String str = Integer.toString(integer, radix);
			sb.append(StringUtils.leftPad(str.toUpperCase(), size, "0"));
		}
		return sb.toString();
	}
	
	
	private static PrivateKey privateKey;
	
	private static PublicKey publicKey;

	/**
	 * 
	 * @param keyContent 私钥内容串
	 * @param password  密码 
	 * @param keyAlgorithm  秘钥算法
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String keyContent ,String password ,String keyAlgorithm) throws Exception {
		if(privateKey == null)
			getRSAPrivateKeyByKeyContent(keyContent, password, keyAlgorithm);
		return privateKey;
	}
	
	/**
	 * 
	 * @param keyContent 公钥内容串
	 * @param keyAlgorithm 秘钥算法
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String keyContent, String keyAlgorithm) throws Exception {
		if(publicKey == null)
			getRSAPublicKeyByKeyContent(keyContent, keyAlgorithm);
		return publicKey;
	}

	
	
	
}

