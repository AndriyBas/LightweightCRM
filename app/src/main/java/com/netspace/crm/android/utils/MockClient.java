package com.netspace.crm.android.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by Oleh Kolomiets
 */
public class MockClient implements Client {

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());
        Log.d("MockClient", "Request: " + uri.toString());
        String response = "";
        if (uri.getPath().equals("/CRMWebAPI/api/v1/activities")) {
            response = "";
        } else {
            if (uri.getPath().equals("/CRMWebAPI/api/v1/Contacts")) {
                response = "{\"name\":\"User\"}";
            }
        }
        return new Response(
                request.getUrl(),
                200,
                "nothing",
                Collections.EMPTY_LIST,
                new TypedByteArray("application/json", response.getBytes("UTF-16")));
    }
}
