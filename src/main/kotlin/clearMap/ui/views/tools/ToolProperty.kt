package clearMap.ui.views.tools

// Candidate for CwShared

import clearMap.model.commands.ICommand
import rb.owl.bindable.Bindable
import rb.vectrix.linear.Vec2f


sealed class ToolProperty<T>( default: T) {
    abstract val hrName: String

    val valueBind = Bindable(default)
    var value by valueBind
}


class SliderProperty(override val hrName: String, default: Float, val min: Float, val max: Float) : ToolProperty<Float>(default)
class SizeProperty( override val hrName: String, default: Float) : ToolProperty<Float>(default)
class CheckBoxProperty( override val hrName: String, default: Boolean) : ToolProperty<Boolean>(default)
class DropDownProperty<T>( override val hrName: String, default: T, val values: Array<T>) : ToolProperty<T>(default) {
    fun setNthOption(n: Int) {value = values.getOrNull(n) ?: value}
}
class RadioButtonProperty<T>( override val hrName: String, default: T, val values: Array<T>) : ToolProperty<T>(default)
class ButtonProperty(override val hrName: String, val command: ICommand) : ToolProperty<Boolean>(false)
class FloatBoxProperty(override val hrName: String, default: Float) : ToolProperty<Float>(default)
class DualFloatBoxProperty(override val hrName: String, val label1: String, val label2: String, default: Vec2f) : ToolProperty<Vec2f>(default)
