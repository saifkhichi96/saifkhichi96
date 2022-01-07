package com.saifkhichi.app.db.remote

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
 * A module which informs the Hilt which [WebService] to use when
 * injecting dependencies.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
@Module
@InstallIn(ViewModelComponent::class, ActivityComponent::class)
object ServiceModule {

    @Provides
    fun provideWebService(@ApplicationContext context: Context): WebService {
        return FirebaseDatabaseService() // use Firebase Realtime Database
    }

    @Provides
    fun provideRemoteDatabase(@ApplicationContext context: Context): RemoteDatabase {
        return FirebaseDatabase(context, persistent = null)
    }

}