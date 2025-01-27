package com.tourcool.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aries.ui.util.StatusBarUtil;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.library.core.log.TourCooLogUtil;
import com.frame.library.core.manager.GlideManager;
import com.frame.library.core.module.fragment.BaseTitleFragment;
import com.frame.library.core.retrofit.BaseLoadingObserver;
import com.frame.library.core.retrofit.BaseObserver;
import com.frame.library.core.threadpool.ThreadPoolManager;
import com.frame.library.core.util.NetworkUtil;
import com.frame.library.core.util.SizeUtil;
import com.frame.library.core.util.StringUtil;
import com.frame.library.core.util.ToastUtil;
import com.frame.library.core.widget.titlebar.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tourcool.adapter.MatrixAdapter;
import com.tourcool.adapter.TwoLevelChildAdapter;
import com.tourcool.bean.MatrixBean;
import com.tourcool.bean.account.AccountHelper;
import com.tourcool.bean.account.UserInfo;
import com.tourcool.bean.screen.Channel;
import com.tourcool.bean.screen.ChildNode;
import com.tourcool.bean.screen.ColumnItem;
import com.tourcool.bean.screen.ScreenEntity;
import com.tourcool.bean.screen.ScreenPart;
import com.tourcool.bean.weather.SimpleWeather;
import com.tourcool.core.MyApplication;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.config.RequestConfig;
import com.tourcool.core.constant.ScreenConstant;
import com.tourcool.core.module.WebViewActivity;
import com.tourcool.core.retrofit.repository.ApiRepository;
import com.tourcool.core.util.DateUtil;
import com.tourcool.core.util.TourCooUtil;
import com.tourcool.event.account.UserInfoEvent;
import com.tourcool.smartcity.R;
import com.tourcool.ui.calender.YellowCalenderDetailActivity;
import com.tourcool.ui.certify.SelectCertifyActivity;
import com.tourcool.ui.citizen_card.CitizenCardTabActivity;
import com.tourcool.ui.constellation.ConstellationListActivity;
import com.tourcool.ui.driver.AgainstScoreQueryActivity;
import com.tourcool.ui.driver.DriverIllegalQueryActivity;
import com.tourcool.ui.express.ExpressQueryActivity;
import com.tourcool.ui.garbage.GarbageQueryActivity;
import com.tourcool.ui.mvp.account.LoginActivity;
import com.tourcool.ui.mvp.search.SearchActivity;
import com.tourcool.ui.mvp.service.SecondaryServiceActivity;
import com.tourcool.ui.mvp.service.ServiceActivity;
import com.tourcool.ui.mvp.weather.WeatherActivity;
import com.tourcool.ui.parking.FastParkingActivity;
import com.tourcool.ui.social.SocialBaseInfoActivity;
import com.tourcool.ui.social.detail.SocialListDetailActivity;
import com.tourcool.util.DeviceUtils;
import com.tourcool.widget.webview.CommonWebViewActivity;
import com.tourcool.widget.webview.WebViewConstant;
import com.trello.rxlifecycle3.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;

import static com.frame.library.core.util.StringUtil.LINE_HORIZONTAL;
import static com.frame.library.core.util.StringUtil.SYMBOL_TEMP;
import static com.tourcool.core.config.RequestConfig.CODE_REQUEST_SUCCESS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CERTIFY_NAME;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_CONSTELLATION;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_DRIVER_AGAINST;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_DRIVER_SCORE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_EXPRESS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_GARBAGE;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_KITCHEN;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_PARKING;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_BASE_INFO;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_BIRTH;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_GS;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER;
import static com.tourcool.core.constant.ItemConstant.ITEM_TYPE_YELLOW_CALENDER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_LINK_INNER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_LINK_OUTER;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_NATIVE;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_NONE;
import static com.tourcool.core.constant.ScreenConstant.CLICK_TYPE_WAITING;
import static com.tourcool.core.constant.ScreenConstant.LAYOUT_STYLE_CONTAINS_SUBLISTS;
import static com.tourcool.core.constant.ScreenConstant.LAYOUT_STYLE_HORIZONTAL_BANNER;
import static com.tourcool.core.constant.ScreenConstant.LAYOUT_STYLE_IMAGE;
import static com.tourcool.core.constant.ScreenConstant.LAYOUT_STYLE_IMAGE_TEXT_LIST;
import static com.tourcool.core.constant.ScreenConstant.LAYOUT_STYLE_VERTICAL_BANNER;
import static com.tourcool.core.constant.ScreenConstant.SUB_CHANNEL;
import static com.tourcool.core.constant.ScreenConstant.SUB_COLUMN;
import static com.tourcool.core.constant.ScreenConstant.TIP_WAIT_DEV;
import static com.tourcool.core.constant.SocialConstant.EXTRA_SOCIAL_TYPE;
import static com.tourcool.core.constant.SocialConstant.TIP_GO_CERTIFY;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_DUO_YUN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_HEAVY_RAIN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_QING;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_RAIN_AND_SNOW;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_RAIN_MIDDLE;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_SMALL_SNOW;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_TOP_RAIN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_XIAO_YU;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_YIN;
import static com.tourcool.core.constant.WeatherConstant.WEATHER_YU;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_RICH_TEXT_ENABLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_TITLE;
import static com.tourcool.widget.webview.WebViewConstant.EXTRA_WEB_VIEW_URL;

/**
 * @author :JenkinsZhou
 * @description :主页
 * @company :途酷科技
 * @date 2019年11月12日9:51
 * @Email: 971613168@qq.com
 */
