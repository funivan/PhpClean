import java.io.File

class Block(private val file: File) {
    fun name() = file.name.replace("Inspection.html", "")
    fun short() = file.readText()
            .replace(Regex("<!-- main -->.*"), "")
            .replace("<code>", "```php")
            .replace("</code>", "```").trim()
}

var content = ""
val directory = "../../../../../../resources/inspectionDescriptions"
File(directory)
        .list()
        .map { Block(File(directory + "/" + it)) }
        .forEach {
            val description = "#### ${it.name()} \n${it.short()}"
            content = content + description + "\n\n"
        }

println(content)