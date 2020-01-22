import javafx.application.Application
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Scene
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.stage.Stage

class GUI: Application() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GUI::class.java)
        }
    }

    private val cont = Controller()
    private val crazyFace = CrazyFace(cont)
    private val simpleStyle = "-fx-background-radius: 50; -fx-font-size: 29px"
    private val hoverStyle = "-fx-background-radius: 50; -fx-font-size: 30px; -fx-background-color: linear-gradient(SkyBlue 0%, PaleGreen 60%, Yellow 100%)"

    override fun start(stage: Stage) {
        stage.title = "Quest"
        stage.isResizable = false
        stage.icons.add(Image("resources/other/icon.png"))
        stage.centerOnScreen()
        stage.setOnCloseRequest { cont.beforeCloseApp() }

        // --MENU--

        cont.buttonNewGame.minWidth = 240.0
        cont.buttonNewGame.style = simpleStyle
        cont.buttonNewGame.setOnMouseEntered { cont.buttonNewGame.style = hoverStyle }
        cont.buttonNewGame.setOnMouseExited { cont.buttonNewGame.style = simpleStyle }
        cont.buttonNewGame.setOnMouseClicked {
            cont.newGame()
            stage.scene = cont.game
        }

        cont.buttonContinueGame.minWidth = 240.0
        cont.buttonContinueGame.style = simpleStyle
        cont.buttonContinueGame.isDisable = !cont.isThereSaveFile()
        cont.buttonContinueGame.setOnMouseEntered { cont.buttonContinueGame.style = hoverStyle }
        cont.buttonContinueGame.setOnMouseExited { cont.buttonContinueGame.style = simpleStyle }
        cont.buttonContinueGame.setOnMouseClicked {
            if (cont.continueGame()) stage.scene = cont.game
        }

        cont.buttonStartStopMusic.graphicProperty().value =
            ImageView(Image("resources/other/musicOn.jpg"))
        cont.buttonStartStopMusic.style = "-fx-padding: 7 7 7 7; -fx-background-radius: 50"
        cont.buttonStartStopMusic.setOnMouseClicked {
            cont.buttonStartStopMusic.graphicProperty().value = if (cont.getAudioPlayerStatus())
                ImageView(Image("resources/other/musicOff.jpg"))
            else
                ImageView(Image("resources/other/musicOn.jpg"))

            cont.startStopAudioPlayer()
        }

        cont.linkGithub.style = "-fx-font-size: 17px"
        cont.linkGithub.setOnMouseClicked { cont.openGitHub() }

        val gridMenu = GridPane()
        gridMenu.id = "gridMenu"

        gridMenu.layoutX = 0.0
        gridMenu.layoutY = 0.0

        // only one column
        gridMenu.columnConstraints
            .add(ColumnConstraints(750.0, 750.0, 750.0))

        // row 1
        gridMenu.rowConstraints.add(RowConstraints(260.0))
        GridPane.setRowIndex(cont.buttonNewGame, 0)
        GridPane.setHalignment(cont.buttonNewGame, HPos.CENTER)
        GridPane.setValignment(cont.buttonNewGame, VPos.BOTTOM)

        // row 2
        gridMenu.rowConstraints.add(RowConstraints(100.0))
        GridPane.setRowIndex(cont.buttonContinueGame, 1)
        GridPane.setHalignment(cont.buttonContinueGame, HPos.CENTER)
        GridPane.setValignment(cont.buttonContinueGame, VPos.CENTER)

        // row 3
        gridMenu.rowConstraints.add(RowConstraints(50.0))
        GridPane.setRowIndex(cont.buttonStartStopMusic, 2)
        GridPane.setHalignment(cont.buttonStartStopMusic, HPos.CENTER)
        GridPane.setValignment(cont.buttonStartStopMusic, VPos.TOP)

        // row 4
        gridMenu.rowConstraints.add(RowConstraints(190.0))
        GridPane.setRowIndex(cont.linkGithub, 3)
        GridPane.setHalignment(cont.linkGithub, HPos.CENTER)
        GridPane.setValignment(cont.linkGithub, VPos.BOTTOM)

        gridMenu.children.addAll(cont.buttonNewGame, cont.buttonContinueGame,
            cont.buttonStartStopMusic, cont.linkGithub)

        cont.menu = Scene(gridMenu)
        cont.setStyle()


        // --GAME--
        cont.password.layoutX = 295.0
        cont.password.layoutY = 265.0
        cont.password.maxWidth = 140.0
        cont.password.style = "-fx-font-size: 20px"
        cont.password.isVisible = false
        cont.password.setOnKeyReleased { cont.getPassword(cont.password.text) }

        cont.infoLabel.layoutX = 0.0
        cont.infoLabel.layoutY = 485.0
        cont.infoLabel.minWidth = 750.0
        cont.infoLabel.alignment = Pos.CENTER
        cont.infoLabel.style = "-fx-font-size: 20px; -fx-text-fill: white"


        cont.mainPane.children.addAll(cont.locationsViewer, cont.password,
            cont.infoLabel)
        cont.mainPane.setOnMouseClicked { cont.click(it.x.toInt(), it.y.toInt()) }

        cont.crazyFacePane.isVisible = false

        val inventory = HBox()
        cont.item1.setOnMouseClicked { cont.clickedOnItem1() }
        cont.item1.setOnMouseEntered { cont.mouseIsAboveItem1() }
        cont.item1.setOnMouseExited { cont.cleanInfoLabel() }

        cont.item2.setOnMouseClicked { cont.clickedOnItem2() }
        cont.item2.setOnMouseEntered { cont.mouseIsAboveItem2() }
        cont.item2.setOnMouseExited { cont.cleanInfoLabel() }

        cont.item3.setOnMouseClicked { cont.clickedOnItem3() }
        cont.item3.setOnMouseEntered { cont.mouseIsAboveItem3() }
        cont.item3.setOnMouseExited { cont.cleanInfoLabel() }

        cont.item4.setOnMouseClicked { cont.clickedOnItem4() }
        cont.item4.setOnMouseEntered { cont.mouseIsAboveItem4() }
        cont.item4.setOnMouseExited { cont.cleanInfoLabel() }

        cont.item5.setOnMouseClicked { cont.clickedOnItem5() }
        cont.item5.setOnMouseEntered { cont.mouseIsAboveItem5() }
        cont.item5.setOnMouseExited { cont.cleanInfoLabel() }

        cont.item6.setOnMouseClicked { cont.clickedOnItem6() }
        cont.item6.setOnMouseEntered { cont.mouseIsAboveItem6() }
        cont.item6.setOnMouseExited { cont.cleanInfoLabel() }

        cont.item7.setOnMouseClicked { cont.clickedOnItem7() }
        cont.item7.setOnMouseEntered { cont.mouseIsAboveItem7() }
        cont.item7.setOnMouseExited { cont.cleanInfoLabel() }

        cont.buttonMerge.minHeight = 50.0
        cont.buttonMerge.style = "-fx-font-size: 20px"
        cont.buttonMerge.setOnMouseClicked { cont.merge() }

        cont.buttonBack.setMinSize(30.0, 30.0)
        cont.buttonBack.setMaxSize(30.0, 30.0)
        cont.buttonBack.graphicProperty().value =
            ImageView(Image("resources/other/back.png"))
        cont.buttonBack.tooltip = Tooltip("Finish the game")
        cont.buttonBack.setOnMouseClicked { cont.terminateGame() }

        inventory.children.addAll(cont.item1, cont.item2,
            cont.item3, cont.item4, cont.item5, cont.item6,
            cont.item7, cont.buttonMerge, cont.buttonBack)
        inventory.spacing = 20.0
        inventory.alignment = Pos.CENTER

        val gridGame = GridPane()
        gridGame.isGridLinesVisible = true

        gridGame.layoutX = 0.0
        gridGame.layoutY = 0.0

        // only one column
        gridGame.columnConstraints
            .add(ColumnConstraints(750.0, 750.0, 750.0))

        // row 1
        gridGame.rowConstraints.add(RowConstraints(530.0))
        GridPane.setRowIndex(cont.mainPane, 0)
        GridPane.setRowIndex(cont.crazyFacePane, 0)
        GridPane.setHalignment(cont.mainPane, HPos.CENTER)
        GridPane.setValignment(cont.mainPane, VPos.CENTER)

        // row 2
        gridGame.rowConstraints.add(RowConstraints(70.0))
        GridPane.setRowIndex(inventory, 1)
        GridPane.setHalignment(inventory, HPos.CENTER)
        GridPane.setValignment(inventory, VPos.CENTER)

        gridGame.children.addAll(cont.mainPane, cont.crazyFacePane, inventory)

        cont.game = Scene(gridGame)


        // --START--
        stage.scene = cont.menu
        stage.show()
        cont.player.isRepeat = true
        cont.startStopAudioPlayer() // start background music play



        // Crazy Face
        crazyFace.sky.layoutX = 0.0
        crazyFace.sky.layoutY = 0.0

        crazyFace.ground.layoutX = 0.0
        crazyFace.ground.layoutY = 480.0

        crazyFace.face.layoutX = 150.0
        crazyFace.face.layoutY = 280.0

        crazyFace.startButton.layoutX = 340.0
        crazyFace.startButton.layoutY = 175.0
        crazyFace.startButton.style = "-fx-font-size: 20px"

        crazyFace.scoreLabel.layoutX = 350.0
        crazyFace.scoreLabel.layoutY = 100.0
        crazyFace.scoreLabel.style = "-fx-font-size: 35px"

        crazyFace.drawSkyAndGround()
        crazyFace.drawFace(true)

        crazyFace.startButton.setOnMouseClicked { crazyFace.start() }

        cont.crazyFacePane.children.addAll(crazyFace.sky, crazyFace.ground, crazyFace.barrier1,
            crazyFace.barrier2, crazyFace.face, crazyFace.scoreLabel, crazyFace.startButton)

        cont.crazyFacePane.setOnMouseClicked {
            if (!crazyFace.startButton.isVisible) crazyFace.upliftFace()
        }

        cont.game!!.setOnKeyTyped {
            if (!crazyFace.startButton.isVisible) crazyFace.upliftFace()
        }
    }
}
