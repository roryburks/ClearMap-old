package clearMap.ui.dialog

import clearMap.model.IMasterModel
import clearMap.ui.resources.SpiriteIcons
import sgui.components.IComponentProvider
import sguiSwing.SwIcon
import sguiSwing.components.jcomponent
import javax.swing.JOptionPane

interface IDialog
{
    fun <T> runDialog(panel: (master: IMasterModel, ui: IComponentProvider) -> IDialogPanel<T>): T?
}

class Dialog(
    private val _master: IMasterModel,
    private val _ui : IComponentProvider) : IDialog
{
    override fun <T> runDialog(panel: (master: IMasterModel, ui: IComponentProvider) -> IDialogPanel<T>): T? = runDialogPanel(panel(_master, _ui))

    fun <T> runDialogPanel(panel: IDialogPanel<T>) = when(JOptionPane.showConfirmDialog(
        null,
        panel.jcomponent,
        panel.title,
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE,
        ((panel.icon) as? SwIcon)?.icon ?:  SpiriteIcons.BigIcons.NewLayer.icon))
    {
        JOptionPane.OK_OPTION -> panel.result
        else -> null
    }
}