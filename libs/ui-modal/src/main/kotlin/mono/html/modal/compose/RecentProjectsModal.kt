/*
 * Copyright (c) 2023, tuanchauict
 */

@file:Suppress("FunctionName")

package mono.html.modal.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.browser.document
import mono.ui.compose.components.Icons
import mono.ui.compose.ext.classes
import mono.ui.compose.ext.onConsumeClick
import mono.ui.compose.ext.sideEffectFocus
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import mono.html.Div as MonoDiv

private typealias SelectAction = (ProjectItem, isRemoved: Boolean) -> Unit

class ProjectItem(val id: String, val name: String)

fun showRecentProjectsModal(
    projectItems: List<ProjectItem>,
    onSelect: SelectAction
) {
    val body = document.body ?: return
    val container = body.MonoDiv()
    renderComposable(container) {
        RecentProjectsModal(projectItems, onSelect) {
            container.remove()
        }
    }
}

@Composable
private fun RecentProjectsModal(
    projects: List<ProjectItem>,
    onSelect: SelectAction,
    onDismiss: () -> Unit
) {
    Div(
        attrs = {
            classes("recent-project-modal")

            onClick {
                onDismiss()
            }
        }
    ) {
        Div(attrs = {
            classes("container")

            onConsumeClick {}
        }) {
            val filter = remember { mutableStateOf("") }
            val requestingRemoveProjectId = remember { mutableStateOf("") }

            FilterInput {
                filter.value = it
                requestingRemoveProjectId.value = ""
            }
            ProjectList(filter.value, projects, requestingRemoveProjectId.value) {
                when (it) {
                    is Action.Open -> {
                        onSelect(it.project, false)
                        onDismiss()
                    }

                    is Action.RemoveConfirm -> {
                        onSelect(it.project, true)
                        onDismiss()
                    }

                    is Action.RequestRemove -> requestingRemoveProjectId.value = it.project.id
                    Action.SuspendRemove -> requestingRemoveProjectId.value = ""
                }
            }
        }
    }
}

@Composable
private fun FilterInput(onChange: (String) -> Unit) {
    Div(
        attrs = {
            classes("filter-container")
        }
    ) {
        Input(type = InputType.Text) {
            id("recent-project-filter-input")
            placeholder("Filter by name")

            onInput { onChange(it.value) }
        }

        sideEffectFocus("#recent-project-filter-input")
    }
}

@Composable
private fun ProjectList(
    filter: String,
    projects: List<ProjectItem>,
    requestingRemoveProjectId: String,
    onAction: (Action) -> Unit
) {
    val filteredProjects = if (filter.isNotEmpty()) {
        projects
            .filter { it.name.contains(filter, ignoreCase = true) }
            .sortedBy { it.name }
    } else {
        projects
    }

    if (filteredProjects.isNotEmpty()) {
        Div(
            attrs = { classes("list") }
        ) {
            for (project in filteredProjects) {
                val isRemoveConfirming = project.id == requestingRemoveProjectId
                ProjectContent(project, isRemoveConfirming, onAction)
            }
        }
    } else {
        Div(
            attrs = { classes("no-item") }
        ) {
            Span {
                Text("Not found")
            }
        }
    }
}

@Composable
private fun ProjectContent(
    item: ProjectItem,
    isRemoveConfirming: Boolean,
    onAction: (Action) -> Unit
) {
    Div(
        attrs = {
            classes(
                "item" to true,
                "removing" to isRemoveConfirming,
                "normal" to !isRemoveConfirming
            )
            if (!isRemoveConfirming) {
                onConsumeClick { onAction(Action.Open(item)) }
            }
        }
    ) {
        Div {
            if (isRemoveConfirming) {
                Text("""Delete "${item.name}"?""")
            } else {
                Text(item.name)
            }
        }
        Div(
            attrs = { classes("actions") }
        ) {
            if (isRemoveConfirming) {
                RemoveProjectConfirm(item, onAction)
            } else {
                RemoveProjectRequest(item, onAction)
            }
        }
    }
}

@Composable
private fun RemoveProjectConfirm(item: ProjectItem, onAction: (Action) -> Unit) {
    Div(
        attrs = {
            classes("action")
            onConsumeClick {
                onAction(Action.RemoveConfirm(item))
            }
        }
    ) {
        Icons.Remove(12)
    }
    Div(
        attrs = {
            classes("action")
            onConsumeClick {
                onAction(Action.SuspendRemove)
            }
        }
    ) {
        Icons.Cross(12)
    }
}

@Composable
private fun RemoveProjectRequest(project: ProjectItem, onAction: (Action) -> Unit) {
    Div(
        attrs = {
            classes("action")
            onConsumeClick { onAction(Action.RequestRemove(project)) }
        }
    ) {
        Icons.Remove(12)
    }
}

private sealed class Action {
    data class Open(val project: ProjectItem) : Action()
    data class RequestRemove(val project: ProjectItem) : Action()
    data class RemoveConfirm(val project: ProjectItem) : Action()
    object SuspendRemove : Action()
}
