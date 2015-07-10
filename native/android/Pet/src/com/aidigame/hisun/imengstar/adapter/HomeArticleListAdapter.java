package com.aidigame.hisun.imengstar.adapter;

import java.io.File;
import java.util.ArrayList;

import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Article;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.Gift;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.ui.ActivityWebActivity;
import com.aidigame.hisun.imengstar.ui.ArticleActivity;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.view.StaticViewPager;
import com.aidigame.hisun.imengstar.R;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeArticleListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	ImageLoader imageLoader;
	Context context;
	ArrayList<Article> articleList;
	public HomeArticleListAdapter(Context context,ArrayList<Article> giftList){
		this.context=context;
		this.articleList=giftList;
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.noimg)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
	}
    public void update(ArrayList<Article> giftList){
    	this.articleList=giftList;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return articleList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return articleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_fragment_home_article, null);
			holder=new Holder();
			holder.titleTv=(TextView)convertView.findViewById(R.id.title_tv);
			holder.title_1=(TextView)convertView.findViewById(R.id.title_1);
			holder.contextTv=(TextView)convertView.findViewById(R.id.content_tv);
			holder.topicIv=(ImageView)convertView.findViewById(R.id.topic_imge_iv);
//			holder.viewpager=(StaticViewPager)convertView.findViewById(R.id.topic_imge_iv);
			holder.contentIv=(ImageView)convertView.findViewById(R.id.content_iv);
			holder.contentLayout=(RelativeLayout)convertView.findViewById(R.id.content_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		Article article=articleList.get(position);
		if(position==0&&article.banner!=null&&article.banner.size()>0){
			holder.topicIv.setVisibility(View.VISIBLE);
			loadImage(holder.topicIv, article.banner.get(0),false);
			loadImage(new ImageView(context), article.banner.get(0), true);
			holder.title_1.setVisibility(View.VISIBLE);
			holder.title_1.setText(article.banner.get(0).title);
			/*holder.viewpager.setVisibility(View.VISIBLE);
			holder.viewpager.canScroll=false;
			initViewPager(holder.viewpager,article.banner);*/
		}else {
			holder.topicIv.setImageDrawable(new BitmapDrawable());
			holder.topicIv.setVisibility(View.GONE);
			holder.title_1.setVisibility(View.GONE);
//			holder.viewpager.setVisibility(View.GONE);
		}
		if(position==0&&StringUtil.isEmpty(article.title)){
			holder.titleTv.setVisibility(View.GONE);
			holder.contentLayout.setVisibility(View.GONE);
		}else{
			holder.titleTv.setVisibility(View.VISIBLE);
			holder.contentLayout.setVisibility(View.VISIBLE);
			holder.titleTv.setText(article.title);
			holder.contextTv.setText(article.description);
			loadImage(holder.contentIv, article,true);
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent intent=new Intent(context,ArticleActivity.class);
				intent.putExtra("article", articleList.get(position));*/
				Banner banner=new Banner();
				banner.icon_path=articleList.get(position).share_path;
				banner.description=articleList.get(position).description;
				banner.title=articleList.get(position).title;
				banner.url=articleList.get(position).url;
				Intent intent=new Intent(context,ActivityWebActivity.class);
				intent.putExtra("banner", banner);
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	public void loadImage(final ImageView icon,final Article article,final boolean isShare){
		
		imageLoader=ImageLoader.getInstance();
		String url="";
		if(isShare){
			url=article.icon;
		}else{
			url=article.image;
		}
		if(!isShare){
			icon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*Intent intent=new Intent(context,ArticleActivity.class);
					intent.putExtra("article", article);*/
					Banner banner=new Banner();
					banner.icon_path=article.share_path;
					banner.description=article.description;
					banner.title=article.title;
					banner.url=article.url;
					Intent intent=new Intent(context,ActivityWebActivity.class);
					intent.putExtra("banner", banner);
					context.startActivity(intent);
				}
			});
		}
			imageLoader.displayImage(url, icon, displayImageOptions,new ImageLoadingListener() {
				
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
					if(isShare){
					String name="article"+article.article_id+"_"+article.create_time;
					if(loadedImage!=null&&StringUtil.isEmpty(article.share_path)){
					   File f=new File(Constants.Picture_Root_Path+File.separator+name+".jpg");
					   if(f.exists()){
						   article.share_path=Constants.Picture_Root_Path+File.separator+name+".jpg";
					   }else{
						   String path=ImageUtil.compressImageByName(loadedImage, name);
							if(!StringUtil.isEmpty(path)){
								article.share_path=path;
							}else{
								article.share_path=ImageUtil.compressImage(loadedImage, name);
							}
					   }
					
					
					/*BitmapFactory.Options options=new BitmapFactory.Options();
					options.inSampleSize=16;
					icon.setImageBitmap(BitmapFactory.decodeFile(article.share_path, options));*/
					}
					}else{
						/*if(loadedImage!=null){
							Matrix matrix=new Matrix();
							matrix.setScale((Constants.screen_width-context.getResources().getDimension(R.dimen.one_dip)*30)*1f/loadedImage.getWidth(), (Constants.screen_width-context.getResources().getDimension(R.dimen.one_dip)*30)*1f/loadedImage.getWidth());
							icon.setImageBitmap(Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(),matrix,true));
						}*/
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		
		
	}
	private void displayImage(ImageView giftIV,Article gift) {
		// TODO Auto-generated method stub
		ImageFetcher mImageFetcher = new ImageFetcher(context, Constants.screen_width);
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=1;
//		LogUtil.i("me", "照片详情页面Topic图片缩放比例"+StringUtil.topicImageGetScaleByDPI(context));
	/*	if(StringUtil.topicImageGetScaleByDPI(context)>=2){
			options.inPreferredConfig=Bitmap.Config.ARGB_4444;
		}else{
			options.inPreferredConfig=Bitmap.Config.ARGB_8888;
		}*/
		
		options.inPurgeable=true;
		options.inInputShareable=true;
		mImageFetcher.itemUrl="noAdd";
		mImageFetcher.setWidth((Constants.screen_width/*-context.getResources().getDimensionPixelSize(R.dimen.one_dip)*30*/));
		mImageFetcher.setImageCache(new ImageCache(context, new ImageCacheParams(gift.image)));
		mImageFetcher.loadImage(gift.image, giftIV,options);
	}
	private void displayImage2(ImageView giftIV,Article gift) {
		// TODO Auto-generated method stub
		ImageFetcher mImageFetcher = new ImageFetcher(context, Constants.screen_width);
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=1;
//		LogUtil.i("me", "照片详情页面Topic图片缩放比例"+StringUtil.topicImageGetScaleByDPI(context));
	/*	if(StringUtil.topicImageGetScaleByDPI(context)>=2){
			options.inPreferredConfig=Bitmap.Config.ARGB_4444;
		}else{
			options.inPreferredConfig=Bitmap.Config.ARGB_8888;
		}*/
		
		options.inPurgeable=true;
		options.inInputShareable=true;
		mImageFetcher.itemUrl="noAdd";
		mImageFetcher.setWidth((context.getResources().getDimensionPixelSize(R.dimen.one_dip)*72));
		mImageFetcher.setImageCache(new ImageCache(context, new ImageCacheParams(gift.icon)));
		
		mImageFetcher.loadImage(gift.icon, giftIV,options);
	}
	ViewPager viewpager;
	private void initViewPager(ViewPager viewpager,ArrayList<Article> articles){
		MyPagerAdapter adapter=new MyPagerAdapter(articles);
		this.viewpager=viewpager;
		viewpager.setAdapter(adapter);
		if(articles.size()>0){
			circleHandler.sendEmptyMessageDelayed(1, 1000);
		}
	}
	int currentPosition=0;
	Handler circleHandler=new Handler(){
    	public void handleMessage(Message msg) {
    		if(msg.what==1){
    			currentPosition++;
    			viewpager.setCurrentItem(currentPosition);
    			circleHandler.sendEmptyMessageDelayed(1, 1000);
    		}
    	};
    };
	class Holder{
		TextView titleTv,contextTv,title_1;
		ImageView topicIv,contentIv;
		StaticViewPager viewpager;
		RelativeLayout contentLayout;
	}
	class MyPagerAdapter extends PagerAdapter{
				ArrayList<Article> articles;
				private  ArrayList<ImageView> imageViews=new ArrayList<ImageView>();
				
				public MyPagerAdapter(ArrayList<Article> articles){
					this.articles=articles;
				}
	        	@Override
	        	public int getCount() {
	        		// TODO Auto-generated method stub
	        		if(articles.size()==0)return 0;
	        		return 1999999999;
	        	}

	        	@Override
	        	public boolean isViewFromObject(View arg0, Object arg1) {
	        		// TODO Auto-generated method stub
	        		return arg0==arg1;
	        	}
	        	@Override
	        	public void destroyItem(ViewGroup container, int position, Object object) {
	        		// TODO Auto-generated method stub
	        		if(imageViews.size()>0){
	        			container.removeView(imageViews.get(position%imageViews.size()));
	        		}
	        		
	        	}
	        	@Override
	        	public int getItemPosition(Object object) {
	        		// TODO Auto-generated method stub
	        		return super.getItemPosition(object);
//	        		return POSITION_NONE;
	        	}
	        	@Override
	        	public CharSequence getPageTitle(int position) {
	        		// TODO Auto-generated method stub
	        		return super.getPageTitle(position);
	        	}
	        	@Override
	        	public Object instantiateItem(ViewGroup container,final int position) {
	        		// TODO Auto-generated method stubI
	        		
	        		/*ImageView iv=null;
	        		if(imageViews.size()==0){
	        			iv=(ImageView)LayoutInflater.from(activity).inflate(R.layout.item_banner_iv, null);
	        			imageViews.add(0, iv);
	        		}else{
	        			iv=imageViews.get(position%imageViews.size());
	        			if(iv==null){
	        				iv=(ImageView)LayoutInflater.from(activity).inflate(R.layout.item_banner_iv, null);
	        			}
	        			imageViews.add(position%imageViews.size(), iv);
	        		}*/
	        		
	        		ImageView iv=(ImageView)LayoutInflater.from(context).inflate(R.layout.item_banner_iv, null);
	        		iv.setImageResource(R.drawable.empty_photo);
	        		iv.setScaleType(ScaleType.CENTER_CROP);
//	        		loadImage(iv, articles.get(position%articles.size()).image);
	        		imageLoader=ImageLoader.getInstance();
	        		imageLoader.displayImage(articles.get(position%articles.size()).image, iv, displayImageOptions,new ImageLoadingListener() {
						
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
							
							LinearLayout.LayoutParams param=(LinearLayout.LayoutParams)viewpager.getLayoutParams();
							if(param!=null){
//								param.width=Constants.screen_width-context.getResources().getDimensionPixelOffset(R.dimen.one_dip)*30;
//								param.height=(int)(param.width*1f/loadedImage.getWidth()*1f*loadedImage.getHeight());
								LogUtil.i("mi", "下载文章banner图片成功param.height="+param.height+";param.width="+param.width);
//								viewpager.setLayoutParams(param);
								LogUtil.i("mi", "下载文章banner图片成功viewpager.getWidth()="+viewpager.getWidth()+";viewpager.getHeight()="+viewpager.getHeight());
							}
							if(loadedImage!=null){
							
							String name="article";
							String path=ImageUtil.compressImage(loadedImage, name);
							if(!StringUtil.isEmpty(path)){
								articles.get(position%articles.size()).share_path=path;
							}else{
								articles.get(position%articles.size()).share_path=ImageUtil.compressImage(loadedImage, name);
							}
							}
							
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							
						}
					});
	        		if(imageViews.size()==0){
	        			imageViews.add(iv);
	        		}else{
	        			imageViews.add(position%imageViews.size(),iv);
	        		}
	        		iv.setTag(articles.get(position%articles.size()));
	        		iv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							/*Intent intent=new Intent(context,ArticleActivity.class);
							intent.putExtra("article",articles.get(position%articles.size()));
							context.startActivity(intent);*/
							
							
							Banner banner=new Banner();
							banner.icon_path=articleList.get(position%articles.size()).share_path;
							banner.description=articleList.get(position%articles.size()).description;
							banner.title=articleList.get(position%articles.size()).title;
							banner.url=articleList.get(position%articles.size()).url;
							Intent intent=new Intent(context,ActivityWebActivity.class);
							intent.putExtra("banner", banner);
							context.startActivity(intent);
						}
					});
	        		container.addView(iv);
	        		return iv;
	        	}
			};

}
