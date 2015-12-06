package com.netspace.crm.android.api;


import com.netspace.crm.android.model.Contact;
import com.netspace.crm.android.model.ResponseModel;
import com.netspace.crm.android.model.SyncModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;
import taskDB.Task;

/**
 * Created by Oleh Kolomiets
 */
public interface ApiService {

    @GET("/v1/Contacts")
    void logIn(@Header("Authorization") String id, Callback<Contact> callback);

    @GET("/v1/activities")
    void loadTasks(@Query("count") int count,
                   @Query("offset") int offset,
                   Callback<ResponseModel> callback);

    @GET("/v1/activity/modified")
    void syncTask(@Query("lastSyncTime") String lastSyncTime,
                  Callback<SyncModel> callback);

    @PUT("/v1/activity")
    void updateTask(@Body Task t, Callback<Task> callback);

    @POST("/v1/activity")
    void postTask(@Body Task t, Callback<Task> callback);
}
