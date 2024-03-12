package pro.linguistcopilot.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.di.scope.AppComponentScope
import pro.linguistcopilot.feature.book.room.BookItemDao
import pro.linguistcopilot.room.AppDatabase

@Module
interface RoomModule {
    companion object {
        @AppComponentScope
        @Provides
        fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }

        @AppComponentScope
        @Provides
        fun provideBookItemDao(appDatabase: AppDatabase): BookItemDao {
            return appDatabase.bookItemDao()
        }
    }
}