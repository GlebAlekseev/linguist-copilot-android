package pro.linguistcopilot.core.utils.elmslie_decompose

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import vivid.money.elmslie.core.store.DefaultActor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.Actor

class RetainedElmStore<Event : Any, State : Any, Effect : Any, Command : Any>(
    initialState: State,
    reducer: StateReducer<Event, State, Effect, Command>,
    actor: Actor<Command, out Event>,
    startEvent: Event? = null,
) : Store<Event, Effect, State> by ElmStore(
    initialState = initialState,
    reducer = reducer,
    actor = actor.toDefaultActor(),
    startEvent = startEvent,
), InstanceKeeper.Instance

fun <Command : Any, Event : Any> Actor<Command, Event>.toDefaultActor() =
    DefaultActor<Command, Event> { command -> execute(command) }