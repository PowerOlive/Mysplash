package com.wangdaye.user.vm;

import android.text.TextUtils;

import com.wangdaye.common.presenter.event.DownloadEventResponsePresenter;
import com.wangdaye.common.presenter.event.PhotoEventResponsePresenter;
import com.wangdaye.user.repository.UserPhotosViewRepository;

import javax.inject.Inject;

public class UserLikesViewModel extends UserPhotosViewModel {

    @Inject
    public UserLikesViewModel(UserPhotosViewRepository repository,
                              PhotoEventResponsePresenter photoEventResponsePresenter,
                              DownloadEventResponsePresenter downloadEventResponsePresenter) {
        super(repository, photoEventResponsePresenter, downloadEventResponsePresenter);
    }

    @Override
    public void refresh() {
        if (TextUtils.isEmpty(getUsername())) {
            return;
        }
        getRepository().getUserLikes(
                getListResource(),
                getUsername(),
                getPhotosOrder().getValue(),
                true
        );
    }

    @Override
    public void load() {
        if (TextUtils.isEmpty(getUsername())) {
            return;
        }
        getRepository().getUserLikes(
                getListResource(),
                getUsername(),
                getPhotosOrder().getValue(),
                false
        );
    }
}
