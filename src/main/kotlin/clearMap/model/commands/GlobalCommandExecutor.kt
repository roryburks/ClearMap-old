package clearMap.model.commands


import clearMap.hybrid.Hybrid
import clearMap.model.master.IMasterModel
import clearMap.model.map.CwMap
import clearMap.model.map.CwTileModel
import clearMap.ui.dialog.FileType
import clearMap.ui.dialog.NewMapDialog
import rbJvm.glow.awt.ImageBI
import javax.imageio.ImageIO

//import clearMap.ui.dialog.NewMapDialog

class GlobalCommandExecutor( private val _master : IMasterModel) : ICommandExecutor{
    override fun executeCommand(string: String, extra: Any?): Boolean {
        try
        {
            commands[string]?.action?.invoke(_master) ?: return false
            return true
        }catch (e : CommandNotValidException)
        {
            return false
        }
    }

    override val validCommands: List<String> get() = commands.values.map { it.commandString }
    override val domain: String get() = "global"

}

private val commands  = HashMap<String,GlobalCommand>()
class GlobalCommand
internal constructor(
    val name: String,
    val action: (master: IMasterModel)->Unit)
    : ICommand
{
    init {commands[name] = this}

    override val commandString: String get() = "global.$name"
    override val keyCommand: KeyCommand get() = KeyCommand(commandString)
}

object GlobalCommands {
    val NewMap = GlobalCommand("newMap") {
        val newMapResults = it.dialog
            .runDialog { master, ui -> NewMapDialog(ui) }
            ?: return@GlobalCommand
        val newMap = CwMap(newMapResults.width, newMapResults.height)
        it.mapSpace.mapsBind.list.add(newMap)
        it.mapSpace.mapsBind.currentlySelected = newMap
    }

    val ImportTile = GlobalCommand("importTile") {
        val map = it.mapSpace.mapsBind.currentlySelected ?: return@GlobalCommand
        val toImportFile = it.dialog.pickFile(FileType.ImportImage) ?: return@GlobalCommand
        val img = ImageIO.read(toImportFile)
        val internalImg = Hybrid.imageConverter.convertToInternal(ImageBI(img))
        map.tiles.add(CwTileModel(internalImg, toImportFile.nameWithoutExtension))
    }
}