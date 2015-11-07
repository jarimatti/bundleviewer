package fi.jarimatti.bundleviewer

import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest

/**
 * View OSGi bundle contents.
 * <p>
 *     Shows bundle manifest.
 * </p>
 *
 * <p>
 *     Roadmap:
 *     <ul>
 *         <li>show bundle manifest
 *         <li>parse OSGi bundle information from manifest
 *         <li>show linked OSGi files parsed from manifest
 *         <li>dynamically update information if bundle changes
 *         <li>UI
 *     </ul>
 * </p>
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

    static void main(final String[] args) {

        if (args.size() == 0) {
            usage()
            System.exit(1)
        }

        final String filename = args[0]

        new BundleViewer(filename)
    }

    static def usage() {
        println("Usage: BundleViewer <bundle.jar>")
        println("   Displays bundle manifest.")
    }

    def BundleViewer(final String filename) {

        this.filename = filename

        reload()

        displayManifest()
        displayServiceComponents()
    }

    def reload() {
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
        manifest.mainAttributes[SERVICE_COMPONENTS].toString().split(",")
    }

    private displayServiceComponents() {
        if (serviceComponents.isEmpty()) {
            println("No service components.")
        } else {
            serviceComponents.each {
                println("Component XML = $it.key")
                println(it.value)
            }
        }
    }

    static private loadJarEntry(jf, fn) {
        def entry = jf.getJarEntry(fn)
        jf.getInputStream(entry).getText()
    }

    private displayManifest() {
        displayMainAttributes(manifest)
        println()
        displayEntries(manifest)
    }

    static private displayEntries(final Manifest manifest) {
        if (manifest.entries.isEmpty()) {
            println("Entries: No entries.")
        } else {
            println("Entries (${manifest.entries.size()}):")
            manifest.entries.each {
                println("  Entry key = $it.key")
                it.value.each {
                    println("    $it.key = $it.value")
                }
            }
        }
    }

    static private displayMainAttributes(Manifest manifest) {
        println("Main attributes (${manifest.mainAttributes.size()}):")
        manifest.mainAttributes.each {
            println("  $it.key = $it.value")
        }
    }

}
