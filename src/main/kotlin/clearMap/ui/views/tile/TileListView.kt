package clearMap.ui.views.tile

import clearMap.model.map.CwMap
import clearMap.model.map.CwTileModel
import clearMap.model.master.IMasterModel
import clearMap.ui.systems.omniContainer.IOmniComponent
import rb.IContract
import rb.owl.bindable.addObserver
import rb.owl.bindableMList.*
import sgui.components.IComponent
import sgui.components.IComponentProvider
import sgui.components.ITreeViewNonUI
import sguiSwing.SwIcon

class TileListView(
    private val _master: IMasterModel,
    private val _ui: IComponentProvider)
    : IOmniComponent
{


    override val component: IComponent get() = _panel
    override val icon: SwIcon? get() =  null
    override val name: String get() = "Tile Sheets"

    private val _panel = _ui.CrossPanel()

    private val _listView = _ui.TreeView<CwTileModel>()

    init {
        _panel.setLayout { rows.add(_listView, flex = 1f) }
    }

    private fun rebuild() {
        _listView.clearRoots()

        val currentMap  = _master.mapSpace.mapsBind.currentlySelected ?: return
        val selected = currentMap.tilesBind.currentlySelected
        _listView.constructTree {
            currentMap.tiles.forEach { Node(it, _attributes) }
        }
    }

    private val _attributes = object : ITreeViewNonUI.ITreeNodeAttributes<CwTileModel> {
        override fun makeComponent(t: CwTileModel): ITreeViewNonUI.ITreeComponent =
            ITreeViewNonUI.SimpleTreeComponent(_ui.Label(t.name))

    }


    // Bindings
    val tileObs = object : IMutableListObserver<CwTileModel> {
        override val trigger = object: IListTriggers<CwTileModel> {
            override fun elementsAdded(index: Int, elements: Collection<CwTileModel>) {rebuild()}
            override fun elementsRemoved(elements: Collection<CwTileModel>) {rebuild()}
            override fun elementsChanged(changes: Set<ListChange<CwTileModel>>) {rebuild()}
            override fun elementsPermuted(permutation: ListPermuation) {rebuild()}
        }
    }
    val selectedMapK  = _master.mapSpace.mapsBind.currentlySelectedBind.addObserver { new, _ ->
        tilesK?.void()
        tilesK = new?.tilesBind?.addObserver(tileObs, true)
        rebuild()
    }
    var tilesK : IContract? =null

    val selectedK = _listView.selectedBind.addObserver { new, old ->
        if( new != null)
            _master.mapSpace.mapsBind.currentlySelected?.tilesBind?.currentlySelected = new
    }

    override fun close() {
        selectedMapK.void()
        tilesK?.void()
        selectedK.void()
    }
}