package carloan.com.carloan.network.modify;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import carloan.com.carloan.network.Res;
import carloan.com.carloan.network.ex.ToastException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by jhhuang on 2016/10/20.
 * QQ:781913268
 * Description：自定义gson转换器
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T>
{
    private final Gson gson;
    private final Type type;

    public GsonResponseBodyConverter(Gson gson, Type type)
    {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException
    {
        String response = value.string();
        try
        {
            //ResultResponse 只解析code字段进行约定异常处理
//            ResultResponse resultResponse = gson.fromJson(response, ResultResponse.class);
//            if (resultResponse.getCode() == -1)
//            {
//                //解析非标准约定数据resultResponse.getCode(), errResponse.getMsg()
//                return gson.fromJson(response, type);
//            } else
//            {
//                //ErrResponse 将msg解析为异常消息文本
//                ErrResponse errResponse = gson.fromJson(response, ErrResponse.class);
//                throw new ResultException(resultResponse.getCode(), errResponse.getMsg());
//            }
            Res res = gson.fromJson(response, Res.class);
            if ("0000".equals(res.getError()))
            {
                //解析非标准约定数据
                return gson.fromJson(response, type);
            } else
            {

                throw new ToastException(res.getMsg());
            }
        } finally
        {
            value.close();
        }
    }
}
