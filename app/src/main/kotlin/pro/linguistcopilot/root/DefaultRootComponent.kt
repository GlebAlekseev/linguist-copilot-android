package pro.linguistcopilot.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.details.DetailsComponent
import pro.linguistcopilot.list.ListComponent

class DefaultRootComponent @AssistedInject constructor(
    private val listFactory: ListComponent.Factory,
    private val detailsFactory: DetailsComponent.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {
    private val nav = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.RootState>> =
        childStack(
            source = nav,
            key = "test1",
            initialConfiguration = Config.List,
            serializer = Config.serializer(),
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, context: ComponentContext): RootComponent.RootState =
        when (config) {
            is Config.List -> RootComponent.RootState.ListState(listComponent(context))
            is Config.Details -> RootComponent.RootState.DetailsState(detailsComponent(context, config))
        }

    private fun listComponent(context: ComponentContext): ListComponent =
        listFactory(
            componentContext = context,
            onItemSelected = { nav.push(Config.Details(itemId = it)) },
        )

    private fun detailsComponent(context: ComponentContext, config: Config.Details): DetailsComponent =
        detailsFactory(
            componentContext = context,
            itemId = config.itemId,
            onFinished = nav::pop,
        )

    @Serializable
    private sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Details(val itemId: String) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}