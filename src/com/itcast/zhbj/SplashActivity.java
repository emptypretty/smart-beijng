package com.itcast.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.itcast.zhbj.utils.PrefUtils;

/**
 * 闪屏页
 * 
 * @author Administrator
 * 
 */
public class SplashActivity extends Activity {

	private RelativeLayout rl_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		rl_root = (RelativeLayout) findViewById(R.id.rl_root);

		// 旋转
		RotateAnimation animation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);// 基于自身中心点旋转360度
		animation.setDuration(1000);// 动画时间
		animation.setFillAfter(true);// 保持住动画结束的状态

		// 缩放
		ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animScale.setDuration(1000);
		animScale.setFillAfter(true);

		// 渐变
		AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
		animAlpha.setDuration(1000);
		animAlpha.setFillAfter(true);

		// 动画集合
		AnimationSet set = new AnimationSet(false);
		set.addAnimation(animation);
		set.addAnimation(animScale);
		set.addAnimation(animAlpha);

		rl_root.setAnimation(set);

		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 判断有没有展示过引导页

				boolean isGuideShow = PrefUtils.getBoolean(
						getApplicationContext(), "is_guide_show", false);

				if (!isGuideShow) {
					// 跳到新手引导页
					startActivity(new Intent(getApplicationContext(),
							GuideActivity.class));
				} else {
					// 跳到主页面
					startActivity(new Intent(getApplicationContext(),
							MainActivity.class));
				}

				finish();
			}
		});

	}
}
