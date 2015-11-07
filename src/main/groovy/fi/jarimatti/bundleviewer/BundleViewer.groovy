package fi.jarimatti.bundleviewer

import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest

/**
 * Reads and contains OSGi bundle specific data from a jar file.
 */
class BundleViewer {

    /**
     * The bundle jar file name.
     */
    String filename

    /**
     * Copy of the manifest in the loaded file.
     */
    Manifest manifest

    /**
     * Service component file names and file contents.
     */
    Map<String, String> serviceComponents

    static SERVICE_COMPONENTS = new Attributes.Name("Service-Component")

    def BundleViewer(final String filename) {
        this.filename = filename
        reload()
    }

    /**
     * Reload the bundle data from the jar file.
     */
    public void reload() {
        new JarFile(filename).withCloseable {
            readManifest(it)
            readServiceComponents(it)
        }
    }

    private readManifest(JarFile jf) {
        manifest = new Manifest(jf.manifest)
    }

    private readServiceComponents(JarFile jf) {
        serviceComponents = [:]
        serviceComponentFilenames().each {
            serviceComponents.put(it, loadJarEntry(jf, it))
        }
    }

    private serviceComponentFilenames() {
        manifest.mainAttributes[SERVICE_COMPONENTS].split(",")
    }

    static private loadJarEntry(jf, fn) {
        def entry = jf.getJarEntry(fn)
        jf.getInputStream(entry).getText()
    }
}
