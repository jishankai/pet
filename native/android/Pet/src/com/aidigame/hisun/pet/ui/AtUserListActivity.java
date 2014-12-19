package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.AtUserListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.TalkMessage;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView;
import com.aidigame.hisun.pet.view.PullToRefreshAndMoreView.PullToRefreshAndMoreListener;

/**
 * @用户列表和发布到列表
 * @author admin
 *
 */
public class AtUserListActivity extends Activity implements PullToRefreshAndMoreListener{
	FrameLayout frameLayout;
	View viewTopWhite;
	
	
	ImageView back;
	TextView sureTV,searchTV,titleTv;
	EditText inputET;
	PullToRefreshAndMoreView pullToRefreshAndMoreView;
	ListView listView;
	ArrayList<User> topicList;
	ArrayList<Animal> animals;
	AtUserListAdapter adapter;
	String userIdString;
	int mode;//1,@小伙伴；2，发布到某个宠物
	RelativeLayout searchLayout;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		UiUtil.setWidthAndHeight(this);
		setContentView(R.layout.activity_atuser_list);
		mode=getIntent().getIntExtra("mode", 1);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		back=(ImageView)findViewById(R.id.back);
		sureTV=(TextView)findViewById(R.id.sure_tv);
		inputET=(EditText)findViewById(R.id.input_topic_et);
		searchTV=(TextView)findViewById(R.id.textView2);
		titleTv=(TextView)findViewById(R.id.textView1);
		searchLayout=(RelativeLayout)findViewById(R.id.relativeLayout1);
		pullToRefreshAndMoreView=(PullToRefreshAndMoreView)findViewById(R.id.activity_listview);
		pullToRefreshAndMoreView.setListener(this);
		listView=pullToRefreshAndMoreView.getListView();
		
