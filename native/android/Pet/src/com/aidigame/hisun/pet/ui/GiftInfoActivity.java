package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Gift;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
/**
 * 礼物详情界面
 * @author admin
 *
 */
public class GiftInfoActivity extends Activity implements OnClickListener{
	Gift gift;
	ImageView giftIV,closeIv;
	TextView giftDesTV,priceTV,ades2TV,scopeTV,standardTV,gperiodTV,postModeTV,
	        buyTV;
	int num=1;
	LinearLayout blurLayout;
	Handler handler;
	ImageFetcher mImageFetcher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_gift_info);
		gift=(Gift)getIntent().getSerializableExtra("gift");
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
		mImageFetcher = new ImageFetcher(this, Constants.screen_width);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		giftIV=(ImageView)findViewById(R.id.imageView1);
		giftDesTV=(TextView)findViewById(R.id.textView1);
		priceTV=(TextView)findViewById(R.id.textView2);
		ades2TV=(TextView)findViewById(R.id.textView4);
		scopeTV=(TextView)findViewById(R.id.textView5);
		standardTV=(TextView)findViewById(R.id.textView6);
		gperiodTV=(TextView)findViewById(R.id.textView7);
		postModeTV=(TextView)findViewById(R.id.textView8);
		buyTV=(TextView)findViewById(R.id.buy_tv);
		closeIv=(ImageView)findViewById(R.id.close_iv);
		buyTV.setOnClickListener(this);
		closeIv.setOnClickListener(this);
		setBlurImageBackground();
		
		giftDesTV.setText(gift.name);
		priceTV.setText(""+gift.price);
		String[] strs=gift.totalDes.split("&");
		if(strs!=null&&strs.length>0){
			if(strs.length>1){
				ades2TV.setVisibility(View.VISIBLE);
				ades2TV.setText(""+strs[0]);
			}
            if(strs.length>2){
            	scopeTV.setVisibility(View.VISIBLE);
            	scopeTV.setText(""+strs[1]);
			}
            if(strs.length>3){
            	standardTV.setVisibility(View.VISIBLE);
            	standardTV.setText(""+strs[2]);
			}
            if(strs.length>4){
            	gperiodTV.setVisibility(View.VISIBLE);
            	gperiodTV.setText(""+strs[3]);
			}
            if(strs.length>5){
            	postModeTV.setVisibility(View.VISIBLE);
            	postModeTV.setText(""+strs[4]);
			}

			
		}
		displayImage();
		
	}
	private void displayImage() {
		// TODO Auto-generated method stub
		final BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=1;
		LogUtil.i("me", "照片详情页面Topic图片缩放比例"+StringUtil.topicImageGetScaleByDPI(this));
	/*	if(StringUtil.topicImageGetScaleByDPI(context)>=2){
			options.inPreferredConfig=Bitmap.Config.ARGB_4444;
		}else{
			options.inPreferredConfig=Bitmap.Config.ARGB_8888;
		}*/
		
		options.inPurgeable=true;
		options.inInputShareable=true;
		mImageFetcher.itemUrl="item/";
		mImageFetcher.setWidth((Constants.screen_width-this.getResources().getDimensionPixelSize(R.dimen.one_dip)*70));
		mImageFetcher.setImageCache(new ImageCache(this, new ImageCacheParams(gift.bigImage)));
		
		mImageFetcher.loadImage(gift.bigImage, giftIV,options);
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		blurLayout=(LinearLayout)findViewById(R.id.layout);
       
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.buy_tv:
			Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
				
				@Override
				public void onClose() {
					// TODO Auto-generated method stub
//					finish();
				}
				
				@Override
				public void onButtonTwo() {
					// TODO Auto-generated method stub
					exchangeFood();
				}
				
				@Override
				public void onButtonOne() {
					// TODO Auto-generated method stub
//					finish();
				}
			};
			Intent intent=new Intent(this,Dialog4Activity.class);
			intent.putExtra("mode", 4);
			intent.putExtra("name", gift.name);
			intent.putExtra("num", gift.price);
			this.startActivity(intent);
//			Toast.makeText(this,"购买成功", Toast.LIGENGTH_SHORT).show();
			break;
		case R.id.close_iv:
			finish();
			break;
		
		   
		}
	}
	   @Override
	   protected void onPause() {
	   	// TODO Auto-generated method stub
	   	super.onPause();
	   	StringUtil.umengOnPause(this);
	   }
	      @Override
	   protected void onResume() {
	   	// TODO Auto-generated method stub
	   	super.onResume();
	   	StringUtil.umengOnResume(this);
	   }
	      public void exchangeFood(){
	    	  new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final int food=HttpUtil.exchangeFood(handler, gift, GiftInfoActivity.this);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(food>=0&&food!=gift.animal.foodNum){
								modifyAddress();
								gift.animal.foodNum=food;
								if(ExchangeActivity.exchangeActivity!=null){
									ExchangeActivity.exchangeActivity.foodTv.setText(""+food);
									ExchangeActivity.exchangeActivity.animal.foodNum=food;
								}
							}else{
								Toast.makeText(GiftInfoActivity.this, "兑换失败", Toast.LENGTH_LONG).show();
								finish();
							}
							
						}
					});
				}
			}).start();
	      }
	      /**
	       * 修改地址弹窗
	       */
	      public void modifyAddress(){
	    	  Dialog3Activity.listener=new Dialog3Activity.Dialog3ActivityListener() {
				
				@Override
				public void onClose() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onButtonTwo() {
					// TODO Auto-generated method stub
					finish();
				}
				
				@Override
				public void onButtonOne() {
					// TODO Auto-generated method stub
					Intent intent=new Intent(GiftInfoActivity.this,ReceiverAddressActivity.class);
					GiftInfoActivity.this.startActivity(intent);
					finish();
				}
			};
	    	  Intent intent=new Intent(this,Dialog3Activity.class);
	    	  intent.putExtra("mode", 3);
	    	  this.startActivity(intent);
	      }

}