@SuppressWarnings("unchecked")
public class MainHomeFragment extends BaseTitleFragment implements View.OnClickListener, OnRefreshListener {
    private Handler mHandler = new Handler();
    private SmartRefreshLayout mRefreshLayout;
    private static final String AIR_QUALITY = "空气质量";
    public static final String EXTRA_CLASSIFY_NAME = "EXTRA_CLASSIFY_NAME";
    public static final String EXTRA_FIRST_CHILD_ID = "EXTRA_FIRST_CHILD_ID";
    private LinearLayout llContainer;
    private TextView tvTemperature;
    private TextView tvWeatherDesc;
    private TextView tvAirQuality;
    private TextView tvDate;
    private ImageView ivWeather;
    private List<View> viewList = new ArrayList<>();
    private SparseArray<View> views = new SparseArray<>();
    /**
     * 搜索框高度
     */
    private int searchLayoutHeight;
    /**
     * 天气布局高度
     */
    private int weatherLayoutHeight;
    private RelativeLayout rlWeather;
    RelativeLayout rlSearch;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home_new;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(MainHomeFragment.this)) {
            EventBus.getDefault().register(MainHomeFragment.this);
        }
        initRefresh();
        llContainer = mContentView.findViewById(R.id.llContainer);
        tvTemperature = mContentView.findViewById(R.id.tvTemperature);
        tvWeatherDesc = mContentView.findViewById(R.id.tvWeatherDesc);
        tvAirQuality = mContentView.findViewById(R.id.tvAirQuality);
        tvDate = mContentView.findViewById(R.id.tvDate);
        ivWeather = mContentView.findViewById(R.id.ivWeather);
        TourCooLogUtil.i("设备信息:"+ DeviceUtils.getPhoneDetail());
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {
        initHeader();
        getHomeInfo();
    }

    public static MainHomeFragment newInstance() {
        Bundle args = new Bundle();
        MainHomeFragment fragment = new MainHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void initHeader() {
        //将搜索框下移
        rlSearch = mContentView.findViewById(R.id.rlSearch);
        rlWeather = mContentView.findViewById(R.id.rlWeather);
        RelativeLayout.LayoutParams searchParams = (RelativeLayout.LayoutParams) rlSearch.getLayoutParams();
        int weatherMarginTop = SizeUtil.dp2px(12);
        searchParams.setMargins(0, StatusBarUtil.getStatusBarHeight(), 0, 0);
        rlSearch.setLayoutParams(searchParams);
        rlSearch.post(() -> {
            searchLayoutHeight = rlSearch.getMeasuredHeight();
            TourCooLogUtil.i(TAG, "searchLayoutHeight高度:" + searchLayoutHeight);
        });
        rlSearch.setOnClickListener(this);
        rlWeather.post(() -> {
            weatherLayoutHeight = rlWeather.getMeasuredHeight();
            TourCooLogUtil.i(TAG, "weatherLayoutHeight高度:" + weatherLayoutHeight);
            initContainer(searchLayoutHeight + weatherLayoutHeight + weatherMarginTop + StatusBarUtil.getStatusBarHeight());
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlWeather:
                skipWeather();
                break;
            case R.id.rlSearch:
//                ARouter.getInstance().build(ACTIVITY_URL_SEARCH).navigation();
                Intent intent = new Intent();
                intent.setClass(mContext, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /**
     * 初始化头部高度
     *
     * @param marginTop 顶部边距
     */
    private void initContainer(int marginTop) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContainer.getLayoutParams();
        layoutParams.setMargins(0, marginTop, 0, 0);
        llContainer.setLayoutParams(layoutParams);
    }


    private void loadWeatherInfo(ScreenEntity screenEntity) {
        SimpleWeather weather;
        if (screenEntity == null || screenEntity.getWeather() == null) {
            weather = new SimpleWeather();
            weather.setDate(LINE_HORIZONTAL);
            weather.setTemp(LINE_HORIZONTAL);
            weather.setDate(LINE_HORIZONTAL);
            weather.setQuality(LINE_HORIZONTAL);
            weather.setWeather("");
        } else {
            weather = screenEntity.getWeather();
        }
        //天气描述

        tvWeatherDesc.setText(StringUtil.getNotNullValue(weather.getWeather()));
      /*  switch (StringUtil.getNotNullValue(weather.getWeather())) {
            case WEATHER_DUO_YUN:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_duoyun, ivWeather);
                break;
            case WEATHER_QING:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_day_qing, ivWeather);
                break;
            case WEATHER_YIN:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_yin, ivWeather);
                break;
            case WEATHER_XIAO_YU:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_xiao_yu, ivWeather);
                break;
            default:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_unknown, ivWeather);
                break;
        }*/
        switch ((StringUtil.getNotNullValue(weather.getWeather()))) {
            case WEATHER_QING:
                GlideManager.loadImg(R.mipmap.ic_weather_day_qing, ivWeather);
                break;
            case WEATHER_DUO_YUN:
                GlideManager.loadImg(R.mipmap.ic_weather_duoyun, ivWeather);
                break;
            case WEATHER_YIN:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_yin, ivWeather);
                break;
            case WEATHER_XIAO_YU:
            case WEATHER_YU:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_xiao_yu, ivWeather);
                break;
            case WEATHER_RAIN_AND_SNOW:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_rain_and_snow, ivWeather);
                break;
            case WEATHER_SMALL_SNOW:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_small_snow, ivWeather);
                break;
            case WEATHER_RAIN_MIDDLE:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_middle_snow, ivWeather);
                break;
            //暴雨和雷雨
            case WEATHER_HEAVY_RAIN:
            case WEATHER_TOP_RAIN:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_top_rain, ivWeather);
                break;
            default:
                GlideManager.loadImgCenterInside(R.mipmap.ic_weather_unknown, ivWeather);
                break;
        }

        tvAirQuality.setText(transformAirQuality(weather.getQuality()));
        tvTemperature.setText(transformTemp(weather.getTemp()));
        tvDate.setText(transformDate(weather.getDate()));
        rlWeather.setOnClickListener(this);
    }

    private String transformDate(String date) {
        return "[" + DateUtil.formatDateToMonthAndDaySlash(date) + "]";
    }

    private String transformAirQuality(String air) {
        if (TextUtils.isEmpty(air)) {
            return AIR_QUALITY + ": " + LINE_HORIZONTAL;
        }
        return air.contains(AIR_QUALITY) ? air : AIR_QUALITY + ": " + air;
    }

    private String transformTemp(String temp) {
        if (TextUtils.isEmpty(temp)) {
            return LINE_HORIZONTAL + SYMBOL_TEMP;
        }
        if (temp.lastIndexOf(SYMBOL_TEMP) != -1) {
            return temp;
        }
        return temp + SYMBOL_TEMP;
    }


    private void loadScreenInfo(ScreenEntity screenEntity) {
        if (screenEntity == null || screenEntity.getChildren() == null) {
            ToastUtil.showFailed("未获取到屏幕配置信息");
            return;
        }
        List<ScreenPart> screenPartList = screenEntity.getChildren();
        loadWeatherInfo(screenEntity);
        rlSearch.setOnClickListener(this);
        for (ScreenPart screenPart : screenPartList) {
            if (screenPart == null) {
                continue;
            }
            //这里修复了bug
            screenPart.setColumnName(screenPart.getDetail().getName());
            switch (screenPart.getLayoutStyle()) {
                case LAYOUT_STYLE_IMAGE_TEXT_LIST:
                    //加载矩阵
                    loadMatrix(getMatrixList(screenPart), false, screenPart.getScreenPartId());
                    break;
                case LAYOUT_STYLE_HORIZONTAL_BANNER:
                    //横向banner图
                    loadHorizontalBanner(screenPart, screenPart.getScreenPartId());
                    break;
                case LAYOUT_STYLE_VERTICAL_BANNER:
                    //垂直新闻
                    loadHomeViewFlipperData(screenPart, false, screenPart.getScreenPartId());
                    break;
                case LAYOUT_STYLE_CONTAINS_SUBLISTS:
                    loadSecondList(screenPart, screenPart.getScreenPartId());
                    break;
                case LAYOUT_STYLE_IMAGE:
                    loadImageView(screenPart, screenPart.getScreenPartId());
                    break;
                default:
                    break;
            }
        }
        View lineView = createLineView(1);
        addViewToContainer(lineView);
        viewList.add(lineView);
        View lineView1 = createLineView(2);
        addViewToContainer(lineView1);
        viewList.add(lineView1);
    }


    private void loadMatrix(List<MatrixBean> matrixList, boolean translate, int tag) {
        LinearLayout linearLayout = getViewByViewId(R.id.llMatrix, tag);
        if (linearLayout == null) {
            linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, null);
            cacheView(R.id.llMatrix, tag, linearLayout);
        }
        RecyclerView recyclerView = getViewByViewId(R.id.rvCommon, tag);
        if (recyclerView == null) {
            recyclerView = linearLayout.findViewById(R.id.rvCommon);
            cacheView(R.id.rvCommon, tag, recyclerView);
        }
        if (recyclerView == null) {
            return;
        }
        recyclerView.setBackgroundColor(TourCooUtil.getColor(R.color.whiteCommon));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        params.setMargins(0, SizeUtil.dp2px(10), 0, 0);
        recyclerView.setLayoutParams(params);
        MatrixAdapter adapter = new MatrixAdapter();
        //二级布局为网格布局
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setNewData(matrixList);
        viewList.add(linearLayout);
        addViewToContainer(linearLayout);
        View lineView = createLineView(tag);
        addViewToContainer(lineView);
        viewList.add(lineView);
        recyclerView.setPadding(0, SizeUtil.dp2px(10f), 0, 0);
        if (translate) {
            recyclerView.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
        initMatrixClickListener(adapter);
    }

    private void loadHorizontalBanner(ScreenPart screenPart, int tag) {
        if (screenPart == null || screenPart.getLayoutStyle() != LAYOUT_STYLE_HORIZONTAL_BANNER || screenPart.getChildren() == null) {
            TourCooLogUtil.e(TAG, "loadHorizentalBanner()--->数据不匹配");
            return;
        }
        List<ChildNode> nodeList = screenPart.getChildren();
        List<Channel> channelList = new ArrayList<>();
        Channel channel;
        for (ChildNode childNode : nodeList) {
            boolean disabled = (childNode == null || (childNode.getDetail() == null) || (!childNode.isVisible()) || (!SUB_CHANNEL.equalsIgnoreCase(childNode.getType())));
            if (disabled) {
                continue;
            }
            channel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        if (channelList.isEmpty()) {
            TourCooLogUtil.e(TAG, "banner数据源为空");
            return;
        }
//        List<String> imageList = new ArrayList<>();
        /*for (Channel currentChannel : channelList) {
            if (currentChannel != null) {
                imageList.add(TourCooUtil.getUrl(currentChannel.getIcon()));
            }
        }*/
        LinearLayout linearLayout = getViewByViewId(R.id.llBanner, tag);
        BGABanner bgaBanner = getViewByViewId(R.id.banner, tag);
        if (linearLayout == null) {
            linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_banner, null);
            cacheView(R.id.llBanner, tag, linearLayout);
        }
        if (bgaBanner == null) {
            bgaBanner = linearLayout.findViewById(R.id.banner);
            cacheView(R.id.banner, tag, bgaBanner);
        } else {
            TourCooLogUtil.i("banner已经从缓存中获取");
        }
        bgaBanner.setPadding(SizeUtil.dp2px(10), 0, SizeUtil.dp2px(10), 0);
        bgaBanner.setAdapter((banner, itemView, model, position) -> {
            try {
                ImageView imageView = (ImageView) itemView;
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Channel selectChannel = (Channel) model;
//                GlideManager.loadImgFitCenter(TourCooUtil.getUrl(selectChannel.getIcon()), imageView);
                //设置图片宽高比
                float scale = (float) 750 / (float) 320;
//获取屏幕的宽度
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Point size = new Point();
                wm.getDefaultDisplay().getSize(size);
                int screenWidth = size.x;
//计算BGABanner的应有高度
                int viewHeight = Math.round(screenWidth / scale);
//设置BGABanner的宽高属性
                ViewGroup.LayoutParams banner_params = banner.getLayoutParams();
                banner_params.height = viewHeight;
                banner.setLayoutParams(banner_params);
//此处使用的是glide的override函数直接设置图片尺寸
                GlideManager.loadImgBySize(TourCooUtil.getUrl(selectChannel.getIcon()), imageView, banner_params.width, banner_params.height);

            } catch (Exception e) {
                TourCooLogUtil.e(TAG, "fillBannerItem:" + e.toString());
            }

        });
        bgaBanner.setAutoPlayAble(channelList.size() > 1);
        bgaBanner.setData(channelList, getTips(channelList));
        bgaBanner.setDelegate((banner, itemView, model, position) -> {
            Channel selectChannel = null;
            try {
                selectChannel = (Channel) model;
            } catch (ClassCastException e) {
                TourCooLogUtil.e(TAG, "fillBannerItem:" + e.toString());
            }
            if (selectChannel != null) {
                skipByChannel(selectChannel);
            }
        });
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bgaBanner.getLayoutParams();
        params.height = MyApplication.getImageHeight();
        bgaBanner.setTransitionEffect(TransitionEffect.Default);
        viewList.add(linearLayout);
        addViewToContainer(linearLayout);
    }


    private List<MatrixBean> getMatrixList(ScreenPart screenPart) {
        if (screenPart == null || screenPart.getLayoutStyle() != LAYOUT_STYLE_IMAGE_TEXT_LIST || screenPart.getChildren() == null) {
            TourCooLogUtil.e(TAG, "未匹配到矩阵数据!");
            return null;
        }
        List<MatrixBean> matrixBeanList = new ArrayList<>();
        List<ChildNode> childNodeList = screenPart.getChildren();
        for (ChildNode childNode : childNodeList) {
            if (childNode == null || (!childNode.isVisible()) || childNode.getDetail() == null) {
                continue;
            }
            MatrixBean matrixBean;
            switch (childNode.getType()) {
                case SUB_CHANNEL:
                    matrixBean = convertMatrix(parseJavaBean(childNode.getDetail(), Channel.class));
                    if (screenPart.getDetail() != null && matrixBean != null) {
                        matrixBean.setType(SUB_CHANNEL);
                        matrixBean.setParentsName(screenPart.getDetail().getName());
                        TourCooLogUtil.d(TAG, "screenPart.getDetail().getName()=" + screenPart.getDetail().getName());
                    }

                    if (matrixBean == null) {
                        TourCooLogUtil.e(TAG, "matrixBean==null!");
                        return null;
                    }
                    matrixBeanList.add(matrixBean);
                    break;
                case SUB_COLUMN:
                    matrixBean = convertMatrix(screenPart, childNode, parseJavaBean(childNode.getDetail(), ColumnItem.class));
                    if (matrixBean == null) {
                        TourCooLogUtil.e(TAG, "matrixBean==null!");
                        return null;
                    }
                    matrixBeanList.add(matrixBean);
                    break;
                default:
                    break;
            }
        }
        return matrixBeanList;
    }


    private MatrixBean convertMatrix(ScreenPart screenPart, ChildNode childNode, ColumnItem columnItem) {
        if (screenPart == null) {
            return null;
        }
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setMatrixName(StringUtil.getNotNullValue(columnItem.getName()));
        matrixBean.setMatrixTitle(StringUtil.getNotNullValue(columnItem.getName()));
        matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(columnItem.getIcon()));
        matrixBean.setLink(StringUtil.getNotNullValue(columnItem.getLink()));
        matrixBean.setJumpWay(columnItem.getJumpWay());
        //这里修复了bug
        matrixBean.setColumnName(screenPart.getDetail().getName());
        matrixBean.setParentsName(columnItem.getName());
        matrixBean.setChildren(childNode.getChildren());
