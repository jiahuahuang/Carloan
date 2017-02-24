package carloan.com.carloan.component;

import com.jiahuaandroid.basetools.utils.LogUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by jhhuang on 2016/10/14.
 * QQ:781913268
 * Description：事件处理
 */
public class RxBus
{
    private static final String TAG = "RxBus";
    // 主题
    private final Subject<Object, Object> bus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus()
    {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault()
    {
        return RxBusHolder.sInstance;
    }

    private static class RxBusHolder
    {
        private static final RxBus sInstance = new RxBus();
    }

    private boolean hasObserverable()
    {
        return bus.hasObservers();
    }

    // 提供了一个新的事件
    public void post(Object o)
    {
        if(hasObserverable()) {
            try
            {
                bus.onNext(o);
            } catch (Exception e)
            {
                LogUtil.e(TAG,"post : ");
                e.printStackTrace();
            }
        }
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObserverable(Class<T> eventType)
    {
        return bus.ofType(eventType).observeOn(AndroidSchedulers.mainThread());
    }

}
