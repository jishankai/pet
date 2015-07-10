package com.aidigame.hisun.imengstar.adapter;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.bean.PetPicture.Comments;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.huanxin.NewSmileUtils;
import com.aidigame.hisun.imengstar.ui.ChargeActivity;
import com.aidigame.hisun.imengstar.ui.Dialog4Activity;
import com.aidigame.hisun.imengstar.ui.DialogGiveSbGiftActivity1;
import com.aidigame.hisun.imengstar.ui.DialogNoteActivity;
import com.aidigame.hisun.imengstar.ui.HomeActivity;
import com.aidigame.hisun.imengstar.ui.MarketActivity;
import com.aidigame.hisun.imengstar.ui.NewPetKingdomActivity;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.HorizontalListView2;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.widget.fragment.DialogJoinKingdom;
import com.aidigame.hisun.imengstar.widget.fragment.DialogJoinKingdom.ResultListener;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ClassicPictureAdapter extends BaseAdapter {
	private ArrayList<PetPicture> pictures;
	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions displayImageOptions2;//显示图片的格式
	private Handler handler;
	private View popupParent,black_layout;
	private ClassicPictureAdapterListener classicPictureAdapterListener;
	public Holder holder;
	private int mode=2;//2,精选；1，关注
	public ClassicPictureAdapter(ArrayList<PetPicture> pictures,Context context,View popupParent,View black_layout,int mode) {
		this.pictures=pictures;
		this.context=context;
		this.popupParent=popupParent;
		this.black_layout=black_layout;
		this.mode=mode;
		handler=HandleHttpConnectionException.getInstance().getHandler(context);
		displayImageOptions2=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.pet_icon)
	            .showImageOnFail(R.drawable.pet_icon)
		        .cacheInMemory(false)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(ops)
                .build();
	}
	public void update(ArrayList<PetPicture> pictures){
		this.pictures=pictures;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pictures.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pictures.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder1=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_classic_pictures_list, null);
			holder1=new Holder();
			holder1.bigIv=(ImageView)convertView.findViewById(R.id.big_iv);
			holder1.moreIv=(ImageView)convertView.findViewById(R.id.more_iv);
			holder1.pentIv=(ImageView)convertView.findViewById(R.id.peng_iv);
			holder1.likedIv=(ImageView)convertView.findViewById(R.id.like_iv);
			holder1.petNameTv=(TextView)convertView.findViewById(R.id.pet_name_tv);
			holder1.pengTv=(TextView)convertView.findViewById(R.id.peng_tv);
			holder1.topicTv=(TextView)convertView.findViewById(R.id.topic_tv);
			holder1.desTv=(TextView)convertView.findViewById(R.id.des_tv);
			holder1.likeTv=(TextView)convertView.findViewById(R.id.like_tv);
			holder1.likeNumTv=(TextView)convertView.findViewById(R.id.like_num_tv);
			holder1.commentNumTv=(TextView)convertView.findViewById(R.id.comment_num_tv);
			holder1.comment1Tv=(TextView)convertView.findViewById(R.id.comment1_tv);
			holder1.comment2Tv=(TextView)convertView.findViewById(R.id.comment2_tv);
			holder1.petIconView=(RoundImageView)convertView.findViewById(R.id.pet_icon_view);
			holder1.comment1IconView=(TextView)convertView.findViewById(R.id.comment1_icon_view);
			holder1.commnet2IconView=(TextView)convertView.findViewById(R.id.comment2_icon_view);
			holder1.pengtLayout=(LinearLayout)convertView.findViewById(R.id.peng_layout);
			holder1.likeLayout=(LinearLayout)convertView.findViewById(R.id.like_layout);
			holder1.commentLayout=(LinearLayout)convertView.findViewById(R.id.comment_layout);
			holder1.giftLayout=(LinearLayout)convertView.findViewById(R.id.gift_layout);
			holder1.seeAllCommentLayout=(LinearLayout)convertView.findViewById(R.id.see_all_comment_layout);
			holder1.comment1Layout=(LinearLayout)convertView.findViewById(R.id.comment1_layout);
			holder1.comment2Layout=(LinearLayout)convertView.findViewById(R.id.comment2_layout);
			holder1.horizontalListView2=(HorizontalListView2)convertView.findViewById(R.id.galleryview);
			holder1.seeAllLikeLayout=(LinearLayout)convertView.findViewById(R.id.see_all_like_layout);
			holder1.timeTv=(TextView)convertView.findViewById(R.id.time_tv);
			holder1.containerLayout=(LinearLayout)convertView.findViewById(R.id.container_layout);
			convertView.setTag(holder1);
		}else{
			holder1=(Holder)convertView.getTag();
		}
		/*if(position==0){
			LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.containerLayout.getLayoutParams();
			if(param!=null){
				param.topMargin=context.getResources().getDimensionPixelOffset(R.dimen.one_dip)*32;
				holder.containerLayout.setLayoutParams(param);
			}
		}*/
		final Holder holder=holder1;
		final PetPicture picture=pictures.get(position);
		if(mode==2){
			holder.timeTv.setVisibility(View.GONE);
			holder.pengtLayout.setVisibility(View.VISIBLE);
			pengTA(holder, picture.animal);
		}else{
			holder.timeTv.setVisibility(View.VISIBLE);
			holder.pengtLayout.setVisibility(View.GONE);
			holder.timeTv.setText(StringUtil.judgeTime(picture.create_time));
		}
		
		loadIcon(holder.petIconView, picture.animal);
		holder.petNameTv.setText(picture.animal.pet_nickName);
		/*if(StringUtil.isEmpty(picture.topic_name)){
			holder.topicTv.setVisibility(View.GONE);
		}else{
			holder.topicTv.setVisibility(View.VISIBLE);
			holder.topicTv.setText("#"+picture.topic_name+"#");
		}
		if(StringUtil.isEmpty(picture.cmt)){
			holder.desTv.setVisibility(View.GONE);
		}else{
			holder.desTv.setVisibility(View.VISIBLE);
			holder.desTv.setText(picture.cmt);
		}*/
		holder.desTv.setVisibility(View.GONE);
		holder.topicTv.setVisibility(View.GONE);
		if(StringUtil.isEmpty(picture.topic_name)){
			holder.topicTv.setVisibility(View.GONE);
		}else{
			holder.topicTv.setVisibility(View.VISIBLE);
			String html="<html>"
				    +"<body>"
				    + "<font color=\"#ff6666\">"
					+(StringUtil.isEmpty(picture.topic_name)?"":("#"+picture.topic_name+"#"))
					+ "</font>"
				    +"</body>"
		            +"</html>";
			holder.topicTv.setText(Html.fromHtml(html));;
		}
		if(StringUtil.isEmpty(picture.cmt)){
			holder.desTv.setVisibility(View.GONE);
		}else{
			holder.desTv.setVisibility(View.VISIBLE);
			
			Spannable span = NewSmileUtils.getSmiledText(context, picture.cmt);
			// 设置内容
			holder.desTv.setText(span, BufferType.SPANNABLE);
//			holder.desTv.setText(Html.fromHtml(html));
		}
		
		
		displayImage(holder.bigIv, picture.url, position);
		
		MyClickListener myClickListener=new MyClickListener(holder, picture,position);
		if(current_holder_position!=-1&&current_holder_position==position){
			this.holder=new Holder();
			copyHolder(this.holder,holder);;
		}
		holder.likeLayout.setOnClickListener(myClickListener);
		holder.commentLayout.setOnClickListener(myClickListener);
		holder.giftLayout.setOnClickListener(myClickListener);
		holder.petIconView.setOnClickListener(myClickListener);
		holder.seeAllLikeLayout.setOnClickListener(myClickListener);
		holder.horizontalListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				PetPicture p7=(PetPicture)picture;
				Intent intent7=new Intent(context,NewShowTopicActivity.class);
				intent7.putExtra("PetPicture", p7);
				intent7.putExtra("mode", 1);
				context.startActivity(intent7);
			}
		});
		if(picture.like_txUrlList!=null&&picture.like_txUrlList.size()>0){
			holder.seeAllLikeLayout.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.horizontalListView2.getLayoutParams();
			int oneDip=context.getResources().getDimensionPixelOffset(R.dimen.one_dip);
			if(param==null){
				param=new LinearLayout.LayoutParams(oneDip*66,oneDip*32);
			}
			param.height=oneDip*32;
			int num=picture.like_txUrlList.size();
			if(picture.like_txUrlList.size()>=6)num=6;
			num++;
			param.width=oneDip*78*num;
			holder.horizontalListView2.setLayoutParams(param);
			holder.horizontalListView2.setVisibility(View.VISIBLE);
			holder.horizontalListView2.setAdapter(new GalleryAdapter2(context, picture.like_txUrlList));
			
			if(PetApplication.myUser!=null&&picture.like_txUrlList.contains(PetApplication.myUser.u_iconUrl)){
				holder.likedIv.setImageResource(R.drawable.page_liked);
				holder.likeTv.setText("已赞");
			}else{
				holder.likedIv.setImageResource(R.drawable.page_like);
				holder.likeTv.setText("赞Ta");
			}
		}else{
			holder.horizontalListView2.setVisibility(View.INVISIBLE);
			holder.seeAllLikeLayout.setVisibility(View.GONE);
			holder.likedIv.setImageResource(R.drawable.page_like);
			holder.likeTv.setText("赞Ta");
		}
		holder.likeNumTv.setText(""+picture.likes);
		holder.comment1Layout.setVisibility(View.GONE);
		holder.comment2Layout.setVisibility(View.GONE);
		holder.pp=picture;
		holder.comment1Layout.setOnClickListener(myClickListener);
		holder.comment2Layout.setOnClickListener(myClickListener);
		holder.seeAllCommentLayout.setOnClickListener(myClickListener);
		holder.moreIv.setOnClickListener(myClickListener);
		setComments(holder, picture);
		return convertView;
	}
	public void loadIcon(RoundImageView icon,final Animal  data){
		imageLoader =ImageLoader.getInstance();
		int w=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader.displayImage(Constants.ANIMAL_THUBMAIL_DOWNLOAD_TX+data.pet_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", icon, displayImageOptions2, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void loadIcon2(RoundImageView icon,final String url){
		imageLoader =ImageLoader.getInstance();
		int w=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+url+"@"+w+"w_"+w+"h_0l.jpg", icon, displayImageOptions2, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void displayImage(ImageView iv,String url,final int position){
		ImageFetcher imageFetcher=new ImageFetcher(context, Constants.screen_width);
		imageFetcher.setWidth(Constants.screen_width);
        int h=Constants.screen_height/2;
        int w=Constants.screen_width/2;
        imageFetcher.IP=imageFetcher.UPLOAD_THUMBMAIL_IMAGE;
        imageFetcher.setImageCache(new ImageCache(context, url+"@"+w+"w_"+h+"h_"+"1l.jpg"));
        imageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
        	
			@Override
			public void onComplete(Bitmap bitmap) {
				// TODO Auto-generated method stub
				
			    
			}
			@Override
			public void getPath(String path) {
				// TODO Auto-generated method stub
				File f=new File(path);
				for(int i=0;i<pictures.size();i++){
					if(f.getName().contains(pictures.get(i).url)){
						pictures.get(i).petPicture_path=path;;
					}
				}
				
			}
			
			
		});
        
        
        
        
        imageFetcher.loadImage(url+"@"+w+"w_"+h+"h_"+"1l.jpg", iv, /*options*/null);
        iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				HomeActivity.showTopic=true;
				PetPicture p=(PetPicture)pictures.get(position);
				Intent intent6=new Intent(context,NewShowTopicActivity.class);
				intent6.putExtra("PetPicture", p);
				context.startActivity(intent6);
			}
		});
        
	}
	public void pengTA(Holder holder,final Animal animal){
		 if(/*animal.hasJoinOrCreate*/PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(animal)){
			   holder.pentIv.setImageResource(R.drawable.icon_tick);
				holder.pengtLayout.setClickable(false);
				holder.pengTv.setText("捧ing");
				holder.pengTv.setTextColor(context.getResources().getColor(R.color.peng_ing_color));
//			   holder.supportIv.setVisibility(View.INVISIBLE);
		   }else{
			   holder.pentIv.setImageResource(R.drawable.plus);
				holder.pengtLayout.setClickable(true);
				holder.pengTv.setText("捧TA");
				holder.pengTv.setTextColor(context.getResources().getColor(R.color.peng_ing_color));
			   
			   final ImageView supportIV=holder.pentIv;
			   final TextView pentTV=holder.pengTv;
			   final LinearLayout pengLayout=holder.pengtLayout;
			    holder.pengtLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(UserStatusUtil.isLoginSuccess(HomeActivity.homeActivity,popupParent,HomeActivity.homeActivity.discoveryFragment.black_layout)){
							
							if(PetApplication.myUser!=null&&PetApplication.myUser.aniList!=null&&PetApplication.myUser.aniList.contains(animal) ){
//								pengta_tv.setVisibility(View.GONE);
								Intent intent=new Intent(context,DialogNoteActivity.class);
								intent.putExtra("mode", 10);
								intent.putExtra("info", "您已经捧TA了");
								context.startActivity(intent);
								supportIV.setImageResource(R.drawable.icon_tick);
								pentTV.setText("捧ing");
								pengLayout.setClickable(false);
								return ;
							}
							
							
							int num=0;
							int count=0;
							for(int i=0;i<PetApplication.myUser.aniList.size();i++){
//								if(Constants.user.aniList.get(i).master_id!=Constants.user.userId)
									count++;
							}
							
							
							if(count>=10&&count<=20){
								num=(count)*5;
							}else if(count>20){
								num=100;
							}
							
							if(PetApplication.myUser.coinCount<num){
//								DialogNote dialog=new DialogNote(HomeActivity.homeActivity.discoveryFragment.popupParent, HomeActivity.homeActivity, HomeActivity.homeActivity.discoveryFragment.black_layout, 1);
								/*Intent intent=new Intent(context,DialogNoteActivity.class);
								intent.putExtra("mode", 10);
								intent.putExtra("info", "钱包君告急！挣够金币再来捧萌星吧");
								context.startActivity(intent);*/
								
								Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
									
									@Override
									public void onClose() {
										// TODO Auto-generated method stub
									}
									
									@Override
									public void onButtonTwo() {
										// TODO Auto-generated method stub
										Intent intent=new Intent(context,ChargeActivity.class);
										context.startActivity(intent);
									}
									
									@Override
									public void onButtonOne() {
										// TODO Auto-generated method stub
									}
								};
								 Intent intent=new Intent(context,Dialog4Activity.class);
								 intent.putExtra("mode", 8);
								 intent.putExtra("num", num);
								 context.startActivity(intent);
							return;
								
						}
						
						
						DialogJoinKingdom dialog=new DialogJoinKingdom(HomeActivity.homeActivity.discoveryFragment.popupParent, HomeActivity.homeActivity, HomeActivity.homeActivity.discoveryFragment.black_layout, animal);
						dialog.setResultListener(new ResultListener() {
							
							@Override
							public void getResult(boolean isSuccess) {
								// TODO Auto-generated method stub
								if(isSuccess){
									supportIV.setImageResource(R.drawable.icon_tick);
									pentTV.setText("捧ing");
									pengLayout.setClickable(false);
								}
								
								animal.hasJoinOrCreate=isSuccess;
//								supportIV.setVisibility(View.INVISIBLE);
								/*if(isSuccess){
									p.setPeoplesNum(data.fans,isSuccess);
								}*/
							}
						});
						}
						
					}
				});
			   
			   
		   }
		
	}
	private   void actionLike(final Holder holder,final PetPicture picture){
		//TODO 判断有没有注册
		if(!UserStatusUtil.isLoginSuccess((Activity)context,popupParent,black_layout)){
			return;
		}
		//TODO 显示进度条
		/*if(showProgress!=null){
			showProgress.showProgress();
		}else{
			showProgress=new ShowProgress(this, progressLayout);
		}*/
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final boolean flag=HttpUtil.likeImage(picture,handler,context);
				
					holder.likedIv.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(flag){
								holder.likedIv.setImageResource(R.drawable.page_liked);
								holder.likeTv.setText("已赞");
								holder.likeNumTv.setText(""+picture.likes);
								LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)holder.horizontalListView2.getLayoutParams();
								int oneDip=context.getResources().getDimensionPixelOffset(R.dimen.one_dip);
								if(param==null){
									param=new LinearLayout.LayoutParams(oneDip*66,oneDip*32);
								}
								param.height=oneDip*32;
								int num=picture.like_txUrlList.size();
								if(picture.like_txUrlList.size()>=6)num=6;
								num++;
								param.width=oneDip*78*num;
								holder.horizontalListView2.setLayoutParams(param);
								GalleryAdapter2 ba=(GalleryAdapter2)(holder.horizontalListView2.getAdapter());
								if(ba!=null){
									holder.horizontalListView2.setVisibility(View.VISIBLE);
									holder.seeAllLikeLayout.setVisibility(View.VISIBLE);
									ba.update(picture.like_txUrlList);
									 ba.notifyDataSetChanged();
								}else {
									holder.horizontalListView2.setVisibility(View.VISIBLE);
									holder.seeAllLikeLayout.setVisibility(View.VISIBLE);
									holder.horizontalListView2.setAdapter(new GalleryAdapter2(context, picture.like_txUrlList) );
								}
						       
							}else{
								Intent intent=new Intent(context,DialogNoteActivity.class);
								intent.putExtra("mode", 10);
								intent.putExtra("info", "您已经点过赞了");
								context.startActivity(intent);
							}
							//TODO  取消进度条
