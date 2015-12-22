package com.netspace.crm.android.api;


import com.netspace.crm.android.model.Contact;
import com.netspace.crm.android.model.ResponseModel;
import com.netspace.crm.android.model.SyncModel;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;
import rx.Observable;
import taskDB.Task;

/**
 * Created by Oleh Kolomiets
 */
public interface ApiService {

    @GET("/v1/Contacts")
    Observable<Contact> logIn(@Header("Authorization") String id);

    @GET("/v1/activities")
    Observable<ResponseModel> loadTasks(@Query("count") int count,
                   @Query("offset") int offset);

    @GET("/v1/activity/modified")
    Observable<SyncModel> syncTask(@Query("lastSyncTime") String lastSyncTime);

    @PUT("/v1/activity")
    Observable<Task> updateTask(@Body Task t);

    @POST("/v1/activity")
    Observable<Task> postTask(@Body Task t);
}
