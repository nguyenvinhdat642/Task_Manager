package com.example.taskmanager.di

import android.app.Application
import androidx.room.Room
import com.example.taskmanager.data.database.TaskDatabase
import com.example.taskmanager.data.database.repository.PlanRepository
import com.example.taskmanager.data.database.repository.PlanRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase{
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "task_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlanRepository(db: TaskDatabase): PlanRepository{
        return PlanRepositoryImpl(db.planDao())
    }
}