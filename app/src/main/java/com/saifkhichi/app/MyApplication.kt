package com.saifkhichi.app

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.orhanobut.hawk.Hawk
import com.stripe.Stripe
import com.stripe.android.PaymentConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init dependencies
        Hawk.init(this).build()

        // Configure Stripe SDK
        PaymentConfiguration.init(applicationContext, BuildConfig.STRIPE_PUBLIC_KEY)
        Stripe.apiKey = BuildConfig.STRIPE_SECRET_KEY

        // Enable dynamic colors with Material 3
        DynamicColors.applyToActivitiesIfAvailable(this);
    }

}