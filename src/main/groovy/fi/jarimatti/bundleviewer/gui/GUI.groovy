package fi.jarimatti.bundleviewer.gui

import fi.jarimatti.bundleviewer.BundleViewer
import fi.jarimatti.bundleviewer.render.ManifestRenderer
import javafx.application.Application
import javafx.beans.InvalidationListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage

class GUI extends Application {

    /**
     * The file name of the bundle.
     */
    BundleViewer bundleViewer

    /**
     * Area for the selected element.
     */
    TextArea contentView

    /**
     * Tree for the bundle contents.
     */
    ListView<String> listView

    TextField filenameTextField

    /**
     * Bundle component names.
     */
    ObservableList<String> bundleComponentNames

    @Override
    void start(Stage primaryStage) throws Exception {
        bundleViewer = new BundleViewer(this.parameters.unnamed[0])

        setupUI(primaryStage)
        updateUI()
    }

    void updateUI() {
        def selectedItem = listView.selectionModel.selectedItem

        if ("MANIFEST.MF" == selectedItem) {
            contentView.text = new ManifestRenderer().render(bundleViewer.manifest)
        } else {
            contentView.text = bundleViewer.serviceComponents[selectedItem]
        }
    }

    def reload() {
        bundleViewer.reload()
        filenameTextField.text = bundleViewer.file.path
        fillBundleComponentNames()
    }

    def fillBundleComponentNames() {
        bundleComponentNames.with {
            clear()
            add("MANIFEST.MF")
            bundleViewer.serviceComponents.each {
                add(it.key)
            }
        }
    }

    def setupUI(Stage stage) {
        contentView = new TextArea()
        contentView.editable = false

        bundleComponentNames = FXCollections.observableArrayList()
        fillBundleComponentNames()

        listView = new ListView<>(bundleComponentNames).with {
            editable = false
            selectionModel.selectedItemProperty().addListener({ x ->
                updateUI()
            } as InvalidationListener)
            return it
        }

        def grid = new GridPane().with {
            padding = new Insets(5)
            hgap = 5
            vgap = 5

            add(buildHeader(), 0, 0, 2, 1)
            add(listView, 0, 1)
            add(contentView, 1, 1)

            def row2 = new RowConstraints(250, 250, Double.MAX_VALUE)
            row2.setVgrow(Priority.ALWAYS)
            rowConstraints.addAll(new RowConstraints(), row2)

            def col2 = new ColumnConstraints(250, 250, Double.MAX_VALUE)
            col2.setHgrow(Priority.ALWAYS)
            columnConstraints.addAll(new ColumnConstraints(), col2)

            return it
        }

        stage.with {
            title = "Bundle Viewer"
            scene = new Scene(grid, 500, 500)
            minHeight = 500
            minWidth = 500
        }
        stage.show()
    }

    def buildHeader() {
        filenameTextField = new TextField(bundleViewer.file.path)
        filenameTextField.editable = false

        def selectFileButton = new Button("...")
        selectFileButton.onAction = {
            def chooser = new FileChooser().with {
                title = "Select bundle"
                selectedExtensionFilter = new ExtensionFilter("Bundle", [".jar"])
                return it
            }

            def f = chooser.showOpenDialog(null)

            if (f) {
                bundleViewer = new BundleViewer(f.path)
                fillBundleComponentNames()
                updateUI()
            }
        }

        def reloadButton = new Button("Reload")
        reloadButton.onAction = {
            reload()
            updateUI()
        }

        new HBox().with {
            setHgrow(filenameTextField, Priority.ALWAYS)
            children.add(filenameTextField)
            children.add(selectFileButton)
            children.add(reloadButton)
            spacing = 5

            return it
        }
    }
}
