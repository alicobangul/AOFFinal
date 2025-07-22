package basesoftware.com.aoffinal.domain.use_case;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;
import basesoftware.com.aoffinal.domain.callback.Result;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;
import basesoftware.com.aoffinal.domain.repository.Repository;
import dagger.hilt.android.scopes.ViewModelScoped;

@ViewModelScoped
public class SaveDataUseCase {

    private final Repository repository;

    @Inject
    public SaveDataUseCase(Repository repository) { this.repository = repository; }

    public void execute(List<CourseDbModel> courses, ResultCallBack<Result> callBack) {

        List<CourseDbModel> coursesDb = new ArrayList<>(courses);

        ResultCallBack<Result> resultCallBack = new ResultCallBack<>() {
            @Override
            public void onSuccess(Result result) { callBack.onSuccess(result); }
            @Override
            public void onFailure(@Nullable Throwable throwable) { callBack.onFailure(throwable); }
        };

        repository.saveDataInDb(coursesDb, resultCallBack);

    }

}