package sguiSwing

import sgui.systems.KeypressCode
import java.awt.event.KeyEvent

object SwKeycodeMapper {
    fun map(vkCode: Int) : KeypressCode? = when(vkCode) {
        KeyEvent.VK_ESCAPE -> KeypressCode.KC_ESC
        else -> null
    }
}