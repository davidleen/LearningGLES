package com.opengles.book;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.*;
import android.widget.*;
import com.opengles.book.galaxy.GalaxyGame;
import com.opengles.book.screen.*;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.opengles.book.screen.ShadowTest.ShadowScreen;
import com.opengles.book.screen.cubeCollisionDemo.CubeCollisionDemoScreen;
import com.opengles.book.screen.dollDemo.DollDemoScreen;
import com.opengles.book.screen.snooker.SnookerScreen;
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


        info = new MenuInfo("立方体碰撞检测", CubeCollisionDemoScreen.class.getName());

        datas.add(info);

        info = new MenuInfo("人偶展示", DollDemoScreen.class.getName());

        datas.add(info);

        info = new MenuInfo("老鹰动画", FlyingEagerScreen.class.getName());

        datas.add(info);


        info = new MenuInfo("桌球游戏", SnookerScreen.class.getName());

        datas.add(info);



        info = new MenuInfo("阴影映射测试", ShadowScreen.class.getName());

        datas.add(info);


        info = new MenuInfo("测试fbo",  null);

        datas.add(info);


		MenuAdapter adapter = new MenuAdapter(this, datas);

		setListAdapter(adapter);






	}
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("运动治疗");
        contextMenu.add(0, 0, 0, "完成");
    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id=info.position;


        switch (item.getItemId()) {
            case 0:
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_LONG).show();

                break;
        }
        return super.onContextItemSelected(item);
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