//        matrixBean.setParentsName(screenPart.getColumnName());
        if (TextUtils.isEmpty(columnItem.getCircleIcon())) {
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(columnItem.getIcon()));
        } else {
            matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(columnItem.getCircleIcon()));
        }
        matrixBean.setType(SUB_COLUMN);
        matrixBean.setLink(TourCooUtil.getUrl(columnItem.getLink()));
        matrixBean.setId(columnItem.getId());
        return matrixBean;
    }


    /**
     * 加载滚动新闻
     *
     * @param screenPart
     */
    private void loadHomeViewFlipperData(ScreenPart screenPart, boolean translate, int tag) {
        if (screenPart == null || ScreenConstant.LAYOUT_STYLE_VERTICAL_BANNER != screenPart.getLayoutStyle() || screenPart.getChildren() == null) {
            return;
        }
        List<ChildNode> childNodeList = screenPart.getChildren();
        List<Channel> channelList = new ArrayList<>();
        boolean disable;
        Channel channel = null;
        for (ChildNode childNode : childNodeList) {
            disable = childNode == null || childNode.getDetail() == null || (!SUB_CHANNEL.equalsIgnoreCase(childNode.getType()));
            if (disable) {
                continue;
            }
            channel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        if (channelList.isEmpty()) {
            TourCooLogUtil.e(TAG, "channelList为空");
            return;
        }
        View viewFlipperRoot = getViewByViewId(R.id.llViewFlipper, tag);
        if (viewFlipperRoot == null) {
            viewFlipperRoot = LayoutInflater.from(mContext).inflate(R.layout.view_flipper_layout, null);
            cacheView(R.id.llViewFlipper, tag, viewFlipperRoot);
        }
        ImageView ivBulletin = getViewByViewId(R.id.ivBulletin, tag);
        if (ivBulletin == null) {
            ivBulletin = viewFlipperRoot.findViewById(R.id.ivBulletin);
            cacheView(R.id.ivBulletin, tag, ivBulletin);
        }
        View viewLineVertical = getViewByViewId(R.id.viewLineVertical, tag);
        if (viewLineVertical == null) {
            viewLineVertical = viewFlipperRoot.findViewById(R.id.viewLineVertical);
//            views.put(R.id.viewLineVertical, viewLineVertical);
            cacheView(R.id.viewLineVertical, tag, viewLineVertical);
        }
        Drawable defaultDrawable = TourCooUtil.getDrawable(R.mipmap.ic_avatar_default);
        float aspectRatio = (float) defaultDrawable.getIntrinsicHeight() / (float) defaultDrawable.getIntrinsicWidth();
        View finalViewLineVertical = viewLineVertical;
        ImageView finalIvBulletin = ivBulletin;
        GlideManager.loadRoundImgByListener(TourCooUtil.getUrl(screenPart.getDetail().getIcon()), ivBulletin, 5, defaultDrawable, false, new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                TourCooLogUtil.e("图片加载失败", "onLoadFailed:" + e.toString());
                resetViewLayoutParams(aspectRatio, finalViewLineVertical, finalIvBulletin);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                TourCooLogUtil.i("图片加载完成", "图片高度:" + resource.getIntrinsicHeight() + "图片宽度:" + resource.getIntrinsicWidth());
                //图片宽高比
                float aspectRatio = (float) resource.getIntrinsicHeight() / (float) resource.getIntrinsicWidth();
                resetViewLayoutParams(aspectRatio, finalViewLineVertical, finalIvBulletin);
                return false;
            }
        });
        View contentLayout;
