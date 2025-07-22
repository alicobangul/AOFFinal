package basesoftware.com.aoffinal.domain.repository;

import java.util.List;
import javax.annotation.Nonnull;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;
import basesoftware.com.aoffinal.domain.callback.Result;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;

public interface Repository {

    void getSavedDataInDb(ResultCallBack<List<CourseDbModel>> callBack);

    void saveDataInDb(List<CourseDbModel> courseDbModel, @Nonnull ResultCallBack<Result> callBack);

    void deleteSavedDataInDb(@Nonnull ResultCallBack<Result> callBack);

}