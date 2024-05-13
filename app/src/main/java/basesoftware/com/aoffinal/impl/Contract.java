package basesoftware.com.aoffinal.impl;

import basesoftware.com.aoffinal.model.domain.TrainingModel;

public interface Contract {

    interface View {

        void checkSavedDataInDb();

        void updateView(TrainingModel trainingModel);

        void textChanged(Integer viewTag, String viewText);

        void saveQuestion();

        void saveResult(Boolean isSuccess);

        void deleteResult(Boolean isSuccess);

        void visitStoreQuestion();

    }

    interface Presenter {

        void initBinds(Contract.View view);

        void calculateClick();

        void checkSavedDataInDb();

        void deleteSavedDataInDb();

        void saveData();

        void updateView(TrainingModel trainingModel);

        void textChanged(Integer viewTag, String viewText);

        void calculateComplete();

        void saveResult(Boolean isSuccess);

        void deleteResult(Boolean isSuccess);
    }

}
