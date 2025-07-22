package basesoftware.com.aoffinal.domain.callback;

import androidx.annotation.Nullable;

public interface ResultCallBack<T> {

    void onSuccess(T result);
    void onFailure(@Nullable Throwable throwable);

}
