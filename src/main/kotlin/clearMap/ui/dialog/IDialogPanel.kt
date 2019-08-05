package clearMap.ui.dialog

import sgui.IIcon
import sgui.components.IComponent


interface IDialogPanel<T> : IComponent {
    val result : T
    val title: String
    val icon: IIcon? get() = null
}