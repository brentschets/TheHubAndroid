package brentschets.com.projecthub.context

import android.app.Application
import brentschets.com.projecthub.injection.components.DaggerNetworkComponent
import brentschets.com.projecthub.injection.components.NetworkComponent
import brentschets.com.projecthub.injection.modules.NetworkModule

class App: Application() {

    /**
     * Er is een instance nodig van de dagger [NetworkComponent] om de injectie mee uit te voeren
     */
    companion object {
        lateinit var injector: NetworkComponent
    }

    override fun onCreate() {
        super.onCreate()
        injector = DaggerNetworkComponent.
                builder().
                networkModule(NetworkModule(this)).
                build()
    }
}