package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.MessagJson;
import com.aidigame.hisun.pet.ui.ChatActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.PetKingdomActivity;
import com.aidigame.hisun.pet.ui.UserDossierActivity;
import com.aidigame.hisun.pet.ui.WarningDialogActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.aidigame.hisun.pet.widget.fragment.DialogGoRegister;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
/**
 * 消息界面  消息列表
 * @author admin
 *
 */
public class MessageListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
	Activity context;
	
	ArrayList<TalkMessage> datas;
	LinearLayout progressLayout;
	HandleHttpConnectionException handleHttpConnectionException;
	public MessageListAdapter(Context context,ArrayList<TalkMessage> datas,LinearLayout progressLayout){
		this.context=(Activity)context;
		this.datas=datas;
		this.progressLayout=progressLayout;
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
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
		        .decodingOptions(options)
                .build();
	   touchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
	}
	public void update(ArrayList<TalkMessage> datas){
		this.datas=datas;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private float x,ux;
	LinearLayout rightLayout;
	RelativeLayout leftLayout;
	boolean isDeleting=false;
	int touchSlop;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_message_list, null);
			holder.icon=(RoundImageView)convertView.findViewById(R.id.imageView1);
			holder.mailType=(TextView)convertView.findViewById(R.id.textView1);
			holder.timeTv=(TextView)convertView.findViewById(R.id.textView2);
			holder.textTv=(TextView)convertView.findViewById(R.id.textView3);
			holder.nameTv=(TextView)convertView.findViewById(R.id.textView4);
			holder.deleteTv=(TextView)convertView.findViewById(R.id.deleteTv);
			holder.numTV=(TextView)convertView.findViewById(R.id.imageView2);
			holder.rightLayout=(LinearLayout)convertView.findViewById(R.id.right_layout);
			holder.leftLayout=(RelativeLayout)convertView.findViewById(R.id.left_layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		int h=convertView.getMeasuredHeight();
		holder.deleteTv.setLayoutParams(new LinearLayout.LayoutParams(h, h));
		android.view.ViewGroup.LayoutParams params=holder.rightLayout.getLayoutParams();
		params.height=h;
		params.width=h;
		holder.rightLayout.setLayoutParams(params);
		TalkMessage data=datas.get(position);
			
			if(data.msgList!=null&&data.msgList.size()>0){
				holder.textTv.setText(data.msgList.get(data.msgList.size()-1).content);
				holder.timeTv.setText(judgeTime(data.msgList.get(data.msgList.size()-1).time));
			}
			if(data.usr_id==1){
				holder.icon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.wangwang));
			}else if(data.usr_id==2){
				holder.icon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.miaomiao));
			}else if(data.usr_id==3){
				holder.icon.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.xiaoge));
			}else{
				ImageLoader imageLoader=ImageLoader.getInstance();
				imageLoader.displayImage(Constants.USER_DOWNLOAD_TX+data.usr_tx, holder.icon, displayImageOptions);
			}
			
		
		
