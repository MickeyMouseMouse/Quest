import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.stage.Stage

class GUIDialogs(private val icon: Image? = null, private val textSize: Int = 18) {
    private lateinit var alert: Alert

    fun showError(errorText: String) {
        alert = Alert(Alert.AlertType.ERROR)
        show(errorText)
    }

    fun showWarning(warning: String) {
        alert = Alert(Alert.AlertType.WARNING)
        show(warning)
    }

    fun showInformation(infoText: String) {
        alert = Alert(Alert.AlertType.INFORMATION)
        show(infoText)
    }

    private fun show(text: String) {
        val dialogPane = alert.dialogPane
        if (icon != null) (dialogPane.scene.window as Stage).icons.add(icon)
        dialogPane?.style = "-fx-font-size: $textSize px"

        alert.headerText = null
        alert.contentText = text
        alert.showAndWait()
    }
}