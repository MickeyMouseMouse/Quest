import jaco.mp3.player.MP3Player
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.stage.Stage
import java.awt.Desktop
import java.io.File
import java.net.URI

/*
    How to add external library to project?
        File->Project Structure...->Modules->Dependencies->icon plus->JARs or directories...
        Set check the box opposite the library
        Go to Artifacts; below "Available Elements" double click on the library
*/

class Controller {
    private val model = Model()
    private val guiDialog = GUIDialogs(Image("resources/other/icon.png"))

    val player = MP3Player(javaClass.getResource("resources/sounds/background.mp3"))
    private var isPlayerOn = false

    private val saveFile = File("save.txt")

    var menu: Scene? = null
    val buttonNewGame = Button("New Game")
    val buttonContinueGame = Button("Continue Game")
    val buttonStartStopMusic = Button()
    val linkGithub = Hyperlink("View code in GitHub")

    var game: Scene? = null
    val mainPane = Pane()
    val crazyFacePane = Pane()
    val locationsViewer = ImageView()
    val password = TextField()
    val infoLabelPane = Pane()
    val infoLabel = Label()
    val item1 = ImageView()
    val item2 = ImageView()
    val item3 = ImageView()
    val item4 = ImageView()
    val item5 = ImageView()
    val item6 = ImageView()
    val item7 = ImageView()
    val buttonMerge = Button("Merge")
    val buttonBack = Button()

    fun setStyle() { menu?.stylesheets?.add("resources/other/style.css") }

    fun newGame() {
        if (isThereSaveFile()) saveFile.delete()
        model.loadNewGame()
        updateGUI()
    }

    // load progress from save.txt
    // returns true if loading was successful
    fun continueGame(): Boolean {
        return if (model.loadGameFromSaveFile(saveFile.readText())) {
            updateGUI()
            true
        } else {
            guiDialog.showError("Fail. Save-file is corrupted.")
            false
        }
    }

    fun isThereSaveFile() = saveFile.exists()

    // save progress to save.txt
    private fun saveGame() {
        if (isThereSaveFile()) saveFile.delete()
        saveFile.writeText(model.getProgress())
    }

    // get the coordinates of the mouse click and take the next step in the game
    fun click(x: Int, y: Int) {
        //println("$x - $y")
        if (model.nextMove(x, y)) updateGUI()
    }

    fun terminateGame() {
        saveGame()
        buttonContinueGame.isDisable = !isThereSaveFile()
        (buttonBack.scene.window as Stage).scene = menu
    }

    // load new locations or new object to inventory
    private fun updateGUI() {
        // additional sounds
        if (isPlayerOn) playSound(model.getSound())
        model.soundPlayed()

        //finish of the game
        if (model.getGameStatus() == 2) {
            if (isThereSaveFile()) saveFile.delete()
            buttonContinueGame.isDisable = true

            (buttonBack.scene.window as Stage).scene = menu
            guiDialog.showInformation("Congratulations! You completed the game!")
            return
        }

        val name = StringBuilder(model.getLocation().toString())
        if (model.getLocation() == 48) {
            crazyFacePane.isVisible = true
        } else {
            crazyFacePane.isVisible = false
            locationsViewer.image = Image("resources/locations/$name.png")

            // show password field for safe1/safe2/iPad
            password.isVisible = if (model.getLocation() == 47) {
                password.text = ""
                true
            } else { false }

            // some comments from the main character
            if (model.getSpeech() != null) {
                infoLabel.text = model.getSpeech()
                infoLabelPane.isVisible = true
                model.speechLabelShowed()
            } else {
                infoLabelPane.isVisible = false
            }
        }

        // inventory
        val tmp = model.getInventory()
        name.clear()
        name.append(tmp[0].first)
        if (tmp[0].second == 1) name.append("s")
        item1.image = Image("resources/inventory/$name.png")

        name.clear()
        name.append(tmp[1].first)
        if (tmp[1].second == 1) name.append("s")
        item2.image = Image("resources/inventory/$name.png")

        name.clear()
        name.append(tmp[2].first)
        if (tmp[2].second == 1) name.append("s")
        item3.image = Image("resources/inventory/$name.png")

        name.clear()
        name.append(tmp[3].first)
        if (tmp[3].second == 1) name.append("s")
        item4.image = Image("resources/inventory/$name.png")

        name.clear()
        name.append(tmp[4].first)
        if (tmp[4].second == 1) name.append("s")
        item5.image = Image("resources/inventory/$name.png")

        name.clear()
        name.append(tmp[5].first)
        if (tmp[5].second == 1) name.append("s")
        item6.image = Image("resources/inventory/$name.png")

        name.clear()
        name.append(tmp[6].first)
        if (tmp[6].second == 1) name.append("s")
        item7.image = Image("resources/inventory/$name.png")
    }

