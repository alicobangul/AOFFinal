package basesoftware.com.aoffinal.presenter;

import javax.inject.Inject;
import basesoftware.com.aoffinal.impl.Contract;
import basesoftware.com.aoffinal.model.TrainingRepository;
import basesoftware.com.aoffinal.model.domain.TrainingModel;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
public class Presenter implements Contract.Presenter {

    @Inject
    TrainingRepository repository;

    private Contract.View view;

    @Inject
    public Presenter() { }

    @Override
    public void initBinds(Contract.View view) {

        this.view = view;

        repository.initBinds(this);

    }

    @Override
    public void calculateClick() { repository.trainingCalculate(); }

    @Override
    public void checkSavedDataInDb() { repository.checkSavedDataInDb(); }

    @Override
    public void deleteSavedDataInDb() { repository.deleteSavedDataInDb(); }

    @Override
    public void saveData() { repository.saveData(); }

    @Override
    public void saveResult(Boolean isSuccess) { view.saveResult(isSuccess); }

    @Override
    public void deleteResult(Boolean isSuccess) { view.deleteResult(isSuccess); }

    @Override
    public void updateView(TrainingModel trainingModel) { view.updateView(trainingModel); }

    @Override
    public void textChanged(Integer viewTag, String viewText) { repository.textChanged(viewTag, viewText); }

    @Override
    public void calculateComplete() { view.saveQuestion(); }

}
