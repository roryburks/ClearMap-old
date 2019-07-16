package clearMap.model.tools

import clearMap.ui.views.tools.*
import rb.owl.Observable
import rb.owl.bindable.Bindable

class CollisionObjectToolsModel  : MToolsetManager {

    override val toolsetObservable = Observable<IToolsetManager.ToolsetPropertyObserver>()

    val Circle = ColCircleTool(this)
    val LineSegment = ColLineSegmentTool(this)

    override val selectedToolBinding = Bindable<Tool>(Circle)
    override var selectedTool by selectedToolBinding

    override fun triggerToolsetChanged(tool: Tool, property: ToolProperty<*>) {
        toolsetObservable.trigger {it.onToolPropertyChanged(tool, property)}
    }

    val tools = listOf(
        Circle, LineSegment)



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

class ColLineSegmentTool(manager: MToolsetManager) : Tool(manager)
{
    override val description: String get() = "LineSegment"
    override val iconX: Int get() = 0
    override val iconY: Int get() = 0

    val x1Bind by scheme.Property(DoubleBoxProperty("x1", 0.0))
    var x1 by x1Bind
    val y1Bind by scheme.Property(DoubleBoxProperty("y1", 0.0))
    var y1 by y1Bind
    val x2Bind by scheme.Property(DoubleBoxProperty("x2", 0.0))
    var x2 by x2Bind
    val y2Bind by scheme.Property(DoubleBoxProperty("y2", 0.0))
    var y2 by y2Bind
}