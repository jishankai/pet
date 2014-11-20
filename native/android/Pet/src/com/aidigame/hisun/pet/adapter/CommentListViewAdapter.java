package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import u.aly.co;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.util.StringUtil;

import android.content.Context;
import android.provider.ContactsContract.Contacts.Data;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 评论列表
 * @author admin
 *
 */
public class CommentListViewAdapter extends BaseAdapter{
	Context context;
	ArrayList<PetPicture.Comments> listComment;
	ClickUserName clickUserName;
	public CommentListViewAdapter(Context context,ArrayList<PetPicture.Comments> listComment){
		this.context=context;
		this.listComment=listComment;
	}
	public void update(ArrayList<PetPicture.Comments> listComment){
		this.listComment=listComment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listComment.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listComment.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_comment_listview, null);
			holder.nameTextView=(TextView)convertView.findViewById(R.id.textView1);
			holder.body=(TextView)convertView.findViewById(R.id.textView2);
			holder.time=(TextView)convertView.findViewById(R.id.textView3);
			holder.layout=(RelativeLayout)convertView.findViewById(R.id.layout);
			holder.warning_iv=(ImageView)convertView.findViewById(R.id.warning_iv);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		final PetPicture.Comments comments=listComment.get(position);
		
		if(!StringUtil.isEmpty(Constants.CON_VERSION)&&"1.0".equals(Constants.CON_VERSION)){
			holder.warning_iv.setVisibility(View.VISIBLE);
		}else{
			holder.warning_iv.setVisibility(View.INVISIBLE);
		}
		
		if(comments.isReply){
			
			String[] str=comments.reply_name.split("@");
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
				String html="<html><body>"
						+ "<font color=\"#fb6137\">"
						+""+(comments.name==null?"":comments.name)
						+ "</font>"
						+"回复"
						+ "<font color=\"#fb6137\">"
						+""+(str[1]==null?"":str[1])
						+ "</font>"
						+":"
						+ "</body></html>";
				holder.nameTextView.setText(Html.fromHtml(html));
			}else{
				String html="<html><body>"
						+ "<font color=\"#fb6137\">"
						+""+(comments.name==null?"":comments.name)
						+ "</font>"
						+":"
						+ "</body></html>";
				holder.nameTextView.setText(Html.fromHtml(html));
			}
			
		}else{
			String html="<html><body>"
					+ "<font color=\"#fb6137\">"
					+""+(comments.name==null?"":comments.name)
					+ "</font>"
					+":"
					+ "</body></html>";
			holder.nameTextView.setText(Html.fromHtml(html));
		}
		
		holder.body.setText(""+(comments.body==null?"":comments.body));
		holder.time.setText(""+judgeTime(comments.create_time));
		holder.warning_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickUserName!=null){
					clickUserName.reportComment();
				}
			}
		});
		/*holder.nameTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickUserName!=null){
					clickUserName.clickUserName(comments);
				}
			}
		});*/
		holder.layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(clickUserName!=null){
					clickUserName.clickUserName(comments);
				}
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
	}
	public String judgeTime(long time2){
		long time1=System.currentTimeMillis()/1000;
		long time=time1-time2;
		/*int[] oldTimes=getTimeArray(time2*1000);
		int[] nowTimes=getTimeArray(time1);
		for(int i=0;i<oldTimes.length;i++){
			int t=0;
			switch (i) {
			case 0:
				t=nowTimes[0]-oldTimes[0];
				if(t<=0){
					continue;
				}else{
					return t+"年前";
				}	
			case 1:
				t=nowTimes[1]-oldTimes[1];
				if(t<=0){
					continue;
				}else{
					return t+"个月前";
				}	
			case 2:
				t=nowTimes[2]-oldTimes[2];
				if(t<=0){
					continue;
				}else{
					return t+"天前";
				}	
			case 3:
				t=nowTimes[3]-oldTimes[3];
				if(t<=0){
					continue;
				}else{
					return t+"个小时前";
				}	
			case 4:
				t=nowTimes[4]-oldTimes[4];
				if(t<=0){
					continue;
				}else{
					return t+"分钟前";
				}	
			case 5:
				t=nowTimes[5]-oldTimes[5];
				if(t<0){
					continue;
				}else{
					return t+"秒前";
				}
				
			}
		}*/
         String str="";
         StringBuffer sb=new StringBuffer();
         sb.append("");
         int mode=0;
         if(time<0){
        	 time=-time;
        	 mode=1;
        	 sb.append("未来");
         }
		if(time<60){
			sb.append( str+time+"秒");
		}else if(time/(60)<60){
			sb.append( str+time/(60)+"分");
		}else if(time/(60*60)<24){
			sb.append(  str+time/(60*60)+"个小时");
		}else if(time/(60*60*24)<30){
			sb.append(  str+time/(60*60*24)+"天");
		}else if(time/(60*60*24*30)<12){
			sb.append(  str+time/(60*60*24)+"个月");
		}else if(time/(60*60*24*30*12)<1000){
			sb.append( str+time/(60*60*24*30*12)+"年");
		}
		if(mode==0){
			sb.append("前");
		}else{
			sb.append("后");
		}
		if(time<60){
			return "刚刚";
		}else{
			return sb.toString();
		}
		
	}
	public void setClickUserName(ClickUserName clickUserName){
		this.clickUserName=clickUserName;
	}
	public static interface ClickUserName{
		void clickUserName(PetPicture.Comments cmt);
		void reportComment();
	}
	

}
