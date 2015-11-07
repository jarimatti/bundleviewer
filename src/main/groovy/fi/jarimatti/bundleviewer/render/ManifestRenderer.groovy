package fi.jarimatti.bundleviewer.render

import java.util.jar.Manifest

/**
 * Render a bundle manifest to string.
 */
class ManifestRenderer {

    /**
     * Indentation for rendering the manifest to string.
     */
    private indentString

    ManifestRenderer(int indent = 0) {
        indentString = ""
        indent.times {
             indentString += " "
        }
    }

    def render(Manifest m) {
        def baos = new ByteArrayOutputStream()
        m.write(baos)
        def sw = new StringWriter()
        baos.toString().eachLine {
            sw.println(indentString + it)
        }
        sw
    }

    private displayEntries(final Manifest manifest, OutputStream out) {
        if (manifest.entries.isEmpty()) {
            out.withPrintWriter {
                println(indentString + "No entries.")
            }
        } else {
            out.withPrintWriter {
                println(indentString + "Entries (${manifest.entries.size()}):")
                manifest.entries.each {
                    println(indentString + "  Entry key = $it.key")
                    it.value.each {
                        println(indentString + "    $it.key = $it.value")
                    }
                }
            }
        }
    }

    private displayMainAttributes(Manifest manifest, OutputStream out) {
        out.withPrintWriter {
            println(indentString + "Main attributes (${manifest.mainAttributes.size()}):")
            manifest.mainAttributes.each {
                println(indentString + "  $it.key = $it.value")
            }
        }
    }

}
