package clearMap.model.penner

import sgui.components.events.MouseEvent

interface IPenner{
    fun step()
    fun penDown(button: MouseEvent.MouseButton)
    fun penUp(button: MouseEvent.MouseButton)
    fun reset()

    fun rawUpdateX(rawX: Int)
    fun rawUpdateY(rawY: Int)
    fun rawUpdatePressure(rawPressure: Float)
}

class Penner : IPenner
{
    override fun step() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun penDown(button: MouseEvent.MouseButton) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun penUp(button: MouseEvent.MouseButton) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reset() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rawUpdateX(rawX: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rawUpdateY(rawY: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun rawUpdatePressure(rawPressure: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}