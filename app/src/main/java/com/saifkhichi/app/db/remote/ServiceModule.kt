package com.saifkhichi.app.db.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * A module which informs the Hilt which [WebService] to use when
 * injecting dependencies.
 *
 * @author saifkhichi96
 * @since 1.0.0
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindWebService(
        service: FirebaseDatabaseService, // use Firebase Realtime Database
    ): WebService
}