package com.wangdaye.photo.vm;

import com.wangdaye.base.resource.Resource;
import com.wangdaye.common.base.vm.BrowsableViewModel;
import com.wangdaye.common.bus.event.DownloadEvent;
import com.wangdaye.base.unsplash.Photo;
import com.wangdaye.common.bus.MessageBus;
import com.wangdaye.common.bus.event.PhotoEvent;
import com.wangdaye.photo.repository.PhotoActivityRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Photo activity model.
 * */
public class PhotoActivityModel extends BrowsableViewModel<Photo> {

    private PhotoActivityRepository repository;

    private Disposable photoEventDisposable;
    private Disposable downloadEventDisposable;

    private String photoId;

    @Inject
    public PhotoActivityModel(PhotoActivityRepository repository) {
        super();

        this.repository = repository;

        this.photoEventDisposable = MessageBus.getInstance()
                .toObservable(PhotoEvent.class)
                .subscribe(photoEvent -> {
                    if (photoEvent.photo.id.equals(photoId)) {
                        setPhoto(photoEvent.photo);
                    }
                });
        this.downloadEventDisposable = MessageBus.getInstance()
                .toObservable(DownloadEvent.class)
                .subscribe(event -> {
                    if (event.title.equals(photoId)) {
                        setClonePhoto();
                    }
                });

        this.photoId = null;
    }

    public void init(@NonNull Resource<Photo> resource, String photoId) {
        boolean init = super.init(resource);

        if (this.photoId == null) {
            this.photoId = photoId;
        }

        if (init) {
            checkToRequestPhoto();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        repository.cancel();
        photoEventDisposable.dispose();
        downloadEventDisposable.dispose();
    }

    public void checkToRequestPhoto() {
        if (getResource().getValue() != null
                && (getResource().getValue().data == null || !getResource().getValue().data.complete)) {
            repository.getAPhoto(getResource(), photoId);
        }
    }

    public void likeOrDislikePhoto(boolean like) {
        repository.likeOrDislikePhoto(getResource(), photoId, like);
    }

    public void setPhoto(@NonNull Photo photo) {
        setResource(Resource.success(photo));
        photoId = photo.id;
        checkToRequestPhoto();
    }

    private void setClonePhoto() {
        if (getResource().getValue() != null) {
            setResource(new Resource<>(getResource().getValue()));
        }
    }
}
