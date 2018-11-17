package com.example.wilsonzhu.searchengine.Activity.Models;

import android.content.Context;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;

public class RequestQueueService {

    private static RequestQueueService mRequestQueueInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private RequestQueueService(Context context)
    {
        // SINGLETON PATTERN
        // private constructor so this class cannot be instantiated by other classes

        mContext = context;
    }

    public static RequestQueueService getRequestQueueInstance(Context context) {
        if (mRequestQueueInstance == null)
        {
            mRequestQueueInstance = new RequestQueueService(context);
        }
        return mRequestQueueInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public void addToQueue(Request request, String tag)
    {
        request.setTag(tag);
        getRequestQueue().add(request);
    }
}
