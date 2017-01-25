package com.yb.funny.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.yb.funny.R;
import com.yb.funny.entity.User;
import com.yb.funny.fragment.PrizeInfoFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/1/22.
 */

@ContentView(R.layout.activity_prize)
public class PrizeActivity extends AppCompatActivity{


    /**
     * 选项卡下划线长度
     */
    private static int lineWidth = 0;

    /**
     * 偏移量
     *		 （手机屏幕宽度/3-选项卡长度）/2
     */
    private static int offset = 0;

    /**
     * 选项卡总数
     */
    private static final int TAB_COUNT = 2;
    /**
     * 当前显示的选项卡位置
     */
    private int current_index = 0;

    @ViewInject(R.id.prize_pager)
    private ViewPager vPager;
    @ViewInject(R.id.prize_tab1)
    private TextView bill;
    @ViewInject(R.id.prize_tab2)
    private TextView flow;
    @ViewInject(R.id.prize_name)
    private TextView name;
    @ViewInject(R.id.prize_integral)
    private TextView integral;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        User user = (User) getIntent().getSerializableExtra("user");
        initActivity(user);


        bill.setOnClickListener(new MyOnClickListener(0));
        flow.setOnClickListener(new MyOnClickListener(1));
        final TextView[] titles = {bill,flow};



        vPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount()
            {
                return TAB_COUNT;
            }

            @Override
            public Fragment getItem(int index)//直接创建fragment对象并返回
            {
                switch (index)
                {
                    case 0:
                        return new PrizeInfoFragment(1);
                    case 1:
                        return new PrizeInfoFragment(2);
                }
                return null;
            }
        });
        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            int one = offset * 2 + lineWidth;// 页卡1 -> 页卡2 偏移量
            @Override
            public void onPageSelected(int index)//设置标题的颜色以及下划线的移动效果
            {
                Animation animation = new TranslateAnimation(one*current_index,one*index, 0,0);
                animation.setFillAfter(true);
                animation.setDuration(300);
                titles[current_index].setTextColor(Color.BLACK);
                titles[index].setTextColor(Color.RED);
                current_index = index;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int index)
            {
            }
        });
        vPager.setCurrentItem(0);
        titles[1].setTextColor(Color.BLACK);
        titles[0].setTextColor(Color.RED);
    }


    private void initActivity(User user){
        name.setText(user.getName());
        integral.setText("积分："+user.getIntegral());
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener{
        private int index = 0 ;
        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            vPager.setCurrentItem(index);
        }
    }
}
