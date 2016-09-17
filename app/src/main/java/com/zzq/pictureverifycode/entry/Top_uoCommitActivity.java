package com.zzq.pictureverifycode.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rentalcarapp.R;
import com.example.sqzc.config.Utils;
import com.example.sqzc.tool.StatusBar;
import com.example.sqzc.top_up.adapter.Top_upCommitBean;
import com.example.sqzc.top_up.adapter.Top_upListBean;
import com.example.sqzc.top_up.adapter.TopupCommitAdapter;
import com.example.sqzc.view.Dialog_topup;
import com.example.sqzc.view.MyListView;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * 确认支付储值卡页面
 *
 * @author potter-lee
 */
public class Top_uoCommitActivity extends Activity implements OnClickListener {
    private TextView title;
    private ImageView top_right;
    private RelativeLayout back;
    private LinearLayout mLl_weixin_pay_topup, mLl_alipay_topup,
            mLl_unionpay_topup;
    private TextView mTv_topup_rule, mTv_all_num, mTv_all_amount;
    ArrayList<String> mTopupComList = new ArrayList<String>();
    Dialog dialog;
    CheckBox mCheckBox_topup_rule;
    boolean istouch = true;
    private String GenerateOrderUrl;
    private MyListView mLv_topup_commit;
    private List<Top_upListBean> mTopupData;
    private ArrayList<Top_upListBean> commitTopupMylist = new ArrayList<Top_upListBean>();

    private List<Top_upCommitBean> orderData = new ArrayList<>();


    // private ArrayList<Top_upCommitBean> datalist;
    JSONObject orderDetails = new JSONObject();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_uo_commit);
        GenerateOrderUrl = Utils.getValue(Top_uoCommitActivity.this, "urls")
                + "/CustomersJson.asmx/GenerateOrder";

        // 沉浸式状态栏
        TextView status = (TextView) findViewById(R.id.status);
        new StatusBar().statusBar(status, Top_uoCommitActivity.this);

        Intent intent = this.getIntent();
        mTopupData = (ArrayList<Top_upListBean>) intent
                .getSerializableExtra("myTopupMylist");
        for (int i = 0; i < mTopupData.size(); i++) {
            if (mTopupData.get(i).getCardCount() > 0) {
                commitTopupMylist.add(mTopupData.get(i));
            }
        }

        try {
            init();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GenerateOrder();
    }

    private void init() throws JSONException {
        title = (TextView) findViewById(R.id.title);
        title.setText("购买充值卡");
        back = (RelativeLayout) findViewById(R.id.back);
        top_right = (ImageView) findViewById(R.id.top_right);
        mLl_weixin_pay_topup = (LinearLayout) findViewById(R.id.ll_weixin_pay_topup);
        mLl_alipay_topup = (LinearLayout) findViewById(R.id.ll_alipay_topup);
        mLl_unionpay_topup = (LinearLayout) findViewById(R.id.ll_unionpay_topup);
        mLv_topup_commit = (MyListView) findViewById(R.id.Lv_topup_commit);
        mTv_topup_rule = (TextView) findViewById(R.id.tv_topup_rule);
        mCheckBox_topup_rule = (CheckBox) findViewById(R.id.checkBox_topup_rule);
        mTv_all_num = (TextView) findViewById(R.id.tv_all_num);
        mTv_all_amount = (TextView) findViewById(R.id.tv_all_amount);
        back.setOnClickListener(this);
        top_right.setOnClickListener(this);
        mLl_weixin_pay_topup.setOnClickListener(this);
        mLl_alipay_topup.setOnClickListener(this);
        mLl_unionpay_topup.setOnClickListener(this);
        mTv_topup_rule.setOnClickListener(this);
        mCheckBox_topup_rule
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            istouch = true;
                        } else {
                            istouch = false;
                        }
                    }
                });

        int allNum = 0;
        int allAmount = 0;


        for (int i = 0; i < commitTopupMylist.size(); i++) {
            allNum += commitTopupMylist.get(i).getCardCount();
            String amount = commitTopupMylist.get(i).getDiscountAmount();
            int iAmount = Integer.parseInt(amount.substring(0,
                    amount.length() - 3));
            allAmount += iAmount * commitTopupMylist.get(i).getCardCount();
            // //遍历 把数组添加到orderDetails
            // orderDetails.put("CradValue", commitTopupMylist.get(i)
            // .getCardValue());
            // orderDetails.put("DiscountAmount", commitTopupMylist.get(i)
            // .getDiscountAmount());
            // orderDetails.put("CardCount", commitTopupMylist.get(i)
            // .getCardCount());
            Top_upCommitBean orderDetail = new Top_upCommitBean(
                    commitTopupMylist.get(i).getCardValue(), commitTopupMylist
                    .get(i).getDiscountAmount(), commitTopupMylist.get(
                    i).getCardCount());
            orderData.add(orderDetail);
        }
//		String sg = new Gson().toJson(orderData);
        orderDetails.put("orderDetails", orderData);

        mTv_all_num.setText(allNum + "");
        mTv_all_amount.setText(allAmount + "");

        Log.e("orderDetails", orderDetails.toString());
        setAdapter();
    }

    private void setAdapter() {
        // TODO Auto-generated method stub
        TopupCommitAdapter mAdapter = new TopupCommitAdapter(commitTopupMylist,
                this);
        mLv_topup_commit.setAdapter(mAdapter);
    }

    /**
     * 生成订单号
     */
    private void GenerateOrder() {
        RequestParams params = new RequestParams();

        Top_upCommitInfo info = new Top_upCommitInfo();
        Top_upCommitInfo.OrderForm orderForm = new Top_upCommitInfo.OrderForm();
        orderForm.setChannelName("APP");
        orderForm.setMemberID(Utils.getValue(this, "Mobile"));
        orderForm.setMemberPhone(Utils.getValue(this, "CustomerCode"));
        info.setOrderDetails(orderData);
        info.setOrderForm(orderForm);


        Log.e("jsonString", jsonString.toString());
        params.addBodyParameter("reserved", "");
        params.addBodyParameter("jsonString", new Gson().toJson(info));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_alipay_topup:
                if (istouch == false) {
                    Utils.showShortToast(Top_uoCommitActivity.this, "请阅读并勾选购卡条款");
                    return;
                }
                break;
            case R.id.ll_weixin_pay_topup:
                if (istouch == false) {
                    Utils.showShortToast(Top_uoCommitActivity.this, "请阅读并勾选购卡条款");
                    return;
                }
                break;
            case R.id.ll_unionpay_topup:
                if (istouch == false) {
                    Utils.showShortToast(Top_uoCommitActivity.this, "请阅读并勾选购卡条款");
                    return;
                }
                break;
            case R.id.tv_topup_rule:

                dialog = new Dialog_topup(this, R.style.customDialog,
                        R.layout.dialog_topup_rule, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        switch (v.getId()) {
                            case R.id.back_image:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.top_right:
                // 调用手机的拨号功能
                Intent call = new Intent();
                call.setAction(Intent.ACTION_DIAL); // android.intent.action.DIAL
                call.setData(Uri.parse("tel:10105678"));
                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call);
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        TCAgent.onResume(this);
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("SplashScreen");
    }

    public void onPause() {
        super.onPause();
        TCAgent.onPause(this);
        MobclickAgent.onPause(this);
        MobclickAgent.onPageStart("SplashScreen");
    }
}
