package com.tourcool.ui.mvp.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.core.module.mvp.BaseMvpTitleActivity;
import com.tourcool.core.module.mvp.BasePresenter;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.smartcity.R;
import com.tourcool.util.DataCleanUtil;

import org.greenrobot.eventbus.EventBus;

import static com.tourcool.core.constant.RouteConstance.ACTIVITY_URL_SETTING;
import static com.tourcool.util.DataCleanUtil.EMPTY_CACHE;
import static com.tourcool.util.DataCleanUtil.clearAllCache;

/**
 * @author :JenkinsZhou
 * @description :系统设置
 * @company :途酷科技
 * @date 2019年10月08日16:56
 * @Email: 971613168@qq.com
 */
@Route(path = ACTIVITY_URL_SETTING)
public class SystemSettingActivity extends BaseMvpTitleActivity implements View.OnClickListener {
    private TextView tvLogout;
    private TextView tvBindPhone;
    private TextView tvCacheSize;
    public static final int REQUEST_CODE_BIND_PHONE = 101;
    public static final String TAG = "";

    @Override
    protected void loadPresenter() {

    }

    @Override
    public void loadData() {
        showCache();
        showBottomButton(AccountHelper.getInstance().isLogin());
        showSetting();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_system_setting;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        super.setTitleBar(titleBar);
        titleBar.setTitleMainText("系统设置");
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.llBindPhone).setOnClickListener(this);
        findViewById(R.id.llClearCache).setOnClickListener(this);
        findViewById(R.id.llEditPhone).setOnClickListener(this);
        tvLogout = findViewById(R.id.tvLogout);
        tvBindPhone = findViewById(R.id.tvBindPhone);
        tvCacheSize = findViewById(R.id.tvCacheSize);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBindPhone:
                if (AccountHelper.getInstance().isLogin()) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, BindPhoneActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_BIND_PHONE);
                } else {
                    skipLogin();
                }
                break;
            case R.id.llClearCache:
                if (getCacheSize().equalsIgnoreCase(EMPTY_CACHE)) {
                    ToastUtil.show("暂无缓存");
                    return;
                }
                cleanCache();
                showCache();
                ToastUtil.showSuccess("清除成功");
                break;
            case R.id.llEditPhone:
                if (!AccountHelper.getInstance().isLogin()) {
                    skipLogin();
                    return;
                }
                Intent editIntent = new Intent();
                editIntent.setClass(mContext, EditPasswordActivityNew.class);
                startActivity(editIntent);
                break;
            case R.id.tvLogout:
                if (AccountHelper.getInstance().isLogin()) {
                    requestLogout();
                } else {
                    skipLogin();
                    finish();
                }
                break;
            default:
                break;
        }
    }


    private void showBottomButton(boolean isLogin) {
        if (isLogin) {
            tvLogout.setText("退出登录");
        } else {
            tvLogout.setText("登录");
        }
    }


    private void showSetting() {
        if (AccountHelper.getInstance().isLogin()) {
            tvBindPhone.setText(AccountHelper.getInstance().getUserInfo().getPhoneNumber());
        }
    }


    private void requestLogout() {
        showLoading("正在退出");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoading();
                AccountHelper.getInstance().logout();
                notifyRefreshUserInfo();
                finish();
            }
        }, 500);

      /*  ApiRepository.getInstance().requestLogout().compose(bindUntilEvent(ActivityEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            AccountHelper.getInstance().logout();
                            notifyRefreshUserInfo();
                            finish();
                        } else {
                            ToastUtil.showFailed(entity.errorMsg);
                        }
                    }
                });*/
    }


    private void skipLogin() {
//        ARouter.getInstance().build(RouteConstance.ACTIVITY_URL_LOGIN).navigation();
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        startActivity(intent);
    }


    private void notifyRefreshUserInfo() {
        UserInfoEvent userInfoEvent = new UserInfoEvent();
        EventBus.getDefault().post(userInfoEvent);
    }


    /**
     * 清空缓存
     */
    private void cleanCache() {
        clearAllCache(mContext);
    }


    private void showCache() {
        TourCooLogUtil.i("缓存大小：" + getCacheSize());
        if (EMPTY_CACHE.equalsIgnoreCase(getCacheSize())) {
            tvCacheSize.setText("");
        } else {
            tvCacheSize.setText(getCacheSize());
        }
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    private String getCacheSize() {
        String str = "";
        try {
            str = DataCleanUtil.getTotalCacheSize(mContext);
        } catch (Exception e) {
            TourCooLogUtil.i("错误信息", e.toString());
            e.printStackTrace();
            return str;
        }
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE_BIND_PHONE == requestCode) {
                showSetting();
            }
        }
    }
}