//        ImageView ivNewsIcon;
        TextView tvNewsContent;
        ViewFlipper homeViewFlipper = viewFlipperRoot.findViewById(R.id.viewFlipper);
        for (Channel newsBean : channelList) {
            if (newsBean == null || TextUtils.isEmpty(newsBean.getTitle())) {
                continue;
            }
            contentLayout = View.inflate(mContext, R.layout.layout_news, null);
//            ivNewsIcon = contentLayout.findViewById(R.id.icBulletin);
            tvNewsContent = contentLayout.findViewById(R.id.tvNewsContent);
            tvNewsContent.setText(newsBean.getTitle());
            tvNewsContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Channel clickChannel = channelList.get(homeViewFlipper.getDisplayedChild());
                    if (clickChannel == null) {
                        return;
                    }
                    skipByChannel(clickChannel);
                }
            });
            homeViewFlipper.addView(contentLayout);
        }
        viewList.add(viewFlipperRoot);
        View lineView = createLineView(tag);
        viewList.add(lineView);
        addViewToContainer(viewFlipperRoot);
        addViewToContainer(lineView);
        if (translate) {
            llContainer.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }
    }


    /**
     * 创建分割线
     */
    private View createLineView(int tag) {
        View lineView = getViewByViewId(R.id.lLineView, tag);
        if (lineView == null) {
            lineView = LayoutInflater.from(mContext).inflate(R.layout.line_view_verticle_layout, null);
            cacheView(R.id.lLineView, tag, lineView);
            return lineView;
        }
        return lineView;

    }


    private void loadSecondList(ScreenPart screenPart, int tag) {
        boolean illegal = screenPart == null || ScreenConstant.LAYOUT_STYLE_CONTAINS_SUBLISTS != screenPart.getLayoutStyle() || screenPart.getChildren() == null || screenPart.getDetail() == null;
        if (illegal) {
            return;
        }
        List<ChildNode> nodeList = screenPart.getChildren();
        List<Channel> channelList = new ArrayList<>();
        Channel currentChannel;
        for (ChildNode childNode : nodeList) {
            currentChannel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (currentChannel == null || (!childNode.isVisible())) {
                continue;
            }
            currentChannel.setIcon(TourCooUtil.getUrl(currentChannel.getIcon()));
            channelList.add(currentChannel);
        }
        View rootView = getViewByViewId(R.id.llTwoLevel, tag);
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(R.layout.item_two_level_layout, null);
            cacheView(R.id.llTwoLevel, tag, rootView);
        }
        LinearLayout llServiceHeader = getViewByViewId(R.id.llServiceHeader, tag);
        if (llServiceHeader == null) {
            llServiceHeader = rootView.findViewById(R.id.llServiceHeader);
            cacheView(R.id.llServiceHeader, tag, llServiceHeader);
        }

        TextView tvGroupName = getViewByViewId(R.id.tvGroupName, tag);
        if (tvGroupName == null) {
            tvGroupName = rootView.findViewById(R.id.tvGroupName);
            cacheView(R.id.tvGroupName, tag, tvGroupName);
        }

        String groupName = screenPart.getDetail().getName();
        tvGroupName.setText(groupName);
        RecyclerView rvCommonChild = getViewByViewId(R.id.rvCommonChild, tag);
        if (rvCommonChild == null) {
            rvCommonChild = rootView.findViewById(R.id.rvCommonChild);
            cacheView(R.id.rvCommonChild, tag, rvCommonChild);
        }
        TwoLevelChildAdapter adapter = new TwoLevelChildAdapter();
        //二级布局为网格布局
        rvCommonChild.setLayoutManager(new GridLayoutManager(mContext, 2));
        adapter.bindToRecyclerView(rvCommonChild);
        adapter.setNewData(channelList);
        addViewToContainer(rootView);
        viewList.add(rootView);
        View lineView = createLineView(tag);
        addViewToContainer(lineView);
        viewList.add(lineView);
        rootView.setPadding(0, SizeUtil.dp2px(10f), 0, 0);
        setSecondLevelListClickListener(adapter);
        llServiceHeader.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(mContext, ServiceActivity.class);
            //把分组名称传过去
            intent.putExtra(EXTRA_CLASSIFY_NAME, groupName);
            if (!adapter.getData().isEmpty()) {
                //把第一个子条目的id传过去
                intent.putExtra(EXTRA_FIRST_CHILD_ID, adapter.getData().get(0).getId());
            }
            startActivity(intent);
        });
        /*if (translate) {
            llContainer.setBackgroundColor(TourCooUtil.getColor(R.color.transparent));
        }*/
    }

    private void initMatrixClickListener(MatrixAdapter adapter) {
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            MatrixBean matrixBean = (MatrixBean) adapter1.getData().get(position);
            if (matrixBean == null) {
                return;
            }
            switch (TourCooUtil.getNotNullValue(matrixBean.getType())) {
                case SUB_COLUMN:
                    List<Channel> channelList = new ArrayList<>();
                    if (matrixBean.getChildren() != null) {
                        channelList.addAll(parseChannelList(matrixBean.getChildren()));
                    }
                    Intent intent = new Intent();
                    intent.setClass(mContext, SecondaryServiceActivity.class);
                    intent.putExtra("columnName", matrixBean.getColumnName());
                    intent.putExtra("groupName", StringUtil.getNotNullValue(matrixBean.getParentsName()));
                    intent.putExtra("channelList", (Serializable) channelList);
                    TourCooLogUtil.i(TAG, "channelList=" + matrixBean.getColumnName());
                    TourCooLogUtil.i(TAG, "channelList=" + matrixBean.getParentsName());
//                intent.putExtra("secondService", item.getChildren());
                    startActivity(intent);
                    break;
                case SUB_CHANNEL:
                    skipByMatrix(matrixBean);
                    break;
                default:
                    WebViewActivity.start(mContext, TourCooUtil.getUrl(matrixBean.getLink()), true);
                    break;
            }

        });
    }

    private List<Channel> parseChannelList(Object children) {
        List<Channel> channelList = new ArrayList<>();
        String jsonData = JSON.toJSONString(children);
        JSONArray jsonArray = JSON.parseArray(jsonData);
        TourCooLogUtil.i(TAG, jsonArray);
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = (JSONObject) jsonArray.get(i);
            JSONObject detail = (JSONObject) jsonObject.get("detail");
            if (detail == null) {
                continue;
            }
            Channel channel = JSON.parseObject(detail.toJSONString(), Channel.class);
            if (channel != null) {
                channelList.add(channel);
            }
        }
        return channelList;
    }


    private void removeView() {
        for (View view : viewList) {
            llContainer.removeView(view);
        }
        viewList.clear();
    }


    private void loadImageView(ScreenPart screenPart, int tag) {
        if (screenPart == null || screenPart.getChildren() == null || LAYOUT_STYLE_IMAGE != screenPart.getLayoutStyle()) {
            return;
        }
        String imageUrl = "";
        List<ChildNode> childNodeList = screenPart.getChildren();
        LinearLayout linearLayout = getViewByViewId(R.id.llImage, tag);
        if (linearLayout == null) {
            linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_image_layout, null);
            cacheView(R.id.llImage, tag, linearLayout);
        }
        ImageView imageView = getViewByViewId(R.id.imageView, tag);
        if (imageView == null) {
            imageView = linearLayout.findViewById(R.id.imageView);
            cacheView(R.id.imageView, tag, imageView);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.setMargins(SizeUtil.dp2px(15), 0, SizeUtil.dp2px(15), 0);
        imageView.setLayoutParams(layoutParams);
        if (!childNodeList.isEmpty() && childNodeList.get(0) != null) {
            ChildNode childNode = childNodeList.get(0);
            Channel channel = parseJavaBean(childNode.getDetail(), Channel.class);
            if (channel != null) {
                imageUrl = channel.getIcon();
                setImageClickListener(channel, imageView);
            }
        }
        TourCooLogUtil.i(TAG, "图片地址:" + imageUrl);
        GlideManager.loadRoundImg(TourCooUtil.getUrl(imageUrl), imageView, 5, R.mipmap.ic_avatar_default, true);
        addViewToContainer(linearLayout);
        viewList.add(linearLayout);
        addLine(tag);
    }


    private void setImageClickListener(Channel channel, ImageView imageView) {
        if (channel == null || imageView == null) {
            return;
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipByChannel(channel);
            }
        });

    }


    private void resetViewLayoutParams(float aspectRatio, View line, ImageView imageView) {
        TourCooLogUtil.d(TAG, "图片信息：高宽比-->" + aspectRatio);
        //宽度固定
        float finalWidth = SizeUtil.dp2px(58);
        //最终高度
        float finalHeight = finalWidth * aspectRatio;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.height = (int) finalHeight;
        layoutParams.width = (int) finalWidth;
        line.getLayoutParams().height = (int) (finalHeight + SizeUtil.dp2px(10));
        TourCooLogUtil.d(TAG, "图片信息：宽-->" + layoutParams.width);
        TourCooLogUtil.i(TAG, "图片信息：高-->" + layoutParams.height);
        imageView.setLayoutParams(layoutParams);
    }


    private void setSecondLevelListClickListener(TwoLevelChildAdapter adapter) {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Channel channel = (Channel) adapter.getData().get(position);
                if (channel == null) {
                    return;
                }
                skipByChannel(channel);
            }
        });
    }

    private MatrixBean convertMatrix(Channel channel) {
        if (channel == null) {
            return null;
        }
        MatrixBean matrixBean = new MatrixBean();
        matrixBean.setMatrixName(StringUtil.getNotNullValue(channel.getName()));
        matrixBean.setMatrixTitle(channel.getTitle());
        matrixBean.setMatrixIconUrl(TourCooUtil.getUrl(channel.getCircleIcon()));
        matrixBean.setLink(StringUtil.getNotNullValue(channel.getLink()));
        matrixBean.setJumpWay(channel.getJumpWay());
        matrixBean.setType(channel.getType());
        matrixBean.setContent(channel.getContent());
        matrixBean.setRichText(channel.getContent());
        return matrixBean;
    }


    private void getHomeInfo() {
        if (!NetworkUtil.isConnected(mContext)) {
            loadNetErrorView();
            refreshFinish();
            return;
        }
        ApiRepository.getInstance().requestHomeInfo(1).compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseLoadingObserver<BaseResult<Object>>() {
                    @Override
                    public void onRequestError(Throwable e) {
                        super.onRequestError(e);
                        TourCooLogUtil.e(TAG, "onRequestError---->" + e.toString());
                        refreshFinish();
                        loadNoDataView();
                    }

                    @Override
                    public void onRequestNext(BaseResult entity) {
                        handleRequestSuccessCallback(entity);
                    }
                });
    }

    private void handleRequestSuccessCallback(BaseResult<Object> result) {
        mRefreshLayout.finishRefresh();
        if (result == null) {
            ToastUtil.showFailed("服务器数据异常");
            return;
        }
        if (result.status != RequestConfig.CODE_REQUEST_SUCCESS) {
            ToastUtil.showFailed(result.errorMsg);
            return;
        }
        ThreadPoolManager.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                ScreenEntity screenEntity = parseJavaBean(result.data, ScreenEntity.class);
                if (screenEntity == null) {
                    return;
                }
                runMainThread(() -> {
                    //先移除view 防止重复加载
                    removeView();
                    loadScreenInfo(screenEntity);
                });
            }
        });
    }


    /**
     * 切回主线程
     *
     * @param runnable
     */
    private void runMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }


    private void loadNetErrorView() {
        View emptyView = View.inflate(mContext, R.layout.common_status_layout_no_network, null);
        removeAllView();
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHomeInfo();
            }
        });
        addViewToContainer(emptyView);
        viewList.add(emptyView);
    }

    private void removeAllView() {
        llContainer.removeAllViews();
        viewList.clear();
    }

    private void refreshFinish() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getHomeInfo();
    }


    private void loadNoDataView() {
        View emptyView = View.inflate(mContext, R.layout.common_status_layout_empty, null);
        removeAllView();
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHomeInfo();
            }
        });
        addViewToContainer(emptyView);
        viewList.add(emptyView);
    }

    private void initRefresh() {
        mRefreshLayout = mContentView.findViewById(R.id.smartRefreshCommon);
        mRefreshLayout.setOnRefreshListener(this);
        ClassicsHeader header = new ClassicsHeader(mContext).setAccentColor(TourCooUtil.getColor(R.color.white));
        header.setBackgroundColor(TourCooUtil.getColor(R.color.colorPrimary));
        mRefreshLayout.setEnableHeaderTranslationContent(true)
                .setEnableOverScrollDrag(true);
        mRefreshLayout.setRefreshHeader(header);
    }


    private void skipWeather() {
        Intent intent = new Intent();
        intent.setClass(mContext, WeatherActivity.class);
        startActivity(intent);
    }


    private List<String> getTips(List<Channel> channelList) {
        List<String> listTips = new ArrayList<>();
        int size = channelList == null ? 0 : channelList.size();
        for (int i = 0; i < size; i++) {
            listTips.add(StringUtil.getNotNullValue(channelList.get(i).getTitle()));
        }
        return listTips;
    }

    private void skipBrightKitchen() {
        Intent intent = new Intent();
        intent.setClass(mContext, CitizenCardTabActivity.class);
//        intent.setClass(mContext, VideoListActivity.class);
        startActivity(intent);
    }

    private void skipLogin() {
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        startActivity(intent);
    }

    private void skipParking() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, FastParkingActivity.class);
        startActivity(intent);
    }

    private void skipSocialBase() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
            return;
        }
        if (!AccountHelper.getInstance().getUserInfo().isVerified()) {
            ToastUtil.show(TIP_GO_CERTIFY);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, SocialBaseInfoActivity.class);
        startActivity(intent);
    }

    private void skipSocialListDetail(String type) {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
            return;
        }
        if (!AccountHelper.getInstance().getUserInfo().isVerified()) {
            ToastUtil.show(TIP_GO_CERTIFY);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, SocialListDetailActivity.class);
        intent.putExtra(EXTRA_SOCIAL_TYPE, type);
        startActivity(intent);
    }


    private void skipNativeByCondition(String title, String link) {
        switch (StringUtil.getNotNullValue(title)) {
            case ITEM_TYPE_SOCIAL_BASE_INFO:
                skipSocialBase();
                break;
            case ITEM_TYPE_SOCIAL_QUERY_GS:
            case ITEM_TYPE_SOCIAL_QUERY_TAKE_CARE_OLDER:
            case ITEM_TYPE_SOCIAL_QUERY_LOSE_WORK:
            case ITEM_TYPE_SOCIAL_QUERY_BIRTH:
                skipSocialListDetail(title);
                break;
            case ITEM_TYPE_KITCHEN:
                skipBrightKitchen();
                break;
            case ITEM_TYPE_PARKING:
                skipParking();
                break;
            case ITEM_TYPE_CONSTELLATION:
                skipConstellation();
                break;
            case ITEM_TYPE_EXPRESS:
                skipExpress();
                break;
            case ITEM_TYPE_GARBAGE:
                skipGarbage();
                break;
            case ITEM_TYPE_YELLOW_CALENDER:
                skipYellowCalender();
                break;
            case ITEM_TYPE_CERTIFY_NAME:
                skipCertify();
                break;
            case ITEM_TYPE_DRIVER_AGAINST:
                skipDriverAgainst();
                break;
            case ITEM_TYPE_DRIVER_SCORE:
                skipDriverScore();
                break;
            default:
                break;
        }
    }

    /**
     * 星座
     */
    private void skipConstellation() {
        Intent intent = new Intent();
        intent.setClass(mContext, ConstellationListActivity.class);
        startActivity(intent);
    }

    /**
     * 快递物流
     */
    private void skipExpress() {
        Intent intent = new Intent();
        intent.setClass(mContext, ExpressQueryActivity.class);
        startActivity(intent);
    }


    /**
     * 快递物流
     */
    private void skipGarbage() {
        Intent intent = new Intent();
        intent.setClass(mContext, GarbageQueryActivity.class);
        startActivity(intent);
    }


    /**
     * 查黄历
     */
    private void skipYellowCalender() {
        Intent intent = new Intent();
        intent.setClass(mContext, YellowCalenderDetailActivity.class);
        startActivity(intent);
    }

    private void skipWebView(String link, String title) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WEB_VIEW_URL, link);
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, false);
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, title);
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }

    private void skipWebViewRich(String richContent, String urlTitle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RICH_TEXT_ENABLE, true);
        intent.putExtra(EXTRA_WEB_VIEW_URL, "");
        intent.putExtra(EXTRA_WEB_VIEW_TITLE, urlTitle);
        WebViewConstant.richText = richContent;
        intent.setClass(mContext, CommonWebViewActivity.class);
        startActivity(intent);
    }


    private void skipByChannel(Channel channel) {
        switch (channel.getJumpWay()) {
            case CLICK_TYPE_LINK_OUTER:
                //展示外链
                skipWebView(StringUtil.getNotNullValue(channel.getLink()), channel.getTitle());
                break;
            case CLICK_TYPE_NONE:
//                        ToastUtil.show("什么也不做");
                break;
            case CLICK_TYPE_NATIVE:
                //展示原生
                skipNativeByCondition(channel.getTitle(), channel.getLink());
                break;
            case CLICK_TYPE_LINK_INNER:
                //展示富文本
                skipWebViewRich(channel.getContent(), channel.getTitle());
                break;

            case CLICK_TYPE_WAITING:
                //待开发
                ToastUtil.show(TIP_WAIT_DEV);
                break;
            default:
                break;
        }
    }

    private void skipByMatrix(MatrixBean matrixBean) {
        switch (matrixBean.getJumpWay()) {
            case CLICK_TYPE_LINK_OUTER:
                //展示外链
                skipWebView(StringUtil.getNotNullValue(matrixBean.getLink()), matrixBean.getMatrixTitle());
                break;
            case CLICK_TYPE_NONE:
//                        ToastUtil.show("什么也不做");
                break;
            case CLICK_TYPE_NATIVE:
                //展示原生
                skipNativeByCondition(matrixBean.getMatrixTitle(), matrixBean.getLink());
                break;
            case CLICK_TYPE_LINK_INNER:
                //展示富文本
                skipWebViewRich(matrixBean.getRichText(), matrixBean.getMatrixTitle());
                break;
            case CLICK_TYPE_WAITING:
                //待开发
                ToastUtil.show(TIP_WAIT_DEV);
                break;
            default:
                break;
        }
    }

    private void skipCertify() {
        if (!AccountHelper.getInstance().isLogin()) {
            skipLogin();
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, SelectCertifyActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(MainHomeFragment.this);
        super.onDestroy();
    }


    /**
     * 收到用户信息消息
     *
     * @param userInfoEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoRefreshEvent(UserInfoEvent userInfoEvent) {
        if (userInfoEvent == null) {
            return;
        }
        TourCooLogUtil.i(TAG, "刷新用户信息");
        if (AccountHelper.getInstance().isLogin()) {
            refreshUserInfo();
        }

    }


    /**
     * 刷新用户信息
     */
    private void refreshUserInfo() {
        ApiRepository.getInstance().requestUserInfo().compose(bindUntilEvent(FragmentEvent.DESTROY)).
                subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onRequestNext(BaseResult entity) {
                        if (entity == null) {
                            return;
                        }
                        if (entity.status == CODE_REQUEST_SUCCESS) {
                            UserInfo userInfo = parseJavaBean(entity.data, UserInfo.class);
                            if (userInfo == null) {
                                return;
                            }
                            AccountHelper.getInstance().saveUserInfoToDisk(userInfo);
                        }
                    }

                    @Override
                    public void onRequestError(Throwable e) {

                        TourCooLogUtil.e(TAG, e.toString());

                    }
                });
    }


    private void skipDriverAgainst() {
        Intent intent = new Intent();
        intent.setClass(mContext, DriverIllegalQueryActivity.class);
        startActivity(intent);
    }

    private void skipDriverScore() {
        Intent intent = new Intent();
        intent.setClass(mContext, AgainstScoreQueryActivity.class);
        startActivity(intent);
    }


    @SuppressWarnings("unchecked")
    private <T extends View> T getViewByViewId(int viewId, int tag) {
        int key = viewId + tag;
        View view = views.get(key);
        return (T) view;
    }


    private void cacheView(int layoutId, int tag, View view) {
        if (view != null) {
            views.put(layoutId + tag, view);
        }
    }

    private void addViewToContainer(View view) {
        if (view != null && llContainer.indexOfChild(view) < 0) {
            llContainer.addView(view);
        }
    }


    private void addLine(int tag){
        View lineView = createLineView(tag);
        addViewToContainer(lineView);
        viewList.add(lineView);
    }
}
