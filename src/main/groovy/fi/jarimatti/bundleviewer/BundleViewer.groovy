package fi.jarimatti.bundleviewer

import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest

/**
 * Reads and contains OSGi bundle specific data from a jar file.
 */
class BundleViewer {

    /**
     * The bundle jar file.
     */
    File file

    /**
     * Copy of the manifest in the loaded file.
     */
    Manifest manifest

    /**
     * Service component file names and file contents.
     */
    Map<String, String> serviceComponents

    static SERVICE_COMPONENT = new Attributes.Name("Service-Component")

    def BundleViewer(final String filename) {
        this(new File(filename))
    }

    def BundleViewer(final File file) {
        this.file = file
        reload()
    }

    /**
     * Reload the bundle data from the jar file.
     */
    public void reload() {
        new JarFile(file).withCloseable {
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

    private List<String> serviceComponentFilenames() {
        if (manifest.mainAttributes[SERVICE_COMPONENT]) {
            manifest.mainAttributes[SERVICE_COMPONENT].toString().split(",")
        } else {
            []
        }
    }

    static private String loadJarEntry(jf, fn) {
        def entry = jf.getJarEntry(fn)
        jf.getInputStream(entry).getText()
    }
}
