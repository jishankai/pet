/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aidigame.hisun.imengstar.huanxin;

import java.util.List;
import java.util.regex.Pattern;

import com.aidigame.hisun.imengstar.R;
import com.aidigame.hisun.imengstar.util.StringUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class ExpressionAdapter extends ArrayAdapter<String>{
	int mode=0;//0,环信聊天；1，发表评论
    Context context;
	public ExpressionAdapter(Context context, int textViewResourceId, List<String> objects,int mode) {
		super(context, textViewResourceId, objects);
		this.mode=mode;
		this.context=context;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(getContext(), R.layout.row_expression, null);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
		
		String filename = getItem(position);
		int resId = getContext().getResources().getIdentifier(filename, "drawable", getContext().getPackageName());
		if(mode==0){
			imageView.setImageResource(resId);
		}else{
			if(!StringUtil.isEmpty(filename))
			imageView.setImageResource(NewSmileUtils.getSmiles(context, filename));
		}
		
		
		return convertView;
	}
	
}
