package mx.ine.demo.Requests.Model;

import mx.ine.demo.Requests.APIUrl;
import mx.ine.demo.Util.Util;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UsuarioService {

    @POST(APIUrl.LOGIN)
    @Headers(Util.NO_AUTH)
    Call<String> login(@Body RequestBody data);

    @POST(APIUrl.LOGOUT)
    @Headers(Util.AUTH_KEY)
    Call<String> logout();

    @POST(APIUrl.REGISTRO)
    @Headers(Util.NO_AUTH)
    Call<String> registrar(@Body RequestBody data);

    @POST(APIUrl.VALIDATION)
    @Headers(Util.NO_AUTH)
    Call<String> validation(@Body RequestBody data);

    @POST(APIUrl.VERIFY_DOCUMENT)
    @Headers(Util.AUTH_IPSIDY)
    Call<String> sendVerifyDocument(@Body RequestBody data);

    @POST
    @Headers(Util.AUTH_IECISA)
    Call<String> sendData(@Body RequestBody data, @Url String url);

}
