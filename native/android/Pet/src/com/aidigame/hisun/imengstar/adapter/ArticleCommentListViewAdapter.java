package com.aidigame.hisun.imengstar.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import u.aly.co;

import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.bean.Article.ArticleComment;
import com.aidigame.hisun.imengstar.bean.PetPicture.Comments;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.json.UserImagesJson;
import com.aidigame.hisun.imengstar.huanxin.NewSmileUtils;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract.Contacts.Data;
import android.text.Html;
import android.text.Spannable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
/**
 * 评论列表
 * @author admin
 *
 */
public class ArticleCommentListViewAdapter extends BaseAdapter{
	Context context;
	ArrayList<ArticleComment> list;
	DisplayImageOptions displayImageOptions;
	ClickUserName clickUserName;
	public ArticleCommentListViewAdapter(Context context,ArrayList<ArticleComment> listComment){
		this.context=context;
		this.list=listComment;
BitmapFactory.Options options=new BitmapFactory.Options();
		
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
	            .showImageOnFail(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//		        .decodingOptions(options)
                .build();
	}
	public void update(ArrayList<ArticleComment> listComment){
		this.list=listComment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_article_comment, null);
			holder.nameTextView=(TextView)convertView.findViewById(R.id.textView1);
			holder.body=(TextView)convertView.findViewById(R.id.textView2);
			holder.time=(TextView)convertView.findViewById(R.id.textView3);
			holder.layout=(RelativeLayout)convertView.findViewById(R.id.layout);
			holder.warning_iv=(ImageView)convertView.findViewById(R.id.warning_iv);
			holder.byLayout=(LinearLayout)convertView.findViewById(R.id.bylayout);
			holder.byTv=(TextView)convertView.findViewById(R.id.bytv);
			holder.huifuTv=(TextView)convertView.findViewById(R.id.huifu_tv);
			holder.usericon=(RoundImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final ArticleComment comments=list.get(position);
		ImageLoader imageLoader=ImageLoader.getInstance();
		int w=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		imageLoader.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+comments.usr_tx+"@"+w+"w_"+w+"h_0l.jpg", holder.usericon, displayImageOptions);
		if(!StringUtil.isEmpty(Constants.CON_VERSION)&&"1.0".equals(Constants.CON_VERSION)){
			holder.warning_iv.setVisibility(View.VISIBLE);
		}else{
			holder.warning_iv.setVisibility(View.GONE);
		}
		holder.body.setVisibility(View.VISIBLE);
		if(comments.isReply){
			
			/*String[] str=comments.reply_name.split("@");
			if(comments.reply_name.contains("@")){
				str=comments.reply_name.split("@");
			}else if(comments.reply_name.contains("&")){
				str=comments.reply_name.split("&");
			}else if(!StringUtil.isEmpty(comments.reply_name)){
				str=new String[2];
				str[0]=comments.name;
				str[1]=comments.reply_name;
			}
			if(str.length>1){
				holder.byLayout.setVisibility(View.VISIBLE);
				holder.huifuTv.setVisibility(View.GONE);
				holder.nameTextView.setText(comments.name);
				
				holder.body.setVisibility(View.GONE);
				String html="<html><body>"
						+ "<font color=\"#3d3d3d\">"
                        +"回复  "
						+ "</font>"
						
						+str[1]
								+ "<font color=\"#3d3d3d\">"
                        +":"+(comments.body==null?"":comments.body)
                        + "</font>"
						+ "</body></html>";
				holder.byTv.setText(Html.fromHtml(html));
			}else{
				holder.byLayout.setVisibility(View.GONE);
				holder.huifuTv.setVisibility(View.GONE);
				holder.nameTextView.setText(comments.name);
			}*/
			
		}else{
			holder.byLayout.setVisibility(View.GONE);
			holder.huifuTv.setVisibility(View.GONE);
			holder.nameTextView.setText(comments.user_name);
		}
		Spannable span = NewSmileUtils.getSmiledText(context, comments.comment);
		// 设置内容
		holder.body.setText(span, BufferType.SPANNABLE);
//		holder.body.setText(""+(comments.comment==null?"":comments.comment));
		holder.time.setText(""+StringUtil.judgeTime(comments.create_time));
		holder.warning_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickUserName!=null){
					clickUserName.reportComment();
				}
			}
		});
		final GestureDetector gesture=new GestureDetector(new MyGestureDector(comments));
		holder.layout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gesture.onTouchEvent(event);
			}
		});
		return convertView;
	}
	class Holder{
		TextView nameTextView;
		TextView body;
		TextView time;
		RelativeLayout layout;
		ImageView warning_iv;
		LinearLayout byLayout;
		TextView byTv,huifuTv;
		RoundImageView usericon;
	}
	class MyGestureDector implements OnGestureListener{
		ArticleComment comments;
		public MyGestureDector(ArticleComment comments){
			this.comments=comments;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			if(clickUserName!=null){
				clickUserName.clickUserName(comments);
			}
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			if(clickUserName!=null){
				clickUserName.onScroll(e1, e2, distanceX, distanceY);;
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

	public void setClickUserName(ClickUserName clickUserName){
		this.clickUserName=clickUserName;
	}
	public static interface ClickUserName{
		void clickUserName(ArticleComment cmt);
		void reportComment();
		void onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3);
	}
	

}
