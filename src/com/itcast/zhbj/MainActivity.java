package com.itcast.zhbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.itcast.zhbj.fragment.ContentFragment;
import com.itcast.zhbj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页面
 * 
 * 1.当一个activity要展示fragment的话，必须继承FragmentActivity 2.使用FragmentManager进行替换
 * 3.将现有布局掏空，根布局建议使用FrameLayout 4.开始事务，进行替换操作，并提交
 * 
 * 
 * 解决v4冲突问题： 当导入SlidingMenu 由于SlidingMenu具有v4包，zhbj也有v4包，有时候会冲突
 * 解决办法：删除zhbj的v4包，以SlidingMenu的为准 注意：如果build path 中有报错，应该移除该报错的包，系统会自动编译
 */
public class MainActivity extends SlidingFragmentActivity {

	private static final String TAG_CONTENT = "TAG_CONTENT";
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 设置侧边栏布局
		setBehindContentView(R.layout.left_menu);

		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
		slidingMenu.setBehindOffset(240);// 屏幕预留300px

		initFragment();
	}

	// 初始化Fragment
	private void initFragment() {
		// 获取fragment管理器
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// 开始一个事务
		transaction
				.replace(R.id.fl_content, new ContentFragment(), TAG_CONTENT);
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				TAG_LEFT_MENU);
		// 使用fragment替换现有布局，参1：当前布局的id；参2；要替换的fragment.参3：打一个标记，方便以后找到该Fragment对象

		transaction.commit();
		// 通过tag找到fragment
		// ContentFragment fragment = (ContentFragment)
		// fm.findFragmentByTag(TAG_CONTENT);
	}
}
