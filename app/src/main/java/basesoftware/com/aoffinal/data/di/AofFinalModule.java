package basesoftware.com.aoffinal.data.di;

import android.content.Context;
import androidx.room.Room;
import javax.inject.Singleton;
import basesoftware.com.aoffinal.data.local.CourseDao;
import basesoftware.com.aoffinal.data.local.CourseDatabase;
import basesoftware.com.aoffinal.data.repository.RepositoryImpl;
import basesoftware.com.aoffinal.domain.repository.Repository;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AofFinalModule {

    @Singleton
    @Provides
    public static CourseDatabase courseDatabaseProvider(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, CourseDatabase.class, "Course").fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    @Singleton
    @Provides
    public static CourseDao courseDaoProvider(CourseDatabase courseDatabase) { return courseDatabase.courseDao(); }

    @Singleton
    @Provides
    public static Repository injectRepository(CourseDao courseDao) { return new RepositoryImpl(courseDao); }

}