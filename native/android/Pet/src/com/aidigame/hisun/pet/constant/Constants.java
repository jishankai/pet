package com.aidigame.hisun.pet.constant;

import java.io.File;

import android.R.integer;
import android.os.Environment;

import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.http.json.InfoJson;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tencent.mm.sdk.openapi.IWXAPI;

public class Constants {
	public static String IMIE;
	public static String OPEN_UDID;
	public static int status;//0 微信登陆；1 新浪登陆；2 随便看看
	//aviary第三方类库的key值
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
	//根据一串user_id获取多个user信息
	public static String OTHERS_INFO="http://"+IP+"/dc/index.php?r=user/othersApi&";
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
	
	//活动相关api
	//获取活动列表
	public static String ACTIVITY_LIST="http://"+IP+"/dc/index.php?r=topic/listApi";
    //活动宣传图
	public static String ACTIVITY_IMAGE="http://"+IP+"/dc/images/topic/";
	//获取某一活动详细信息
	public static String ACTIVITY_INFO="http://"+IP+"/dc/index.php?r=topic/infoApi";
	//获取奖品详细信息
	public static  String ACTIVITY_REWARD_INFO="http://"+IP+"/dc/index.php?r=topic/rewardApi";
	//活动详情的最新列表
	public static String ACTIVITY_NEWEST="http://"+IP+"/dc/index.php?r=topic/newestApi&";
	//活动详情的最热列表
	public static String ACTIVITY_POPULAR="http://"+IP+"/dc/index.php?r=topic/popularApi&";
	
	
	
	//消息
	//消息列表 含消息和系统   &mail_id=&is_system=;
	public static String MAIL_LIST="http://"+IP+"/dc/index.php?r=mail/listApi";
	//发送消息  采用post方式  传递参数 to_id  和body
	public static String MAIL_CREATE="http://"+IP+"/dc/index.php?r=mail/createApi";
	//删除消息  mail_id
	public static String MAIL_DELETE="http://"+IP+"/dc/index.php?r=mail/deleteApi";
	//获取最新消息数目和活动数目
	public static String MAIL_ACTIVITY_NEW_NUM="http://"+IP+"/dc/index.php?r=user/notifyApi";
	//发表评论  采用post方式  传递参数 img_id 和body
	public static String ADD_A_COMMENT="http://"+IP+"/dc/index.php?r=image/commentApi";
	
	//获取种族类别
	public static String GET_RACE_TYPE="http://"+IP+"/dc/index.php?r=user/typeApi";
	//欢迎页图片下载
    public static String WELCOME_IMAGE="http://"+IP+"/dc/index.php?r=user/welcomeApi";
	//判断新浪账号是否绑定
    public static String WELCOME="http://"+IP+"/dc/index.php?r=user/welcomeApi";
	//经验
	public static final int LEVEL_1=30;
	public static final int LEVEL_2=150;
	public static final int LEVEL_3=400;
	public static final int LEVEL_4=750;
	public static final int LEVEL_5=1200;
	public static final int LEVEL_6=1800;
	
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
	//相机拍摄的照片保存位置
	public static String Picture_Camera=Environment.getExternalStorageDirectory()+File.separator+"pet"+File.separator+"camera";
	
	
	
	//下载完图片列表
	public static final int MESSAGE_DOWNLOAD_IMAGES_LIST=11;
	//错误信息提示
	public static final  int ERROR_MESSAGE=51;
	
	
	/** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String APP_KEY      = "1235051367";//
    public static final String APP_SECRET="c243cdd567118b985b4c284ca4c5f5d9";
    public static final String Weixin_APP_KEY="wxef59ead737c3c450";
    public static final String Weixin_APP_SECRET="30bc4fc9447a20f6c7be3bf174a9c7c1";
    public static IWXAPI api;
    /*
     * 签名   c5be57472fb352e042aac4a66f831a35   新浪
     * 微信    a3e7b7ff1d4e3c1091dc2cc2e2d779de
     * 
     *  自己key 79d8332783adef56882ac3398ea494c8
     */
    /** 
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * 
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * 
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    public static Oauth2AccessToken accessToken;
    /** 通过 code 获取 Token 的 URL */
    public static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";
	
    
    //提示消息字符串
    public static final String NOTE_MESSAGE_1="数据正在加载中，请稍后";
    public static final String NOTE_MESSAGE_2="网络连接异常，请检查网络";
    public static final String NOTE_MESSAGE_3="发生错误，客户端似乎有问题";
    public static final String NOTE_MESSAGE_4="服务器由于遇到错误而不能完成该请求";
    
    
    
    //"setup" SharedPreference 中存储内容的key值
    public static final String LOCK_TO_WEIXIN="LOCK_TO_WEIXIN";//账号绑定到微信，返回boolean值   是否绑定
    public static final String LOCK_TO_XINLANG="LOCK_TO_XINLANG";//账号绑定到新浪，返回boolean值   是否绑定
    //照片是否同步发送到微信朋友圈
    public static final String PICTURE_SEND_TO_WEIXIN="PICTURE_SEND_TO_WEIXIN";
    //照片是否同步发送到新浪微博
    public static final String PICTURE_SEND_TO_XINLANG="PICTURE_SEND_TO_XINLANG";
    //用户账号可见性 值为int 1为所有人；2为我关注的人；3为自己
    public  static final String ACOUNT_VISIVAL_AREAR="ACOUNT_VISIVAL_AREAR";
}
