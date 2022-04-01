package edu.cnm.deepdive.tvnclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.tvnclient.BuildConfig;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.model.dto.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.UUID;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TVNServiceProxy {

  String ISO_8601_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  @GET("organizations")
  Single<List<Organization>> getAll(@Header("Authorization") String bearerToken);

  @GET("organizations")
  Single<List<Organization>> findOrganizations(@Query("q") String fragment, @Header("Authorization") String bearerToken);

  @POST("organizations")
  Single<Organization> addOrganization(@Body Organization organization, @Header("Authorization") String bearerToken);

  @GET("organizations/{organizationId}")
  Single<Organization> getOrganization(@Path("organizationId") UUID organizationId, @Header("Authorization") String bearerToken);

  @PUT("organizations/{organizationId}")
  Single<Organization> modifyOrganization(@Path("organizationId") UUID organizationId, @Body Organization organization, @Header("Authorization") String bearerToken);

  @DELETE("organizations/{organizationId}")
  Completable deleteOrganization(@Path("organizationId") UUID organizationId, @Header("Authorization") String bearerToken);

  @GET("organizations/{organizationId}/name")
  Single<String> getOrganizationName(@Path("organizationId") UUID organizationId, @Header("Authorization") String bearerToken);

  @PUT("organizations/{organizationId}/name")
  Single<String> addOrganizationName(@Path("organizationId") UUID organizationId, @Header("Authorization") String bearerToken);

  @GET("organizations/{organizationId}/opportunities")
  Single<List<Opportunity>> getAllOpportunities(@Path("organizationId") UUID organizationId, @Header("Authorization") String bearerToken);

  @POST("organizations/{organizationId}/opportunities")
  Single<Opportunity> addOpportunity(@Path("organizationId") UUID organizationId, @Body Opportunity opportunity, @Header("Authorization") String bearerToken);

  @GET("organizations/{organizationId}/opportunities/{opportunityId}")
  Single<Opportunity> getOpportunity(@Path("organizationId") UUID organizationId, @Path("opportunityId") UUID opportunityId, @Header("Authorization") String bearerToken);

  @PUT("organizations/{organizationId}/opportunities/{opportunityId}")
  Single<Opportunity> modifyOpportunity(@Path("organizationId") UUID organizationId, @Path("opportunityId") UUID opportunityId, @Body Opportunity opportunity, @Header("Authorization") String bearerToken);

  @DELETE("organizations/{organizationId}/opportunities/{opportunityId}")
  Completable deleteOpportunity(@Path("organizationId") UUID organizationId, @Path("opportunityId") UUID opportunityId, @Header("Authorization") String bearerToken);

  @GET("users/me")
  Single<User> getCurrentUser(@Header("Authorization") String bearerToken);
// TODO Add proxy method for updating user profile when available.

  @GET("users/me/favorites")
  Single<List<Organization>> getFavorites(@Header("Authorization") String bearerToken);

  @GET("users/me/favorites/{organizationId}")
  Single<Boolean> isFavorite(@Path("organizationId") UUID organizationId, @Header("Authorization") String bearerToken);

  @PUT("users/me/favorites/{organizationId}")
  Single<Boolean> setFavorite(@Path("organizationId") UUID organizationId, @Body boolean favorite, @Header("Authorization") String bearerToken);

  @GET("users/me/volunteers")
  Single<List<Organization>> getVolunteers(@Header("Authorization") String bearerToken);

  @GET("users/volunteers/{organizationId}")
  Single<Boolean> isVolunteer(@Path("organizationId") UUID organizationId, @Header("Authorization") String bearerToken);

  @PUT("users/volunteers/{organizationId}")
  Single<Boolean> setVolunteer(@Path("organizationId") UUID organizationId, @Body boolean volunteer, @Header("Authorization") String bearerToken);

  static TVNServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }
  class InstanceHolder {

    private static final TVNServiceProxy INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat(ISO_8601_DATETIME_FORMAT)
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
          .build();
      INSTANCE = retrofit.create(TVNServiceProxy.class);

    }

  }

}
