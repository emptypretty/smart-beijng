package com.itcast.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
	private static SharedPreferences sp;
	private static final String SHARE_PREFS_NAME = "incast";

	// 1,存储boolean变量方法
	public static void putBoolean(Context ctx, String key, boolean value) {
		// name存储文件名称
		if (sp == null) {
			sp = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	// 2,读取boolean变量方法
	public static boolean getBoolean(Context ctx, String key, boolean defValue) {
		// name存储文件名称
		if (sp == null) {
			sp = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}

	public static void putString(Context ctx, String key, String value) {
		// name存储文件名称
		if (sp == null) {
			sp = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context ctx, String key, String defValue) {
		// name存储文件名称
		if (sp == null) {
			sp = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}

	/**
	 * @param ctx
	 * @param key
	 *            需要移除节点的名称
	 */
	public static void remove(Context ctx, String key) {
		// TODO Auto-generated method stub
		if (sp == null) {
			sp = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		sp.edit().remove(key).commit();
	}

	// 1,存储boolean变量方法
	public static void putInt(Context ctx, String key, int value) {
		// name存储文件名称
		if (sp == null) {
			sp = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}

	// 2,读取boolean变量方法
	public static int getInt(Context ctx, String key, int defValue) {
		// name存储文件名称
		if (sp == null) {
			sp = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defValue);
	}
}
