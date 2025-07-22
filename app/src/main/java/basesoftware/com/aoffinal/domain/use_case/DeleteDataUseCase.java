package basesoftware.com.aoffinal.domain.use_case;

import androidx.annotation.Nullable;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import basesoftware.com.aoffinal.domain.callback.Result;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;
import basesoftware.com.aoffinal.domain.repository.Repository;
import dagger.hilt.android.scopes.ViewModelScoped;

@ViewModelScoped
public class DeleteDataUseCase {

    private final Repository repository;

    @Inject
    public DeleteDataUseCase(Repository repository) { this.repository = repository; }

    public void execute(@Nonnull ResultCallBack<Result> viewmodelCallBack) {

        ResultCallBack<Result> dataCallback = new ResultCallBack<>() {
            @Override
            public void onSuccess(Result result) { viewmodelCallBack.onSuccess(result); }

            @Override
            public void onFailure(@Nullable Throwable throwable) { viewmodelCallBack.onFailure(throwable); }
        };

        repository.deleteSavedDataInDb(dataCallback);

    }

}
