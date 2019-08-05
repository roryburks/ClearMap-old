package clearMap.ui.dialog

import clearMap.model.master.IMasterModel
import clearMap.ui.resources.SpiriteIcons
import sgui.components.IComponentProvider
import sguiSwing.SwIcon
import sguiSwing.components.jcomponent
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

enum class FileType {
    ImportImage
}

interface IDialog
{
    fun <T> runDialog(panel: (master: IMasterModel, ui: IComponentProvider) -> IDialogPanel<T>): T?
    fun pickFile( type : FileType) : File?
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

    override fun pickFile(type: FileType): File? {
        val fc = JFileChooser()

        val prefKey = when( type) {
            FileType.ImportImage -> "file.importImage"
        }

        val defaultFile = File(_master.preferences.getString(prefKey) ?: System.getProperty("user.dir"))

        fc.choosableFileFilters.forEach { fc.removeChoosableFileFilter(it) }
        val filters = when(type) {
            FileType.ImportImage -> listOf(
                FileNameExtensionFilter("Supported Image Files", "jpg", "jpeg", "png", "bmp"),
                FileNameExtensionFilter("JPEG File", "jpg", "jpeg"),
                FileNameExtensionFilter("PNG File", "png"),
                FileNameExtensionFilter("Bitmap File", "bmp"),
                fc.acceptAllFileFilter)
        }
        filters.forEach { fc.addChoosableFileFilter(it) }

        fc.currentDirectory = defaultFile
        fc.selectedFile = defaultFile

        val result = when(type) {
            FileType.ImportImage -> fc.showOpenDialog(null)
        }

        return if( result == JFileChooser.APPROVE_OPTION) {
            val file = fc.selectedFile

            _master.preferences.putString(prefKey, file.path)

            file
        } else null
    }
}