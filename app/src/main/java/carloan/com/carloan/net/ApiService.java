package carloan.com.carloan.net;

import carloan.com.carloan.bean.req.SendMessageReq;
import carloan.com.carloan.network.BaseRes;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jiahua on 17-2-24.
 * Description：
 */

public interface ApiService
{
    //获取登录短信验证码
    @POST("/appsrv")
    Observable<BaseRes> getVerificationCodeBySMS(@Body SendMessageReq sendMessageReq);
}
