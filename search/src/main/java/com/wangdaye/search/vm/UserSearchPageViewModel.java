package com.wangdaye.search.vm;

import com.wangdaye.base.unsplash.User;
import com.wangdaye.common.presenter.event.UserEventResponsePresenter;
import com.wangdaye.search.repository.UserSearchPageViewRepository;

import javax.inject.Inject;

public class UserSearchPageViewModel extends AbstractSearchPageViewModel<User, User> {

    private UserSearchPageViewRepository repository;
    private UserEventResponsePresenter presenter;

    @Inject
    public UserSearchPageViewModel(UserSearchPageViewRepository repository,
                                   UserEventResponsePresenter presenter) {
        super(User.class);
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.cancel();
        presenter.clearResponse();
    }

    @Override
    public void refresh() {
        repository.getUsers(getListResource(), getQuery(), true);
    }

    @Override
    public void load() {
        repository.getUsers(getListResource(), getQuery(), false);
    }

    // interface.

    @Override
    public void accept(User user) {
        presenter.updateUser(getListResource(), user, false);
    }
}