//							showProgress.progressCancel();
						}
					});
				
			}
		}).start();
		
	}
	public void setComments(Holder holder,final PetPicture picture){
		if(holder==null&&this.holder!=null){
			holder=this.holder;
			this.holder=null;
			current_holder_position=-1;
		}else{
			
		}
		if(picture.commentsList!=null){
			holder.commentNumTv.setText("查看所有"+picture.commentsList.size()+"条评论");
			if(picture.commentsList.size()>=1){
				holder.comment1Layout.setVisibility(View.VISIBLE);
				Comments comments=picture.commentsList.get(0);
//				loadIcon2(holder.comment1IconView,picture.commentsList.get(0).usr_tx);
				
				
				
				Spannable span = NewSmileUtils.getSmiledText(context, comments.body);
				// 设置内容
				holder.comment1Tv.setText(span, BufferType.SPANNABLE);
				
				if(comments.isReply){
					
					String[] str=comments.reply_name.split("@");
					if(comments.reply_name.contains("@")){
						str=comments.reply_name.split("@");
					}else if(comments.reply_name.contains("&")){
						str=comments.reply_name.split("&");
					}else
					if(!StringUtil.isEmpty(comments.reply_name)){
						str=new String[2];
						str[0]=comments.name;
						str[1]=comments.reply_name;
					}
					if(str.length>1){
						String html="<html><body>"
								+ "<font color=\"#ff6666\">"
								+comments.name
								+ "</font>"
								/*+ "<font color=\"#3d3d3d\">"
		                        +"回复  "
								+ "</font>"
								+ "<font color=\"#ff6666\">"
								+str[1]+":"
								+ "</font>"*/
								+ "</body></html>";
						holder.comment1IconView.setText(Html.fromHtml(html));
					}else{
						holder.comment1IconView.setText(""+comments.name+":");
					}
					
				}else{
					holder.comment1IconView.setText(""+comments.name+":");
				}
				
				
				
			}
			if(picture.commentsList.size()>=2){
				holder.comment2Layout.setVisibility(View.VISIBLE);
				
				if(picture.commentsList.size()>1){
					Comments comments=picture.commentsList.get(1);
					Spannable span = NewSmileUtils.getSmiledText(context, picture.commentsList.get(1).body);
					// 设置内容
					holder.comment2Tv.setText(span, BufferType.SPANNABLE);
					if(comments.isReply){
						
						String[] str=comments.reply_name.split("@");
						if(comments.reply_name.contains("@")){
							str=comments.reply_name.split("@");
						}else if(comments.reply_name.contains("&")){
							str=comments.reply_name.split("&");
						}else
						if(!StringUtil.isEmpty(comments.reply_name)){
							str=new String[2];
							str[0]=comments.name;
							str[1]=comments.reply_name;
						}
						if(str.length>1){
							String html="<html><body>"
									+ "<font color=\"#ff6666\">"
									+comments.name
									+ "</font>"
									/*+ "<font color=\"#3d3d3d\">"
			                        +"回复  "
									+ "</font>"
									+ "<font color=\"#ff6666\">"
									+str[1]+":"
									+ "</font>"	*/	
									+ "</body></html>";
							holder.commnet2IconView.setText(Html.fromHtml(html));
						}else{
							holder.commnet2IconView.setText(""+comments.name+":");
						}
						
					}else{
						holder.commnet2IconView.setText(""+comments.name+":");
					}
			}
		}
		}else{
			holder.commentNumTv.setText("查看所有"+0+"条评论");
		}
	}
	public static class Holder{
		ImageView bigIv,moreIv,pentIv,likedIv;
		TextView petNameTv,pengTv,topicTv,desTv,likeTv,likeNumTv,commentNumTv,comment1Tv,comment2Tv,timeTv,comment1IconView,commnet2IconView;
		RoundImageView petIconView;
		LinearLayout pengtLayout,likeLayout,commentLayout,giftLayout,seeAllCommentLayout,comment1Layout,comment2Layout,seeAllLikeLayout,containerLayout;
		HorizontalListView2 horizontalListView2;
		public PetPicture pp;
 	}
	public static void copyHolder(Holder holder1,Holder holder2){
		holder1.commentNumTv=holder2.commentNumTv;
		holder1.comment1Layout=holder2.comment1Layout;
		holder1.comment1Tv=holder2.comment1Tv;
		holder1.comment1IconView=holder2.comment1IconView;
		holder1.comment2Layout=holder2.comment2Layout;
		holder1.comment2Tv=holder2.comment2Tv;
		holder1.commnet2IconView=holder2.commnet2IconView;
		holder1.pp=holder2.pp;
	}
	public  int current_holder_position=-1;
    class MyClickListener implements OnClickListener{
    	Holder holder;
    	PetPicture picture;
    	int postion;
        public MyClickListener(Holder holder,PetPicture picture,int position){
        	this.holder=holder;
        	this.picture=picture;
        	this.postion=position;
        }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.like_layout:
				actionLike(holder, picture);
				break;
			case R.id.gift_layout:
				if(UserStatusUtil.isLoginSuccess((Activity)context,popupParent,black_layout)){
					sendGift(picture.animal);
				}
				
				break;
			case R.id.comment_layout:
            if(classicPictureAdapterListener!=null){
            	current_holder_position=postion;
					classicPictureAdapterListener.clickComment(picture,holder);
				}
				break;
			case R.id.comment1_layout:
				if(!UserStatusUtil.isLoginSuccess((Activity)context,popupParent,black_layout)){
					return;
				}
				if(classicPictureAdapterListener!=null){
					current_holder_position=postion;
					classicPictureAdapterListener.clickReplyComment(picture.commentsList.get(0),picture,holder);
				}
				break;
			case R.id.comment2_layout:
				if(!UserStatusUtil.isLoginSuccess((Activity)context,popupParent,black_layout)){
					return;
				}
				if(classicPictureAdapterListener!=null){
					current_holder_position=postion;
					classicPictureAdapterListener.clickReplyComment(picture.commentsList.get(1),picture,holder);
				}
				break;
			case R.id.more_iv:
				if(classicPictureAdapterListener!=null){
					classicPictureAdapterListener.clickMore(picture);
				}
				break;
			case R.id.pet_icon_view:
				if(NewPetKingdomActivity.petKingdomActivity!=null){
					if(NewPetKingdomActivity.petKingdomActivity.loadedImage1!=null&&!NewPetKingdomActivity.petKingdomActivity.loadedImage1.isRecycled()){
						NewPetKingdomActivity.petKingdomActivity.loadedImage1.recycle();
					}
					NewPetKingdomActivity.petKingdomActivity.loadedImage1=null;
					NewPetKingdomActivity.petKingdomActivity.finish();
					NewPetKingdomActivity.petKingdomActivity=null;
				}
				Intent intent=new Intent(context,NewPetKingdomActivity.class);
				intent.putExtra("animal",picture.animal);
				context.startActivity(intent);
				break;
			case R.id.see_all_comment_layout:
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				HomeActivity.showTopic=true;
				PetPicture p=(PetPicture)picture;
				Intent intent6=new Intent(context,NewShowTopicActivity.class);
				intent6.putExtra("PetPicture", p);
				intent6.putExtra("mode", 3);
				context.startActivity(intent6);
				break;
			case R.id.see_all_like_layout:
				if(NewShowTopicActivity.newShowTopicActivity!=null){
					NewShowTopicActivity.newShowTopicActivity.recyle();
				}
				HomeActivity.showTopic=true;
				PetPicture p7=(PetPicture)picture;
				Intent intent7=new Intent(context,NewShowTopicActivity.class);
				intent7.putExtra("PetPicture", p7);
				intent7.putExtra("mode", 1);
				context.startActivity(intent7);
				break;

			default:
				break;
			}
		}
    	
    }
    public void setClassicPictureAdapterListener(ClassicPictureAdapterListener classicPictureAdapterListener){
    	this.classicPictureAdapterListener=classicPictureAdapterListener;
    }
    public void sendGift(final Animal animal){

		
		
		
		Intent intent2=new Intent(context,DialogGiveSbGiftActivity1.class);
		intent2.putExtra("animal",animal);
		context.startActivity(intent2);
		
		
		
		DialogGiveSbGiftActivity1 dgb=DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity;
		DialogGiveSbGiftActivity1.dialogGoListener=new DialogGiveSbGiftActivity1.DialogGoListener() {
			
			@Override
			public void toDo() {
				// TODO Auto-generated method stub
				
				Intent intent=intent=new Intent(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity,MarketActivity.class);
				
				
				DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.startActivity(intent);
				
			}

			@Override
			public void closeDialog() {
				// TODO Auto-generated method stub
				if(DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity!=null){
					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity.finish();
					DialogGiveSbGiftActivity1.dialogGiveSbGiftActivity=null;
				}
				
			}
			@Override
			public void lastResult(boolean isSuccess) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void unRegister() {
				// TODO Auto-generated method stub
//				dialog.dismiss();
				if(!UserStatusUtil.isLoginSuccess((Activity)context, popupParent, black_layout)){
	        		
	        	};
			}
		};
	}

	

    public static interface ClassicPictureAdapterListener{
    	void clickComment(PetPicture petPicture,Holder holder);
    	void clickReplyComment(com.aidigame.hisun.imengstar.bean.PetPicture.Comments comments,PetPicture petPicture,Holder holder);
    	void clickGift();
    	void clickMore(PetPicture picture);
    }
}
