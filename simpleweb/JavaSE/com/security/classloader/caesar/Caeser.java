package com.security.classloader.caesar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Caeser {
	private static byte c = 3;
	/**
	 * 加密类文件 
	 * @throws IOException 
	 */
	public static void encryptClass(String classFileDir){
		File dir = new File(classFileDir);
		
		if(! dir.isDirectory()){
			throw new IllegalStateException("please input dir path");
		}
		
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			File f = files[i];
			FileInputStream in = null;
			FileOutputStream out = null;
			try {
				in = new FileInputStream(f);
				String name = f.getName();
				String newName = name.replaceAll(".class", ".caeser");
				out = new FileOutputStream(newName);
				
				int ch = -1;
				while((ch = in.read()) != -1){
					ch += c;
					out.write((byte)ch);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				try {
					if(out != null){
						out.close();						
					}
					if(in != null){
						in.close();						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}
	}
}
