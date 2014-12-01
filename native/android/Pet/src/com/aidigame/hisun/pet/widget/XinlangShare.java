package com.aidigame.hisun.pet.widget;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.aidigame.hisun.pet.FirstPageActivity;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.ShakeActivity;
import com.aidigame.hisun.pet.ui.SubmitPictureActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.UsersAPI;

public class XinlangShare {
	public static ShareXinlangResultListener listener;
	public static void xinlangAuth(final Activity context){
		WeiboAuth weiboAuth=new WeiboAuth(context, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		weiboAuth.authorize(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub
				
//					Toast.makeText(FirstPageActivity.this, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
					LogUtil.i("exception", arg0.getMessage());
					return;
			}
			
			@Override
			public void onComplete(Bundle arg0) {
				// TODO Auto-generated method stub
				LogUtil.i("me", "授权code完毕");
				if(arg0==null){
					Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
					return;
				};
				String code=arg0.getString("code");
				if(code==null){
					Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
					return;
				};;
				WeiboParameters parameters=new WeiboParameters();
				parameters.put(WBConstants.AUTH_PARAMS_CLIENT_ID,Constants.APP_KEY);
				parameters.put(WBConstants.AUTH_PARAMS_CLIENT_SECRET, Constants.APP_SECRET);
				parameters.put(WBConstants.AUTH_PARAMS_GRANT_TYPE,    "authorization_code");
				parameters.put(WBConstants.AUTH_PARAMS_CODE,          code);
				parameters.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,  Constants.REDIRECT_URL);
				//异步请求获取token
				AsyncWeiboRunner.requestAsync(Constants.OAUTH2_ACCESS_TOKEN_URL, parameters, "POST", new RequestListener() {
					
					@Override
					public void onWeiboException(WeiboException arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
					}
					
					@Override
					public void onComplete(String arg0) {
						// TODO Auto-generated method stub
						if(arg0!=null){
							/*
							 * {"access_token":"2.00iDvQeF2RJa2Bd245f03f0fgRNORB","remind_in":"157679999",
							 * "expires_in":157679999,"uid":"5175750186",
							 * "scope":"follow_app_official_microblog"}
							 */
							Oauth2AccessToken accessToken=new Oauth2AccessToken(arg0);
							LogUtil.i("me", ""+arg0);
							if(accessToken!=null&&accessToken.isSessionValid()){
								SharedPreferences sp=context.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
								Editor editor=sp.edit();
								editor.putString("xinlangToken", arg0);
								editor.commit();
								Constants.accessToken=accessToken;
								LogUtil.i("me", ""+arg0);
								getXinLangInfo(context);
								if (context instanceof NewShowTopicActivity) {
									
								}
								if(context instanceof NewHomeActivity){
									NewHomeActivity.homeActivity.setupFragment.getXinlangToken(true);
								}
								
								Toast.makeText(context, "获取新浪微博授权成功", Toast.LENGTH_LONG).show();
							}else{
								Toast.makeText(context, "获取新浪微博授权  失败", Toast.LENGTH_LONG).show();;
							}
						}else{
							Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();;
						}
					}
				});
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Toast.makeText(context, "获取新浪微博授权 失败", Toast.LENGTH_LONG).show();
				return;
			}
		}, WeiboAuth.OBTAIN_AUTH_CODE);
	}
	
	public static void sharePicture(UserImagesJson.Data data,final Context context){
		boolean flag=false;
		WeiboParameters parameters=new WeiboParameters();
		if(data.comment==null||"".equals(data.comment)){
			parameters.put("status", data.des);
		}else{
			parameters.put("status", data.des);
		}
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=2;
		parameters.put("pic", BitmapFactory.decodeFile(data.path,options));
		parameters.put("access_token", Constants.accessToken.getToken());
		LogUtil.i("exception", "新浪微博分享照片:"+data.path);
		AsyncWeiboRunner.requestAsync("https://api.weibo.com/2/statuses/upload.json", parameters, "POST", new RequestListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub
				LogUtil.i("exception", "新浪微博分享照片异常"+arg0.getMessage()+";token="+Constants.accessToken.getToken());
				LogUtil.i("me","分享到新浪微博操作失败，原因是"+arg0.getMessage() +";token="+Constants.accessToken.getToken());
				Toast.makeText(context, "分享到新浪微博操作失败，原因是"+arg0.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				LogUtil.i("exception", "新浪微博分享照片返回结果："+arg0);
				LogUtil.i("exception",";token="+Constants.accessToken.getToken());
				LogUtil.i("me",";token="+Constants.accessToken.getToken());
				Toast.makeText(context, "成功分享到新浪微博", Toast.LENGTH_LONG).show();
				/*if(context instanceof ShowTopicActivity){
					ShowTopicActivity sh=(ShowTopicActivity)context;
					sh.shareNumChange();
				}*/
				if(Constants.whereShare==1){
					
					SubmitPictureActivity.submitPictureActivity.addShares(false);
				}else if(Constants.whereShare==2){
				}else if(Constants.whereShare==3){
				}else if(Constants.whereShare==4){
					if(ShakeActivity.shakeActivity!=null){
						ShakeActivity.shakeActivity.shareNumChange();
					}
				}
				if(listener!=null){
					listener.resultOk();
				}
				/*
				 * 06-22 10:08:09.660: I/exception(12548): 新浪微博分享照片返回结果：
				 * {"created_at":"Sun Jun 22 10:07:57 +0800 2014","id":3724203955699228,
				 * "mid":"3724203955699228","idstr":"3724203955699228","text":"房子",
				 * "source":"<a href=\"http://open.weibo.com\" rel=\"nofollow\">未通过审核应用</a>",
				 * "favorited":false,"truncated":false,"in_reply_to_status_id":"",
				 * "in_reply_to_user_id":"","in_reply_to_screen_name":"",
				 * "pic_urls":[{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/005EgV3Ijw1ehmo0z6m4gj30lc0sg3zo.jpg"}],
				 * "thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/005EgV3Ijw1ehmo0z6m4gj30lc0sg3zo.jpg","bmiddle_pic":"http://ww1.sinaimg.cn/bmiddle/005EgV3Ijw1ehmo0z6m4gj30lc0sg3zo.jpg","original_pic":"http://ww1.sinaimg.cn/large/005EgV3Ijw1ehmo0z6m4gj30lc0sg3zo.jpg","geo":null,"user":{"id":5175750186,"idstr":"5175750186","class":1,"screen_name":"阿猫阿狗APP","name":"阿猫阿狗APP","province":"11","city":"5","location":"北京 朝阳区","description":"","url":"","profile_image_url":"http://tp3.sinaimg.cn/5175750186/50/40057090019/0","profile_url":"u/5175750186","domain":"","weihao":"","gender":"f","followers_count":1,"friends_count":11,"statuses_count":0,"favourites_count":0,"created_at":"Tue Jun 10 15:08:07 +0800 2014","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/5175750186/180/40057090019/0","avatar_hd":"http://ww2.sinaimg.cn/crop.16.2.59.59.1024/005EgV3Igw1eh91e8pr98j302r027dfp.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":0,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"worldcup_guess":0},
				 * "reposts_count":0,"comments_count":0,"attitudes_count":0,"mlevel":0,"visible":{"type":0,"list_id":0}}
				 */
			}
		});
		
		
	}
    /**
     * 获取新浪微博账号信息，昵称及头像
     * @param context
     */
	public static void getXinLangInfo(Context context){
    	final SharedPreferences sp=context.getSharedPreferences("setup", Context.MODE_WORLD_WRITEABLE);
        String str=sp.getString("xinlangToken", null);
        if(str!=null){
        	
        	Oauth2AccessToken token2=new Oauth2AccessToken(str);
        	if(token2!=null&&token2.isSessionValid()){
        		String uid=token2.getUid();
        		UsersAPI usersAPI=new UsersAPI(token2);
        		usersAPI.show(Long.parseLong(uid), new RequestListener() {
					
					@Override
					public void onWeiboException(WeiboException arg0) {
						// TODO Auto-generated method stub
						//失败
					}
					
					@Override
					public void onComplete(String arg0) {
						// TODO Auto-generated method stub
			           /*
			            * {"id":5175750186,"idstr":"5175750186","class":1,"screen_name":"阿猫阿狗APP",
			            * "name":"阿猫阿狗APP","province":"11","city":"5","location":"北京 朝阳区",
			            * "description":"","url":"",
			            * "profile_image_url":"http://tp3.sinaimg.cn/5175750186/50/40057090019/0",
			            * "profile_url":"u/5175750186","domain":"",
			            * "weihao":"","gender":"f","followers_count":7,"friends_count":11,
			            * "statuses_count":30,"favourites_count":0,
			            * "created_at":"Tue Jun 10 15:08:07 +0800 2014","following":false,
			            * "allow_all_act_msg":false,"geo_enabled":true,"verified":false,
			            * "verified_type":-1,"remark":"",
			            * "status":{"created_at":"Wed Jul 02 18:45:49 +0800 2014",
			            *           "id":3727958164327545,"mid":"3727958164327545",
			            *           "idstr":"3727958164327545","text":"宠物秀秀",
			            *           "source":"<a href=\"http://app.weibo.com/t/feed/1Zlc5F\" rel=\"nofollow\">未通过审核应用</a>",
			            *           "favorited":false,"truncated":false,"in_reply_to_status_id":"",
			            *           "in_reply_to_user_id":"","in_reply_to_screen_name":"",
			            *           "pic_urls":[{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/005EgV3Ijw1ehyn6xapubj304v03y74b.jpg"}],
			            *           "thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/005EgV3Ijw1ehyn6xapubj304v03y74b.jpg",
			            *           "bmiddle_pic":"http://ww3.sinaimg.cn/bmiddle/005EgV3Ijw1ehyn6xapubj304v03y74b.jpg",
			            *           "original_pic":"http://ww3.sinaimg.cn/large/005EgV3Ijw1ehyn6xapubj304v03y74b.jpg",
			            *           "geo":null,"reposts_count":0,"comments_count":0,"attitudes_count":0,"mlevel":0,
			            *           "visible":{"type":0,"list_id":0},"darwin_tags":[]
			            *          },
			            * "ptype":0,"allow_all_comment":true,
			            * "avatar_large":"http://tp3.sinaimg.cn/5175750186/180/40057090019/0",
			            * "avatar_hd":"http://ww2.sinaimg.cn/crop.16.2.59.59.1024/005EgV3Igw1eh91e8pr98j302r027dfp.jpg",
			            * "verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"",
			            * "verified_source_url":"","follow_me":false,"online_status":1,"bi_followers_count":0,
			            * "lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,
			            * "worldcup_guess":0
			            * }
			            */
						final String temp=arg0;
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									JSONObject o1=new JSONObject(temp);
									String name=o1.getString("name");
									String iconUrl=o1.getString("profile_image_url");
									String idString=o1.getString("idstr");
									SharedPreferences.Editor editor=sp.edit();
									editor.putString("xinlang_name",name );
									if(iconUrl!=null){
										boolean flag=HttpUtil.downloadImage(iconUrl, Constants.Picture_ICON_Path+File.separator+"xinlang_"+idString+".png");
										if(flag){
											editor.putString("xinlang_icon", Constants.Picture_ICON_Path+File.separator+"xinlang_"+idString+".png");
										}
									}
									editor.commit();
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();
						
						
					}
				});
        	}
        	
        }
    }
   
	public static interface ShareXinlangResultListener{
    	void resultOk();
    }
}
