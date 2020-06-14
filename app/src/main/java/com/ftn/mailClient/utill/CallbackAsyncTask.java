package com.ftn.mailClient.utill;

import android.os.AsyncTask;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;

/**
 * Async task with callback called on execution of postExecute function of AsyncTask
 * @param <Param> Parameter that is sent on execute of async task
 * @param <Stat>  Async task status parameter
 * @param <Ret> Return of the async task
 */
public abstract class CallbackAsyncTask<Param, Stat, Ret> extends AsyncTask<Param, Stat, Ret> {
    protected OnPostExecuteFunctionFunctionalInterface<Ret> onPostExecuteFunctionFunctionalInterface;

    public CallbackAsyncTask(OnPostExecuteFunctionFunctionalInterface<Ret> onPostExecuteFunctionFunctionalInterface){
        this.onPostExecuteFunctionFunctionalInterface = onPostExecuteFunctionFunctionalInterface;
    }


    @Override
    protected void onPostExecute(Ret ret) {
        super.onPostExecute(ret);
        onPostExecuteFunctionFunctionalInterface.postExecuteFunction(ret);
    }
}
