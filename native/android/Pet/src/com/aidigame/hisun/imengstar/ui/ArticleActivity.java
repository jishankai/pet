package com.aidigame.hisun.imengstar.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.ArticleCommentListViewAdapter;
import com.aidigame.hisun.imengstar.bean.Article;
import com.aidigame.hisun.imengstar.bean.Article.ArticleComment;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.huanxin.ChatActivity;
import com.aidigame.hisun.imengstar.huanxin.ExpandGridView;
import com.aidigame.hisun.imengstar.huanxin.ExpressionAdapter;
import com.aidigame.hisun.imengstar.huanxin.ExpressionPagerAdapter;
import com.aidigame.hisun.imengstar.huanxin.NewSmileUtils;
import com.aidigame.hisun.imengstar.huanxin.PasteEditText;
import com.aidigame.hisun.imengstar.util.ImageUtil;
import com.aidigame.hisun.imengstar.util.LogUtil;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.R;
/**
 * 浏览文章并评论
 * @author admin
 *
 */
public class ArticleActivity extends BaseActivity implements OnClickListener{
	private WebView webView;
	private ImageView backIv,likeIv,shareIv,commnetsIv,cancelIv,commitIv,guidIv;
	private TextView addCommentTv,commentsNumTv;
	private RelativeLayout comments_layout,editLayout;
	private XListView xListView;
	private EditText et;
	private LinearLayout commentsContinerLayout;
	private FrameLayout rootLayout;
	private ArrayList<ArticleComment> articleComments;
	private ArticleCommentListViewAdapter articleCommentListViewAdapter;
	private Article article;
	private  LinearLayout emotionLayout;
	private ImageView emotionIv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		article=(Article)getIntent().getSerializableExtra("article");
		initView();
		initListener();
		initData();
	}
	private void initData() {
		// TODO Auto-generated method stub
		WebSettings ws=webView.getSettings();
		ws.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
//				return super.shouldOverrideUrlLoading(view, url);
				return true;
			}
		});
		webView.loadUrl(article.url);
		articleComments=new ArrayList<ArticleComment>();
		articleCommentListViewAdapter=new ArticleCommentListViewAdapter(this, articleComments);
		xListView.setAdapter(articleCommentListViewAdapter);
		
	}
	private void initListener() {
		// TODO Auto-generated method stub
		backIv.setOnClickListener(this);
		likeIv.setOnClickListener(this);
		shareIv.setOnClickListener(this);
		commnetsIv.setOnClickListener(this);
		cancelIv.setOnClickListener(this);
		commitIv.setOnClickListener(this);
		addCommentTv.setOnClickListener(this);
		editLayout.setOnClickListener(this);
		guidIv.setOnClickListener(this);
	}
	private void initView() {
		// TODO Auto-generated method stub
		webView=(WebView)findViewById(R.id.webview);
		backIv=(ImageView)findViewById(R.id.back_iv);
		likeIv=(ImageView)findViewById(R.id.add_like_iv);
		shareIv=(ImageView)findViewById(R.id.share_iv);
		commnetsIv=(ImageView)findViewById(R.id.page_comment_iv);
		cancelIv=(ImageView)findViewById(R.id.cancel_edit_iv);
		commitIv=(ImageView)findViewById(R.id.commit_edit_iv);
		addCommentTv=(TextView)findViewById(R.id.show_send_comment_tv);
		commentsNumTv=(TextView)findViewById(R.id.show_comments_num_iv);
		comments_layout=(RelativeLayout)findViewById(R.id.comments_layout);
		editLayout=(RelativeLayout)findViewById(R.id.edit_comment_layout);
		rootLayout=(FrameLayout)findViewById(R.id.foot_layout);
		xListView=(XListView)findViewById(R.id.listview);
		et=(EditText)findViewById(R.id.edit_et);
		
		
		
		initEmotion();
		
		
		
		guidIv=(ImageView)findViewById(R.id.guide);
		SharedPreferences sp=getSharedPreferences(Constants.BASEIC_SHAREDPREFERENCE_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor e=sp.edit();
		boolean guide2=sp.getBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE9, true);
		if(guide2){
			guidIv.setImageResource(R.drawable.guide9);
			guidIv.setVisibility(View.VISIBLE);
			e.putBoolean(Constants.BASEIC_SHAREDPREFERENCE_NAME_GUIDE9, false);
			e.commit();
		}else{
			guidIv.setVisibility(View.GONE);
			
		}
		
		commentsContinerLayout=(LinearLayout)findViewById(R.id.comment_linearlayout);
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_iv:
			if(isTaskRoot()){
				if(HomeActivity.homeActivity!=null){
					ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
					am.moveTaskToFront(HomeActivity.homeActivity.getTaskId(), 0);
				}else{
					Intent intent=new Intent(this,HomeActivity.class);
					this.startActivity(intent);
				}
			}
			
		
			finish();
			System.gc();
			break;
		case R.id.add_like_iv:
			
			break;
		case R.id.share_iv:
			
			break;
		case R.id.page_comment_iv:
			if(comments_layout.getVisibility()!=View.VISIBLE){
				LogUtil.i("mi", "commentsContinerLayout.getY()="+commentsContinerLayout.getY()+";getResources().getDimensionPixelOffset(R.dimen.one_dip)*300="+getResources().getDimensionPixelOffset(R.dimen.one_dip)*400);
			TranslateAnimation ta=new TranslateAnimation(0, 0,/* commentsContinerLayout.getY()*/getResources().getDimensionPixelOffset(R.dimen.one_dip)*300,0);
//			ta.setFillAfter(true);
			ta.setDuration(300);
//			Animation anim=AnimationUtils.loadAnimation(this, R.anim.anim_translate);
			
			commentsContinerLayout.clearAnimation();
			commentsContinerLayout.setAnimation(ta);
			comments_layout.setVisibility(View.VISIBLE);
			ta.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
//					comments_layout.setVisibility(View.VISIBLE);
				}
			});
			ta.start();
			}else{
				comments_layout.setVisibility(View.GONE);
				TranslateAnimation ta=new TranslateAnimation(0, 0, 0, /*commentsContinerLayout.getY()+*/getResources().getDimensionPixelOffset(R.dimen.one_dip)*300);
//				ta.setFillAfter(true);
				ta.setDuration(300);
//				Animation anim=AnimationUtils.loadAnimation(this, R.anim.anim_translate_2);
				commentsContinerLayout.clearAnimation();
				commentsContinerLayout.setAnimation(ta);
				
				ta.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						comments_layout.setVisibility(View.GONE);
					}
				});
