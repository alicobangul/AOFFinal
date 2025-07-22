package basesoftware.com.aoffinal.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;

@Database(entities = {CourseDbModel.class}, version = 2, exportSchema = false)
public abstract class CourseDatabase extends RoomDatabase {
    public abstract CourseDao courseDao();
}