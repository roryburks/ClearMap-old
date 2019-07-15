package clearMap.ui.views.tools

import rb.owl.bindable.addObserver
import kotlin.reflect.KProperty


// Candidate for CwShared
interface IToolsetManager
{
    fun triggerToolsetChanged(tool: Tool,  property: ToolProperty<*>)
}


abstract class Tool(private val _manager: IToolsetManager) {
    abstract val description: String
    abstract val iconX: Int
    abstract val iconY: Int



    protected val scheme = ToolScheme()
    val properties: List<ToolProperty<*>> get() = scheme.properties

    inner class ToolScheme {
        internal val properties = mutableListOf<ToolProperty<*>>()

        fun <T,R> Property( t: T) : ToolPropDelegate<R> where T : ToolProperty<R> {
            properties.add(t)
            t.valueBind.addObserver { _, _ -> _manager.triggerToolsetChanged(this@Tool, t) }
            return ToolPropDelegate(t)
        }

        inner class ToolPropDelegate<T> internal constructor(val toolProperty: ToolProperty<T>) {
            operator fun getValue(thisRef: Any, prop: KProperty<*>) = toolProperty.valueBind
        }
    }
}