package com.aidigame.hisun.pet.waterfull1;

import com.aidigame.hisun.pet.http.json.UserImagesJson;

import android.content.res.AssetManager;

public class FlowTag {
	private int flowId;
	public boolean halfHeight;
	private UserImagesJson.Data data;
	public final int what = 1;

	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public UserImagesJson.Data getData() {
		return data;
	}

	public void setData(UserImagesJson.Data data) {
		this.data = data;
	}

	private AssetManager assetManager;
	private int ItemWidth;

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public int getItemWidth() {
		return ItemWidth;
	}

	public void setItemWidth(int itemWidth) {
		ItemWidth = itemWidth;
	}
}
