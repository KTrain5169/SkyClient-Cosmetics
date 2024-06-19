package co.skyclient.scc.gui.greeting.components

import cc.polyfrost.oneconfig.libs.elementa.ElementaVersion
import cc.polyfrost.oneconfig.libs.elementa.WindowScreen
import cc.polyfrost.oneconfig.libs.elementa.components.UIBlock
import cc.polyfrost.oneconfig.libs.elementa.components.UIImage
import cc.polyfrost.oneconfig.libs.elementa.components.UIText
import cc.polyfrost.oneconfig.libs.elementa.components.Window
import cc.polyfrost.oneconfig.libs.elementa.constraints.CenterConstraint
import cc.polyfrost.oneconfig.libs.elementa.constraints.animation.Animations
import cc.polyfrost.oneconfig.libs.elementa.dsl.*
import cc.polyfrost.oneconfig.libs.elementa.utils.withAlpha
import cc.polyfrost.oneconfig.libs.universal.GuiScale
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.Multithreading
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import java.awt.Color

open class GreetingSlide<T : GuiScreen>(private val nextGui: Class<T>?, val onClick: () -> Unit = {}) : WindowScreen(version = ElementaVersion.V1, drawDefaultBackground = false, restoreCurrentGuiOnClose = false, newGuiScale = GuiScale.scaleForScreenSize().ordinal) {
    init {
        if (previousScale == Int.MIN_VALUE) {
            previousScale = Minecraft.getMinecraft().gameSettings.guiScale
        }
    }

    private var hasInit = false

    private val background by UIBlock(VigilancePalette.getBackground()) constrain {
        x = 0.pixels()
        y = 0.pixels()
        width = 100.percent()
        height = 100.percent()
    } childOf window

    val blackbar by UIBlock(color = TRANSPARENT_BLACK.withAlpha(128)) constrain {
        x = 0.pixels()
        y = 90.percent()
        width = 100.percent()
        height = 10.percent()
    } childOf window

    private val skyclientLogo by UIImage.ofResource("/assets/scc/skyclient_logo.png") constrain {
        x = 2.pixels()
        y = CenterConstraint()
        height = HeightRelativeConstraint(0.75f)
        width = HeightRelativeConstraint(0.75f)
    } childOf blackbar

    override fun updateGuiScale() {
        newGuiScale = GuiScale.scaleForScreenSize().ordinal
        super.updateGuiScale()
    }

    private val skyclientText by UIText("SkyClient") constrain {
        x = 2.pixels(alignOpposite = true)
        y = CenterConstraint()
        color = Color.DARK_GRAY.toConstraint()
    } childOf blackbar

    private val fade by UIBlock(Color.BLACK) constrain {
        x = 0.pixels()
        y = 0.pixels()
        width = 100.percent()
        height = 100.percent()
    } childOf window

    private val button by ButtonComponent("Next") {
        setButtonFloat()
        displayNextScreen()
    } constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    } childOf blackbar

    fun hideNextButton() {
        button.hide(true)
    }

    protected fun displayNextScreen() {
        fade.setFloating(true)
        fade.animate {
            setColorAnimation(Animations.LINEAR, 0.5f, Color.BLACK.toConstraint())
        }
        Multithreading.runAsync {
            while (fade.getColor().alpha != 255) { ; }
            Minecraft.getMinecraft().addScheduledTask {
                onClick()
                displayScreen(nextGui?.getConstructor()?.newInstance())
            }
        }
    }

    protected open fun setButtonFloat() {
        button.setFloating(true)
    }

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (!hasInit) {
            hasInit = true
            try {
                @Suppress("UNCHECKED_CAST")
                currentSlide = mc.currentScreen::class.java as Class<GreetingSlide<*>>
            } catch (e: Exception) {
                e.printStackTrace()
            }
            fade.setFloating(true)
            fade.animate {
                setColorAnimation(Animations.LINEAR, 0.5f, TRANSPARENT_BLACK.toConstraint())
            }
        }
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
        if (fade.getColor().alpha == 0) {
            Window.enqueueRenderOperation {
                fade.setFloating(false)
                setButtonFloat()
            }
        }
    }

    companion object {
        val TRANSPARENT_BLACK = Color(0, 0, 0, 0)
        var previousScale = Int.MIN_VALUE
        var currentSlide: Class<GreetingSlide<*>>? = null
    }
}