    fun clickedOnItem1() {
        model.inventory(0)
        updateGUI()
    }

    fun mouseIsAboveItem1() {
        infoLabel.text = getDescriptionOfObject(0)
        infoLabelPane.isVisible = infoLabel.text != ""
    }

    fun clickedOnItem2() {
        model.inventory(1)
        updateGUI()
    }

    fun mouseIsAboveItem2() {
        infoLabel.text = getDescriptionOfObject(1)
        infoLabelPane.isVisible = infoLabel.text != ""
    }

    fun clickedOnItem3() {
        model.inventory(2)
        updateGUI()
    }

    fun mouseIsAboveItem3() {
        infoLabel.text = getDescriptionOfObject(2)
        infoLabelPane.isVisible = infoLabel.text != ""
    }

    fun clickedOnItem4() {
        model.inventory(3)
        updateGUI()
    }

    fun mouseIsAboveItem4() {
        infoLabel.text = getDescriptionOfObject(3)
        infoLabelPane.isVisible = infoLabel.text != ""
    }

    fun clickedOnItem5() {
        model.inventory(4)
        updateGUI()
    }

    fun mouseIsAboveItem5() {
        infoLabel.text = getDescriptionOfObject(4)
        infoLabelPane.isVisible = infoLabel.text != ""
    }

    fun clickedOnItem6() {
        model.inventory(5)
        updateGUI()
    }

    fun mouseIsAboveItem6() {
        infoLabel.text = getDescriptionOfObject(5)
        infoLabelPane.isVisible = infoLabel.text != ""
    }

    fun clickedOnItem7() {
        model.inventory(6)
        updateGUI()
    }

    fun mouseIsAboveItem7() {
        infoLabel.text = getDescriptionOfObject(6)
        infoLabelPane.isVisible = infoLabel.text != ""
    }

    private fun getDescriptionOfObject(number: Int): String {
        return when (model.getInventory()[number].first) {
            1 -> "Packed box (I can't open it with my hands)"
            2 -> "Safe (A password is required)"
            3 -> "Knife"
            4 -> "Battery"
            5 -> "Key"
            6 -> "Hammer"
            7 -> "Mirror"
            8 -> "Flashlight (It doesn't work)"
            9 -> "Flashlight"
            10 -> "Fork"
            11 -> "Empty bucket"
            12 -> "Bucket with water"
            13 -> "Scoop"
            14 -> "Photo"
            15 -> "iPad"
            else -> ""
        }
    }

    fun hideInfoLabel() { infoLabelPane.isVisible = false }

    fun getPassword(str: String) { if (model.passwords(str)) updateGUI() }

    fun merge() { if (model.merge()) updateGUI() }

    fun crazyFaceCompleted() {
        model.crazyFaceCompleted()
        updateGUI()
    }

    fun getAudioPlayerStatus() = isPlayerOn

    fun startStopAudioPlayer() {
        isPlayerOn = if (isPlayerOn) {
            player.pause()
            false
        } else {
            player.play()
            true
        }
    }

    // for short sound
    private fun playSound(number: Int) {
        if (number == 0) return

        val sounds = MP3Player()

        val name = when (number) {
            1 -> "take"
            2 -> "ladder"
            else -> "applause"
        }

        sounds.addToPlayList(javaClass.getResource("resources/sounds/$name.mp3"))
        sounds.play()
    }

    fun openGitHub() {
        Desktop.getDesktop().browse(URI("https://github.com/MickeyMouseMouse/Quest"))
    }

    fun beforeCloseApp() {
        if (isPlayerOn) player.stop()
        if (model.getGameStatus() == 1) saveGame()
    }
}