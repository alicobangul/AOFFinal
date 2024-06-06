package basesoftware.com.aoffinal.impl;

import javax.annotation.Nullable;

import basesoftware.com.aoffinal.model.roomdb.TrainingDbModel;

public interface IModelObserver {

    void saveResult(Boolean isSuccess);

    void savedDataControl(@Nullable TrainingDbModel trainingDbModel);

    void deleteResult(Boolean isSuccess);

}
