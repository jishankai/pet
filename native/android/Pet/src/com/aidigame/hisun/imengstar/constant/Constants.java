package com.aidigame.hisun.imengstar.constant;

import java.io.File;

import android.R.integer;
import android.os.Environment;

import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.http.json.InfoJson;
import com.tencent.mm.sdk.openapi.IWXAPI;

public class Constants {
	
	
	//与web服务端进行数据交互 必须有的 一个value值   ，在登陆的时候获得

	
	public static String VERSION;//强制更新
	public static String realVersion;//版本更新
	public static String android_url;
	public static long apk_size;
	public static String CON_VERSION;
	public static String DOWNLOAD_APK="http://home4pet.aidigame.com/pet_beta_1.0.15.apk";
	
	public static long Toatl_animal;
	public static long Toatl_food;
	
	
	
	
	
	//临时声音文件
	public static String audioPath;
	public static boolean hasStart=false;
	public static String IMIE;
	public static String OPEN_UDID;
	public static int status;//0 微信登陆；1 新浪登陆；2 随便看看
	public static int planet=1;//1.猫；2.狗
	//aviary第三方类库的key值
	public static String EXTRA_IN_API_KEY_SECRET="f6d0dd319088fd5a";
	
	public static boolean netStatus;//网络状态，是否联网
	//注册成功
	public static final int REGISTER_SUCCESS=1;
	public static final int REGISTER_FAIL=3;
	//登陆成功
	public static final int LOGIN_SUCCESS=2;
	
	public static   int screen_width;
	public static   int screen_height;
	
	//IP地址
//	public static String IP="54.199.161.210";
//	public static String IP="54.199.161.210:8001";
//	public static String IP2="54.199.161.210:8001";
//	public static String URL_ROOT="/dc/index.php?";
	public static String URL_ROOT="/index.php?";
	public static String URL_ROOT2="/index.php?";//
	public static String IP="release4pet.imengstar.com";//   release4pet.imengstar.com   dev4pet.imengstar.com
	public static String IP2="release4pet.imengstar.com";//release4pet.aidigame.com
	public static String test="";//-test
	public static String USER_DOWNLOAD_TX="http://pet"+test+"4tx.oss-cn-beijing.aliyuncs.com/"+"tx_usr/";
	public static String ANIMAL_DOWNLOAD_TX="http://pet"+test+"4tx.oss-cn-beijing.aliyuncs.com/"+"tx_ani/";
public static String PICTURE_TYPE_MENUS="http://pet"+test+"4upload.oss-cn-beijing.aliyuncs.com/menu/";
	
    //下载声音
    public static String DOWNLOAD_VOICE="http://pet"+test+"4voices.oss-cn-beijing.aliyuncs.com/ani/";//不需要加密
  //欢迎页图片下载
    public static String WELCOME_IMAGE="http://pet"+test+"4welcome.oss-cn-beijing.aliyuncs.com/";//
    public static String UPLOAD_IMAGE_RETURN_URL="http://pet"+test+"4upload.oss-cn-beijing.aliyuncs.com/";
	
	
	
	public static String ANIMAL_THUBMAIL_DOWNLOAD_TX="http://txthumb.imengstar.com/tx_ani/";
	public static String USER_THUBMAIL_DOWNLOAD_TX="http://txthumb.imengstar.com/tx_usr/";
	 public static String UPLOAD_IMAGE_THUBMAIL_IMAG="http://uploadthumb.imengstar.com/";
	/*public static String ANIMAL_THUBMAIL_DOWNLOAD_TX="http://devtxthumb.imengstar.com/tx_ani/";
	public static String USER_THUBMAIL_DOWNLOAD_TX="http://devtxthumb.imengstar.com/tx_usr/";
	 public static String UPLOAD_IMAGE_THUBMAIL_IMAG="http://devuploadthumb.imengstar.com/";*/
	
	
   
    public static String DOWNLOAD_FOOD_PATH="item/";
	
	
    
  //打赏食物
  	public static String AWARD_FOOD=IP2+URL_ROOT2+"r=image/rewardFoodApi&img_id=";
    
    
	//获得SID
	public static String GET_SID=URL_ROOT+"r=user/getSIDApi&uid=";
	//获得SID
	public static String GET_GIFT_INFO=URL_ROOT+"r=gift/listApi&sig=";
	//我的萌星
	public static String MY_PET_CARD=IP2+URL_ROOT2+"r=animal/mineApi";
	//萌星推荐
	public static String PET_RECOMMEND=IP2+URL_ROOT2+"r=user/recommendApi";
	//修改用宠物 语言心情
	public static String PET_MODIFY_ANOUNCE=IP2+URL_ROOT2+"r=animal/modifyMsgApi&aid=";
	
