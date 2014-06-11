package com.aidigame.hisun.pet.constant;

import java.io.File;

import android.os.Environment;

import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.http.json.InfoJson;

public class Constants {
	public static String IMIE;
	public static int status;//0 微信登陆；1 新浪登陆；2 随便看看
	public static String EXTRA_IN_API_KEY_SECRET="f6d0dd319088fd5a";
	public static User user;
	//注册成功
	public static final int REGISTER_SUCCESS=1;
	public static final int REGISTER_FAIL=3;
	//登陆成功
	public static final int LOGIN_SUCCESS=2;
	
	public static   int screen_width;
	public static   int screen_height;
	
	//IP地址
	public static String IP="54.199.161.210";
	//注册路径
	public static String REGISTER_PATH="/dc/index.php?r=user/registerApi";
	//登陆路径
	public static String LOGIN_PATH="/dc/index.php?r=user/loginApi";
	
	
	
	//用户信息查询路径info
	public static String INFO_PATH="/dc/index.php?r=user/infoApi&sig=";
    //用户信息Json解析对象
	public static InfoJson infoJson;
	//用户个人主页   图片列表，每次 返回10个
	public static String USER_IMAGES="http://"+IP+"/dc/index.php?r=user/imagesApi&";
	//Favorite   图片列表，每次 返回10个
	public static String IMAGE_FAVORITE="http://"+IP+"/dc/index.php?r=image/favoriteApi&";	
	//Favorite   图片列表，每次 返回10个
	public static String IMAGE_RANDOM="http://"+IP+"/dc/index.php?r=image/randomApi&";
	//获得一张图片的详细信息
	public static String IMAGE_INFO="http://"+IP+"/dc/index.php?r=image/infoApi&";
	//获得其他用户的详细信息
	public static String OTHER_INFO="http://"+IP+"/dc/index.php?r=user/otherApi&";
	//头像上传路径
	public static String USER_UPDATE_TX="http://"+IP+"/dc/index.php?r=user/txApi&sig=";
	//下载头像
	public static String USER_DOWNLOAD_TX="http://"+IP+"/dc/images/tx/";
	//加关注
	public static String USER_FOLLOW="http://"+IP+"/dc/index.php?r=user/followApi&";
	//取消关注
	public static String USER_UNFOLLOW="http://"+IP+"/dc/index.php?r=user/unfollowApi&";
	//关注列表
	public static String USER_FOLLOWING="http://"+IP+"/dc/index.php?r=user/followingApi&";
	//被关注列表
	public static String USER_FOLLOWER="http://"+IP+"/dc/index.php?r=user/followerApi&";
	//上传图片路径
	public static  String UPLOAD_IMAGE_PATH="/dc/index.php?r=image/uploadApi&sig=";//不需要加密
	//上传图片返回url地址
	public static String UPLOAD_IMAGE_RETURN_URL="http://"+IP+"/dc/images/upload/";
	//查看一张图片的所有信息
	public static String DOWNLOAD_IMAGE_INFO="/dc/index.php?r=image/infoApi&img_id";
	//喜欢图片路径
	public static String LIKE_IMAGE_PATH="/dc/index.php?r=image/likeApi";
	//删除图片路径
	public static String DELETE_IMAGE_PATH="/dc/index.php?r=image/deleteApi";
    
	
	
	//与web服务端进行数据交互 必须有的 一个value值   ，在登陆的时候获得
	public static String SID;
	//登陆是否成功
	public static boolean isSuccess=false;
	
	//本地Topic保存位置
	public static String Picture_Topic_Path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"topic";
	//本地Icon保存位置
	public static String Picture_ICON_Path=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"icon";
	//根目录
	public static  String Picture_Root_Path=Environment.getExternalStorageDirectory()+File.separator+"pet";
	
	
	
	//下载完图片列表
	public static final int MESSAGE_DOWNLOAD_IMAGES_LIST=11;
}
