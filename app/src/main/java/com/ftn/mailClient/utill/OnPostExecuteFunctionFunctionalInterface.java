package com.ftn.mailClient.utill;

@FunctionalInterface
public interface OnPostExecuteFunctionFunctionalInterface<R extends Object> {
    abstract void postExecuteFunction(R value);
}
