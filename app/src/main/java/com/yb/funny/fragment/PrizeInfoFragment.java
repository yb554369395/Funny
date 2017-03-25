package com.yb.funny.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONArray;
import com.yb.funny.R;
import com.yb.funny.entity.Prize;
import com.yb.funny.util.Constant;
import com.yb.funny.view.FlowRadioGroup;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.List;


/**
 * 积分兑换专区
 * Created by Yangbin on 2015/12/27.
 */
public class PrizeInfoFragment extends Fragment {

    public interface OnPrizeSelectedListener {
        void onPrizeSeletced(Prize prize);
    }

    OnPrizeSelectedListener listener;

    private View view;
    private FlowRadioGroup radioGroup;
    private List<Prize> list;
    private int prizetype;
    private Prize prize;


    public PrizeInfoFragment() {
        this.prizetype = 1;
    }


    public PrizeInfoFragment(int prizetype) {
        this.prizetype = prizetype;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_prizeinfo, container, false);

        radioGroup = (FlowRadioGroup) view.findViewById(R.id.prize_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton tempButton = (RadioButton) group.findViewById(checkedId);
                prize = (Prize) tempButton.getTag();

                listener.onPrizeSeletced(prize);
            }
        });

        getPrize(prizetype);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 加载奖品列表
     *
     * @param prizetype
     */
    private void getPrize(final int prizetype) {
        RequestParams params = new RequestParams(Constant.URI + "prize");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("prizetype", prizetype + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                list = JSONArray.parseArray(s, Prize.class);

                for (Prize prize : list) {
                    RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 5, 10, 5);//设置边距
                    RadioButton button = new RadioButton(getActivity());
                    button.setTag(prize);
                    button.setButtonDrawable(android.R.color.transparent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        button.setBackground(getResources().getDrawable(R.drawable.radio));
                    }
                    button.setGravity(Gravity.CENTER);
                    button.setPadding(10, 10, 10, 10);
                    button.setTextSize(20);
                    if (prizetype == 1) {
                        button.setText(prize.getPrizeamount() + "元" + "\n" + prize.getRequiredpoints() + "积分");
                    } else {
                        button.setText(prize.getPrizeamount() + "M" + "\n" + prize.getRequiredpoints() + "积分");
                    }
                    radioGroup.addView(button, lp);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnPrizeSelectedListener) activity;
    }
}
