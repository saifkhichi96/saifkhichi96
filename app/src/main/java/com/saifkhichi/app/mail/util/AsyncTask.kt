package com.saifkhichi.app.mail.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 * AsyncTask is a simplified version of [android.os.AsyncTask]. It is designed to be used as a
 * drop-in replacement for [android.os.AsyncTask] in most cases.
 */
abstract class AsyncTask<Params, Result> {

    var onPostExecute: ((result: Result) -> Unit)? = null

    protected abstract fun doInBackground(vararg params: Params): Result

    fun execute(vararg params: Params): AsyncTask<Params, Result> {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            /*
            * Your task will be executed here you can execute anything here that
            * you cannot execute in UI thread for example a network operation.
            * This is a background thread, so you cannot access view elements here.
            * */
            val result = doInBackground(*params)
            handler.post {
                /*
                * You can perform any operation that requires UI Thread here.
                * */
                onPostExecute?.invoke(result)
            }
        }
        return this
    }

}