package com.aidigame.hisun.imengstar.adapter;

import java.util.ArrayList;

import com.aidigame.hisun.imengstar.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EarnMoneyTaskListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Integer> tasks;
	public EarnMoneyTaskListAdapter(Context context,ArrayList<Integer> tasks){
		this.context=context;
		this.tasks=tasks;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_user_center_earn_money, null);
			holder=new Holder();
			holder.typeIv=(ImageView)convertView.findViewById(R.id.type_iv);
			holder.desTv=(TextView)convertView.findViewById(R.id.des_tv);
			holder.numTv=(TextView)convertView.findViewById(R.id.gold_num_tv);
			holder.resultTv=(TextView)convertView.findViewById(R.id.result_tv);
			convertView.setTag(holder);
		}else{
			holder=(Holder)convertView.getTag();
		}
		int type=tasks.get(position);
		switch (type) {
		case 0:
			holder.typeIv.setImageResource(R.drawable.quest_like);
			holder.desTv.setText("点赞20次");
			holder.numTv.setText("+15");
			holder.resultTv.setText("");
			holder.resultTv.setBackgroundResource(R.drawable.mark_claimed);
			break;
		case 1:
			holder.typeIv.setImageResource(R.drawable.quest_comment);
			holder.desTv.setText("评论10次");
			holder.numTv.setText("+20");
			holder.resultTv.setText("");
			holder.resultTv.setBackgroundResource(R.drawable.bt_claim);
			break;
		case 2:
			holder.typeIv.setImageResource(R.drawable.quest_share);
			holder.desTv.setText("分享1次");
			holder.numTv.setText("+5");
			holder.resultTv.setText("未完成");
			holder.resultTv.setBackgroundDrawable(null);;
			break;
		case 3:
			holder.typeIv.setImageResource(R.drawable.quest_food);
			holder.desTv.setText("赏粮20次");
			holder.numTv.setText("+20");
			holder.resultTv.setText("未完成");
			holder.resultTv.setBackgroundDrawable(null);;
			break;
		}
		return convertView;
	}
	class Holder{
		ImageView typeIv;
		TextView desTv,numTv,resultTv;
	}

}
