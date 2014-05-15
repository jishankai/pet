package com.example.picturedemo.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.picturedemo.bean.Picture;
import com.example.picturedemo.ui.ChosePictureActivity;

public class ScanUtil {
	/**
	 * 获取系统相册中的相片列表
	 * @param context
	 * @return
	 */
	public static ArrayList<Picture> getPictures(Context context){
		ArrayList<Picture> list=new ArrayList<Picture>();
		Cursor cursor=context.getContentResolver()
				.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		  new String[]{MediaStore.Images.ImageColumns.TITLE,
			           MediaStore.Images.ImageColumns._ID,
			           MediaStore.Images.ImageColumns.DISPLAY_NAME,
			           MediaStore.Images.ImageColumns.DATA,
			           MediaStore.Images.ImageColumns.SIZE}, null, null, null);
		if(cursor!=null){
			String temp="";
			Picture p=null;
			while(cursor.moveToNext()){
				p=new Picture();
				p.name=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE));
				p.id=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
				p.fullName=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
				p.path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
				p.size=cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE));
				temp+=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE))+";";
				temp+=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))+";";
				temp+=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME))+";";
				temp+=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
//				Log.i("me", temp);
				if(p.size/1024>30){
					list.add(p);
				}
				
			}
			
		}
		return list;
	}

}
