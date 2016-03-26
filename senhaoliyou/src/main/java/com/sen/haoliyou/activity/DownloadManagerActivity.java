package com.sen.haoliyou.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.mozillaonline.providers.DownloadManager;
import com.sen.haoliyou.R;
import com.sen.haoliyou.fragment.DownloadedFragment;
import com.sen.haoliyou.fragment.DownloadingFragment;
import com.sen.haoliyou.tools.ResourcesUtils;

import java.util.ArrayList;
import java.util.List;


public class DownloadManagerActivity extends FragmentActivity implements OnClickListener{


	AppCompatButton btnDonwnloading;
	AppCompatButton btnDownloaded;
	
	/**
	 * 作为页面容器的ViewPager
	 */
	ViewPager mViewPager;
	/**
	 * 页面集合
	 */
	List<Fragment> fragmentList;
	
	/**
	 * 四个Fragment（页面）
	 */
	DownloadingFragment downloadingFragment;
	DownloadedFragment downloadedFragment;
	
	//覆盖层
	
	//屏幕宽度
	int screenWidth;
	//当前选中的项
	int currenttab=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DownloadManager mDownloadManager = new DownloadManager(
				getContentResolver(),getPackageName());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download_manager);
		btnDonwnloading=(AppCompatButton)findViewById(R.id.btn_donwnloading);
		btnDownloaded=(AppCompatButton)findViewById(R.id.btn_downloaded);
		findViewById(R.id.down_imgbtn_close).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
			}
		});
		
		 
		
		btnDonwnloading.setOnClickListener(this);
		btnDownloaded.setOnClickListener(this);
		
		mViewPager=(ViewPager) findViewById(R.id.viewpager);
		
		fragmentList=new ArrayList<Fragment>();
		downloadingFragment=new DownloadingFragment();
		downloadedFragment=new DownloadedFragment();
		
		fragmentList.add(downloadingFragment);
		fragmentList.add(downloadedFragment);
		
		
		
		
		mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
		
		changeIndexColor(mViewPager.getCurrentItem());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				changeIndexColor(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	
	private void changeIndexColor(int item) {

		switch (item) {
		case 0:
			btnDonwnloading.setTextColor(ResourcesUtils.getResColor(this,R.color.tab_bgcolor));
			btnDownloaded.setTextColor(ResourcesUtils.getResColor(this,R.color.font_h1));
			btnDonwnloading.setBackgroundColor(ResourcesUtils.getResColor(this,R.color.theme_color));
			btnDownloaded.setBackgroundColor(ResourcesUtils.getResColor(this,R.color.tab_bgcolor));
			break;
		case 1:
			btnDonwnloading.setTextColor(ResourcesUtils.getResColor(this,R.color.font_h1));
			btnDownloaded.setTextColor(ResourcesUtils.getResColor(this,R.color.tab_bgcolor));
			btnDonwnloading.setBackgroundColor(ResourcesUtils.getResColor(this,R.color.tab_bgcolor));
			btnDownloaded.setBackgroundColor(ResourcesUtils.getResColor(this,R.color.theme_color));
			break;
		
		}
	}

	

	
	class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter
	{

		public MyFrageStatePagerAdapter(FragmentManager fm) 
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
		/**
		 * 每次更新完成ViewPager的内容后，调用该接口，
		 */
		@Override
		public void finishUpdate(ViewGroup container) 
		{
			super.finishUpdate(container);//这句话要放在最前面，否则会报错
			//获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
			int currentItem=mViewPager.getCurrentItem();
			if (currentItem==currenttab)
			{
				return ;
			}
			currenttab=mViewPager.getCurrentItem();
			
		}
		
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btn_donwnloading:
			changeView(0);
			changeIndexColor(0);
			break;
		case R.id.btn_downloaded:
			changeView(1);
			changeIndexColor(1);
			break;
		
		}
	}
	//手动设置ViewPager要显示的视图
	private void changeView(int desTab)
	{
		mViewPager.setCurrentItem(desTab, true);
	}


}
