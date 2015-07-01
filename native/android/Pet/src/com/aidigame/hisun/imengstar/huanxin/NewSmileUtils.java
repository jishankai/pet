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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aidigame.hisun.imengstar.R;
import com.aidigame.hisun.imengstar.util.LogUtil;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;



public class NewSmileUtils {
	public static final String ee_1 = "[啊哒哒]";
	public static final String ee_2 = "[伐开心]";
	public static final String ee_3 = "[唱曲儿]";
	public static final String ee_4 = "[不会吧]";
	public static final String ee_5 = "[泪奔ing]";
	public static final String ee_6 = "[不要啦]";
	public static final String ee_7 = "[呃呵呵]";
	public static final String ee_8 = "[Emm]";
	public static final String ee_9 = "[小恶魔]";
	public static final String ee_10 = "[抛媚眼]";
	public static final String ee_11 = "[扶额]";
	public static final String ee_12 = "[苦笑]";
	public static final String ee_13 = "[哈哈]";
	public static final String ee_14 = "[笑尿e]";
	public static final String ee_15 = "[黑线条]";
	public static final String ee_16 = "[哼哼哼]";
	public static final String ee_17 = "[捂嘴]";
	public static final String ee_18 = "[惊呆]";
	public static final String ee_19 = "[震惊]";
	public static final String ee_20 = "[好囧]";
	public static final String ee_21 = "[e抗议]";
	public static final String ee_22 = "[可爱]";
	public static final String ee_23 = "[吹口哨]";
	public static final String ee_24 = "[哭泣]";
	public static final String ee_25 = "[打招呼]";
	public static final String ee_26 = "[好凌乱]";
	public static final String ee_27 = "[腹黑]";
	public static final String ee_28 = "[气炸]";
	public static final String ee_29 = "[庆祝]";
	public static final String ee_30 = "[卖萌啦]";
	public static final String ee_31 = "[闪光中]";
	public static final String ee_32 = "[生气]";
	public static final String ee_33 = "[吗鬼]";
	public static final String ee_34 = "[酱紫a]";
	public static final String ee_35 = "[偷笑]";
	public static final String ee_36 = "[无辜d]";
	public static final String ee_37 = "[捂脸]";
	public static final String ee_38 = "[无语了]";
	public static final String ee_39 = "[笑哭]";
	public static final String ee_40 = "[喜欢]";
	public static final String ee_41 = "[着迷]";
	public static final String ee_42 = "[星眼]";
	public static final String ee_43 = "[再见]";
	public static final String ee_44 = "[吃惊]";
	
	
	
	private static final Factory spannableFactory = Spannable.Factory
	        .getInstance();
	
	public static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();
	public static ArrayList<String> tags;

