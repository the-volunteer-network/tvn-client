package edu.cnm.deepdive.tvnclient.service;

import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.UUID;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TVNServiceProxy {

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
  Single<List<Opportunity>> getOpportunity(@Path("organizationId") UUID organizationId, @Path("opportunityId") UUID opportunityId, @Header("Authorization") String bearerToken);

  @PUT("organizations/{organizationId}/opportunities/{opportunityId}")
  Single<List<Opportunity>> addOpportunity(@Path("organizationId") UUID organizationId, @Path("opportunityId") UUID opportunityId, @Body Opportunity opportunity, @Header("Authorization") String bearerToken);

}
