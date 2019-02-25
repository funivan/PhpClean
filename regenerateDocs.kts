import java.io.File

class Block(private val file: File) {
    fun file() = file
    fun uid() = file.name.replace("Inspection.html", "")
    fun short() = file.readText()
            .replace(Regex("<!-- main -->(.*)", RegexOption.DOT_MATCHES_ALL), "")
            .replace("<code>", "```php")
            .replace("</code>", "```").trim()
}

var changed = 0
val blocks = mutableListOf<Block>()
val inspectionDirectory = "src/main/resources/inspectionDescriptions"
val directory = "src/main/kotlin/com/funivan/idea/phpClean/inspections"
File(directory)
        .walkTopDown()
        .filter { it.name.contains("Inspection.kt") }
        .map { Block(File(it.path.replace(".kt", ".html"))) }
        .forEach {
            blocks.add(it)
            val file = File(inspectionDirectory + "/" + it.file().name)
            if (!file.exists()) {
                file.createNewFile()
            }
            if (file.readText() != it.file().readText()) {
                changed++
                file.writeText(it.file().readText())
            }
        }
val readme = File("README.md")
var content = readme.readText()
content = content.replace(
        Regex("(<!-- inspections -->)(.+)", RegexOption.DOT_MATCHES_ALL),
        "$1"
)
content = content + "\n" + blocks.sortedBy { it.uid() }
        .map { "#### ${it.uid()} \n${it.short()}\n" }
        .joinToString("")
if (readme.readText() != content) {
    changed++
    readme.writeText(content)
}
println(changed)