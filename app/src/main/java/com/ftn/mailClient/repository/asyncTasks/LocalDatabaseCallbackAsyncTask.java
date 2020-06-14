package com.ftn.mailClient.repository.asyncTasks;

import android.os.AsyncTask;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.utill.CallbackAsyncTask;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;

/**
 * Async task with callback called on execution of postExecute function of AsyncTask
 * @param <Param> Parameter that is sent on execute of async task
 * @param <Stat>  Async task status parameter
 * @param <Ret> Return of the async task
 */
public abstract class LocalDatabaseCallbackAsyncTask<Param, Stat, Ret> extends CallbackAsyncTask<Param, Stat, Ret> {
    protected LocalDatabase localDatabase;

    public LocalDatabaseCallbackAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Ret> onPostExecuteFunctionFunctionalInterface){
        super(onPostExecuteFunctionFunctionalInterface);
        this.localDatabase = localDatabase;
    }


    @Override
    protected void onPostExecute(Ret ret) {
        super.onPostExecute(ret);
        onPostExecuteFunctionFunctionalInterface.postExecuteFunction(ret);
    }
}
