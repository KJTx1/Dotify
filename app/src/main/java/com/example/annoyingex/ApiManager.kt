package com.example.annoyingex

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class ApiManager(context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun getMsgs(onMsgReady: (List<String>) -> Unit, onError: (() -> Unit)? = null) {
        val msgURL = "https://raw.githubusercontent.com/echeeUW/codesnippets/master/ex_messages.json"
        val request = StringRequest(
            Request.Method.GET, msgURL,
            {response ->
                val gson = Gson()
                val data = gson.fromJson(response, EXmsg::class.java)
                onMsgReady(data.messages)
            },
            {
                onError?.invoke()
                Log.i("error", it.toString())
            }
        )

        request.setShouldCache(false)

        queue.add(request)
    }
}