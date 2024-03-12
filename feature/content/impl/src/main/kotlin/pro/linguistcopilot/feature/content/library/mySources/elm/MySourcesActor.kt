package pro.linguistcopilot.feature.content.library.mySources.elm

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.linguistcopilot.feature.book.usecase.GetGoodBookListUseCase
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject


class MySourcesActor @Inject constructor(
    private val getGoodBookListUseCase: GetGoodBookListUseCase,
) : Actor<MySourcesCommand, MySourcesEvent.Internal> {
    override fun execute(command: MySourcesCommand): Flow<MySourcesEvent.Internal> {
        return when (command) {
            is MySourcesCommand.GetList -> flow {
                delay(1000)
                getGoodBookListUseCase.invoke().collect {
                    emit(
                        MySourcesEvent.Internal.Loaded(it)
                    )
                }
            }
        }
    }
}