	static {
		
	    addPattern(emoticons, ee_1, R.drawable.adada);
	    addPattern(emoticons, ee_2, R.drawable.bukaixin);
	    addPattern(emoticons, ee_3, R.drawable.changge);
	    addPattern(emoticons, ee_4, R.drawable.chijing);
	    addPattern(emoticons, ee_5, R.drawable.daku);
	    addPattern(emoticons, ee_6, R.drawable.duqi);
	    addPattern(emoticons, ee_7, R.drawable.ehehe);
	    addPattern(emoticons, ee_8, R.drawable.em);
	    addPattern(emoticons, ee_9, R.drawable.emo);
	    addPattern(emoticons, ee_10, R.drawable.fangdian);
	    addPattern(emoticons, ee_11, R.drawable.fuehan);
	    addPattern(emoticons, ee_12, R.drawable.fuexiao);
	    addPattern(emoticons, ee_13, R.drawable.haha);
	    addPattern(emoticons, ee_14, R.drawable.hahaha);
	    addPattern(emoticons, ee_15, R.drawable.heixian);
	    addPattern(emoticons, ee_16, R.drawable.hengheng);
	    addPattern(emoticons, ee_17, R.drawable.hohoho);
	    addPattern(emoticons, ee_18, R.drawable.jingran);
	    addPattern(emoticons, ee_19, R.drawable.jingya);
	    addPattern(emoticons, ee_20, R.drawable.jiong);
	    addPattern(emoticons, ee_21, R.drawable.kangyi);
	    addPattern(emoticons, ee_22, R.drawable.keai);
	    addPattern(emoticons, ee_23, R.drawable.koushao);
	    addPattern(emoticons, ee_24, R.drawable.kuqi);
	    addPattern(emoticons, ee_25, R.drawable.laia);
	    addPattern(emoticons, ee_26, R.drawable.lingluan);
	    addPattern(emoticons, ee_27, R.drawable.niwanle);
	    addPattern(emoticons, ee_28, R.drawable.nu);
	    addPattern(emoticons, ee_29, R.drawable.qingzhu);
	    addPattern(emoticons, ee_30, R.drawable.rouyan);
	    addPattern(emoticons, ee_31, R.drawable.shanguang);
	    addPattern(emoticons, ee_32, R.drawable.shengqi);
	    addPattern(emoticons, ee_33, R.drawable.shenmegui);
	    addPattern(emoticons, ee_34, R.drawable.soga);
	    addPattern(emoticons, ee_35, R.drawable.touxiao);
	    addPattern(emoticons, ee_36, R.drawable.wugu);
	    addPattern(emoticons, ee_37, R.drawable.wulian);
	    addPattern(emoticons, ee_38, R.drawable.wuyu);
	    addPattern(emoticons, ee_39, R.drawable.xiaozhongdailei);
	    addPattern(emoticons, ee_40, R.drawable.xihuan);
	    addPattern(emoticons, ee_41, R.drawable.zhaomi);
	    addPattern(emoticons, ee_42, R.drawable.xingxing);
	    addPattern(emoticons, ee_43, R.drawable.zaijian);
	    addPattern(emoticons, ee_44, R.drawable.zenmezheyang);
	    tags=new ArrayList<String>();
	    tags.add(ee_1);
	    tags.add(ee_2);
	    tags.add(ee_3);
	    tags.add(ee_4);
	    tags.add(ee_5);
	    tags.add(ee_6);
	    tags.add(ee_7);
	    tags.add(ee_8);
	    tags.add(ee_9);
	    tags.add(ee_10);
	    tags.add(ee_11);
	    tags.add(ee_12);
	    tags.add(ee_13);
	    tags.add(ee_14);
	    tags.add(ee_15);
	    tags.add(ee_16);
	    tags.add(ee_17);
	    tags.add(ee_18);
	    tags.add(ee_19);
	    tags.add(ee_20);
	    tags.add(ee_21);
	    tags.add(ee_22);
	    tags.add(ee_23);
	    tags.add(ee_24);
	    tags.add(ee_25);
	    tags.add(ee_26);
	    tags.add(ee_27);
	    tags.add(ee_28);
	    tags.add(ee_29);
	    tags.add(ee_30);
	    tags.add(ee_31);
	    tags.add(ee_32);
	    tags.add(ee_33);
	    tags.add(ee_34);
	    tags.add(ee_35);
	    tags.add(ee_36);
	    tags.add(ee_37);
	    tags.add(ee_38);
	    tags.add(ee_39);
	    tags.add(ee_40);
	    tags.add(ee_41);
	    tags.add(ee_42);
	    tags.add(ee_43);
	    tags.add(ee_44);
	    LogUtil.i("mi"," tags.add(ee_44);" );
	}

	private static void addPattern(Map<Pattern, Integer> map, String smile,
	        int resource) {
	    map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	/**
	 * replace existing spannable with smiles
	 * @param context
	 * @param spannable
	 * @return
	 */
	public static boolean addSmiles(Context context, Spannable spannable) {
	    boolean hasChanges = false;
	    for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(),
	                    matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                spannable.setSpan(new ImageSpan(context, entry.getValue(),DynamicDrawableSpan.ALIGN_BASELINE),
	                        matcher.start(), matcher.end(),
	                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }
	        }
	    }
	    return hasChanges;
	}
	public static int getSmiles(Context context, String text) {
		Spannable spannable = spannableFactory.newSpannable(text);
	    boolean hasChanges = false;
	    for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(),
	                    matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                spannable.setSpan(new ImageSpan(context, entry.getValue()),
	                        matcher.start(), matcher.end(),
	                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	                return entry.getValue();
	            }
	        }
	    }
	    return 0;
	}

	public static Spannable getSmiledText(Context context, CharSequence text) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmiles(context, spannable);
	    return spannable;
	}
	
	public static boolean containsKey(String key){
		boolean b = false;
		for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(key);
	        if (matcher.find()) {
	        	b = true;
	        	break;
	        }
		}
		
		return b;
	}
	
	
	
}
