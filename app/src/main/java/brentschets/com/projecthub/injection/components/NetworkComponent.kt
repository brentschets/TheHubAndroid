package brentschets.com.projecthub.injection.components

import brentschets.com.projecthub.injection.modules.NetworkModule
import brentschets.com.projecthub.viewmodels.PostViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Deze [NetworkComponent] dient als tussenlaag tussen de [NetworkModule] en de effectieve [PostViewModel]
 */
@Singleton
/**
 * We hebben de netwerkmodule nodig voor het ophalen van de data
 */
@Component(modules = [NetworkModule::class])
interface NetworkComponent {
    /**
     * Doet dependency injection op de meegegeven [PostViewModel]
     *
     * @param postViewModel De [OrPostViewModelderViewModel] dat je wilt voorzien van dependency injection. Verplicht van type [PostViewModel].
     */
    fun inject(postViewModel: PostViewModel)
}