//				ta.start();
			
			}
			break;
		case R.id.edit_comment_layout:
			editLayout.setBackgroundDrawable(null);
			editLayout.setVisibility(View.GONE);
			InputMethodManager im0=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			
			im0.hideSoftInputFromWindow(et.getWindowToken(), 0);
			break;
		case R.id.cancel_edit_iv:
			editLayout.setBackgroundDrawable(null);
			editLayout.setVisibility(View.GONE);
			InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			
			im.hideSoftInputFromWindow(et.getWindowToken(), 0);
			break;
		case R.id.commit_edit_iv:
//			String ct=et.getText().toString();
			String ct=mEditTextContent.getText().toString();
            if(StringUtil.isEmpty(ct)){
            	Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_LONG).show();
				return;
			}
			ArticleComment ac=new ArticleComment();
			ac.create_time=System.currentTimeMillis()/1000;
			ac.user_name=PetApplication.myUser.u_nick;
			ac.usr_tx=PetApplication.myUser.u_iconUrl;
			ac.comment=ct;
			articleComments.add(ac);
			articleCommentListViewAdapter.update(articleComments);
			articleCommentListViewAdapter.notifyDataSetChanged();
			editLayout.setBackgroundDrawable(null);
			editLayout.setVisibility(View.GONE);
			InputMethodManager im3=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			
			im3.hideSoftInputFromWindow(et.getWindowToken(), 0);
			commentsNumTv.setVisibility(View.VISIBLE);
			commentsNumTv.setText(""+articleComments.size());
			break;
		case R.id.show_send_comment_tv:
			Bitmap bmp=ImageUtil.getBitmapFromView(rootLayout, this);
		    editLayout.setBackgroundDrawable(new BitmapDrawable(bmp));
		    InputMethodManager im2=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			im2.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			editLayout.setVisibility(View.VISIBLE);
//			et.setFocusable(true);
//			et.setFocusableInTouchMode(true);
//			et.setText("");
//			et.requestFocus();
			mEditTextContent.setFocusable(true);
			mEditTextContent.setFocusableInTouchMode(true);
			mEditTextContent.setText("");
			mEditTextContent.requestFocus();
			break;
		case R.id.guide:
			guidIv.setVisibility(View.GONE);
			guidIv.setImageDrawable(new BitmapDrawable());
			break;
		}
	}
	private List<String> reslist;
	private ViewPager expressionViewpager;
	private PasteEditText mEditTextContent;
	public void initEmotion(){
		emotionIv=(ImageView)findViewById(R.id.emotion_iv);
		emotionLayout=(LinearLayout)findViewById(R.id.ll_face_container);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		emotionIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(emotionLayout.getVisibility()==View.GONE){
					emotionLayout.setVisibility(View.VISIBLE);
				}else{
					emotionLayout.setVisibility(View.GONE);
				}
				
			}
		});
		// 表情list
				reslist = getExpressionRes(44);
				// 初始化表情viewpager
				List<View> views = new ArrayList<View>();
				View gv1 = getGridChildView(1);
				View gv2 = getGridChildView(2);
				views.add(gv1);
				views.add(gv2);
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
		View view = View.inflate(this, R.layout.expression_gridview2, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list,1);
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
							mEditTextContent.append(NewSmileUtils.getSmiledText(ArticleActivity.this, /*(String) field.get(null)*/filename));
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
