package com.aidigame.hisun.imengstar.widget.fragment;

import java.util.ArrayList;
import java.util.List;

import com.aidigame.hisun.imengstar.adapter.ClassicPictureAdapter;
import com.aidigame.hisun.imengstar.adapter.ClassicPictureAdapter.Holder;
import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.bean.PetPicture;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.huanxin.ExpandGridView;
import com.aidigame.hisun.imengstar.huanxin.ExpressionAdapter;
import com.aidigame.hisun.imengstar.huanxin.ExpressionPagerAdapter;
import com.aidigame.hisun.imengstar.huanxin.NewSmileUtils;
import com.aidigame.hisun.imengstar.huanxin.PasteEditText;
import com.aidigame.hisun.imengstar.ui.ArticleActivity;
import com.aidigame.hisun.imengstar.ui.HomeActivity;
import com.aidigame.hisun.imengstar.ui.NewShowTopicActivity;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.view.MyViewPager;
import com.aidigame.hisun.imengstar.R;
import com.umeng.analytics.MobclickAgent;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

@SuppressLint("ValidFragment") public class HomeSeePictureFragment extends Fragment implements OnClickListener{
	public static int LATEST_PICTURE_FRAGMENT=2;
	public static int CLASSIC_PICTURE_FRAGMENT=1;
	public static int FOCUS_PICTURE_FRAGMENT=3;
	public View view;
	private LinearLayout coninerLayout;
	private LatestPictureFragment latestPictureFragment;
	private ClassicPicturesFragment classicPicturesFragment;
	private ClassicPicturesFragment focusPicturesFragment;
	private TextView classicTv,latestTv,focusTv;
	private int current_show=LATEST_PICTURE_FRAGMENT;
	public View popupParent,black_layout;
	//评论相关
//	private  EditText commentET;
	private  TextView send_comment_tv;
	private  LinearLayout addcommentLayout;
	private Handler handler;
	public DiscoveryFragment discoveryFragment;
	public LinearLayout topTabLayout;
	
	
	
	public  MyViewPager viewPager;
	public  RelativeLayout bannersLayout;
	
