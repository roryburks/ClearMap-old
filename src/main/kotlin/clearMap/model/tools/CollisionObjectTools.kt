package clearMap.model.tools

import clearMap.ui.views.tools.*
import rb.owl.Observable
import rb.owl.bindable.Bindable

class CollisionObjectToolsModel  : MToolsetManager {
    val Circle = ColCircleTool(this)

    override val toolsetObservable = Observable<IToolsetManager.ToolsetPropertyObserver>()

    override val selectedToolBinding = Bindable<Tool>(Circle)
    override var selectedTool by selectedToolBinding

    override fun triggerToolsetChanged(tool: Tool, property: ToolProperty<*>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val tools = listOf(ColCircleTool(this))

}


class ColCircleTool(manager: MToolsetManager) : Tool(manager)
{
    override val description: String get() = "Circle"
    override val iconX: Int get() = 0
    override val iconY: Int get() = 0

    val xBind by scheme.Property(DoubleBoxProperty("x", 0.0))
    var x by xBind
    val yBind by scheme.Property(DoubleBoxProperty("y", 0.0))
    var y by yBind
    val rBind by scheme.Property(DoubleBoxProperty("r", 0.0))
    var r by rBind
}