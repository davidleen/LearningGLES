package com.opengles.book;

import java.util.ArrayList;
import java.util.List;

import com.opengles.book.galaxy.GalaxyGame;
import com.opengles.book.screen.*;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.opengles.book.testFBO.TestFboActivity;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		List<MenuInfo> datas = new ArrayList<MainActivity.MenuInfo>();
		MenuInfo info = new MenuInfo("菜单界面", MainScreen.class.getName());

		datas.add(info);

		info = new MenuInfo("灰度地图天空穹", GrayMapScreen.class.getName());

		datas.add(info);

		info = new MenuInfo("篮球倒影+动态字幕",
				Reflect_BasketBall_Screen.class.getName());

		datas.add(info);

		info = new MenuInfo("树林（标志板）-透明模板", TreeOnDesertScreen.class.getName());

		datas.add(info);

		info = new MenuInfo("Obj文件解析展示", OjObjectScreen.class.getName());

		datas.add(info);


        info = new MenuInfo("Obj文件解析展示(FBO)", OjObjectWithFBOScreen.class.getName());

        datas.add(info);

		info = new MenuInfo("地球星空", GalaxyScreen.class.getName());

		datas.add(info);

        info = new MenuInfo("水晶球", CrystalBallScreen.class.getName());

        datas.add(info);
        
        info = new MenuInfo("粒子系统", ParticleSystemScreen.class.getName());

        datas.add(info);

        info = new MenuInfo("FrameBufferDemo", FrameBufferDemoScreen.class.getName());

        datas.add(info);

        info = new MenuInfo("LightTracing", LightTracingScreen.class.getName());

        datas.add(info);



        info = new MenuInfo("测试fbo",  null);

        datas.add(info);


		MenuAdapter adapter = new MenuAdapter(this, datas);

		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

        MenuInfo menuInfo=	(MenuInfo)l.getItemAtPosition(position);
        Intent intent;
        if(menuInfo.screenClassName==null)
        {

              intent = new Intent(MainActivity.this, TestFboActivity.class);

        }else {


              intent = new Intent(MainActivity.this, GalaxyGame.class);
            intent.putExtra(GalaxyGame.PARAMS_SCREEN_NAME, menuInfo.screenClassName);
        }
		startActivity(intent);
	}

	private class MenuAdapter extends BaseAdapter {
		private List<MenuInfo> datas;
		private Context context;

		public MenuAdapter(Context context, List<MenuInfo> datas) {
			super();
			this.datas = datas;
			this.context = context;
		}

		@Override
		public int getCount() {

			return datas.size();
		}

		@Override
		public MenuInfo getItem(int id) {

			return datas.get(id);
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(int itemId, View convertView, ViewGroup viewGroup) {

			if (convertView == null) {
				TextView tv = new TextView(context);
				tv.setTextSize(22);
				tv.setPadding(5, 5, 5, 5);
				convertView=tv;
			}

			TextView tv = (TextView) convertView;
			tv.setText(getItem(itemId).menu);

			return tv;
		}

	}

	private class MenuInfo {
		String menu;
		String screenClassName;

		public MenuInfo(String menu, String screenClassName) {
			super();
			this.menu = menu;
			this.screenClassName = screenClassName;
		}

	}
}
