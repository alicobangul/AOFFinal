package basesoftware.com.aoffinal.di;


import android.content.Context;
import androidx.room.Room;
import basesoftware.com.aoffinal.model.roomdb.TrainingDao;
import basesoftware.com.aoffinal.model.roomdb.TrainingDatabase;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;
import io.reactivex.disposables.CompositeDisposable;

@Module
@InstallIn(ActivityComponent.class)
public class AofFinalModule {

    @ActivityScoped
    @Provides
    public static CompositeDisposable compositeDisposableProvider() { return new CompositeDisposable(); }

    @ActivityScoped
    @Provides
    public static TrainingDatabase trainingDatabaseProvider(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, TrainingDatabase.class, "Training").allowMainThreadQueries().build();
    }

    @ActivityScoped
    @Provides
    public static TrainingDao trainingDaoProvider(TrainingDatabase trainingDatabase) { return trainingDatabase.trainingDao(); }

}
