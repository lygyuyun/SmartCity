package com.tourcool.core.impl;

import android.accounts.AccountsException;
import android.accounts.NetworkErrorException;

import com.frame.library.core.log.TourCooLogUtil;
import com.tourcool.core.MyApplication;
import com.frame.library.core.control.HttpRequestControl;
import com.frame.library.core.control.IHttpRequestControl;
import com.frame.library.core.control.OnHttpRequestListener;
import com.frame.library.core.manager.LoggerManager;
import com.frame.library.core.util.NetworkUtil;
import com.frame.library.core.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.smartcity.R;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import retrofit2.HttpException;

/**
 * @Author: JenkinsZhou on 2018/12/4 18:08
 * @E-Mail: 971613168@qq.com
 * @Function: 网络请求成功/失败全局处理
 * @Description:
 */
@SuppressWarnings("unchecked")
public class HttpRequestControlImpl implements HttpRequestControl {
    private static final int REFRESH_DELAY = 50;
    private static String TAG = "HttpRequestControlImpl";
    public static final String TOKEN_FAILED = "invalid_token";

    @Override
    public void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<?> list, OnHttpRequestListener listener) {
        if (httpRequestControl == null) {
            return;
        }
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
        int size = httpRequestControl.getPageSize();

        LoggerManager.i(TAG, "smartRefreshLayout:" + smartRefreshLayout + ";adapter:" + adapter + ";status:" + ";page:" + page + ";class:" + httpRequestControl.getRequestClass());
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh(REFRESH_DELAY);
        }
        if (adapter == null) {
            return;
        }
        adapter.loadMoreComplete();
        if (list == null || list.size() == 0) {
            //第一页没有
            if (page == 0 || page == 1) {
                adapter.setNewData(new ArrayList());
                statusLayoutManager.showEmptyLayout();
                if (listener != null) {
                    listener.onEmpty();
                }
            } else {
                adapter.loadMoreEnd();
                if (listener != null) {
                    listener.onNoMore();
                }
            }
            return;
        }
        statusLayoutManager.showSuccessLayout();
        if (smartRefreshLayout != null && (smartRefreshLayout.getState() == RefreshState.Refreshing || page == 0 || page == 1)) {
            adapter.setNewData(new ArrayList());
        }
        adapter.addData(list);
        if (listener != null) {
            listener.onNext();
        }
        if (list.size() < size) {
            adapter.loadMoreEnd();
            if (listener != null) {
                listener.onNoMore();
            }
        }
    }

    @Override
    public void httpRequestError(IHttpRequestControl httpRequestControl, Throwable e) {
//        LoggerManager.e(TAG, "httpRequestError:" + e.getMessage());
        TourCooLogUtil.e(TAG, "httpRequestError:" + e.getMessage());
        int reason = R.string.fast_exception_other_error;
//        int code = FastError.EXCEPTION_OTHER_ERROR;
        if (!NetworkUtil.isConnected(MyApplication.getContext())) {
            reason = R.string.fast_exception_network_not_connected;
        } else {
            //网络异常--继承于AccountsException
            if (e instanceof NetworkErrorException) {
                reason = R.string.fast_exception_network_error;
                //账户异常
            } else if (e instanceof AccountsException) {
                reason = R.string.fast_exception_accounts;
                //连接异常--继承于SocketException
            } else if (e instanceof ConnectException) {
                reason = R.string.fast_exception_connect;
                //socket异常
            } else if (e instanceof SocketException) {
                reason = R.string.fast_exception_socket;
                // http异常
            } else if (e instanceof HttpException) {
                if (e.getMessage().contains("401")) {
                    reason = R.string.fast_exception_accounts;
                } else {
                    reason = R.string.fast_exception_http;
                }
                //DNS错误
            } else if (e instanceof UnknownHostException) {
                reason = R.string.fast_exception_unknown_host;
            } else if (e instanceof JsonSyntaxException
                    || e instanceof JsonIOException
                    || e instanceof JsonParseException) {
                //数据格式化错误
                reason = R.string.fast_exception_json_syntax;
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                reason = R.string.fast_exception_time_out;
            } else if (e instanceof ClassCastException) {
                reason = R.string.fast_exception_class_cast;
            }
        }
        if (httpRequestControl == null || httpRequestControl.getStatusLayoutManager() == null) {
            if (TourCooUtil.getNotNullValue(e.getMessage()).contains(TOKEN_FAILED)) {
                ToastUtil.show("登录过期");
            } else {
                ToastUtil.show(reason);
            }

          /*  if (reason == R.string.fast_exception_http) {
                ToastUtil.show(e.toString());
            }else*/
            return;
        }
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh(REFRESH_DELAY, false, false);
        }
        if (adapter != null) {
            adapter.loadMoreComplete();
            if (statusLayoutManager == null) {
                return;
            }
            if (!NetworkUtil.isConnected(MyApplication.getContext())) {
                statusLayoutManager.showCustomLayout(R.layout.common_status_layout_no_network, R.id.llNoNetwok);
                return;
            }
            //初始页
            if (page == 0 || page == 1) {
//                if (!NetworkUtil.isConnected(MyApplication.getContext())) {
//                    //可自定义网络错误页面展示
//                    statusLayoutManager.showCustomLayout(R.layout.layout_status_layout_manager_error);
//                } else {
                statusLayoutManager.showErrorLayout();
//                }
                return;
            }
            //可根据不同错误展示不同错误布局  showCustomLayout(R.layout.xxx);
            statusLayoutManager.showErrorLayout();
        }
    }
}
