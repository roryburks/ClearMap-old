package clearMap.ui.dialog

import sgui.components.IComponentProvider
import sgui.components.crossContainer.ICrossPanel

data class NewMapDialogResult(
    val width: Int,
    val height: Int)

class NewMapDialog(
    private val _ui : IComponentProvider
)
    : ICrossPanel by _ui.CrossPanel(), IDialogPanel<NewMapDialogResult>
{

    override val result: NewMapDialogResult get() = NewMapDialogResult(tfWidth.value, tfHeight.value)
    override val title: String get() = "New Map"

    private val tfWidth = _ui.IntField(min = 1).also { it.value = 1000 }
    private val tfHeight = _ui.IntField(min = 1).also { it.value = 500 }

    init /* Appearance */ {
        setLayout {
            rows.add(_ui.Label("Create New Map"))
            rows.addGap(3)
            rows += {
                add(_ui.Label("Width:"), width = 100)
                add(tfWidth, 80)
                height = 16
            }
            rows += {
                add(_ui.Label("Height:"), width = 100)
                add(tfHeight, 80)
                height = 16
            }
        }
    }
}