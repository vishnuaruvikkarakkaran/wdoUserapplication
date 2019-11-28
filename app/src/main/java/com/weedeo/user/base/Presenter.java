package com.weedeo.user.base;

public interface Presenter<V extends MvpBase> {

    void attachView(V mvpView);

    void detachView();
}
