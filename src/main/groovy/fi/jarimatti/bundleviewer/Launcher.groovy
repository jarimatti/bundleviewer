package fi.jarimatti.bundleviewer

import fi.jarimatti.bundleviewer.gui.GUI
import javafx.application.Application

/**
 * Launch GUI or command line application based on switches.
 */
class Launcher {

    public static void main(String[] args) {

        def cli = new CliBuilder(
                usage: 'bundleviewer [options] <bundle.jar>',
                header: 'Options:')
        cli.help('print this message')
        cli.g('use GUI')

        def options = cli.parse(args)

        if (null == options || options.help || options.arguments().size() != 1) {
            cli.usage()
            System.exit(1)
        }

        def filename = options.arguments().get(0)

        if (options.g) {
            Application.launch(GUI.class, filename)
        } else {
            new BundleViewerCmd(filename).execute()
        }
    }

}
