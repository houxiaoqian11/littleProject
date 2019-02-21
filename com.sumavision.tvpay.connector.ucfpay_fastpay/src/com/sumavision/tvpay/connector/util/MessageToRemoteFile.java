package com.sumavision.tvpay.connector.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class MessageToRemoteFile {
	/**
	 * Description: 从本地上传文件到共享目录
	 * 
	 * @param remoteUrl
	 *            共享文件目录 格式smb://ip/sharefolder
	 *            Smb://username:password@ip/sharefolder
	 * @param localFilePath
	 *            本地文件绝对路径
	 */
	public static void smbPut(String remoteUrl, String localFilePath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File localFile = new File(localFilePath);

			String fileName = localFile.getName();
			SmbFile remoteFile = new SmbFile(remoteUrl + File.separator + fileName);
			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Description: 从共享目录下载文件到本地
	 * 
	 * @param remoteUrl
	 *            共享文件目录 格式smb://ip/sharefolder
	 *            Smb://username:password@ip/sharefolder
	 * @param localFilePath
	 *            本地文件绝对路径
	 * @throws Exception 
	 */
	public static void smbGet(String remoteUrl, String localFilePath , String fileName) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		try {

			SmbFile remoteFile = new SmbFile(remoteUrl +  File.separator + fileName);
			in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
			out = new BufferedOutputStream(new FileOutputStream(localFilePath + File.separator + fileName));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			throw new Exception();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param remoteUrl
	 *            共享文件目录 格式smb://ip/sharefolder
	 *            Smb://username:password@ip/sharefolder
	 * @param msg
	 *            写入信息
	 * @param fileName
	 *            文件名
	 * @throws Exception
	 */
	public static void writeRemoteFile(String remoteUrl, String msg,
			String fileName) throws Exception {
		SmbFile remoteFile = new SmbFile(remoteUrl + File.separator + fileName);
		OutputStream out = new BufferedOutputStream(new SmbFileOutputStream(
				remoteFile));
		out.write(msg.getBytes());
		out.close();
	}

	/**
	 * 
	 * @param remoteUrl
	 *            共享文件目录 格式smb://ip/sharefolder
	 *            Smb://username:password@ip/sharefolder
	 * 
	 * @param fileName
	 *            文件名
	 * @throws Exception
	 */
	public static String readRemoteFile(String remoteUrl, String fileName)
			throws Exception {
		SmbFile remoteFile = new SmbFile(remoteUrl + File.separator + fileName);
		BufferedReader is = new BufferedReader(new InputStreamReader(
				new SmbFileInputStream(remoteFile)));
		String retMsg = is.readLine();
		is.close();
		return retMsg;
	}

	public static void main(String[] args) throws Exception {
		// smbPut("smb://Administrator:conntest@192.161.14.11/share",
		// "E:/aa.txt");
		// System.out.println(1);
//		Long beg = new Date().getTime();
//		
//		SmbFile file = new SmbFile(
//				"smb://administrator:conntest@192.166.69.32/bill99QuickPayCode/");
////		smbGet("smb://Administrator:conntest@192.161.14.11/share//","D:/","aa.txt");
//		System.out.println(file.listFiles()[0]);
//		Long end = new Date().getTime();
//		System.out.println( end - beg);
//		BufferedReader is = new BufferedReader(new InputStreamReader(
//				new SmbFileInputStream(file)));
//		System.out.println(readRemoteFile(
//				"smb://Administrator:conntest@192.161.14.12/share", "aaa.txt"));
		
		writeRemoteFile("smb://smbuser:smb$user@172.16.6.29/quickpayfile/", "2222dds ", "8978789.txt");
	}
}
