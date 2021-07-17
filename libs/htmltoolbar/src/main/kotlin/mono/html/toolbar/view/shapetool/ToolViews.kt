@file:Suppress("FunctionName")

package mono.html.toolbar.view.shapetool

import kotlinx.html.div
import kotlinx.html.js.div
import mono.html.toolbar.view.Tag
import mono.html.toolbar.view.shapetool.Class.ADD_BOTTOM_SPACE
import mono.html.toolbar.view.shapetool.Class.CENTER_EVEN_SPACE
import mono.html.toolbar.view.shapetool.Class.CENTER_VERTICAL
import mono.html.toolbar.view.shapetool.Class.DIVIDER
import mono.html.toolbar.view.shapetool.Class.GRID
import mono.html.toolbar.view.shapetool.Class.MONOFONT
import mono.html.toolbar.view.shapetool.Class.ROW
import mono.html.toolbar.view.shapetool.Class.SECTION
import mono.html.toolbar.view.shapetool.Class.SECTION_TITLE
import mono.html.toolbar.view.shapetool.Class.SMALL_SPACE
import mono.html.toolbar.view.shapetool.Class.TOOL
import org.w3c.dom.HTMLDivElement

internal fun Tag.Section(
    title: String,
    hasDivider: Boolean = true,
    isSmallSpace: Boolean = false,
    block: Tag.() -> Unit
): HTMLDivElement {
    val sectionClasses = classes(
        SECTION,
        DIVIDER x hasDivider,
        SMALL_SPACE x isSmallSpace
    )

    return div(sectionClasses) {
        if (title.isNotEmpty()) {
            div(classes(SECTION_TITLE)) { +title }
        }
        block()
    }
}

internal fun Tag.Tool(
    hasMoreBottomSpace: Boolean = false,
    block: Tag.() -> Unit
): HTMLDivElement {
    val toolClasses = classes(
        TOOL,
        ADD_BOTTOM_SPACE x hasMoreBottomSpace
    )
    return div(toolClasses) {
        block()
    }
}

internal fun Tag.Row(
    isMoreBottomSpaceRequired: Boolean = false,
    isCenterEvenSpace: Boolean = false,
    isVerticalCenter: Boolean = false,
    isMonoFont: Boolean = false,
    isGrid: Boolean = false,
    block: Tag.() -> Unit
): HTMLDivElement {
    val classes = classes(
        ROW,
        ADD_BOTTOM_SPACE x isMoreBottomSpaceRequired,
        CENTER_EVEN_SPACE x isCenterEvenSpace,
        CENTER_VERTICAL x isVerticalCenter,
        GRID x isGrid,
        MONOFONT x isMonoFont
    )
    return div(classes) {
        block()
    }
}