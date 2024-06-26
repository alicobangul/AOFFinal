package basesoftware.com.aoffinal.impl;

import basesoftware.com.aoffinal.domain.model.TrainingModel;

public interface IContract {

    interface IView {

        void checkSavedDataInDb();

        void updateView(TrainingModel trainingModel);

        void textChanged(Integer viewTag, String viewText);

        void saveQuestion();

        void saveResult(Boolean isSuccess);

        void deleteResult(Boolean isSuccess);

        void visitStoreQuestion();

    }

    interface IPresenter {

        void initBinds(IView view);

        void calculateClick(TrainingModel trainingModel);

        void checkSavedDataInDb();

        void deleteSavedDataInDb();

        void saveData();

        void updateView(TrainingModel trainingModel);

        void textChanged(TrainingModel trainingModel, Integer viewTag, String viewText);

    }

}
