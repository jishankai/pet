package com.aidigame.hisun.pet.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Message;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.PetPicture;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserImagesJson;
import com.aidigame.hisun.pet.ui.DetailActivity;
import com.aidigame.hisun.pet.ui.NewHomeActivity;
import com.aidigame.hisun.pet.ui.ShowTopicActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Helper;
import com.dodowaterfall.widget.ScaleImageView;
import com.example.android.bitmapfun.util.ImageCache;
import com.example.android.bitmapfun.util.ImageFetcher;
import com.example.android.bitmapfun.util.ImageCache.ImageCacheParams;
import com.example.android.bitmapfun.util.ImageWorker.LoadCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PLAWaterfull implements IXListViewListener{
	DisplayImageOptions displayImageOptions;
	BitmapFactory.Options options;
	Activity activity;
	LinearLayout parent;
	View view;
	int mode;
	Handler handler;
	int count=0;
	public PLAWaterfull(Activity activity,LinearLayout parent,int mode){
		this.activity=activity;
		this.parent=parent;
		this.mode=mode;
		handler=HandleHttpConnectionException.getInstance().getHandler(activity);
		inite();
//		task = new ContentTask(activity, 2);
		
		
		
	}
	private ImageFetcher mImageFetcher;
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    private int currentPage = 0;
    ContentTask task ;
    
    private class ContentTask extends AsyncTask<String, Integer, ArrayList<DuitangInfo>> {

        private Context mContext;
        private int mType = 1;
        private int last_id=-1;
        public ContentTask(Context context, int type,int last_id) {
            super();
            mContext = context;
            mType = type;
            this.last_id=last_id;
        }

        @Override
        protected ArrayList<DuitangInfo> doInBackground(String... params) {
            try {
            	
            	  UserImagesJson json=null;
          		if(mode==4||mode==5){
          			HttpUtil.downloadActivityImagesList(handler,last_id, 0, activity,DetailActivity.detailActivity.data, mode);
          		}else{
          			json=HttpUtil.downloadPetkingdomImages(handler, last_id,mode,activity,-1);
          		}
          		
          if(json!=null&&json.petPictures!=null){
       	   PetPicture pp=null;
       	   DuitangInfo di=null;
       	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
       	   for(int i=0;i<json.petPictures.size();i++){
       		   pp=json.petPictures.get(i);
       		   di=new DuitangInfo();
       		   di.img_id=pp.img_id;
       		   di.isrc=pp.url;
       		 di.setMsg(pp.cmt);
       		   if(!list.contains(di))
       		   list.add(di);
       	   }
       	return list;
          }	
            	return null;
//                return parseNewsJSON(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<DuitangInfo> result) {
            if (mType == 1) {

                mAdapter.addItemTop(result);
                mAdapter.notifyDataSetChanged();
                mAdapterView.stopRefresh();
            } else if (mType == 2) {
            	 mAdapterView.stopLoadMore();
                mAdapter.addItemLast(result);
                mAdapter.notifyDataSetChanged();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        public ArrayList<DuitangInfo> parseNewsJSON(String url) throws IOException {
        	ArrayList<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
            String json = "";
            if (Helper.checkConnection(mContext)) {
                try {
                    json = Helper.getStringFromUrl(url);

                } catch (IOException e) {
                    Log.e("IOException is : ", e.toString());
                    e.printStackTrace();
                    return duitangs;
                }
            }
            Log.d("MainActiivty", "json:" + json);

            try {
                if (null != json) {
                    JSONObject newsObject = new JSONObject(json);
                    JSONObject jsonObject = newsObject.getJSONObject("data");
                    JSONArray blogsJson = jsonObject.getJSONArray("blogs");

                    for (int i = 0; i < blogsJson.length(); i++) {
                        JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
                        DuitangInfo newsInfo1 = new DuitangInfo();
                        newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
                        newsInfo1.setIsrc(newsInfoLeftObject.isNull("isrc") ? "" : newsInfoLeftObject.getString("isrc"));
                        newsInfo1.setMsg(newsInfoLeftObject.isNull("msg") ? "" : newsInfoLeftObject.getString("msg"));
                        newsInfo1.setHeight(newsInfoLeftObject.getInt("iht"));
                        duitangs.add(newsInfo1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return duitangs;
        }
    }

    /**
     * 添加内容
     * 
     * @param pageindex
     * @param type
     *            1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int pageindex, int type,int last_id) {
        if (task.getStatus() != Status.RUNNING) {
            String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
            Log.d("MainActivity", "current url:" + url);
            ContentTask task = new ContentTask(activity, type,last_id);
//            task.execute(url);
            task.execute();
        }
    }

    public class StaggeredAdapter extends BaseAdapter {
        private Context mContext;
        public ArrayList<DuitangInfo> mInfos;
        private XListView mListView;
        public int count=0;
        public StaggeredAdapter(Context context, XListView xListView) {
            mContext = context;
            mInfos = new ArrayList<DuitangInfo>();
            mListView = xListView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
            ViewHolder holder;
           final DuitangInfo duitangInfo = mInfos.get(position);
              count++;
              LogUtil.i("me", "++++++++++++++++count="+count);
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.infos_list, null);
                holder = new ViewHolder();
                holder.imageView = (/*Scale*/ImageView) convertView.findViewById(R.id.news_pic);
                holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
                convertView.setTag(holder);
            }

            holder = (ViewHolder) convertView.getTag();
//            holder.imageView.setImageWidth(duitangInfo.getWidth());
//            holder.imageView.setImageHeight(duitangInfo.getHeight());
            holder.contentView.setText(duitangInfo.getMsg());
            holder.imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ShowTopicActivity.showTopicActivity!=null){
						if(ShowTopicActivity.showTopicActivity.getBitmap()!=null){
							if(!ShowTopicActivity.showTopicActivity.getBitmap().isRecycled())
								ShowTopicActivity.showTopicActivity.getBitmap().recycle();
						}
						ShowTopicActivity.showTopicActivity.finish();
					}
					Intent intent=new Intent(activity,ShowTopicActivity.class);
					/*if(homeActivity!=null){
					}else {
						intent.putExtra("from", "ScanActivity");
					}*/
					PetPicture pp=new PetPicture();
					pp.img_id=(int)duitangInfo.img_id;
					pp.animal=new Animal();
					pp.animal.a_id=duitangInfo.a_id;
					ArrayList<PetPicture> pictures=new ArrayList<PetPicture>();
					PetPicture p=null;
					for(int i=0;i<mInfos.size();i++){
						p=new PetPicture();
						p.img_id=(int)mInfos.get(i).img_id;
						p.animal=new Animal();
						p.animal.a_id=mInfos.get(i).a_id;
						pictures.add(p);
					}
						ShowTopicActivity.petPictures=pictures;
					intent.putExtra("PetPicture",pp);
//					intent.putExtra("mode", flowTag.mode);
					activity.startActivity(intent);
				}
			});
          
            mImageFetcher.setWidth(Constants.screen_width/2);
            //
            mImageFetcher.setLoadCompleteListener(new LoadCompleteListener() {
				
				@Override
				public void onComplete(Bitmap bitmap) {
					// TODO Auto-generated method stub
					count++;
				    bitmap=null;
				    if(mode==2){
				    	if(HomeFragment.homeFragment!=null&&HomeFragment.homeFragment.showProgress!=null){
				    		if(count>4){
				    			PLAWaterfull.this.parent.setVisibility(View.VISIBLE);
				    			HomeFragment.homeFragment.showProgress.progressCancel();
				    			
				    		}
				    		
				    		
				    		
				    	}
				    }
				    
				}
				@Override
				public void getPath(String path) {
					// TODO Auto-generated method stub
					
				}
				
				
			});
            mImageFetcher.setImageCache(new ImageCache(activity, new ImageCacheParams(duitangInfo.getIsrc())));
            mImageFetcher.loadImage(/*Constants.UPLOAD_IMAGE_RETURN_URL+*/duitangInfo.getIsrc(), holder.imageView,options);
            return convertView;
        }

        class ViewHolder {
            /*Scale*/ImageView imageView;
            TextView contentView;
            TextView timeView;
        }

        @Override
        public int getCount() {
            return mInfos.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mInfos.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        public void addItemLast(ArrayList<DuitangInfo> datas) {
        	if(datas==null)return;
        	if(mInfos!=null){
        		
        	}else{
        		mInfos=new ArrayList<DuitangInfo>();
        	}
        	 for (DuitangInfo info : datas) {
              	if(!mInfos.contains(info)){
              		mInfos.add(info);
              	}
              }
        }

        public void addItemTop(ArrayList<DuitangInfo> datas) {
        	if(datas==null)return;
        	mInfos=new ArrayList<DuitangInfo>();
            for (DuitangInfo info : datas) {
            	if(!mInfos.contains(info)){
            		mInfos.add(info);
            	}
            }
        }
    }

    protected void inite() {
    	options=new BitmapFactory.Options();
		options.inSampleSize=StringUtil.getScaleByDPI(activity);;
		LogUtil.i("me", "图片像素压缩比例="+StringUtil.getScaleByDPI(activity));
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		/*
		 *内存溢出， 
		 */
		displayImageOptions=new DisplayImageOptions
	            .Builder()
		        .cacheOnDisc(true)
//		        .cacheInMemory(true)
//		        .showImageOnLoading(R.drawable.noimg)
		        .showImageOnFail(R.drawable.noimg)
		        .bitmapConfig(Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
    	
    	
    	
        view=LayoutInflater.from(activity).inflate(R.layout.act_pull_to_refresh_sample,null);
   

        mAdapterView = (XListView) view.findViewById(R.id.list);
       
        
       
        mAdapterView.setPullLoadEnable(true);
        mAdapterView.setXListViewListener(this);
        mAdapter = new StaggeredAdapter(activity, mAdapterView);
        mImageFetcher = new ImageFetcher(activity, 240);
        mImageFetcher.setLoadingImage(R.drawable.white_box);
        parent.removeAllViews();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        parent.addView(view);
        mAdapterView.setAdapter(mAdapter);
       
        loadData();
       
    }
    public void clearAndRefresh(){
    	 mAdapter = new StaggeredAdapter(activity, mAdapterView);
         parent.removeAllViews();
         LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
         view.setLayoutParams(params);
         parent.addView(view);
         mAdapterView.setAdapter(mAdapter);
        
         loadData();
    }
    public void loadData(){
    	 task=new ContentTask(activity, 1, -1);
    	 task.execute();
    	 new Thread(new Runnable() {
 			
 			@Override
 			public void run() {
 				// TODO Auto-generated method stub
 				
                UserImagesJson json=null;
                		if(mode==4||mode==5){
                			HttpUtil.downloadActivityImagesList(handler, -1, 0, activity,DetailActivity.detailActivity.data, mode);
                		}else{
                			json=HttpUtil.downloadPetkingdomImages(handler, -1,mode,activity,-1);
                		}
                		
                if(json!=null&&json.petPictures!=null){
             	   PetPicture pp=null;
             	   DuitangInfo di=null;
             	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
             	   for(int i=0;i<json.petPictures.size();i++){
             		   pp=json.petPictures.get(i);
             		   di=new DuitangInfo();
             		   di.img_id=pp.img_id;
             		   di.isrc=pp.url;
             		   di.setMsg(pp.cmt);
             		   if(!list.contains(di))
             		   list.add(di);
             	   }
             	   activity.runOnUiThread(new Runnable() {
 					
 					@Override
 					public void run() {
 						// TODO Auto-generated method stub
 						if(list.size()>0&&mAdapter.mInfos.size()>0){
							if(list.get(0).img_id==mAdapter.mInfos.get(0).img_id){
								
            		   return;
            	   }
						}
 						mAdapter.mInfos=new ArrayList<DuitangInfo>();
						mAdapter.addItemTop(list);
 		            	   mAdapter.notifyDataSetChanged();
 					}
 				});
             	   
                }
 					
 				
 			}
 		})/*.start()*/;
    }

  /*  @Override
    protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapterView.setAdapter(mAdapter);
        AddItemToContainer(currentPage, 2);
    }*/


    @Override
    public void onRefresh() {
    	
        AddItemToContainer(++currentPage, 1,-1);

new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
               UserImagesJson json= HttpUtil.downloadPetkingdomImages(handler, -1,mode,activity,-1);
               if(json!=null&&json.petPictures!=null){
            	   PetPicture pp=null;
            	   DuitangInfo di=null;
            	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
            	   for(int i=0;i<json.petPictures.size();i++){
            		   pp=json.petPictures.get(i);
            		   di=new DuitangInfo();
            		   di.img_id=pp.img_id;
            		   di.isrc=pp.url;
            		   di.setMsg(pp.cmt);
            		   if(!list.contains(di))
            		   list.add(di);
            	   }
            	   
            	   activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(list.size()>0&&mAdapter.mInfos.size()>0){
							if(list.get(0).img_id==mAdapter.mInfos.get(0).img_id){
								 mAdapterView.stopRefresh();
            		   return;
            	   }
						}
						mAdapter.mInfos=new ArrayList<DuitangInfo>();
						mAdapter.addItemTop(list);
		            	   mAdapter.notifyDataSetChanged();
		            	   mAdapterView.stopRefresh();
					}
				});
            	   
               }
					
				
			}
		})/*.start()*/;
    }

    @Override
    public void onLoadMore() {
    	
    	int img_id=-1;
		if(mAdapter.mInfos!=null&&mAdapter.mInfos.size()>0){
			img_id=(int)mAdapter.mInfos.get(mAdapter.mInfos.size()-1).img_id;
		}
        AddItemToContainer(++currentPage, 2,img_id);
    	
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long img_id=-1;
			if(mAdapter.mInfos!=null&&mAdapter.mInfos.size()>0){
				img_id=mAdapter.mInfos.get(mAdapter.mInfos.size()-1).img_id;
			}
               UserImagesJson json= HttpUtil.downloadPetkingdomImages(handler,(int)img_id,mode,activity,-1);
               if(json!=null&&json.petPictures!=null){
            	   PetPicture pp=null;
            	   DuitangInfo di=null;
            	  final  ArrayList<DuitangInfo> list=new ArrayList<DuitangInfo>();
            	   for(int i=0;i<json.petPictures.size();i++){
            		   pp=json.petPictures.get(i);
            		   di=new DuitangInfo();
            		   di.img_id=pp.img_id;
            		   di.isrc=pp.url;
            		   di.setMsg(pp.cmt);
            		   if(!list.contains(di))
            		   list.add(di);
            	   }
            	   activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mAdapter.addItemLast(list);
		            	   mAdapter.notifyDataSetChanged();
		            	   mAdapterView.stopLoadMore();
					}
				});
            	   
               }
					
				
			}
		})/*.start()*/;

    }
	
	

}
