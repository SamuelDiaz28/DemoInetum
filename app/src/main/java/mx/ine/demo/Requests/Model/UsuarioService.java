package mx.ine.demo.Requests.Model;

import mx.ine.demo.Requests.APIUrl;
import mx.ine.demo.Util.Util;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UsuarioService {

    @POST(APIUrl.LOGIN)
    Call<String> login(@Body RequestBody data);

    @POST(APIUrl.LOGOUT)
    @Headers(Util.AUTH)
    Call<String> logout();

    @POST(APIUrl.REGISTRO)
    Call<String> registrar(@Body RequestBody data);

    @POST(APIUrl.VALIDATION)
    Call<String> validation(@Body RequestBody data);
}
