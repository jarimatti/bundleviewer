package fi.jarimatti.bundleviewer.gui

import fi.jarimatti.bundleviewer.BundleViewer
import fi.jarimatti.bundleviewer.render.ManifestRenderer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class GUI extends Application {

    /**
     * The file name of the bundle.
     */
    BundleViewer bundleViewer

    @Override
    void start(Stage primaryStage) throws Exception {

        bundleViewer = new BundleViewer(this.parameters.unnamed[0])

        def manifestView = new TextArea()
        manifestView.editable = false

        updateManifestView(manifestView)

        def btn = new Button("Reload")
        btn.onAction = {
            bundleViewer.reload()

            // Update UI.
            updateManifestView(manifestView)
        }

        def grid = new GridPane()
        grid.add(manifestView, 0, 0)
        grid.add(btn, 0, 1)

        def scene = new Scene(grid, 500, 500)

        primaryStage.title = "Bundle Viewer"
        primaryStage.scene = scene
        primaryStage.show()
    }

    def updateManifestView(TextArea manifestView) {
        def manifestRenderer = new ManifestRenderer()
        manifestView.text = manifestRenderer.render(bundleViewer.manifest)
    }
}
