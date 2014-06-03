package com.aidigame.hisun.pet.constant;

import java.io.File;

import android.os.Environment;

public class Constants {
	
	public static int status;//0 ΢�ŵ�½��1 ����΢����½��2 ��㿴��
	
	
	//IP��ַ
	public static String IP="54.199.161.210";
	//ע��·��
	public static String REGISTER_PATH="/dc/index.php?r=user/registerApi";
	//��½·��
	public static String LOGIN_PATH="/dc/index.php?r=user/loginApi";
	//info ·��
	public static String INFO_PATH="/dc/index.php?r=user/infoApi&sig=";

	//ͼƬ�ϴ�
	public static  String UPLOAD_IMAGE_PATH="/dc/index.php?r=image/uploadApi&sig=";
	//ϲ��ͼƬ   ��������
	public static String LIKE_IMAGE_PATH="/dc/index.php?r=image/likeApi";
	//ɾ��ͼƬ·��
	public static String DELETE_IMAGE_PATH="/dc/index.php?r=image/deleteApi";
    
	
	
	//��½��ȡ��SID
	public static String SID;
	//��½�Ƿ�ɹ�
	public static boolean isSuccess=true;
	
	//本地Topic保存位置
	public static String Picture_Path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"topic";
}
