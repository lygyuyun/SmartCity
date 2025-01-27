package com.tourcool.ui.mvp.contract;


import com.frame.library.core.control.IHttpRequestControl;
import com.frame.library.core.retrofit.BaseObserver;
import com.tourcool.core.base.BaseResult;
import com.tourcool.core.entity.BasePageResult;
import com.tourcool.core.entity.MessageBean;
import com.tourcool.core.module.mvp.IBaseModel;
import com.tourcool.core.module.mvp.IBaseView;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年08月13日17:34
 * @Email: 971613168@qq.com
 */
public interface MessageContract {
    interface MessageModel extends IBaseModel {
        void getMessagePageList(BaseObserver<BaseResult<BasePageResult<MessageBean>>> observer, int userId, int pageIndex);
    }

    interface View extends IBaseView {
        IHttpRequestControl getIHttpRequestControl();

    }

    interface Presenter {
        void getMessageList(int userId, int pageIndex);
    }
}
