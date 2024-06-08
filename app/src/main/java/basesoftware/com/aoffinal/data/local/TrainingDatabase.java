package basesoftware.com.aoffinal.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import basesoftware.com.aoffinal.data.model.TrainingDbModel;

@Database(entities = {TrainingDbModel.class}, version = 1, exportSchema = false)
public abstract class TrainingDatabase extends RoomDatabase {
    public abstract TrainingDao trainingDao();
}