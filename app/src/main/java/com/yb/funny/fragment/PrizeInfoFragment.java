package com.yb.funny.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yb.funny.R;
import com.yb.funny.entity.Prize;
import com.yb.funny.util.Constant;
import com.yb.funny.util.IntegralUtil;
import com.yb.funny.util.LoginUser;
import com.yb.funny.view.FlowRadioGroup;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 积分兑换专区   提示充值成功有错误
 * Created by Marven on 2015/12/27.
 */
public class PrizeInfoFragment extends Fragment {
    private View view;
    private FlowRadioGroup radioGroup;
    private List<Prize> list;
    private int prizetype;
    private Prize prize;
    private Button ok;
    private EditText number;


    public PrizeInfoFragment(int prizetype) {
        this.prizetype = prizetype;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_prizeinfo, container, false);
        number = (EditText) view.findViewById(R.id.prize_number);
        ok = (Button) view.findViewById(R.id.prize_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUser.getInstance().getUser().getIntegral() < prize.getRequiredpoints()) {
                    Toast.makeText(x.app(), "您的积分不足！", Toast.LENGTH_LONG).show();
                }else {
                RequestParams params = new RequestParams(Constant.URI + "order");
                params.setMultipart(true);
                params.addBodyParameter("method", "add");
                params.addBodyParameter("ordertype", prize.getPrizetype() + "");
                if (number.getText().toString().length() != 11) {
                    Toast.makeText(x.app(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                } else {
                    params.addBodyParameter("phonenumber", number.getText().toString());
                }
                params.addBodyParameter("orderperson", LoginUser.getInstance().getUser().getUserid() + "");
                params.addBodyParameter("orderamount", prize.getPrizeamount() + "");
                params.addBodyParameter("starttime", getTime());
                params.addBodyParameter("status", 1 + "");
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        int row = Integer.parseInt(s);
                        Message msg = new Message();
                        msg.what = row;
                        handler.sendMessage(msg);
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
            }
        });
        radioGroup = (FlowRadioGroup) view.findViewById(R.id.prize_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton tempButton = (RadioButton)group.findViewById(checkedId);
                prize = (Prize) tempButton.getTag();
            }
        });

        getPrize(prizetype);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
                number.setText("");
                Toast.makeText(x.app(), "充值成功，请等待充值到账！", Toast.LENGTH_LONG).show();
                IntegralUtil.lessintegral(prize.getRequiredpoints(),LoginUser.getInstance().getUser().getUserid());
            return true;
        }
    });


    private void getPrize(final int prizetype){
        RequestParams params = new RequestParams(Constant.URI + "prize");
        params.setMultipart(true);
        params.addBodyParameter("method", "get");
        params.addBodyParameter("prizetype",prizetype + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                list =  JSONArray.parseArray(s,Prize.class);

                for (Prize prize : list) {
                    RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                            RadioGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 0, 10, 0);//设置边距
                    RadioButton button = new RadioButton(getActivity());
                    button.setTag(prize);
                    button.setButtonDrawable(android.R.color.transparent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        button.setBackground(getResources().getDrawable(R.drawable.radio));
                    }
                    button.setGravity(Gravity.CENTER);
                    button.setPadding(10, 10, 10, 10);
                    button.setTextSize(20);
                    if (prizetype == 1){
                        button.setText(prize.getPrizeamount()+"元"+"\n"+prize.getRequiredpoints()+"积分");
                    }else {
                        button.setText(prize.getPrizeamount()+"M"+"\n"+prize.getRequiredpoints()+"积分");
                    }
                    radioGroup.addView(button,lp);
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

    private String getTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
