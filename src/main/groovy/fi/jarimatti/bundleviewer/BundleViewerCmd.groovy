package fi.jarimatti.bundleviewer

import fi.jarimatti.bundleviewer.render.ManifestRenderer

import java.util.jar.Manifest

/**
 * Command line program to view bundle contents.
 */
class BundleViewerCmd {

    def bundleViewer

    BundleViewerCmd(String filename) {
        bundleViewer = new BundleViewer(filename)
    }

    def execute() {

        def renderer = new ManifestRenderer(2)

        println("Manifest:")
        println(renderer.render(bundleViewer.manifest))
        println()

        displayServiceComponents(bundleViewer.serviceComponents)
    }

    static private displayServiceComponents(sc) {
        if (sc.isEmpty()) {
            println("No service components.")
        } else {
            println("Service components:")
            sc.each {
                println("Component XML = $it.key")
                println(it.value)
            }
        }
    }

}
