package com.mobile.ext.rxjava3.binding;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import com.bumptech.glide.util.Preconditions;

import io.reactivex.rxjava3.core.Observable;

public class RxView {

    private RxView() {
        throw new UnsupportedOperationException();
    }

    @CheckResult
    @NonNull
    public static Observable<View> clicks(@NonNull View view) {
        Preconditions.checkNotNull(view, "view == null");
        return new ClickObservable(view);
    }

    @CheckResult
    @NonNull
    public static InitialValueObservable<TextViewAfterTextChangeEvent> afterTextChangeEvents(@NonNull TextView view) {
        Preconditions.checkNotNull(view, "view == null");
        return new TextViewAfterTextChangeEventObservable(view);
    }
}