	RelativeLayout commentContainerLayout;
	
	
	public HomeSeePictureFragment(){
		super();
	}
	public HomeSeePictureFragment(View popupParent,View black_layout,DiscoveryFragment discoveryFragment,RelativeLayout commentContainerLayout){
	     super();
		this.popupParent=popupParent;
		this.black_layout=black_layout;
		this.discoveryFragment=discoveryFragment;
		this.commentContainerLayout=commentContainerLayout;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initBannerView();
		initView();
		initListener();
	}
	private void initBannerView() {
		// TODO Auto-generated method stub
		viewPager=(MyViewPager)view.findViewById(R.id.viewpager);
        bannersLayout=(RelativeLayout)view.findViewById(R.id.banner_layout);
	}
	private void initListener() {
		// TODO Auto-generated method stub
		classicTv.setOnClickListener(this);
		latestTv.setOnClickListener(this);
		focusTv.setOnClickListener(this);
		mEditTextContent.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
					sendComment();
					return true;
			}
		});
		mEditTextContent.addTextChangedListener(new TextWatcher() {
		
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
			if(s.length()>0){
				canSend=true;
				send_comment_tv.setText("发送");
			}else{
				canSend=false;
				send_comment_tv.setText("取消");
			}
		}
	});
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.fragment_home_see_picture, null);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        handler=HandleHttpConnectionException.getInstance().getHandler(getActivity());
		
	
		return view;
	}
	private void initView() {
		// TODO Auto-generated method stub
		coninerLayout=(LinearLayout)view.findViewById(R.id.list_linearlayout);
		classicTv=(TextView)view.findViewById(R.id.classic_tab_tv);
		latestTv=(TextView)view.findViewById(R.id.latest_tab_tv);
		focusTv=(TextView)view.findViewById(R.id.focus_tab_tv);
		topTabLayout=(LinearLayout)view.findViewById(R.id.tt_layout);
//		ScrollView sv=(ScrollView)view.findViewById(R.id.scrollview);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,HomeActivity.h);

		

		send_comment_tv=(TextView)commentContainerLayout.findViewById(R.id.send_comment);
		addcommentLayout=(LinearLayout)commentContainerLayout.findViewById(R.id.add_comment_linearlayout);
		send_comment_tv.setText("取消");
		send_comment_tv.setOnClickListener(this);
		                                                
		resetTab();
		classicTv.setTextColor(getActivity().getResources().getColor(R.color.orange_red1));
		showClassicPictureFragment();
		initEmotion();
	}
	public void setContainerHeight(){
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,HomeActivity.h);
		coninerLayout.setLayoutParams(params);
	}
	private void showLatestPictureFragment() {
		// TODO Auto-generated method stub
		bannersLayout.setVisibility(View.VISIBLE);
		FragmentManager fm=getFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		if(latestPictureFragment==null){
			latestPictureFragment=new LatestPictureFragment(this);
		}else{
			latestPictureFragment.plaWaterfull.pullRefresh();
		/*	hideFragment(ft);
			ft.show(latestPictureFragment);
			ft.commit();
			current_show=LATEST_PICTURE_FRAGMENT;
			return;*/
		}
		
		if(classicPicturesFragment!=null){
			ft.remove(classicPicturesFragment);
			classicPicturesFragment=null;
		}
		if(focusPicturesFragment!=null){
			ft.remove(focusPicturesFragment);
			focusPicturesFragment=null;
		}
		ft.replace(R.id.list_linearlayout, latestPictureFragment, "LATEST_PICTURE_FRAGMENT");
		
		ft.commit();
		current_show=LATEST_PICTURE_FRAGMENT;
		System.gc();
	}
	public void showClassicPictureFragment(){
		bannersLayout.setVisibility(View.GONE);
		FragmentManager fm=getFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		if(classicPicturesFragment==null){
			classicPicturesFragment=new ClassicPicturesFragment(this,2);
		}else{
			classicPicturesFragment.pullRefresh();
		/*	hideFragment(ft);
			ft.show(classicPicturesFragment);
			ft.commit();
			current_show=CLASSIC_PICTURE_FRAGMENT;
			return;*/
		}
		
		if(latestPictureFragment!=null){
			ft.remove(latestPictureFragment);
			latestPictureFragment=null;
		}
		if(focusPicturesFragment!=null){
			ft.remove(focusPicturesFragment);
			focusPicturesFragment=null;
		}
		ft.replace(R.id.list_linearlayout, classicPicturesFragment, "CLASSIC_PICTURE_FRAGMENT");
		ft.commit();
		current_show=CLASSIC_PICTURE_FRAGMENT;
		System.gc();
	}
	public void addFragment(){
		FragmentManager fm=getFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		focusPicturesFragment=new ClassicPicturesFragment(this,1);
		ft.add(R.id.list_linearlayout, focusPicturesFragment);
		classicPicturesFragment=new ClassicPicturesFragment(this,2);
		ft.add(R.id.list_linearlayout, classicPicturesFragment);
		latestPictureFragment=new LatestPictureFragment(this);
		ft.add(R.id.list_linearlayout, latestPictureFragment);
		ft.show(classicPicturesFragment);
		ft.commit();
	}
	
	public void showFocusPictureFragment(){
		bannersLayout.setVisibility(View.GONE);
		FragmentManager fm=getFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		if(focusPicturesFragment==null){
			focusPicturesFragment=new ClassicPicturesFragment(this,1);
		}else{
			focusPicturesFragment.pullRefresh();
			/*hideFragment(ft);
			ft.show(focusPicturesFragment);
			ft.commit();
			current_show=FOCUS_PICTURE_FRAGMENT;
			return;*/
		}
		
		if(latestPictureFragment!=null){
			ft.remove(latestPictureFragment);
			latestPictureFragment=null;
		}
		if(classicPicturesFragment!=null){
			ft.remove(classicPicturesFragment);
			classicPicturesFragment=null;
		}
		ft.replace(R.id.list_linearlayout, focusPicturesFragment, "FOCUS_PICTURE_FRAGMENT");
		ft.commit();
		current_show=FOCUS_PICTURE_FRAGMENT;
		System.gc();
	}
	public void hideFragment(FragmentTransaction ft){
		if(classicPicturesFragment!=null){
			ft.hide(classicPicturesFragment);
		}
		if(focusPicturesFragment!=null){
			ft.hide(focusPicturesFragment);
		}
		if(latestPictureFragment!=null){
			ft.hide(latestPictureFragment);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.classic_tab_tv:
			if(current_show==CLASSIC_PICTURE_FRAGMENT){
				if(classicPicturesFragment!=null){
					classicPicturesFragment.pullRefresh();
					return;
				}
				
			}
			
			resetTab();
			classicTv.setTextColor(getActivity().getResources().getColor(R.color.orange_red1));
			showClassicPictureFragment();
			break;
		case R.id.latest_tab_tv:
			if(current_show==LATEST_PICTURE_FRAGMENT){
				
				if(latestPictureFragment!=null&&latestPictureFragment.plaWaterfull!=null){
					latestPictureFragment.plaWaterfull.pullRefresh();
					return;
				}
					
			}
			resetTab();
			latestTv.setTextColor(getActivity().getResources().getColor(R.color.orange_red1));
			showLatestPictureFragment();
			break;
		case R.id.focus_tab_tv:
			if(current_show==FOCUS_PICTURE_FRAGMENT&&focusPicturesFragment!=null){
				focusPicturesFragment.pullRefresh();
				return;
			}
			resetTab();
			focusTv.setTextColor(getActivity().getResources().getColor(R.color.orange_red1));
			showFocusPictureFragment();
			break;
		case R.id.send_comment:
			sendComment();
			break;

		default:
			break;
		}
	}
	public void resetTab(){
		latestTv.setTextColor(getActivity().getResources().getColor(R.color.tab_black));
		classicTv.setTextColor(getActivity().getResources().getColor(R.color.tab_black));
		focusTv.setTextColor(getActivity().getResources().getColor(R.color.tab_black));
	}
	/**
	 * 显示评论编辑框
	 */
	private  boolean replySb=false;
	private  boolean show_add_comment=false;
	PetPicture petPicture;
	public    void showCommentEditor(PetPicture petPicture,Holder holder){
         
    	if(!UserStatusUtil.isLoginSuccess(getActivity(), popupParent,black_layout)){
    		return;
    	};
    	replySb=false;
    	emotionIv.setImageResource(R.drawable.chatting_biaoqing_btn_enable);
    	showSmile=true;
    	this.petPicture=petPicture;
    	this.holder=new Holder();
    	ClassicPictureAdapter.copyHolder(this.holder, holder);
		if(show_add_comment){
			show_add_comment=false;
			mEditTextContent.setVisibility(View.INVISIBLE);
			addcommentLayout.setVisibility(View.GONE);
			if(current_show==CLASSIC_PICTURE_FRAGMENT){
				if(classicPicturesFragment!=null){
					classicPicturesFragment.classicPictureAdapter.current_holder_position=-1;
				}
			}else if(current_show==FOCUS_PICTURE_FRAGMENT){
				focusPicturesFragment.classicPictureAdapter.current_holder_position=-1;
			}
		}else{
			mEditTextContent.setVisibility(View.VISIBLE);
			show_add_comment=true;
//			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			mEditTextContent.setEnabled(true);
			mEditTextContent.setFocusable(true);
			mEditTextContent.setFocusableInTouchMode(true);
			if(mEditTextContent.requestFocus(EditText.FOCUS_FORWARD)){
				mEditTextContent.setSelection(0);;
				InputMethodManager m = (InputMethodManager)   
						mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			};
			Animation animation=AnimationUtils.loadAnimation(getActivity(), R.anim.anim_translate_showtopic_addcommentlayout_in);
			addcommentLayout.setAnimation(animation);
			animation.start();
			addcommentLayout.setVisibility(View.VISIBLE);
		}

	}
	/**
	 * 发送评论
	 */
	private  	boolean canSend=false;
	private  boolean sendingComment=false;
	public   void sendComment(){
		if(!canSend){
    		InputMethodManager m = (InputMethodManager)   
    				mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
					m.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);
					addcommentLayout.setVisibility(View.GONE);
					emotionLayout.setVisibility(View.GONE);
					mEditTextContent.setHint("写个评论呗");
					this.holder=null;
					show_add_comment=false;
					return;
    	}
    	if(!UserStatusUtil.isLoginSuccess(getActivity(), popupParent,black_layout)){
    		return;
    	};
    	if(sendingComment){
    		Toast.makeText(getActivity(), "正在发送评论", Toast.LENGTH_LONG).show();
    		return;
    	}
    	final String comment=mEditTextContent.getText().toString();
    	mEditTextContent.setText("");
    	if(replySb){
    		mEditTextContent.setHint("回复"+cmt.name);
    	}else{
    		mEditTextContent.setHint("写个评论呗");
    	}
    	/*commentET.setFocusable(true);
    	commentET.requestFocus();
    	InputMethodManager im=(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
    	im.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    	*/
    	if(StringUtil.isEmpty(comment)){
    		Toast.makeText(getActivity(), "评论内容不能为空。", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if("写个评论呗".equals(comment)){
    		Toast.makeText(getActivity(), "评论内容不能为空。", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	comment.trim();
//    	commentET.setEnabled(false);
    	
    	sendingComment=true;
    	
    	InputMethodManager im=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    	im.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);
    	
    	
    	//测试 发表评论api
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub\
				MyUser temp=null;
				if(replySb){
					temp=HttpUtil.sendComment(getActivity(),comment, petPicture.img_id,cmt.usr_id,cmt.name,handler);
				}else{
					temp=HttpUtil.sendComment(getActivity(),comment, petPicture.img_id,-1,"",handler);
				}
				final MyUser user=temp;
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if(user!=null){
							if(user.exp!=-1){
								PetPicture.Comments comments=new PetPicture.Comments();
								comments.usr_id=PetApplication.myUser.userId;
								comments.create_time=System.currentTimeMillis()/1000;
								comments.body=comment;
								comments.usr_tx=PetApplication.myUser.u_iconUrl;
								if(replySb){
									comments.isReply=true;
									comments.reply_id=cmt.usr_id;
									comments.reply_name=PetApplication.myUser.u_nick+"@"+cmt.name;
								}
								comments.name=PetApplication.myUser.u_nick;
								if(petPicture.commentsList==null){
									petPicture.commentsList=new ArrayList<PetPicture.Comments>();
									petPicture.commentsList.add(comments);
								}else{
									petPicture.commentsList.add(0, comments);
								}
								mEditTextContent.setHint("写个评论呗");
								/*commentTv1.setText(""+petPicture.commentsList.size());
								
								
								if(current_page==1){
									showReverseSide();
								}
								
								showCommentsUsersList();*/
								if(current_show==CLASSIC_PICTURE_FRAGMENT){
									if(classicPicturesFragment!=null){
										classicPicturesFragment.classicPictureAdapter.setComments(null, petPicture);
									}
								}else if(current_show==FOCUS_PICTURE_FRAGMENT){
									focusPicturesFragment.classicPictureAdapter.setComments(null, petPicture);
								}
								MobclickAgent.onEvent(getActivity(), "comment");
//								
							}else{
								Toast.makeText(getActivity(), "发表评论失败。", Toast.LENGTH_SHORT).show();
							     
							}
							sendingComment=false;
							mEditTextContent.setEnabled(true);
							addcommentLayout.setVisibility(View.GONE);
							show_add_comment=false;
							emotionLayout.setVisibility(View.GONE);
							holder=null;
							replySb=false;
						}else{
							Toast.makeText(getActivity(), "评论发送失败", Toast.LENGTH_LONG).show();
						}
						
						
					}
					
				});
			}
		}).start();
    	
	}
	private  	PetPicture.Comments cmt;
	private Holder holder;
	public   void replyComment(PetPicture.Comments cmt,PetPicture petPicture,Holder holder){
		if(PetApplication.myUser!=null&&PetApplication.myUser.userId==cmt.usr_id){
			Toast.makeText(getActivity(), "请不要回复自己发的评论", Toast.LENGTH_LONG).show();;
			return;
		}
	
		replySb=true;
		this.cmt=cmt;
		this.petPicture=petPicture;
		this.holder=holder;
		mEditTextContent.setVisibility(View.VISIBLE);
		if(mEditTextContent.requestFocus(EditText.FOCUS_FORWARD)){
			mEditTextContent.setSelection(0);;
			InputMethodManager m = (InputMethodManager)   
					mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
					m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
//			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		};
		if(show_add_comment){
//			show_add_comment=false;
//			addcommentLayout.setVisibility(View.GONE);
			show_add_comment=true;
			mEditTextContent.setEnabled(true);;
			mEditTextContent.setText("");
			mEditTextContent.setHint("回复"+cmt.name);
		}else{
			show_add_comment=true;
			mEditTextContent.setEnabled(true);;
			mEditTextContent.setText("");
			mEditTextContent.setHint("回复"+cmt.name);
			Animation animation=AnimationUtils.loadAnimation(getActivity(), R.anim.anim_translate_showtopic_addcommentlayout_in);
			addcommentLayout.setAnimation(animation);
			animation.start();
			addcommentLayout.setVisibility(View.VISIBLE);
		}

	}
	public void pullRefresh(){
		if(current_show==CLASSIC_PICTURE_FRAGMENT){
			if(classicPicturesFragment!=null){
				classicPicturesFragment.pullRefresh();
			}
		}else if(current_show==LATEST_PICTURE_FRAGMENT){
			if(latestPictureFragment!=null){
				latestPictureFragment.pullRefresh();
			}
		}else if(current_show==FOCUS_PICTURE_FRAGMENT){
			if(focusPicturesFragment!=null){
				focusPicturesFragment.pullRefresh();
			}
		}
	}
	private List<String> reslist;
	private ViewPager expressionViewpager;
	private PasteEditText mEditTextContent;
	private ImageView emotionIv;
	private  LinearLayout emotionLayout;
	boolean showSmile=true;
	public void initEmotion(){
		emotionIv=(ImageView)commentContainerLayout.findViewById(R.id.emotion_iv);
		emotionLayout=(LinearLayout)commentContainerLayout.findViewById(R.id.ll_face_container);
		expressionViewpager = (ViewPager) commentContainerLayout.findViewById(R.id.vPager);
		mEditTextContent = (PasteEditText) commentContainerLayout.findViewById(R.id.et_sendmessage);
		emotionIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(showSmile){
					emotionIv.setImageResource(R.drawable.expression_keyboard);
					
					InputMethodManager im=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//			    	im.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);i
			    	im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
                    handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							emotionLayout.setVisibility(View.VISIBLE);
						}
					}, 200);
			    	showSmile=false;
				}else{
					
					InputMethodManager m = (InputMethodManager)   
							mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
			    	
			    	emotionIv.setImageResource(R.drawable.chatting_biaoqing_btn_enable);
					emotionLayout.setVisibility(View.GONE);
			    	showSmile=true;
				}
				/*if(emotionLayout.getVisibility()==View.GONE){
					emotionLayout.setVisibility(View.VISIBLE);
				}else{
					emotionLayout.setVisibility(View.GONE);
				}*/
				
			}
		});
		// 表情list
				reslist = getExpressionRes(44);
				// 初始化表情viewpager
				List<View> views = new ArrayList<View>();
				View gv1 = getGridChildView(1);
				View gv2 = getGridChildView(2);
				View gv3 = getGridChildView(3);
				views.add(gv1);
				views.add(gv2);
				views.add(gv3);
				expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
	}
	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
//		return reslist;
		return NewSmileUtils.tags;

	}
	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(getActivity(), R.layout.expression_gridview2, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, 40));
		}else if(i==3){
			
			list.addAll(reslist.subList(40, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(getActivity(), 1, list,1);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
//					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class.forName("com.aidigame.hisun.imengstar.huanxin.NewSmileUtils");
//							Field field = clz.getField(filename);
							mEditTextContent.append(NewSmileUtils.getSmiledText(getActivity(), /*(String) field.get(null)*/filename));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (NewSmileUtils.containsKey(cs.toString()))
											mEditTextContent.getEditableText().delete(i, selectionStart);
										else
											mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									} else {
										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
//					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}


}
