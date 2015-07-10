package com.aidigame.hisun.imengstar.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.ui.ContributeRankListActivity;
import com.aidigame.hisun.imengstar.ui.GetTicketRankListActivity;
import com.aidigame.hisun.imengstar.ui.TicketCardActivity;
import com.aidigame.hisun.imengstar.ui.ContributeRankListActivity.People;
import com.aidigame.hisun.imengstar.view.RoundImageView;
import com.aidigame.hisun.imengstar.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 贡献榜界面，listview适配器
 * @author admin
 *
 */
public class GetTicketRankListAdapter extends BaseAdapter {
	DisplayImageOptions displayImageOptions;//显示图片的格式
    Context  context;
    ArrayList<Animal> list;
    private int gold;
    private Banner banner;
    int mode;//1,长列表；2，短列表
    Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		
    	};
    };
    public GetTicketRankListAdapter(Context context,ArrayList<Animal> list,int mode,int gold,Banner banner){
    	this.context=context;
    	this.list=list;
    	this.mode=mode;
    	this.gold=gold;
    	this.banner=banner;
    	BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=false;
		options.inSampleSize=8;
		options.inPreferredConfig=Bitmap.Config.RGB_565;
		options.inPurgeable=true;
		options.inInputShareable=true;
		displayImageOptions=new DisplayImageOptions
	            .Builder()
	            .showImageOnLoading(R.drawable.user_icon)
		        .cacheInMemory(true)
		        .cacheOnDisc(true)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		        .decodingOptions(options)
                .build();
    }
    public void updateData(ArrayList<Animal> list){
    	this.list=list;
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
	public int h,w,layoutH,arrowH;
	boolean flag=false;
   public boolean arrowCanShow=true;
   public  ImageView currentArrowImageView;
   public int index;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_contribute_list_, null);
			holder=new Holder();
			holder.icon=(RoundImageView)convertView.findViewById(R.id.icon_circleView);
			holder.arrowIV=(ImageView)convertView.findViewById(R.id.imageView3);
			holder.rankNoIV=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.trendIV=(ImageView)convertView.findViewById(R.id.imageView2);
			holder.nameTV=(TextView)convertView.findViewById(R.id.textView1);
			holder.contributeNumTV=(TextView)convertView.findViewById(R.id.textView2);
			holder.rankNoTV=(TextView)convertView.findViewById(R.id.textView3);
			holder.layout=(LinearLayout)convertView.findViewById(R.id.layout);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		Animal people=list.get(position);
		people.ranking=position+1;
		if(people.ranking==1){
			holder.rankNoIV.setVisibility(View.VISIBLE);
			holder.rankNoIV.setImageResource(R.drawable.gold_rank_list);
			holder.rankNoTV.setVisibility(View.GONE);
		}else
		if(people.ranking==2){
			holder.rankNoIV.setVisibility(View.VISIBLE);
			holder.rankNoIV.setImageResource(R.drawable.silver_rank_list);
			holder.rankNoTV.setVisibility(View.GONE);
		}else
		if(people.ranking==3){
			holder.rankNoIV.setVisibility(View.VISIBLE);
			holder.rankNoIV.setImageResource(R.drawable.copper_rank_list);
			holder.rankNoTV.setVisibility(View.GONE);
		}else{
			holder.rankNoIV.setVisibility(View.GONE);
			holder.rankNoTV.setVisibility(View.VISIBLE);
			holder.rankNoTV.setText(""+(people.ranking));
		}
		holder.contributeNumTV.setText(""+people.stars);
		
		final LinearLayout llt=holder.layout;
		
		holder.nameTV.setText(""+people.pet_nickName);
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.displayImage(Constants.ANIMAL_DOWNLOAD_TX+people.pet_iconUrl, holder.icon, displayImageOptions);
		switch (people.change) {
		case -1:
			holder.trendIV.setImageResource(R.drawable.arrow_rank_list_green);
			break;
		case 0:
			holder.trendIV.setVisibility(View.INVISIBLE);
			break;
		case 1:
			holder.trendIV.setImageResource(R.drawable.arrow_rank_list_red);
			break;
		}
		final RoundImageView iv=holder.icon;
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,TicketCardActivity.class);
				intent.putExtra("animal",list.get(position));
				intent.putExtra("gold", gold);
				intent.putExtra("banner", banner);
				context.startActivity(intent);
			}
		});
		return convertView;
	}
	class Holder{
		LinearLayout layout;
		RoundImageView icon;
		TextView nameTV,contributeNumTV,rankNoTV;
		ImageView rankNoIV,trendIV,arrowIV;
		boolean isFocus;//convertview是否为选中状态（突出，显示当前用户排名）
	}

}
