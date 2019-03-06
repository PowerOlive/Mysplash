package com.wangdaye.mysplash.common.network.callback;

import com.wangdaye.mysplash.common.basic.model.ListResource;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class ListResourceCallback<T> extends Callback<List<T>> {

    private MutableLiveData<ListResource<T>> current;
    private boolean refresh;

    public ListResourceCallback(MutableLiveData<ListResource<T>> current, boolean refresh) {
        this.current = current;
        this.refresh = refresh;
    }

    @Override
    public void onSucceed(List<T> list) {
        if (current.getValue() == null) {
            return;
        }
        if (refresh) {
            current.setValue(ListResource.refreshSuccess(current.getValue(), list));
        } else if (list.size() == current.getValue().perPage) {
            current.setValue(ListResource.loadSuccess(current.getValue(), list));
        } else {
            current.setValue(ListResource.allLoaded(current.getValue(), list));
        }
    }

    @Override
    public void onFailed() {
        if (current.getValue() == null) {
            return;
        }
        if (refresh) {
            current.setValue(ListResource.refreshError(current.getValue()));
        } else {
            current.setValue(ListResource.loadError(current.getValue()));
        }
    }
}