package fi.jarimatti.bundleviewer.gui

import fi.jarimatti.bundleviewer.BundleViewer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.StackPane
import javafx.stage.Stage

class GUI extends Application {

    /**
     * The file name of the bundle.
     */
    BundleViewer bundeViewer

    GUI(String filename) {
        super()

        bundeViewer = new BundleViewer(filename)
    }

    @Override
    void start(Stage primaryStage) throws Exception {

        def manifestView = new TextArea()

        def btn = new Button("Reload")
        btn.onAction = {
            bundeViewer.reload()
            // Update UI.
        }

        def root = new StackPane()

        def scene = new Scene(root, 500, 500)

        primaryStage.title = "Bundle Viewer"
        primaryStage.scene = scene
        primaryStage.show()
    }
}
