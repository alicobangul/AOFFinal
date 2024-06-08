package basesoftware.com.aoffinal.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import basesoftware.com.aoffinal.data.model.TrainingDbModel;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface TrainingDao {

    @Query("SELECT * FROM TrainingTable")
    Single<TrainingDbModel> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(TrainingDbModel trainingDbModel);

    @Query("DELETE FROM TrainingTable")
    Completable deleteAll();

}