	//设置密码
	public static String SET_PASSWORD=IP2+URL_ROOT2+"r=user/setPwdApi&pwd=";
	//切换账号
	public static String CHANGE_ACCOUNT=IP2+URL_ROOT2+"r=user/bindUserApi&name=";
	//是否绑定微信或微博
	public static String IS_BIND=IP2+URL_ROOT2+"r=user/loginBy3PartyApi";
	//微信或微博绑定
	public static String BIND_ACCOUNT=IP2+URL_ROOT2+"r=user/bindApi&";
	
	
	//banner列表
	public static String BANNER_LIST=IP2+URL_ROOT2+"r=image/bannerApi";
	//举报图片
	public static String REPORT_IMAGE=IP2+URL_ROOT2+"r=image/reportApi&img_id=";
	//举报用户
	public static String REPORT_USER=IP2+URL_ROOT2+"r=user/reportApi&usr_id=";
	//海选萌星列表
	public static String PET_LIST_VOT=IP2+URL_ROOT2+"r=star/listApi&sig=";
	//海选萌星图片列表
	public static String IMAGE_LIST_VOT=IP2+URL_ROOT2+"r=star/popularApi&sig=";
	//海选萌星图片列表
		public static String IMAGE_LIST_FAVOURITE_VOT=IP2+URL_ROOT2+"r=star/newestApi&sig=";
		//海选萌星赠送推荐票
		public static String IMAGE_SEND_TICKET_VOT=IP2+URL_ROOT2+"r=star/voteApi&sig=";
		//海选萌星赠送推荐票
				public static String IMAGE_BUY_TICKET_VOT=IP2+URL_ROOT2+"r=star/chargeApi&sig=";
	//拉黑用户
	public static String BLOCK_OTHER=IP2+URL_ROOT2+"r=talk/blockApi&talk_id=";
	
	//获取版本更新的信息
	public static String VERSION_INFO=IP2+URL_ROOT2+"r=user/upgradeApi&version=";
	//取消拉黑用户
	public static String UNBLOCK_OTHER=IP2+URL_ROOT2+"r=talk/unBlockApi&usr_id=";
	
	
	//使用邀请码
	public static String USE_INVITECODE=IP2+URL_ROOT2+"r=user/inputCodeApi&code=";
	
	
	//注册路径
	public static String REGISTER_PATH=IP2+URL_ROOT2+"r=user/registerApi";
	//修改用户信息
	public static String USER_MODIFY=IP2+URL_ROOT2+"r=user/modifyInfoApi";
	//修改用户信息
	public static String PET_MODIFY=IP2+URL_ROOT2+"r=animal/modifyInfoApi";
	//登陆路径
//	public static String LOGIN_PATH=URL_ROOT+"r=user/loginApi";
	public static String LOGIN_PATH=URL_ROOT+"r=user/loginApi";
	
	
	
	//修改用户信息
	public static String BEG_FOOD_LIST=IP2+URL_ROOT2+"r=image/ask4FoodApi&page=";
	//宠物所有的求口粮图片
	public static String PET_BEG_PICTURE_LIST=IP2+URL_ROOT2+"r=animal/foodListApi&aid=";
	//赏口粮图片信息
	public static String SHARE_FOOD=IP2+URL_ROOT2+"r=animal/foodApi&aid=";
	
	//宠物是否做过此操作，（挣口粮，摸一摸...），做过的话，返回图片信息
	public static String DO_PICTURE_TYPE=IP2+URL_ROOT2+"r=image/isMenuApi&aid=";
	
	//文章列表
	public static String ARTICLE_LIST=IP2+URL_ROOT2+"r=social/articles&page=";
	//兑换礼物列表
	public static String EXCHANGE_FOOD_LIST=IP2+URL_ROOT2+"r=item/listApi";
	//兑换礼物
	public static String EXCHANGE_FOOD=IP2+URL_ROOT2+"r=item/exchangeApi&";
	//兑换礼物的详细信息
	public static String FOOD_INFO=IP2+URL_ROOT2+"r=item/infoApi&item_id=";
	
	
	
