package dk.edutor.core.view

import java.awt.GraphicsConfigTemplate

open class QuestIdentifier(val id: Int)

class QuestSummary(id: Int, val title: String, val template: String) : QuestIdentifier(id)