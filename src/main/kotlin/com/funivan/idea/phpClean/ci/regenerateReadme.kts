import java.io.File

class Block(private val file: File) {
    fun file() = file
    fun uid() = file.name.replace("Inspection.html", "")
    fun short() = file.readText()
            .replace(Regex("<!-- main -->.*"), "")
            .replace("<code>", "```php")
            .replace("</code>", "```").trim()
}

var content = ""
val inspectionDirectory = "../../../../../../resources/inspectionDescriptions"
val directory = "../inspections"
File(directory)
        .walkTopDown()
        .filter { it.name.contains("Inspection.kt") }
        .map { Block(File(it.path.replace(".kt", ".html"))) }
        .forEach {
            val description = "#### ${it.uid()} \n${it.short()}"
            content = content + description + "\n\n"
            val file = File(inspectionDirectory + "/" + it.file().name)
            if (!file.exists()) {
                file.createNewFile()
            }
            file.writeText(it.file().readText())
        }

println(content)