		setBlurImageBackground();
		if(mode==2){
			chosePetInfo();
		}else{
			atUserInfo();
		}
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AtUserListActivity.this.finish();
			}
		});
		
	}
	/**
	 * @用户界面的列表
	 */
	public void atUserInfo(){
		topicList=new ArrayList<User>();
		
		loadData();
		adapter=new AtUserListAdapter(this, topicList,null);
		listView.setAdapter(adapter);
		
		sureTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String topicStr="";
				String relatesId="";
				int count=0;
				for(int i=0;i<topicList.size();i++){
					if(topicList.get(i).isSelected){
						if(count==0){
							topicStr+="@"+topicList.get(i).u_nick;
						}
						if(count==1){
							topicStr+=","+topicList.get(i).u_nick;
						}
						if(i==0){
							relatesId+=""+topicList.get(i).userId;
						}else{
							relatesId+=","+topicList.get(i).userId;
						}
						
						count++;
					}
				}
				if(count==0){
					Toast.makeText(AtUserListActivity.this, "没有选中需要的@用户",Toast.LENGTH_SHORT).show();
					return;
				}
				if(count==1){
					
					SubmitPictureActivity.submitPictureActivity.setAtUser(topicStr,relatesId);
					AtUserListActivity.this.finish();
					return;
				}
				if(count==2){
					SubmitPictureActivity.submitPictureActivity.setAtUser(topicStr+"两个用户",relatesId);
					AtUserListActivity.this.finish();
					return;
				}
				if(count>2){
					SubmitPictureActivity.submitPictureActivity.setAtUser(topicStr+"等"+count+"个用户",relatesId);
					AtUserListActivity.this.finish();
					return;
				}
				if(StringUtil.isEmpty(topicStr)){
					Toast.makeText(AtUserListActivity.this, "话题名称不能为空", Toast.LENGTH_SHORT).show();
				}else{
					SubmitPictureActivity.submitPictureActivity.setAtUser(topicStr,relatesId);
					AtUserListActivity.this.finish();
				}
				
				
			}
		});
		inputET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int originL=s.length();
				deleteErrorChar(s);
				int currentL=s.length();
				if(originL>currentL){
					Toast.makeText(AtUserListActivity.this, "请不要输入\"@\"", Toast.LENGTH_SHORT).show();
				}
				String string=s.toString();
				if(StringUtil.isEmpty(string))return;
				ArrayList<User> temp=new ArrayList<User>();
				for(int i=0;i<topicList.size();i++){
					if (topicList.get(i).u_nick.contains(string)) {
						if(!temp.contains(topicList.get(i)))
						temp.add(topicList.get(i));
					}
					for(int j=0;j<s.length();j++){
						if((topicList.get(i).u_nick.contains(""+s.charAt(j)))){
							if(!temp.contains(topicList.get(i))){
								temp.add(topicList.get(i));
								break;
							}
								
						}
					}
				}
				for(int i=0;i<topicList.size();i++){
					if(!temp.contains(topicList.get(i))){
						temp.add(topicList.get(i));
					}
				}
				topicList=temp;
				adapter.updateList(temp);
				adapter.notifyDataSetChanged();
				
			}
			public void deleteErrorChar(Editable s){
				for(int i=0;i<s.length();i++){
                    if('@'==(s.charAt(i))){
						s.delete(i, i+1);
						deleteErrorChar(s);
						return;
					}
				}
			}
		});
	}
	
	
	public void chosePetInfo(){
		titleTv.setText("发布到");
		searchLayout.setVisibility(View.GONE);
		animals=new ArrayList<Animal>();
		for(int i=0;i<Constants.user.aniList.size();i++){
			if(Constants.user.userId==Constants.user.aniList.get(i).master_id){
				animals.add(Constants.user.aniList.get(i));
			}
		}
		adapter=new AtUserListAdapter(this, null,animals);
		listView.setAdapter(adapter);
		sureTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animal animal=null;
				for(int i=0;i<Constants.user.aniList.size();i++){
					if(Constants.user.aniList.get(i).isSelected){
						animal=Constants.user.aniList.get(i);
						break;
					}
				}
				if(animal!=null){
					if(SubmitPictureActivity.submitPictureActivity!=null){
						SubmitPictureActivity.submitPictureActivity.setChosePet(animal);
						AtUserListActivity.this.finish();
					}
				}else{
					Toast.makeText(AtUserListActivity.this, "请选择一只宠物", Toast.LENGTH_LONG).show();
				}
				
				
				
			}
		});
	}
	
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
       
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
	}

	private void loadData() {
		// TODO Auto-generated method stub
//		userIdString="75,68,46,33,55,12,45,44,67,66,65,62,59,58,53,52,51,85,86";
		userIdString="";
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SharedPreferences sp=getSharedPreferences(Constants.SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
                ArrayList<TalkMessage> talks=StringUtil.getTalkHistory(AtUserListActivity.this);
                boolean isSuccess=false;
                if(talks!=null&&talks.size()>0){
            	   for(int i=0;i<talks.size();i++){
            		   if(i==0){
            			   userIdString+=talks.get(i).usr_id;
            		   }else{
            			   userIdString+=","+talks.get(i).usr_id;
            		   }
            		   
            	   }
            	 final ArrayList<User> temp =HttpUtil.getOthersList(userIdString, handler,AtUserListActivity.this,0);
            	  if(temp!=null&&temp.size()>0){
            		  isSuccess=true;
            		  runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							topicList=temp;
							adapter.updateList(topicList);
							adapter.notifyDataSetChanged();
						}
					});
            	  }else{
            		  //最近没有联系人
            	  }
               }else{
            	   //最近没有与人联系
               }
                if(!isSuccess){
                	runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(AtUserListActivity.this, "最近没有跟小伙伴们交流沟通，赶快去发条消息试试吧", Toast.LENGTH_LONG).show();
						}
					});
                }
				
			}
		}).start();
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pullToRefreshAndMoreView.onRefreshFinish();
	}
	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		pullToRefreshAndMoreView.onMoreFinish();
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
	

}
