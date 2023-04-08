package basesoftware.com.aoffinal.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import basesoftware.com.aoffinal.model.TrainingModel;

@Database(entities = {TrainingModel.class}, version = 1)
public abstract class TrainingDatabase extends RoomDatabase {
    public abstract TrainingDao trainingDao();
}