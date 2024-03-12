package pro.linguistcopilot.feature.content.library.mySources.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import javax.inject.Inject

class MySourcesReducer @Inject constructor() :
    ScreenDslReducer<MySourcesEvent, MySourcesEvent.Ui, MySourcesEvent.Internal, MySourcesState, MySourcesEffect, MySourcesCommand>(
        MySourcesEvent.Ui::class,
        MySourcesEvent.Internal::class,
    ) {
    override fun Result.internal(event: MySourcesEvent.Internal) {
        when (event) {
            is MySourcesEvent.Internal.Loaded -> {
                state {
                    MySourcesState.Idle(event.list)
                }
            }
        }
    }

    override fun Result.ui(event: MySourcesEvent.Ui) {
        when (event) {
            MySourcesEvent.Ui.Start -> {
                state { MySourcesState.Loading }
                commands { +MySourcesCommand.GetList }
            }
        }
    }
}