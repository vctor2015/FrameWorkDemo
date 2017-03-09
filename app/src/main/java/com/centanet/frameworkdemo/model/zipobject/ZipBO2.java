package com.centanet.frameworkdemo.model.zipobject;

/**
 * Created by vctor2015 on 2016/11/17.
 * <p>
 * 描述:组合
 */

public class ZipBO2<T1,T2> {

    private T1 model1;
    private T2 model2;

    public ZipBO2(T1 model1, T2 model2) {
        this.model1 = model1;
        this.model2 = model2;
    }

    public T1 getModel1() {
        return model1;
    }

    public T2 getModel2() {
        return model2;
    }
}
