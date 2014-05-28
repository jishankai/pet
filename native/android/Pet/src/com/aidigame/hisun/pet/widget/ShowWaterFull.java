package com.aidigame.hisun.pet.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.waterfull.FlowTag;
import com.aidigame.hisun.pet.waterfull.FlowView;
import com.aidigame.hisun.pet.waterfull.LazyScrollView;
import com.aidigame.hisun.pet.waterfull.LazyScrollView.OnScrollListener;

public class ShowWaterFull {
	HomeActivity activity;
	LinearLayout parent;
	View waterfullView;
	
	
	
	//�ٲ���
		private LazyScrollView waterfall_scroll;
		private LinearLayout waterfall_container;
		private ArrayList<LinearLayout> waterfall_items;
		private Display display;
		private AssetManager asset_manager;
		private List<String> image_filenames;
		private final String image_path = "images";
		private Handler handler;
		private int item_width;

		private int column_count = 2;// ��ʾ����
		private int page_count = 15;// ÿ�μ���30��ͼƬ

		private int current_page = 0;// ��ǰҳ��

		private int[] topIndex;
		private int[] bottomIndex;
		private int[] lineIndex;
		private int[] column_height;// ÿ�еĸ߶�

		private HashMap<Integer, String> pins;

		private int loaded_count = 0;// �Ѽ�������

		private HashMap<Integer, Integer>[] pin_mark = null;

		private Context context;

		private HashMap<Integer, FlowView> iviews;
		private int range;
		int scroll_height;
		
