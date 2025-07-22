package basesoftware.com.aoffinal.domain.use_case;

import androidx.annotation.Nullable;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;
import basesoftware.com.aoffinal.domain.repository.Repository;
import dagger.hilt.android.scopes.ViewModelScoped;

@ViewModelScoped
public class GetDataUseCase {

    private final Repository repository;

    @Inject
    public GetDataUseCase(Repository repository) { this.repository = repository; }

    public void execute(@Nonnull ResultCallBack<List<CourseDbModel>> viewmodelCallBack) {

        ResultCallBack<List<CourseDbModel>> dataCallback = new ResultCallBack<>() {

            @Override
            public void onSuccess(List<CourseDbModel> result) { viewmodelCallBack.onSuccess(result); }

            @Override
            public void onFailure(@Nullable Throwable throwable) { viewmodelCallBack.onFailure(throwable); }

        };

        repository.getSavedDataInDb(dataCallback);

    }

}