//		holder.mailType.setVisibility(View.VISIBLE);
			if(data.old_new_msg_num>0){
				holder.numTV.setVisibility(View.VISIBLE);
				holder.numTV.setText(""+data.old_new_msg_num);
			}else{
				holder.numTV.setVisibility(View.INVISIBLE);
			}
			final TextView tv=holder.numTV;
		
		
		holder.nameTv.setText(""+data.usr_name);
		holder.deleteTv.setOnClickListener(new OnClickListener() {
			Toast toast;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rightLayout != null) {
					if(rightLayout.getVisibility() == View.VISIBLE){
						if(isDeleting){
							if(toast!=null)toast.cancel();
							toast=Toast.makeText(context, "正在删除消息,请稍后", Toast.LENGTH_LONG);
							toast.show();
							return;
						}
						if(position==-1)return;
						
						isDeleting=true;
						    
							
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										final TalkMessage dataSystem=datas.get(position);
//										boolean flag=HttpUtil.deleteMail(dataSystem);
										if(true){
											SharedPreferences sp=context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
											Editor editor=sp.edit();
											editor.putString("talk_"+dataSystem.position, "");
											String talk_num=sp.getString("talk_num", null);
											if(!StringUtil.isEmpty(talk_num)){
												String[] strs=talk_num.split(",");
												talk_num="";
												for(int i=0;i<strs.length;i++){
													if(strs[i].equals(""+dataSystem.position)){
														
													}else{
														talk_num+=strs[i]+",";
													}
												}
												editor.putString("talk_num", talk_num);
											}
											editor.commit();
											
											context.runOnUiThread(new Runnable() {
												public void run() {
													MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
													params.leftMargin=0;
													leftLayout.setLayoutParams(params);
													rightLayout.setVisibility(View.GONE);
													Toast.makeText(context, "消息删除成功", Toast.LENGTH_SHORT).show();
													datas.remove(dataSystem);
													isDeleting=false;
													notifyDataSetChanged();
												}
											});
											
											
										}else{
											isDeleting=false;
										}
									}
								}).start();
					}
				}
			}
		});
		
	/*	
		convertView.setOnTouchListener(new OnTouchListener() {
			float startX;
			float distance;
		    boolean isRecord=false;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final Holder holder = (Holder) v.getTag();
				int maxW=v.getMeasuredHeight();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					isRecord=false;
					distance=0;
					startX=0;
					if(!isRecord){
						isRecord=true;
						startX=event.getX();
						if (rightLayout != null) {
							if(rightLayout.getVisibility() == View.VISIBLE){
								MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
								params.leftMargin=0;
								leftLayout.setLayoutParams(params);
								rightLayout.setVisibility(View.GONE);
								return true;
							}
						}
					}
					
//					break;
					return true;

				case MotionEvent.ACTION_MOVE:
					
					if(!isRecord){
						isRecord=true;
						startX=event.getX();
						if (rightLayout != null) {
							if(rightLayout.getVisibility() == View.VISIBLE){
								MarginLayoutParams params=(MarginLayoutParams)leftLayout.getLayoutParams();
								params.leftMargin=0;
								leftLayout.setLayoutParams(params);
								rightLayout.setVisibility(View.GONE);
							}
						}
					}
					LogUtil.i("scroll","startx="+startX+",getX="+event.getX()+",width="+holder.rightLayout.getWidth()+",mwidth="+holder.rightLayout.getMeasuredWidth() );
                   if(startX-event.getX()>0&&startX-event.getX()>=touchSlop){
                	   holder.rightLayout.setVisibility(View.VISIBLE);
                	   MarginLayoutParams params=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
                	   MarginLayoutParams rightParams=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
                	   maxW=v.getMeasuredHeight();
                	   if(params.leftMargin>-maxW/5){
   						distance=startX-event.getX();
   						params.leftMargin=(int) -distance;
   						holder.leftLayout.setLayoutParams(params);
   						rightParams.rightMargin=(int) -(maxW-distance);
   						holder.rightLayout.setLayoutParams(rightParams);
   						RelativeLayout viewLayout=(RelativeLayout)v;
   					}else{
   					}
                	   return true;
					}
					break;

				case MotionEvent.ACTION_UP:
					 MarginLayoutParams params=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
              	   MarginLayoutParams rightParams=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
					if(distance>maxW/5){
						holder.rightLayout.setVisibility(View.VISIBLE);
						MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
						params3.rightMargin=0;
						holder.rightLayout.setLayoutParams(params3);
						MarginLayoutParams params1=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
						params1.leftMargin=-maxW;
						holder.leftLayout.setLayoutParams(params1);
						
						rightLayout = holder.rightLayout;
						leftLayout=holder.leftLayout;
					}else{
						MarginLayoutParams params2=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
						params2.leftMargin=0;
						holder.leftLayout.setLayoutParams(params2);
						MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
						params3.rightMargin=0;
						holder.rightLayout.setLayoutParams(params3);
						holder.rightLayout.setVisibility(View.GONE);
						
						TalkMessage data=datas.get(position);
						data.new_msg=0;
						data.old_new_msg_num=0;
						holder.numTV.setText("0");
						Intent intent=new Intent(context,ChatActivity.class);
						data.sortMsgList();
						intent.putExtra("msg", data);
						context.startActivity(intent);
						tv.setVisibility(View.INVISIBLE);
						tv.setText(""+0);
						
					}
					isRecord=false;
					break;

				default:
					break;
				}
				if(distance>maxW/5){
					if(holder.rightLayout.getMeasuredWidth()==0){
						holder.deleteTv.setLayoutParams(new LinearLayout.LayoutParams(maxW, maxW));
						android.view.ViewGroup.LayoutParams params=holder.rightLayout.getLayoutParams();
						params.height=maxW;
						params.width=maxW;
						holder.rightLayout.setLayoutParams(params);
					}
					holder.rightLayout.setVisibility(View.VISIBLE);
					MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
					params3.rightMargin=0;
					holder.rightLayout.setLayoutParams(params3);
					MarginLayoutParams params1=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
					params1.leftMargin=-maxW;
					holder.leftLayout.setLayoutParams(params1);
					
					rightLayout = holder.rightLayout;
					leftLayout=holder.leftLayout;
				}else{
					MarginLayoutParams params2=(MarginLayoutParams)holder.leftLayout.getLayoutParams();
					params2.leftMargin=0;
					holder.leftLayout.setLayoutParams(params2);
					MarginLayoutParams params3=(MarginLayoutParams)holder.rightLayout.getLayoutParams();
					params3.rightMargin=0;
					holder.rightLayout.setLayoutParams(params3);
					holder.rightLayout.setVisibility(View.GONE);
				}
				return true;
			}
		});*/
		convertView.setClickable(true);
		convertView.setLongClickable(true);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ChatActivity.class);
				datas.get(position).sortMsgList();
				intent.putExtra("msg", datas.get(position));
				context.startActivity(intent);
				datas.get(position).old_new_msg_num=0;
				notifyDataSetChanged();
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showButtons(position);
				return true;
			}
		});
		return convertView;
	}
	public String judgeTime(long time2){
		long time1=System.currentTimeMillis()/1000;
		long time=time1-time2;

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
		return sb.toString();
	}
	class Holder{
		RoundImageView icon;
		TextView nameTv,timeTv,textTv,deleteTv;
		TextView mailType;
		TextView numTV;
		RelativeLayout leftLayout;
		LinearLayout rightLayout;
		
	}
	boolean isShowingButton=false;
	private void showButtons(final int position) {
		// TODO Auto-generated method stub
		isShowingButton=true;
		long l1=System.currentTimeMillis();
		LogUtil.i("scroll",""+( System.currentTimeMillis()-l1));
		final View view=LayoutInflater.from(context).inflate(R.layout.popup_user_dossier, null);
		Animation animation=AnimationUtils.loadAnimation(context, R.anim.anim_translate_showtopic_addcommentlayout_in);
		view.clearAnimation();
		view.setAnimation(animation);
		animation.start();
		
		progressLayout.removeAllViews();
		progressLayout.addView(view);
		progressLayout.setBackgroundResource(R.color.window_black_bagd);
		progressLayout.setVisibility(View.VISIBLE);
//		context.progresslayout.setClickable(true);
		TextView camera=(TextView)view.findViewById(R.id.textView2);
		TextView album=(TextView)view.findViewById(R.id.textView1);
		TextView cancel=(TextView)view.findViewById(R.id.textView3);
		view.findViewById(R.id.line1).setVisibility(View.GONE);
//		album.setVisibility(View.GONE);
		camera.setText("删除");
		cancel.setText("拉黑");
		album.setText("发消息");
		album.setVisibility(View.GONE);
		album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,ChatActivity.class);
				datas.get(position).sortMsgList();
				intent.putExtra("msg", datas.get(position));
				context.startActivity(intent);
				datas.get(position).old_new_msg_num=0;
				notifyDataSetChanged();
				progressLayout.setVisibility(View.INVISIBLE);
				progressLayout.setClickable(false);
				isShowingButton=false;
			}
		});
		camera.setOnClickListener(new OnClickListener() {
			Toast toast;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isDeleting){
					if(toast!=null)toast.cancel();
					toast=Toast.makeText(context, "正在删除消息,请稍后", Toast.LENGTH_LONG);
					toast.show();
					return;
				}
				if(position==-1)return;
				
				isDeleting=true;
				    
					
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								final TalkMessage dataSystem=datas.get(position);
//								boolean flag=HttpUtil.deleteMail(dataSystem);
								if(true){
									SharedPreferences sp=context.getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
									Editor editor=sp.edit();
									editor.putString("talk_"+dataSystem.position, "");
									String talk_num=sp.getString("talk_num", null);
									if(!StringUtil.isEmpty(talk_num)){
										String[] strs=talk_num.split(",");
										talk_num="";
										for(int i=0;i<strs.length;i++){
											if(strs[i].equals(""+dataSystem.position)){
												
											}else{
												talk_num+=strs[i]+",";
											}
										}
										editor.putString("talk_num", talk_num);
									}
									editor.commit();
									
									context.runOnUiThread(new Runnable() {
										public void run() {
											
											Toast.makeText(context, "消息删除成功", Toast.LENGTH_SHORT).show();
											datas.remove(dataSystem);
											isDeleting=false;
											NewHomeActivity.homeActivity.messageFragment.notifyDataChanged(datas);
											progressLayout.setVisibility(View.INVISIBLE);
											progressLayout.setClickable(false);
											isShowingButton=false;
										}
									});
									
									
								}else{
									isDeleting=false;
								}
							}
						}).start();
				
				
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					Intent intent=new Intent(context,WarningDialogActivity.class);
					intent.putExtra("mode",4 );
					intent.putExtra("talk_id", datas.get(position).position);
					context.startActivity(intent);
					progressLayout.setVisibility(View.INVISIBLE);
					progressLayout.setClickable(false);
				isShowingButton=false;
			}
		});
		progressLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Animation animation=AnimationUtils.loadAnimation(context, R.anim.anim_translate_showtopic_addcommentlayout_out);
					view.clearAnimation();
					view.setAnimation(animation);
					animation.start();
					handleHttpConnectionException.getHandler(context).postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressLayout.setVisibility(View.INVISIBLE);
							progressLayout.setClickable(false);
							isShowingButton=false;
						}
					}, 300);
					break;
				}
				return true;
			}
		});
	}


}
