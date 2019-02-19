package com.funivan.idea.phpClean.inspections.deprecatedDocTag

import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.NonEmptyInputValidator
import com.intellij.ui.AddEditRemovePanel

class TagNamesListPanel(data: MutableList<String>) : AddEditRemovePanel<String>(MyTableModel(), data) {
    var modified = false
    override fun removeItem(tag: String): Boolean {
        modified = true
        return true
    }

    override fun editItem(fqName: String): String? {
        val result = Messages.showInputDialog(
                this,
                "Enter tag name:",
                "Edit",
                Messages.getQuestionIcon(),
                fqName,
                NonEmptyInputValidator()
        ) ?: return ""
        val created = result
        if (created == "" || created in data) {
            return null
        }
        modified = true
        return created
    }

    override fun addItem(): String? {
        val result = Messages.showInputDialog(
                this,
                "Enter tag name:",
                "Add",
                Messages.getQuestionIcon(),
                "",
                NonEmptyInputValidator()
        ) ?: return ""
        if (result in data) {
            return null
        }
        modified = true
        return result
    }

    class MyTableModel : AddEditRemovePanel.TableModel<String>() {
        override fun getField(o: String, columnIndex: Int) = o
        override fun getColumnName(columnIndex: Int) = "Tag"
        override fun getColumnCount() = 1
    }
}

