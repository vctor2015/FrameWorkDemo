package com.centanet.frameworkdemo.model;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:干货实体
 */

public class GHResponse<T> {

    private boolean error;
    private int count;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