	//送礼物
	public static String SEND_GIFT_API=URL_ROOT+"r=animal/sendGiftApi&sig=";
	//送礼物
	public static String BUY_GIFT_API=URL_ROOT+"r=item/buyApi&sig=";
	
	
	//摇一摇api
	public static String SHAKE_API=URL_ROOT+"r=animal/shakeApi&sig=";
	//摇一摇api
	public static String SHAKE_SHARE=URL_ROOT+"r=animal/shakeShareApi&sig=";
	//摸一摸api
	public static String TOUCH_API=URL_ROOT+"r=animal/touchApi&sig=";
	
	
	//王国动态列表
	public static String KINGDOM_TRENDS=URL_ROOT+"r=animal/newsApi&sig=";
	//王国成员列表
	public static String KINGDOM_PEOPLES=URL_ROOT+"r=animal/fansApi&sig=";
	//加入王国
	public static String JOIN_KINGDOM=URL_ROOT+"r=animal/joinApi&sig=";
	//分享数api
	public static String IMAGE_SHARE_NUM=URL_ROOT+"r=image/shareApi&sig=";//r=image/shareApi&sig=
	//改变星球
	public static String CHANGE_PLANT=URL_ROOT+"r=user/planetApi&sig=";
	//创建王国
	public static String CREATE_KINGDOM=URL_ROOT+"r=animal/createApi&sig=";
	//创建王国
	public static String IS_TOUCHED=URL_ROOT+"r=animal/isTouchedApi&sig=";
	//发布图片时可以选择的类型
	public static String GET_PICTURE_TYPE=URL_ROOT+"r=image/menuApi&sig=";
	//用户分享数api
	public static String USER_SHARE_NUM=URL_ROOT+"r=user/shareApi&sig=";
	//退出王国
	public static String EXIT_KINGDOM=URL_ROOT+"r=animal/exitApi&sig=";
	//用户和王国的关系
	public static String KING_USER_RELATION=URL_ROOT+"r=animal/relationApi&sig=";
	//宠物的物品栏
	public static String KINGDOM_GIFT=URL_ROOT+"r=animal/itemsApi&sig=";
	//用户的物品栏
	public static String USER_GIFT=URL_ROOT+"r=user/itemsApi&sig=";
	//用户的物品栏
	public static String USER_ACTIVITY=URL_ROOT+"r=user/topicApi&sig=";
	//认养王国    推荐列表
	public static String RECOMMEND_KINGDOM=URL_ROOT+"r=animal/recommendApi&sig=";
	//认养王国    人气列表
	public static String POPULAR_KINGDOM=URL_ROOT+"r=animal/popularApi&sig=";
	//认养王国界面 中 card信息
	public static String CARD_API=URL_ROOT+"r=animal/cardApi&sig=";
	//王国贡献
	public static String CONTRIBUTE_RANK=URL_ROOT+"r=rank/contributionRankApi&sig=";
	//王国贡献
	public static String GET_TICKET_RANK=URL_ROOT+"r=star/rankApi&sig=";
	//人气排行榜
	public static String RQ_RANK=URL_ROOT+"r=rank/rqRankApi&sig=";
	//用户宠物列表
	public static String USER_PETS=URL_ROOT+"r=user/petsApi&sig=";
	//更改用户的默认宠物
	public static String CHANGE_DEFAULT_KINGDOM=URL_ROOT+"r=user/chgDefAniApi&sig=";
	//用户地址
	public static String USER_ADDRESS=URL_ROOT+"r=animal/addressApi&sig=";
	//是否已经上传声音
	public static String isVoicedApi=URL_ROOT+"r=animal/isVoicedApi&sig=";
	
	//商城礼物列表
	public static String MARKET_ITEMS=URL_ROOT+"r=item/listApi&sig=";
	
	
	//用户信息查询路径info
	public static String INFO_PATH=URL_ROOT+"r=user/infoApi&sig=";
	//宠物信息
	public static String ANIMAL_INFO_PATH=URL_ROOT+"r=animal/infoApi&sig=";
	//宠物信息
	public static String TICKET_CARD=URL_ROOT+"r=star/contriApi&sig=";
    //用户信息Json解析对象
	public static InfoJson infoJson;
	//用户个人主页   图片列表，每次 返回10个
	public static String USER_IMAGES="http://"+IP+URL_ROOT+"r=user/imagesApi&";
	//王国图片列表
	public static String KINGDOM_IMAGES="http://"+IP+URL_ROOT+"r=animal/imagesApi&";
	//Favorite   图片列表，每次 返回10个
	public static String IMAGE_FAVORITE="http://"+IP+URL_ROOT+"r=image/favoriteApi&";	
	//Recommend  图片列表，每次 返回10个
	public static String IMAGE_RECOMMEND="http://"+IP+URL_ROOT+"r=image/recommendApi&";
	//Favorite   图片列表，每次 返回10个
	public static String IMAGE_RANDOM="http://"+IP+URL_ROOT+"r=image/randomApi&";
	//获得一张图片的详细信息
	public static String IMAGE_INFO="http://"+IP+URL_ROOT+"r=image/infoApi&";
	//获得其他用户的详细信息
	public static String OTHER_INFO="http://"+IP+URL_ROOT+"r=user/otherApi&";
	//根据一串user_id获取多个user信息
	public static String OTHERS_INFO="http://"+IP+URL_ROOT+"r=user/othersApi&";
	
