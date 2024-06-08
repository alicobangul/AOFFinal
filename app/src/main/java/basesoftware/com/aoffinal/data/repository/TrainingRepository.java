package basesoftware.com.aoffinal.data.repository;

import com.google.gson.Gson;
import java.util.ArrayList;
import javax.annotation.Nullable;
import javax.inject.Inject;
import basesoftware.com.aoffinal.impl.IModelObserver;
import basesoftware.com.aoffinal.domain.model.TrainingModel;
import basesoftware.com.aoffinal.data.local.TrainingDao;
import basesoftware.com.aoffinal.data.model.TrainingDbModel;
import dagger.hilt.android.scopes.ActivityScoped;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class TrainingRepository {

    private final TrainingDao trainingDao;

    private final CompositeDisposable compositeDisposable;

    private final TrainingModel trainingModel = new TrainingModel();

    private final ArrayList<IModelObserver> arrayObservers = new ArrayList<>();

    @Inject
    public TrainingRepository(TrainingDao dao, CompositeDisposable compositeDisposable) {

        this.trainingDao = dao;

        this.compositeDisposable = compositeDisposable;

    }

    public void addObserver(IModelObserver observer) { arrayObservers.add(observer); }

    public void removeObserver(IModelObserver observer) { arrayObservers.remove(observer); }

    public void notifySaveResult(Boolean isSuccess) { for (IModelObserver observer : arrayObservers) observer.saveResult(isSuccess); }

    public void notifyDeleteResult(Boolean isSuccess) { for (IModelObserver observer : arrayObservers) observer.deleteResult(isSuccess); }

    public void notifySavedDataControl(@Nullable TrainingDbModel trainingDbModel) { for (IModelObserver observer : arrayObservers) observer.savedDataControl(trainingDbModel); }

    public TrainingModel getTrainingModel() { return trainingModel; }

    public void checkSavedDataInDb() {

        compositeDisposable.clear();

        compositeDisposable.add(trainingDao.getAll() // Veri alma fonksiyonu çağırıldı
                .subscribeOn(Schedulers.io()) // Main thread kitlememek için I/O thread kullanıldı
                .observeOn(AndroidSchedulers.mainThread()) // Değişiklikler mainThread üzerinde gözlemlenecek
                .subscribe(this::notifySavedDataControl, e -> notifySavedDataControl(null)) // İşlem tamamlandığında çalıştırılacak fonksiyon
        );

    }

    public void saveData() {

        compositeDisposable.clear();

        TrainingDbModel trainingDbModel = new TrainingDbModel(
                trainingModel.getMidtermExam().getValue(),
                trainingModel.getFinalExam().getValue(),
                trainingModel.getAverage().getValue(),
                new Gson().toJson(trainingModel.getArrayTraining()),
                new Gson().toJson(trainingModel.getArrayTrainingResult())
        );

        compositeDisposable.add(
                trainingDao.insert(trainingDbModel)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> notifySaveResult(true), e -> notifySaveResult(false)));

    }

    public void deleteSavedDataInDb() {

        compositeDisposable.clear();

        compositeDisposable.add(
                trainingDao.deleteAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> notifyDeleteResult(true), e -> notifyDeleteResult(false)));

    }

}
