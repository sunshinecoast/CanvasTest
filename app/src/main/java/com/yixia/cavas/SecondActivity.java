package com.yixia.cavas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhaoxiaopo on 2018/7/4.
 */
public class SecondActivity extends FragmentActivity {

    private VerticalViewPager viewpager;
    int[] color = {R.color.colorAccent, R.color.colorPrimary, R.color.questionnaire_answer_color};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        viewpager = findViewById(R.id.viewpager);

        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(container.getContext(), R.layout.view_pager_item, null);
                view.findViewById(R.id.rootview).setBackgroundColor(getColor(color[position % 3]));
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                Log.i("scrollpager", "destroyItem ------->>>    " + position);
            }
        });

        ((ViewPager) viewpager).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
                Log.i("scrollpager", position + "      " + positionOffset + "      " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("scrollpager", "position ------->>>    " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("scrollpager", "state ------->>>    " + state);
            }
        });

        viewpager.getChildAt(viewpager.getCurrentItem());
    }
}
