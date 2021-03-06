package com.wangdaye.common.presenter.pager;

import com.wangdaye.common.base.adapter.footerAdapter.FooterAdapter;
import com.wangdaye.base.i.PagerView;
import com.wangdaye.base.resource.ListResource;
import com.wangdaye.common.base.vm.PagerViewModel;
import com.wangdaye.common.ui.adapter.photo.PhotoAdapter;

public class PagerViewManagePresenter {

    public static <T> void initRefresh(PagerViewModel<T> model, FooterAdapter adapter) {
        assert model.getListResource().getValue() != null;
        model.setListResource(ListResource.initRefreshing(model.getListResource().getValue()));
        adapter.notifyDataSetChanged();

        model.refresh();
    }

    public static <T> void responsePagerListResourceChanged(ListResource<T> resource,
                                                            PagerView view, FooterAdapter adapter) {
        if (resource.dataList.size() == 0
                && (resource.state == ListResource.State.REFRESHING
                || resource.state == ListResource.State.LOADING)) {
            // loading state.
            view.setSwipeRefreshing(false);
            view.setSwipeLoading(false);
            view.setPermitSwipeRefreshing(false);
            view.setPermitSwipeLoading(false);
            view.setState(PagerView.State.LOADING);
        } else if (resource.dataList.size() == 0
                && resource.state == ListResource.State.ERROR) {
            // error state.
            view.setSwipeRefreshing(false);
            view.setSwipeLoading(false);
            view.setPermitSwipeRefreshing(false);
            view.setPermitSwipeLoading(false);
            view.setState(PagerView.State.ERROR);
        } else if (view.getState() != PagerView.State.NORMAL) {
            // error/loading state -> normal state.
            view.setSwipeRefreshing(false);
            view.setSwipeLoading(false);
            view.setPermitSwipeRefreshing(true);
            view.setPermitSwipeLoading(true);
            adapter.notifyDataSetChanged();
            view.setState(PagerView.State.NORMAL);
        } else {
            // normal state control.
            view.setSwipeRefreshing(resource.state == ListResource.State.REFRESHING);
            if (resource.state != ListResource.State.LOADING) {
                view.setSwipeLoading(false);
            }
            if (resource.state == ListResource.State.ALL_LOADED) {
                view.setPermitSwipeLoading(false);
            }

            ListResource.Event event = resource.consumeEvent();
            if (event instanceof ListResource.DataSetChanged) {
                adapter.notifyDataSetChanged();
            } else if (event instanceof ListResource.ItemRangeInserted) {
                int increase = ((ListResource.ItemRangeInserted) event).increase;
                adapter.notifyItemRangeInserted(adapter.getRealItemCount() - increase, increase);
            } else if (event instanceof ListResource.ItemInserted) {
                adapter.notifyItemInserted(((ListResource.ItemInserted) event).index);
            } else if (event instanceof ListResource.ItemChanged) {
                adapter.notifyItemChanged(
                        ((ListResource.ItemChanged) event).index,
                        FooterAdapter.PAYLOAD_UPDATE_ITEM
                );
            } else if (event instanceof ListResource.ItemRemoved) {
                adapter.notifyItemRemoved(
                        ((ListResource.ItemRemoved) event).index
                );
            }
        }
    }

    public static <T> void responsePagerListResourceChangedByDiffUtil(ListResource<T> resource,
                                                                      PagerView view, PhotoAdapter adapter) {
        adapter.updateListByDiffUtil(resource.dataList);

        if (resource.dataList.size() == 0
                && (resource.state == ListResource.State.REFRESHING
                || resource.state == ListResource.State.LOADING)) {
            // loading state.
            view.setSwipeRefreshing(false);
            view.setSwipeLoading(false);
            view.setPermitSwipeRefreshing(false);
            view.setPermitSwipeLoading(false);
            view.setState(PagerView.State.LOADING);
        } else if (resource.dataList.size() == 0
                && resource.state == ListResource.State.ERROR) {
            // error state.
            view.setSwipeRefreshing(false);
            view.setSwipeLoading(false);
            view.setPermitSwipeRefreshing(false);
            view.setPermitSwipeLoading(false);
            view.setState(PagerView.State.ERROR);
        } else if (view.getState() != PagerView.State.NORMAL) {
            // error/loading state -> normal state.
            view.setSwipeRefreshing(false);
            view.setSwipeLoading(false);
            view.setPermitSwipeRefreshing(true);
            view.setPermitSwipeLoading(true);
            view.setState(PagerView.State.NORMAL);
        } else {
            // normal state control.
            view.setSwipeRefreshing(resource.state == ListResource.State.REFRESHING);
            if (resource.state != ListResource.State.LOADING) {
                view.setSwipeLoading(false);
            }
            if (resource.state == ListResource.State.ALL_LOADED) {
                view.setPermitSwipeLoading(false);
            }
        }
    }
}