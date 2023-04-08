package basesoftware.com.aoffinal.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import basesoftware.com.aoffinal.model.TrainingModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface TrainingDao {

    @Query("SELECT * FROM Training")
    Flowable<List<TrainingModel>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(TrainingModel training);

    @Query("DELETE FROM Training")
    Completable delete();

}
