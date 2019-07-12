package com.yucheng.cmis.pub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class AddPackage {

//	private static String path = "C:/addFile/addFile.txt";//配置文件
//	private static String path = "C:/addFile/31416-31418过滤后.txt";//配置文件
	private static String path = "E:/1.20131119信贷项目/addFile/addFile.txt";//配置文件
//	private static String path = "C:/addFile/svn.txt";//配置文件
//	private static String addFillPath = "C:/workspace130617/cmis-main/";//全量文件所在目录
//	private static String addFillPath = "C:/workspace1026/cmis-main/";//全量文件所在目录
//	private static String addFillPath = "C:/workspace130711/cmis-main/";//全量文件所在目录
	private static String addFillPath = "E:/2.workspace/cmis/WebContent/";//全量文件所在目录
	private static String folderPath="E:/1.20131119信贷项目/addFile/cmis/";//生成目录
	private static BufferedReader reader=null;//定义BufferedReader
	private static int fileCounts = 0;
	public static void init(){
		File file = new File(path);
		try {
			reader=new BufferedReader(new FileReader(path));			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void run(){
		String line;
		String newPath = null;
		String addPath;
		int fileCount=0;
		try {
			while((line=reader.readLine())!=null){
				//File addFile = new File(path+line);
				if(!line.contains("."))
					continue;
				newPath = folderPath + line.substring(0,line.lastIndexOf("/"));
				//给生成目录生成文件夹
				File newFile = new File(newPath);
				if(!newFile.exists()){
					newFile.mkdirs();
				}
				newPath = folderPath+line;
				(new File(newPath)).createNewFile();
				addPath = addFillPath+line;
				copyFile(addPath,newPath);
				fileCount++;
			}
			System.out.print("总文件数为："+fileCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("出错文件为："+newPath);
			e.printStackTrace();
		}
	}
	
	public static void run2(){
		String line;
		String newPath;
		String addPath;
		int fileCount=0;
		try {
			while((line=reader.readLine())!=null){
				//File addFile = new File(path+line);
				newPath = folderPath + line.substring(0,line.lastIndexOf("/"));
				//给生成目录生成文件夹
				File newFile = new File(newPath);
				if(!newFile.exists()){
					newFile.mkdirs();
				}
				newPath = folderPath+line;
				(new File(newPath)).createNewFile();
				addPath = addFillPath+line;
				copyFile(addPath,newPath);
				fileCount++;
			}
			System.out.print("总文件数为："+fileCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//确认文件个数
	public static void countFile(File file){
		File list[] = file.listFiles();
		for(int i = 0;i < list.length;i++){
			if(list[i].isFile()){
				fileCounts++;
			}else{
				countFile(list[i]);
			}
		}
	}
	public static void main(String[] args) {
		init();
		run();
		File file = new File(folderPath);
		countFile(file);
		System.out.print("确认后文件个数为："+fileCounts);
	}
	 /** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public static void copyFile(String oldPath, String newPath) {
	   System.out.println("复制文件："+newPath);
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1444]; 
               int length; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小  
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
           } 
       } 
       catch (Exception e) { 
           System.out.println("复制单个文件操作出错"); 
           e.printStackTrace(); 

       } 

   } 
}
