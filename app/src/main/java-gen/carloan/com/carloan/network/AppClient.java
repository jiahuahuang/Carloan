package carloan.com.carloan.network;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import carloan.com.carloan.BuildConfig;
import carloan.com.carloan.network.modify.GsonConverterFactory;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by jhhuang on 2016/5/19.
 * QQ:781913268
 * Description：AppClient
 */
public class AppClient
{
    private static Converter.Factory scalarsconverterfactory = ScalarsConverterFactory.create();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    public static String baseUrl = "http://appsrv.hzszn.com:8081/";
    private Retrofit retrofit;

    public AppClient()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG) {
            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(scalarsconverterfactory)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .baseUrl(baseUrl)
                .build();

    }

    public AppClient(String url)
    {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS);

        if (BuildConfig.DEBUG) {
            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(scalarsconverterfactory)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .baseUrl(url)
                .build();

    }


    public <T> T create(Class<T> clazz)
    {
        return retrofit.create(clazz);
    }

    /**
     * 全局头信息
     */
    class HeaderInterceptor implements Interceptor
    {

        @Override
        public Response intercept(Chain chain) throws IOException
        {
            Request request = chain.request();
            String path = request.url().encodedPath();
            Log.d("AppClient", path + ">>>query");
            //这里设置成你的全局header
            Headers headers = request.headers();
            Request interRequest = chain.request().newBuilder()
                    .headers(headers)
                    .build();
            return chain.proceed(interRequest);
        }
    }
}


