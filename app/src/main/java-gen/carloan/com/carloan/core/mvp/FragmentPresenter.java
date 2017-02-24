package carloan.com.carloan.core.mvp;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import carloan.com.carloan.network.ex.ApiException;
import carloan.com.carloan.network.ex.ResultException;
import carloan.com.carloan.network.ex.ToastException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jhhuang on 2016/12/13.
 * QQ:781913268
 * Description：FragmentPresenter
 */
public class FragmentPresenter<V extends FragmentView> extends BasePresenter<V> implements FragmentLifecycleProvider
{
    @NonNull
    @Override
    public final Observable<FragmentEvent> lifecycle()
    {
        return getMvpView().lifecycle();
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event)
    {
        return getMvpView().bindUntilEvent(event);
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle()
    {
        return getMvpView().bindToLifecycle();
    }

    public <T> Observable.Transformer<T, T> callbackOnIOThread()
    {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .filter(t -> isViewAttached())
                .compose(bindToLifecycle());
    }

    public <T> Observable.Transformer<T, T> verifyOnMainThread()
    {
        return tObservable -> tObservable.filter(t -> isViewAttached())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public abstract class NetSubseriber<T> extends Subscriber<T>
    {

        @Override
        public void onStart()
        {
            super.onStart();
            if (isViewAttached())
            {
                getMvpView().showLoading();
            }
        }

        @Override
        public void onCompleted()
        {
            if (isViewAttached())
            {
                getMvpView().hideLoading();
            }
        }

        @Override
        public void onError(Throwable e)
        {
            e.printStackTrace();
            if (!isViewAttached())
            {
                return;
            }
            getMvpView().hideLoading();
            if (e instanceof ToastException)
            {
                getMvpView().toast(e.getMessage());
            } else if (e instanceof ResultException)
            {
                onResultError(((ResultException) e));
            } else if (e instanceof HttpException)
            {
                onHttpError(((HttpException) e));
            } else if (e instanceof SocketTimeoutException)
            {
                onSocketTimeoutException(((SocketTimeoutException) e));
            } else if (e instanceof ApiException)
            {
                onApiException(((ApiException) e));
            } else if (e instanceof ConnectException)
            {
                onConnectException(((ConnectException) e));
            } else
            {
                onOtherError(e);
            }
        }

        public void onApiException(ApiException e)
        {

        }

        public void onOtherError(Throwable e)
        {
            getMvpView().toast("未知错误");
        }

        public void onResultError(ResultException e)
        {
            getMvpView().toast(e.getMessage());
        }

        public void onHttpError(HttpException e)
        {
            getMvpView().toast("网络错误");
        }

        public void onSocketTimeoutException(SocketTimeoutException e)
        {
            getMvpView().toast("连接超时");
        }

        public void onConnectException(ConnectException e)
        {
            getMvpView().toast("未能连接到服务器");
        }
    }

}