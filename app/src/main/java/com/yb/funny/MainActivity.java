package com.yb.funny;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yb.funny.fragment.ResourceFragment;
import com.yb.funny.fragment.PrizeFragment;
import com.yb.funny.fragment.FragmentParent3;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.tbHeadBar)
    Toolbar mTbHeadBar;


    /*侧滑菜单ListView放置菜单项*/
    @ViewInject(R.id.lvMenu)
    ListView mLvMenu;


    @ViewInject(R.id.drawerLayout)
    DrawerLayout mMyDrawable;

    ActionBarDrawerToggle mToggle;


    private List<Fragment> imageList = new ArrayList<Fragment>() {{
        add(new ResourceFragment());
        add(new PrizeFragment());
        add(new FragmentParent3());
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

  /*初始化Toolbar与DrawableLayout*/
        initToolBarAndDrawableLayout();

        mLvMenu.setAdapter(new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.menu_array)));
        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              addFragmentToStack(imageList.get(position));
                mMyDrawable.closeDrawers();/*收起抽屉*/

            }
        });
    }

    private void initToolBarAndDrawableLayout() {
        setSupportActionBar(mTbHeadBar);
  /*以下俩方法设置返回键可用*/
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  /*设置标题文字不可显示*/
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToggle = new ActionBarDrawerToggle(this, mMyDrawable, mTbHeadBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(MainActivity.this, R.string.open, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //Toast.makeText(MainActivity.this, R.string.close, Toast.LENGTH_SHORT).show();
            }
        };



  /*mMyDrawable.setDrawerListener(mToggle);不推荐*/
        mMyDrawable.addDrawerListener(mToggle);
        mToggle.syncState();/*同步状态*/

        addFragmentToStack(imageList.get(0));

    }

    private void addFragmentToStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }
}