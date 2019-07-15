package clearMap.ui.views.tools

import rb.owl.IObservable
import rb.owl.bindable.Bindable
import rb.owl.bindable.addObserver
import kotlin.reflect.KProperty


// Candidate for CwShared
interface IToolsetManager
{
    val selectedToolBinding: Bindable<Tool>
    var selectedTool: Tool

    interface ToolsetPropertyObserver {
        fun onToolPropertyChanged( tool: Tool, property: ToolProperty<*>)
    }
    val toolsetObservable : IObservable<ToolsetPropertyObserver>

}
interface MToolsetManager : IToolsetManager
{
    fun triggerToolsetChanged(tool: Tool,  property: ToolProperty<*>)
}


abstract class Tool(private val _manager: MToolsetManager) {
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