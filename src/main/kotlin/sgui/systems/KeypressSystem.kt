package sgui.systems

enum class KeypressCode {
    KC_ESC
}

interface IKeypressSystem
{
    val holdingSpace: Boolean
    val hotkeysEnabled: Boolean
}

interface MKeypressSystem : IKeypressSystem
{
    override var holdingSpace: Boolean
    override var hotkeysEnabled: Boolean
}

object KeypressSystem : MKeypressSystem {
    override var holdingSpace: Boolean = false
    override var hotkeysEnabled: Boolean = true
}