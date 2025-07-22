package basesoftware.com.aoffinal.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import basesoftware.com.aoffinal.data.repository.dto.CourseDbModel;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM CourseTable")
    Single<List<CourseDbModel>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<CourseDbModel> courses);

    @Query("DELETE FROM CourseTable")
    Completable deleteAll();

}