package clearMap.ui.systems.omniContainer

// Candidate for CwShared
import sgui.components.IComponent
import sguiSwing.SwIcon

interface IOmniComponent {
    val component: IComponent
    val icon : SwIcon?
    val name : String

    fun close() {}
}