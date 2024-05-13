package basesoftware.com.aoffinal.model.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TrainingDbModel.class}, version = 1, exportSchema = false)
public abstract class TrainingDatabase extends RoomDatabase {
    public abstract TrainingDao trainingDao();
}