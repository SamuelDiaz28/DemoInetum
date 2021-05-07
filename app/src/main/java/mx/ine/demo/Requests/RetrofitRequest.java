package mx.ine.demo.Requests;

import android.util.Base64;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import mx.ine.demo.Util.Util;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitRequest {
    private final static int READ_TIME_OUT = 60 * 60;
    private final static int CONNECT_TIME_OUT = 60 * 60;

    /**
     * Modelo básico de todas las peticiones
     */
    public static <T> T create(Class<T> clazz) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request  = chain.request();
                Request.Builder builder = request.newBuilder();
                builder.cacheControl(new CacheControl.Builder().noCache().build());
                String head = request.headers().name(0);
                if (!head.equals("NOAUTH")){
                    if(head.equals("@IPSIDY")) {
                        builder.addHeader("Authorization", "Basic " + Base64.encodeToString(Util.BASE_IPSYDY.getBytes(), Base64.NO_WRAP));
                        builder.addHeader("Content-type", "application/json");
                    }else if (head.equals("@IECISA")){
                        builder.addHeader("Authorization", "Basic " + Base64.encodeToString(Util.BASE_IECISA.getBytes(), Base64.NO_WRAP));
                        builder.removeHeader("@IECISA");
                    }else if (head.equals("@AUTH")) {
                        builder.addHeader("Authorization", "Bearer " + Util.TOKEN);
                        builder.removeHeader(Util.AUTH_KEY);
                    }
                }
                return chain.proceed(builder.build());
            }
        }).readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS).build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(APIUrl.BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

    /**
     * Crea el objeto RequestBody con los datos
     */
    public static RequestBody createBody(String datos) {
        return RequestBody.create( MediaType.parse("application/json"), datos);
    }
}