	//黑名单列表
	public static String BLOCK_LIST="http://"+IP+URL_ROOT+"r=talk/blockListApi&";
	
	//根据一串user_id获取多个user信息
	public static String ANIMAL_OTHERS_INFO="http://"+IP+URL_ROOT+"r=animal/othersApi&&";
	//头像上传路径
	public static String USER_UPDATE_TX="http://"+IP+URL_ROOT+"r=user/txApi&sig=";
	public static String Animal_UPDATE_TX="http://"+IP+URL_ROOT+"r=animal/txApi&sig=";
	
	//加关注
	public static String USER_FOLLOW="http://"+IP+URL_ROOT+"r=animal/followApi&";
	//取消关注
	public static String USER_UNFOLLOW="http://"+IP+URL_ROOT+"r=animal/unfollowApi&";
	//用户关注列表
	public static String USER_FOLLOWING="http://"+IP+URL_ROOT+"r=user/followingApi&";
	//被关注列表
	public static String USER_FOLLOWER="http://"+IP+URL_ROOT+"r=user/followerApi&";
	//上传图片路径
	public static  String UPLOAD_IMAGE_PATH=URL_ROOT+"r=image/uploadApi&sig=";//不需要加密
	
	//上传音频
    public static  String UPLOAD_VOICE_PATH=URL_ROOT+"r=animal/voiceUpApi&sig=";//不需要加密
    //下载声音
    public static String DOWNLOAD_VOICE_URL=URL_ROOT+"r=animal/voiceDownApi&sig=";//不需要加密

    //官方话题列表
    public static String IMAGE_TOPIC_API=URL_ROOT+"r=image/topicApi&sig=";//不需要加密
	//上传图片返回url地址，图片下载地址
	

	//查看一张图片的所有信息
	public static String DOWNLOAD_IMAGE_INFO=URL_ROOT+"r=image/infoApi&img_id";
	//喜欢图片路径
	public static String LIKE_IMAGE_PATH=URL_ROOT+"r=image/likeApi";
	//删除图片路径
	public static String DELETE_IMAGE_PATH=URL_ROOT+"r=image/deleteApi";
	
	//活动相关api
	//获取活动列表
	public static String ACTIVITY_LIST="http://"+IP+URL_ROOT+"r=topic/listApi";
    //活动宣传图
	public static String ACTIVITY_IMAGE="http://"+IP+"/images/topic/";
	//获取某一活动详细信息
	public static String ACTIVITY_INFO="http://"+IP+URL_ROOT+"r=topic/infoApi";
	//获取奖品详细信息
	public static  String ACTIVITY_REWARD_INFO="http://"+IP+URL_ROOT+"r=topic/rewardApi";
	//活动详情的最新列表
	public static String ACTIVITY_NEWEST="http://"+IP+URL_ROOT+"r=topic/newestApi&";
	//活动详情的最热列表
	public static String ACTIVITY_POPULAR="http://"+IP+URL_ROOT+"r=topic/popularApi&";
	
	
	
	//消息
	//消息列表 含消息和系统   &mail_id=&is_system=;
	public static String MAIL_LIST="http://"+IP+URL_ROOT+"r=mail/listApi";
	//发送消息  采用post方式  传递参数 to_id  和body
	public static String MAIL_CREATE="http://"+IP+URL_ROOT+"r=talk/sendMsgApi";
	
	//对话列表api
	public static String TALK_LIST=URL_ROOT+"r=talk/listApi&sig=";
	//对话的talk_id
	public static String TALK_ID=URL_ROOT+"r=talk/searchApi&sig=";
	//对话列表api
	public static String TALK_DELETE=URL_ROOT+"r=talk/deleteApi&sig=";
	//根据宠物昵称搜索宠物
	public static String SEARCH_PET=URL_ROOT+"r=animal/searchApi&sig=";
	//根据用户昵称，
	public static String SEARCH_USER=URL_ROOT+"r=user/searchApi&sig=";
	//删除消息  mail_id
	public static String MAIL_DELETE="http://"+IP+URL_ROOT+"r=talk/deleteApi";
	//获取最新消息数目和活动数目
	public static String MAIL_ACTIVITY_NEW_NUM="http://"+IP+URL_ROOT+"r=user/notifyApi";
	//发表评论  采用post方式  传递参数 img_id 和body
	public static String ADD_A_COMMENT="http://"+IP+URL_ROOT+"r=image/commentApi";
	