		boolean halfHeight=true;
		int halfHeightCount=0;
	public ShowWaterFull(HomeActivity context,LinearLayout parent){
		this.activity=context;
		this.parent=parent;
		intiView();
	}
	private void intiView() {
		// TODO Auto-generated method stub
		waterfullView=LayoutInflater.from(activity).inflate(R.layout.widget_waterfull, null);
		parent.removeAllViews();
		parent.addView(waterfullView);
		
		
		
		//�ٲ���
				display = activity.getWindowManager().getDefaultDisplay();
				item_width = display.getWidth() / column_count;// ������Ļ��С����ÿ�д�С
				asset_manager = activity.getAssets();

				column_height = new int[column_count];
				context = activity;
				iviews = new HashMap<Integer, FlowView>();
				pins = new HashMap<Integer, String>();
				pin_mark = new HashMap[column_count];

				this.lineIndex = new int[column_count];
				this.bottomIndex = new int[column_count];
				this.topIndex = new int[column_count];

				for (int i = 0; i < column_count; i++) {
					lineIndex[i] = -1;
					bottomIndex[i] = -1;
					pin_mark[i] = new HashMap();
				}

				InitLayout();
	}
	//�ٲ���
		private void InitLayout() {
			waterfall_scroll = (LazyScrollView)waterfullView.findViewById(R.id.waterfall_scroll);
			range = waterfall_scroll.computeVerticalScrollRange();//

			waterfall_scroll.getView();
			waterfall_scroll.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onTop() {
					// ���������
					Log.d("LazyScroll", "Scroll to top");
				}

				@Override
				public void onScroll() {

				}

				@Override
				public void onBottom() {
					// ��������Ͷ�
					AddItemToContainer(++current_page, page_count);
				}

				@Override
				public void onAutoScroll(int l, int t, int oldl, int oldt) {

					// Log.d("MainActivity",
					// String.format("%d  %d  %d  %d", l, t, oldl, oldt));

					// Log.d("MainActivity", "range:" + range);
					// Log.d("MainActivity", "range-t:" + (range - t));
					scroll_height = waterfall_scroll.getMeasuredHeight();
					Log.d("MainActivity", "scroll_height:" + scroll_height);

					if (t > oldt) {// ���¹���
						if (t > 2 * scroll_height) {// ��������Ļ��

							for (int k = 0; k < column_count; k++) {

								LinearLayout localLinearLayout = waterfall_items
										.get(k);

								if (pin_mark[k].get(Math.min(bottomIndex[k] + 1,
										lineIndex[k])) <= t + 3 * scroll_height) {// ��ײ���ͼƬλ��С�ڵ�ǰt+3*��Ļ�߶�

									((FlowView) waterfall_items.get(k)
											.getChildAt(
													Math.min(1 + bottomIndex[k],
															lineIndex[k])))
											.Reload();

									bottomIndex[k] = Math.min(1 + bottomIndex[k],
											lineIndex[k]);

								}
								Log.d("MainActivity",
										"headIndex:" + topIndex[k]
												+ "  footIndex:" + bottomIndex[k]
												+ "  headHeight:"
												+ pin_mark[k].get(topIndex[k]));
								if (pin_mark[k].get(topIndex[k]) < t - 2
										* scroll_height) {// δ����ͼƬ�����λ��<t-������Ļ�߶�

									int i1 = topIndex[k];
									topIndex[k]++;
									((FlowView) localLinearLayout.getChildAt(i1))
											.recycle();
									Log.d("MainActivity", "recycle,k:" + k
											+ " headindex:" + topIndex[k]);

								}
							}

						}
					} else {// ���Ϲ���

						for (int k = 0; k < column_count; k++) {
							LinearLayout localLinearLayout = waterfall_items.get(k);
							if (pin_mark[k].get(bottomIndex[k]) > t + 3
									* scroll_height) {
								((FlowView) localLinearLayout
										.getChildAt(bottomIndex[k])).recycle();

								bottomIndex[k]--;
							}

							if (pin_mark[k].get(Math.max(topIndex[k] - 1, 0)) >= t
									- 2 * scroll_height) {
								((FlowView) localLinearLayout.getChildAt(Math.max(
										-1 + topIndex[k], 0))).Reload();
								topIndex[k] = Math.max(topIndex[k] - 1, 0);
							}
						}

					}

				}
			});

			waterfall_container = (LinearLayout) waterfullView
					.findViewById(R.id.waterfall_container);
			handler = new Handler() {

				@Override
				public void dispatchMessage(Message msg) {

					super.dispatchMessage(msg);
				}

				@Override
				public void handleMessage(Message msg) {

					// super.handleMessage(msg);

					switch (msg.what) {
					case 1:

						FlowView v = (FlowView) msg.obj;
						int w = msg.arg1;
						int h = msg.arg2;
						// Log.d("MainActivity",
						// String.format(
						// "��ȡʵ��View�߶�:%d,ID��%d,columnIndex:%d,rowIndex:%d,filename:%s",
						// v.getHeight(), v.getId(), v
						// .getColumnIndex(), v.getRowIndex(),
						// v.getFlowTag().getFileName()));
						String f = v.getFlowTag().getFileName();

						// �˴�������ֵ
						int columnIndex = GetMinValue(column_height);

						v.setColumnIndex(columnIndex);

						column_height[columnIndex] += h;

						pins.put(v.getId(), f);
						iviews.put(v.getId(), v);
						waterfall_items.get(columnIndex).addView(v);

						lineIndex[columnIndex]++;

						pin_mark[columnIndex].put(lineIndex[columnIndex],
								column_height[columnIndex]);
						bottomIndex[columnIndex] = lineIndex[columnIndex];
						break;
					}

				}

				@Override
				public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
					return super.sendMessageAtTime(msg, uptimeMillis);
				}
			};

			waterfall_items = new ArrayList<LinearLayout>();

			for (int i = 0; i < column_count; i++) {
				LinearLayout itemLayout = new LinearLayout(activity);
				LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(
						item_width, LayoutParams.WRAP_CONTENT);

				

				if(i==0){
					itemLayout.setPadding(0, 0, 2, 0);
				}else{
					itemLayout.setPadding(2, 0, 0, 0);
				}
				itemLayout.setOrientation(LinearLayout.VERTICAL);
				itemLayout.setLayoutParams(itemParam);
				waterfall_items.add(itemLayout);
				waterfall_container.addView(itemLayout);
				
			}

			// ��������ͼƬ·��

			try {
				image_filenames = Arrays.asList(asset_manager.list(image_path));

			} catch (IOException e) {
				e.printStackTrace();
			}
			// ��һ�μ���
			AddItemToContainer(current_page, page_count);
		}

		private void AddItemToContainer(int pageindex, int pagecount) {
			int currentIndex = pageindex * pagecount;

			int imagecount = (pageindex+1)*pagecount;// image_filenames.size();
			/*for (int i = currentIndex; i < pagecount * (pageindex + 1)
					&& i < imagecount; i++) {*/
			for (int i = currentIndex; i < image_filenames.size()
						&& i < imagecount; i++) {
				loaded_count++;
				Random rand = new Random();
//				int r = rand.nextInt(image_filenames.size());
				int r = i;
				AddImage(image_filenames.get(r),
						(int) Math.ceil(loaded_count / (double) column_count),
						loaded_count);
			}

		}

		private void AddImage(String filename, int rowIndex, int id) {

			FlowView item = new FlowView(context);
			// item.setColumnIndex(columnIndex);
            item.setPadding(0, 0, 0, 4);
			item.setRowIndex(rowIndex);
			item.setId(id);
			item.setViewHandler(this.handler);
			item.setScaleType(ScaleType.FIT_XY);
			// ���̲߳���
			FlowTag param = new FlowTag();
			param.setFlowId(id);
			param.setAssetManager(asset_manager);
			param.setFileName(image_path + "/" + filename);
			param.setItemWidth(item_width);
			
			//FIX By SCX
			param.halfHeight=halfHeight;
			if(halfHeight){
				halfHeight=false;
			}else{
				halfHeight=true;
			}
			halfHeightCount++;
			if(halfHeightCount%2==0){
				halfHeight=!halfHeight;
				param.halfHeight=halfHeight;
			}

			item.setFlowTag(param);
			item.LoadImage();
			// waterfall_items.get(columnIndex).addView(item);

		}

		private int GetMinValue(int[] array) {
			int m = 0;
			int length = array.length;
			for (int i = 0; i < length; ++i) {

				if (array[i] < array[m]) {
					m = i;
				}
			}
			return m;
		}

}
