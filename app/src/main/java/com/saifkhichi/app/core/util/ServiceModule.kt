package com.saifkhichi.app.core.util

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.saifkhichi96.android.db.FirebaseDatabase
import io.github.saifkhichi96.android.db.RemoteDatabase

/**
 * ServiceModule provides the dependencies for the services.
 *
 * This module is used by the Hilt framework to inject the dependencies into the services.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
@Module
@InstallIn(ViewModelComponent::class, ActivityComponent::class)
object ServiceModule {

    @Provides
    fun provideRemoteDatabase(@ApplicationContext context: Context): RemoteDatabase {
        return FirebaseDatabase(context, persistent = null)
    }

}