	//获取种族类别
	public static String GET_RACE_TYPE="http://"+IP+URL_ROOT+"r=user/typeApi";
	
	//判断新浪账号是否绑定
    public static String WELCOME="http://"+IP+URL_ROOT+"r=user/welcomeApi";
	//经验
	public static final int LEVEL_1=30;
	public static final int LEVEL_2=150;
	public static final int LEVEL_3=400;
	public static final int LEVEL_4=750;
	public static final int LEVEL_5=1200;
	public static final int LEVEL_6=1800;
	

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
    public static final String APP_KEY      = "4013993168";//
    public static final String APP_SECRET="e3e8a9fde01202ea049095f79ddc904e";
    public static final String Weixin_APP_KEY="wx8461378bbf9bda70";
    public static final String Weixin_APP_SECRET="60dd005c505bf45fe1b2e61af48e58c0";
    public static IWXAPI api;
    /*
     * 签名   c5be57472fb352e042aac4a66f831a35   新浪
     * 微信    a3e7b7ff1d4e3c1091dc2cc2e2d779de   272d2b7823db47ac4534765152c584b1
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
    /** 通过 code 获取 Token 的 URL */
    public static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";
	
    
    //提示消息字符串
    public static final String NOTE_MESSAGE_1="数据正在加载中，请稍后";
    public static final String NOTE_MESSAGE_2="网络连接异常，请检查网络";
    public static final String NOTE_MESSAGE_3="发生错误，客户端似乎有问题";
    public static final String NOTE_MESSAGE_4="服务器由于遇到错误而不能完成该请求";
    public static final String NOTE_MESSAGE_5="上传头像失败";
    
    
    
    
    //打赏时，是否显示提示框
    public static final String GIVE_FOOD_NOTE_SHOW="GIVE_FOOD_NOTE_SHOW";	
    public static final String GIVE_TICKET_NOTE_SHOW="GIVE_TICKET_NOTE_SHOW";	
    //"setup" SharedPreference 中存储内容的key值
    
    public static final String SHAREDPREFERENCE_NAME="setup";	
    public static final String GIFT_INFO="GIFT_INFO";	
    public static final String GIFT_INFO_CODE="GIFT_INFO_CODE";	
    //用于存储基本信息，导航图片是否显示等
    public static final String BASEIC_SHAREDPREFERENCE_NAME="basic";	
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE1="guide1";	
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE2="guide2";
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE3="guide3";
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE4="guide4";
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE5="guide5";
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE6="guide6";
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE7="guide7";
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE8="guide8";
    public static final String BASEIC_SHAREDPREFERENCE_NAME_GUIDE9="guide9";
    public static final String BASEIC_VERSION="version";
    public static final String LOCK_TO_WEIXIN="LOCK_TO_WEIXIN";//账号绑定到微信，返回boolean值   是否绑定
    public static final String LOCK_TO_XINLANG="LOCK_TO_XINLANG";//账号绑定到新浪，返回boolean值   是否绑定
    //照片是否同步发送到微信朋友圈
    public static final String PICTURE_SEND_TO_WEIXIN="PICTURE_SEND_TO_WEIXIN";
    //照片是否同步发送到新浪微博
    public static final String PICTURE_SEND_TO_XINLANG="PICTURE_SEND_TO_XINLANG";
    //用户账号可见性 值为int 1为所有人；2为我关注的人；3为自己
    public  static final String ACOUNT_VISIVAL_AREAR="ACOUNT_VISIVAL_AREAR";
    //当前处于的星球
    public static final String CURRENT_STAR="CURRENT_STAR";//值为String cat（猫星），dog（汪星）
    //是否为第一次启动应用
    public static final String IS_FIRST_START_APP="IS_FIRST_START_APP";//值为boolean  true或false
    
    
    public static int whereShare=-1;//0,照片详情;1,照片发布;2,宠物资料；3，用户资料；4，摇一摇；5，捣捣乱
    public static int shareMode=-1;//0,分享到微信；1，分享到朋友圈;2,新浪微博
    
}
