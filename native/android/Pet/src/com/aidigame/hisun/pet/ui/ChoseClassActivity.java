package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.ClassListViewAdapter;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.UiUtil;

public class ChoseClassActivity extends Activity {
	ListView listView;
	ArrayList<String> list;
	ClassListViewAdapter adapter;
	TextView tv;
	int classs;
	int race;
	boolean choseRace=false;
	String classsName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_chose_class);
		tv=(TextView)findViewById(R.id.textView1);
		listView=(ListView)findViewById(R.id.listView1);
		addClassName();
		adapter=new ClassListViewAdapter(this, list);
		listView.setAdapter(adapter);
		initListener();
		
	}

	public void addClassName(){
		list=new ArrayList<String>();
		list.add("喵星");
		list.add("汪星");
		list.add("其他星");
	}
	private void initListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(choseRace){
					choseRace(position);
					choseRace=false;
				}else{
					choseClass(position);
					choseRace=true;
				}
			}

		});
	}
	/**
	 * 选择种族  喵星或汪星
	 * @param position
	 */
	private void choseClass(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
            classsName=list.get(position);
			classs=2;
			tv.setVisibility(View.INVISIBLE);
			addCatRaceName();
			break;
		case 1:
			classsName=list.get(position);
			classs=1;
			tv.setVisibility(View.INVISIBLE);
			addDogRaceName();
			break;
		case 2:
			
			break;
		}
		adapter.update(list);
		adapter.notifyDataSetChanged();
	}
	/**
	 * 选择 类别     如 萨摩耶犬
	 * @param position
	 */
	private void choseRace(int position) {
		// TODO Auto-generated method stub
		//TODO   界面跳转 确认选择  
		Intent intent=getIntent();
		intent.putExtra("classs", classs);
		intent.putExtra("race", position);
		intent.putExtra("classsName", classsName);
		intent.putExtra("raceName", list.get(position));
		setResult(RESULT_OK,intent);
		this.finish();
	}
	private void addCatRaceName() {
		// TODO Auto-generated method stub
		list=new ArrayList<String>();

		list.add("布偶猫");
		list.add("波斯猫");
		list.add("挪威猫");
		list.add("缅因猫");
		list.add("伯曼猫");
		list.add("索马利猫");
		list.add("土耳其梵猫");
		list.add("美国短尾猫");
		list.add("西伯利亚森林猫");
		list.add("巴厘猫");
		list.add("土耳其安哥拉猫");
		list.add("褴褛猫");
		list.add("拉邦猫");
		list.add("暹罗猫");
		list.add("苏格兰折耳猫");
		list.add("短毛猫");
		list.add("俄罗斯蓝猫");
		list.add("孟买猫");
		list.add("埃及猫");
		list.add("斯芬克斯猫");
		list.add("缅甸猫");
		list.add("阿比西尼亚猫");
		list.add("新加坡猫");
		list.add("中国狸花猫");
		list.add("日本短尾猫");
		list.add("东奇尼猫");
		list.add("卷毛猫");
		list.add("马恩岛猫");
		list.add("奥西猫");
		list.add("沙特尔猫");
		list.add("呵叻猫");
		list.add("美国刚毛猫");
		list.add("哈瓦那棕猫");
		list.add("波米拉猫");
		list.add("东方猫");
		list.add("混血");
		list.add("猫咪");
	}

	private void addDogRaceName() {
		// TODO Auto-generated method stub
		list=new ArrayList<String>();
		
		list.add("吉娃娃");
		list.add("博美犬");
		list.add("马尔济斯犬");
		list.add("约克夏梗");
		list.add("贵宾犬");
		list.add("蝴蝶犬");
		list.add("八哥犬");
		list.add("西施犬");
		list.add("比熊犬");
		list.add("北京犬");
		list.add("迷你杜宾");
		list.add("拉萨犬");
		list.add("冠毛犬");
		list.add("小型雪纳瑞");
		list.add("柯基犬");
		list.add("巴吉度犬");
		list.add("哈士奇");
		list.add("松狮");
		list.add("牧羊犬");
		list.add("柴犬");
		list.add("斗牛犬");
		list.add("萨摩耶犬");
		list.add("腊肠犬");
		list.add("猎兔犬");
		list.add("惠比特犬");
		list.add("拉布拉多");
		list.add("大麦町犬（斑点狗）");
		list.add("爱斯基摩犬");
		list.add("沙皮犬");
		list.add("山地犬");
		list.add("无毛犬");
		list.add("雪纳瑞");
		list.add("藏獒");
		list.add("史毕诺犬");
		list.add("卡斯罗");
		list.add("罗威纳犬");
		list.add("阿拉斯加雪橇犬");
		list.add("金毛");
		list.add("柯利犬");
		list.add("波尔多犬");
		list.add("法国狼犬");
		list.add("雪达犬");
		list.add("奇努克犬");
		list.add("威玛犬");
		list.add("比利时马林诺斯犬");
		list.add("寻回犬");
		list.add("浣熊犬");
		list.add("迦南犬");
		list.add("猎犬");
		list.add("梗犬");
		list.add("牧羊犬");
		list.add("混血");
		list.add("狗狗");

	}
}
