package com.aidigame.hisun.pet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class StringUtil {
	public static String timeFormat(long time){
		//1403514413
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sdf.format(new Date(time*1000));
		
	}
	/** 
	* 将字符串转成unicode 
	* @param str 待转字符串 
	* @return unicode字符串 
	*/ 
	public static String convert(String str) 
	{ 
	str = (str == null ? "" : str); 
	String tmp; 
	StringBuffer sb = new StringBuffer(1000); 
	char c; 
	int i, j; 
	sb.setLength(0); 
	for (i = 0; i < str.length(); i++) 
	{ 
	c = str.charAt(i); 
	sb.append("\\u"); 
	j = (c >>>8); //取出高8位 
	tmp = Integer.toHexString(j); 
	if (tmp.length() == 1) 
	sb.append("0"); 
	sb.append(tmp); 
	j = (c & 0xFF); //取出低8位 
	tmp = Integer.toHexString(j); 
	if (tmp.length() == 1) 
	sb.append("0"); 
	sb.append(tmp); 

	} 
	return (new String(sb)); 
	} 
	/** 
	* 将unicode 字符串 
	* @param str 待转字符串 
	* @return 普通字符串 
	*/ 
	public static String revert(String str) 
	{ 
	str = (str == null ? "" : str); 
	if (str.indexOf("\\u") == -1)//如果不是unicode码则原样返回 
	return str; 

	StringBuffer sb = new StringBuffer(1000); 

	for (int i = 0; i < str.length() - 6;) 
	{ 
	String strTemp = str.substring(i, i + 6); 
	String value = strTemp.substring(2); 
	int c = 0; 
	for (int j = 0; j < value.length(); j++) 
	{ 
	char tempChar = value.charAt(j); 
	int t = 0; 
	switch (tempChar) 
	{ 
	case 'a': 
	t = 10; 
	break; 
	case 'b': 
	t = 11; 
	break; 
	case 'c': 
	t = 12; 
	break; 
	case 'd': 
	t = 13; 
	break; 
	case 'e': 
	t = 14; 
	break; 
	case 'f': 
	t = 15; 
	break; 
	default: 
	t = tempChar - 48; 
	break; 
	} 

	c += t * ((int) Math.pow(16, (value.length() - j - 1))); 
	} 
	sb.append((char) c); 
	i = i + 6; 
	} 
	return sb.toString(); 
	}
	public static void deleteFile(File file){
		if(file.isDirectory()){
			File[] files=file.listFiles();
			if(files.length==0)file.delete();
			for(File f:files){
				if(f.isDirectory()){
					deleteFile(f);
				}else{
					f.delete();
				}
			}
		}else{
			file.delete();
		}
	}
	/**
	 * 判断图片是否存在，两种情况  ：图片 不存在，图片存在但不完整（未下载完）;
	 * @param path
	 * @return
	 */
	public static boolean judgeImageExists(String path){
		boolean flag=false;
		if(path==null)return false;
		File file=new File(path);
		if(file.exists()){
			BitmapFactory.Options opts=new BitmapFactory.Options();
				opts.inJustDecodeBounds=false;
				opts.inSampleSize=32;
				Bitmap bitmap=BitmapFactory.decodeFile(path,opts);
				if(bitmap!=null){
					flag=true;
					if(!bitmap.isRecycled()){
						bitmap.recycle();
					}
				}else{
					flag=false;
					file.delete();
				}
				
		}
		LogUtil.i("me", "判断文件是否存在"+flag);
		return flag;
	}
	/**
	 * 通过流的方式读取图片，然后设置给ImagView
	 * @param path
	 * @param view
	 * @param opt
	 */
	public static void setImageViaStream(String path,ImageView view,BitmapFactory.Options opt){
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(path);
			view.setImageBitmap(BitmapFactory.decodeStream(fis,null,opt));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static boolean isEmpty(String str){
		if(str!=null&&!"".equals(str)&&!" ".equals(str)){
			return false;
		}
		return true;
	}
	


}
