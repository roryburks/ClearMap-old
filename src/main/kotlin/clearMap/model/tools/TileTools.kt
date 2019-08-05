package clearMap.model.tools

import clearMap.ui.views.tools.*
import rb.owl.IObservable
import rb.owl.Observable
import rb.owl.bindable.Bindable

class TileToolsModel : MToolsetManager {
    override val toolsetObservable = Observable<IToolsetManager.ToolsetPropertyObserver>()

    val Rect = TileRectTool(this)

    override val selectedToolBinding: Bindable<Tool> get() = Bindable(Rect)
    override var selectedTool: Tool by selectedToolBinding

    override fun triggerToolsetChanged(tool: Tool, property: ToolProperty<*>) {
        toolsetObservable.trigger {it.onToolPropertyChanged(tool, property)}
    }

    val tools = listOf<Tool>(Rect)
}

class TileRectTool( manager: MToolsetManager) : Tool(manager) {
    override val description: String get() = "Rectangle"
    override val iconX: Int get() = 0
    override val iconY: Int get() = 0

    val xBind by scheme.Property(DoubleBoxProperty("x", 0.0))
    var x by xBind
    val yBind by scheme.Property(DoubleBoxProperty("y", 0.0))
    var y by yBind
    val wBind by scheme.Property(DoubleBoxProperty("w", 0.0))
    var w by wBind
    val hBind by scheme.Property(DoubleBoxProperty("h", 0.0))
    var h by hBind
}