package com.frame.library.core.basis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;

import com.frame.library.core.UiManager;
import com.frame.library.core.control.ActivityDispatchEventControl;
import com.frame.library.core.control.ActivityKeyEventControl;
import com.frame.library.core.control.IBaseView;
import com.frame.library.core.control.IFrameRefreshLoadView;
import com.frame.library.core.control.QuitAppControl;
import com.frame.library.core.util.StackUtil;
import com.frame.library.core.util.TourCooUtil;
import com.frame.library.core.manager.RxJavaManager;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.widget.dialog.FrameLoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.simple.eventbus.EventBus;

import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: JenkinsZhou on 2018/7/13 17:38
 * @E-Mail: 971613168@qq.com
 * Function: 所有Activity的基类
 * Description:
 * 1、2018-6-15 09:31:42 调整滑动返回类控制
 * 2、2018-6-20 17:15:12 调整主页back键操作逻辑
 * 3、2018-6-21 14:05:57 删除滑动返回控制改由全局控制
 * 4、2018-6-22 13:38:32 删除NavigationViewHelper控制方法改由全局控制
 * 5、2018-6-25 13:25:30 增加解决StatusLayoutManager与SmartRefreshLayout冲突解决方案
 * 6、2018-9-25 10:04:31 新增onActivityResult统一处理逻辑
 * 7、2018-9-26 16:59:59 新增按键监听统一处理
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IBaseView {

    protected Activity mContext;
    protected View mContentView;
    protected Unbinder mUnBinder;
    protected FrameLoadingDialog loadingDialog;
    protected Bundle mSavedInstanceState;
    protected boolean mIsViewLoaded = false;
    protected boolean mIsFirstShow = true;
    protected boolean mIsFirstBack = true;
    protected long mDelayBack = 2000;
    protected String TAG = getClass().getSimpleName();
    private QuitAppControl mQuitAppControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isEventBusEnable()) {
            EventBus.getDefault().register(this);
        }
        super.onCreate(savedInstanceState);
        this.mSavedInstanceState = savedInstanceState;
        mContext = this;
        beforeSetContentView();
        mContentView = View.inflate(mContext, getContentLayout(), null);
        //解决StatusLayoutManager与SmartRefreshLayout冲突
        if (this instanceof IFrameRefreshLoadView) {
            if (TourCooUtil.isClassExist("com.scwang.smartrefresh.layout.SmartRefreshLayout")) {
                if (mContentView.getClass() == SmartRefreshLayout.class) {
                    FrameLayout frameLayout = new FrameLayout(mContext);
                    if (mContentView.getLayoutParams() != null) {
                        frameLayout.setLayoutParams(mContentView.getLayoutParams());
                    }
                    frameLayout.addView(mContentView);
                    mContentView = frameLayout;
                }
            }
        }
        setContentView(mContentView);
        mUnBinder = ButterKnife.bind(this);
        mIsViewLoaded = true;
        beforeInitView(savedInstanceState);
        initView(savedInstanceState);
        loadingDialog = new FrameLoadingDialog(mContext,"加载中...");
    }

    @Override
    protected void onResume() {
        beforeFastLazyLoad();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (isEventBusEnable()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        mUnBinder = null;
        mContentView = null;
        mContext = null;
        mSavedInstanceState = null;
        mQuitAppControl = null;
        TAG = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if (list == null || list.size() == 0) {
            return;
        }
        for (Fragment f : list) {
            f.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyDown(this, keyCode, event)) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyUp(this, keyCode, event)) {
                return true;
            }
            return super.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            return control.onKeyLongPress(this, keyCode, event);
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyShortcut(this, keyCode, event)) {
                return true;
            }
            return super.onKeyShortcut(keyCode, event);
        }
        return super.onKeyShortcut(keyCode, event);

    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        ActivityKeyEventControl control = UiManager.getInstance().getActivityKeyEventControl();
        if (control != null) {
            if (control.onKeyMultiple(this, keyCode, repeatCount, event)) {
                return true;
            }
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchTouchEvent(this, ev)) {
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchGenericMotionEvent(this, ev)) {
                return true;
            }
            return super.dispatchGenericMotionEvent(ev);
        }
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchKeyEvent(this, event)) {
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchKeyShortcutEvent(this, event)) {
                return true;
            }
            return super.dispatchKeyShortcutEvent(event);
        }
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchTrackballEvent(this, ev)) {
                return true;
            }
            return super.dispatchTrackballEvent(ev);
        }
        return super.dispatchTrackballEvent(ev);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        ActivityDispatchEventControl control = UiManager.getInstance().getActivityDispatchEventControl();
        if (control != null) {
            if (control.dispatchPopulateAccessibilityEvent(this, event)) {
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent(event);
        }
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        if (UiManager.getInstance().getActivityFragmentControl() != null) {
            UiManager.getInstance().getActivityFragmentControl().setContentViewBackground(mContentView, this.getClass());
        }
    }

    private void beforeFastLazyLoad() {
        //确保视图加载及视图绑定完成避免刷新UI抛出异常
        if (!mIsViewLoaded) {
            RxJavaManager.getInstance().setTimer(10)
                    .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onRequestNext(Long entity) {
                            beforeFastLazyLoad();
                        }


                    });
        } else {
            fastLazyLoad();
        }
    }

    private void fastLazyLoad() {
        if (mIsFirstShow) {
            mIsFirstShow = false;
            loadData();
        }
    }

    /**
     * 退出程序
     */
    protected void quitApp() {
        mQuitAppControl = UiManager.getInstance().getQuitAppControl();
        mDelayBack = mQuitAppControl != null ? mQuitAppControl.quipApp(mIsFirstBack, this) : mDelayBack;
        //时延太小或已是第二次提示直接通知执行最终操作
        if (mDelayBack <= 0 || !mIsFirstBack) {
            if (mQuitAppControl != null) {
                mQuitAppControl.quipApp(false, this);
            } else {
                StackUtil.getInstance().exit();
            }
            return;
        }
        //编写逻辑
        if (mIsFirstBack) {
            mIsFirstBack = false;
            RxJavaManager.getInstance().setTimer(mDelayBack)
                    .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(new BaseObserver<Long>() {
                        @Override
                        public void onRequestNext(Long entity) {
                            mIsFirstBack = true;
                        }
                    });
        }
    }
}
