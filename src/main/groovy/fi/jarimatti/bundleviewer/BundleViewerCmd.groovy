package fi.jarimatti.bundleviewer

import java.util.jar.Manifest

/**
 * Command line program to view bundle contents.
 */
class BundleViewerCmd {

    public static void main(final String[] args) {
        if (args.size() == 0) {
            usage()
            System.exit(1)
        }

        final String filename = args[0]

        def bv = new BundleViewer(filename)

        displayManifest(bv.manifest)
        displayServiceComponents(bv.serviceComponents)
    }

    static usage() {
        println("Usage: BundleViewer <bundle.jar>")
        println("   Displays bundle manifest.")
    }

    static private displayServiceComponents(sc) {
        if (sc.isEmpty()) {
            println("No service components.")
        } else {
            sc.each {
                println("Component XML = $it.key")
                println(it.value)
            }
        }
    }

    static private displayManifest(Manifest m) {
        displayMainAttributes(m)
        println()
        displayEntries(m)
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
