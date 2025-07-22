package basesoftware.com.aoffinal.data.repository;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import basesoftware.com.aoffinal.data.local.CourseDao;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;
import basesoftware.com.aoffinal.domain.callback.Result;
import basesoftware.com.aoffinal.domain.callback.ResultCallBack;
import basesoftware.com.aoffinal.domain.repository.Repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RepositoryImpl implements Repository {

    private final CourseDao courseDao;

    private final CompositeDisposable compositeDisposable;

    @Inject
    public RepositoryImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getSavedDataInDb(@Nonnull ResultCallBack<List<CourseDbModel>> callBack) {

        compositeDisposable.add(courseDao.getAll() // Veri alma fonksiyonu çağırıldı
                .subscribeOn(Schedulers.io()) // Main thread kitlememek için I/O thread kullanıldı
                .observeOn(AndroidSchedulers.mainThread()) // Değişiklikler mainThread üzerinde gözlemlenecek
                .doFinally(compositeDisposable::clear)
                .subscribe(listCourseDbModel -> {

                    if (listCourseDbModel != null && !listCourseDbModel.isEmpty()) callBack.onSuccess(listCourseDbModel);

                    else callBack.onSuccess(new ArrayList<>());

                }, callBack::onFailure)
        ); // İşlem tamamlandığında çalıştırılacak fonksiyon

    }

    @Override
    public void saveDataInDb(List<CourseDbModel> listCourseDbModel, @Nonnull ResultCallBack<Result> callBack) {

        compositeDisposable.add(
                courseDao.insert(listCourseDbModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(compositeDisposable::clear)
                        .subscribe(() -> callBack.onSuccess(Result.OK), callBack::onFailure));

    }

    @Override
    public void deleteSavedDataInDb(@Nonnull ResultCallBack<Result> callBack) {

        compositeDisposable.add(
                courseDao.deleteAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(compositeDisposable::clear)
                        .subscribe(() -> callBack.onSuccess(Result.OK), callBack::onFailure));

    }

}