package com.aidigame.hisun.pet.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.bean.PetPicture.Comments;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.NewShowTopicActivity;
import com.aidigame.hisun.pet.ui.UserCardActivity;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.view.RoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class TopicUsersListAdapter extends BaseAdapter {
	ArrayList<MyUser> users;
	Context context;
	DisplayImageOptions displayImageOptions;
	Animal animal;
	GestureListener listener;
	public TopicUsersListAdapter(Context context,ArrayList<MyUser> users,Animal animal){
		this.context=context;
		this.users=users;
		this.animal=animal;
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
		if(this.users==null){
			this.users=new ArrayList<MyUser>();
		}
	}
	public void update(ArrayList<MyUser> users){
		this.users=users;
		if(this.users==null){
			this.users=new ArrayList<MyUser>();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return users.get(position);
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
			convertView=LayoutInflater.from(context).inflate(R.layout.itemtopic_users_list, null);
			holder=new Holder();
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon);
			holder.nameTv=(TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		MyUser user=users.get(position);
		ImageLoader imageLoader=ImageLoader.getInstance();
		int w=context.getResources().getDimensionPixelSize(R.dimen.one_dip)*54;
		
		imageLoader.displayImage(Constants.USER_THUBMAIL_DOWNLOAD_TX+user.u_iconUrl+"@"+w+"w_"+w+"h_0l.jpg", holder.icon, displayImageOptions);
		if(!StringUtil.isEmpty(user.u_nick)){
			holder.nameTv.setText(""+user.u_nick);
		}
		/*convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(UserDossierActivity.userDossierActivity!=null){
					if(UserDossierActivity.userDossierActivity.loadedImage1!=null&&!UserDossierActivity.userDossierActivity.loadedImage1.isRecycled()){
						UserDossierActivity.userDossierActivity.loadedImage1.recycle();
						UserDossierActivity.userDossierActivity.loadedImage1=null;
					}
					if(UserDossierActivity.userDossierActivity.loadedImage2!=null&&!UserDossierActivity.userDossierActivity.loadedImage2.isRecycled()){
						UserDossierActivity.userDossierActivity.loadedImage2.recycle();
						UserDossierActivity.userDossierActivity.loadedImage2=null;
					}
					UserDossierActivity.userDossierActivity.finish();
				}
				Intent intent=new Intent(context,UserDossierActivity.class);
				User user=users.get(position);
				intent.putExtra("user", user);
				context.startActivity(intent);
			}
		});*/
//		User u=users.get(position);
//		u.currentAnimal=animal;
//		u.userId=animal.master_id;
		final GestureDetector gesture=new GestureDetector(new MyListGestureDector(users.get(position)));
		convertView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				return gesture.onTouchEvent(event) ;
			}
		});
		return convertView;
	}
	public void setGestureListener(GestureListener listener){
		this.listener=listener;
	}
	class Holder{
		RoundImageView icon;
		TextView nameTv;
	}
	public class MyListGestureDector implements OnGestureListener{
    	int mode;//1，分享，送礼，点赞列表；2，评论列表
    	int touchSlop;
    	MyUser user;
    	Comments comments;
    	public MyListGestureDector(MyUser user){
    		
    		this.user=user;
    		this.comments=comments;
    		touchSlop=ViewConfiguration.getTouchSlop();
    	}
		
		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
				/*if(UserDossierActivity.userDossierActivity!=null){
					if(UserDossierActivity.userDossierActivity.loadedImage1!=null&&!UserDossierActivity.userDossierActivity.loadedImage1.isRecycled()){
						UserDossierActivity.userDossierActivity.loadedImage1.recycle();
						UserDossierActivity.userDossierActivity.loadedImage1=null;
					}
					if(UserDossierActivity.userDossierActivity.loadedImage2!=null&&!UserDossierActivity.userDossierActivity.loadedImage2.isRecycled()){
						UserDossierActivity.userDossierActivity.loadedImage2.recycle();
						UserDossierActivity.userDossierActivity.loadedImage2=null;
					}
					UserDossierActivity.userDossierActivity.finish();
				}
				Intent intent=new Intent(context,UserDossierActivity.class);
				intent.putExtra("user", user);
				context.startActivity(intent);*/
			Intent intent=new Intent(context,UserCardActivity.class);
			intent.putExtra("user", user);
			intent.putExtra("from", 1);
			context.startActivity(intent);
			return true;
		}
		
		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			if(listener!=null)listener.onScroll(arg0, arg1, arg2, arg3);
			return false;
			
		}
		
		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return true;
		}
	}
	public interface GestureListener{
		public void onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3);
	}

}
