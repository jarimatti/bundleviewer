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
}
