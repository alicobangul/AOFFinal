package basesoftware.com.aoffinal.impl;

import javax.annotation.Nullable;

import basesoftware.com.aoffinal.data.model.TrainingDbModel;

public interface IModelObserver {

    void saveResult(Boolean isSuccess);

    void savedDataControl(@Nullable TrainingDbModel trainingDbModel);

    void deleteResult(Boolean isSuccess);

}
