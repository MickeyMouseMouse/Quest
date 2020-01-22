import java.lang.StringBuilder
import kotlin.properties.Delegates

class Model {
    // 0 = off, 1 = playing, 2 = completed
    private var gameStatus = 0

    private var locationNumber by Delegates.notNull<Int>()
    private var locationTmp = 1

    // 7 cells for items
    // first number = item
    // second number = selected or not (1 or 0)
    // 0 = empty, 1 = box, 2 = safe, 3 = knife, 4 = battery, 5 = key,
    // 6 = hammer, 7 = mirror, 8 = flashlightOff, 9 = flashlightOn,
    // 10 = fork, 11 = bucket, 12 = bucket with water, 13 = scoop,
    // 14 = photo, 15 = iPad
    private lateinit var inventory: Array<Pair<Int, Int>>

    // flags
    private var box by Delegates.notNull<Boolean>()
    private var safe1 by Delegates.notNull<Boolean>() // under the bed
    private var knife by Delegates.notNull<Boolean>()
    private var battery by Delegates.notNull<Boolean>()
    private var door1Opened by Delegates.notNull<Boolean>() // to first floor
    private var hammer by Delegates.notNull<Boolean>()
    private var mirror by Delegates.notNull<Boolean>()
    private var door2Opened by Delegates.notNull<Boolean>() // to backyard
    private var liftRevealed by Delegates.notNull<Boolean>()
    private var flashlight by Delegates.notNull<Boolean>()
    private var fork by Delegates.notNull<Boolean>()
    private var fireplaceOff by Delegates.notNull<Boolean>()
    private var safe2 by Delegates.notNull<Boolean>() // in the fireplace
    private var emptyBucket by Delegates.notNull<Boolean>()
    private var scoop by Delegates.notNull<Boolean>()
    private var stopperOff by Delegates.notNull<Boolean>()
    private var fullBucket by Delegates.notNull<Boolean>()
    private var flashlightOn by Delegates.notNull<Boolean>()
    private var photo by Delegates.notNull<Boolean>()
    private var iPad by Delegates.notNull<Boolean>()
    private var iPadUnlock by Delegates.notNull<Boolean>()
    private var crazyFaceCompleted by Delegates.notNull<Boolean>()
    private var door3Opened by Delegates.notNull<Boolean>() // to outside

    // additional sounds
    // 0 = no, 1 = short sound, 2 = ladder, 3 = applause
    private var sound = 0

    // words of the main character
    private var speech: String? = null

    fun getGameStatus() = gameStatus
    fun getLocation() = locationNumber
    fun getInventory() = inventory
    fun getSound() = sound
    fun soundPlayed() { sound = 0 }
    fun speechLabelShowed() { speech = null }
    fun getSpeech() = speech

    fun loadNewGame() {
        gameStatus = 1

        locationNumber = 1
        inventory = Array(7) {Pair(0, 0)}

        box = false
        safe1 = false
        knife = false
        battery = false
        door1Opened = false
        hammer = false
        mirror = false
        door2Opened = false
        liftRevealed = false
        flashlight = false
        fork = false
        fireplaceOff = false
        safe2 = false
        emptyBucket = false
        scoop = false
        stopperOff = false
        fullBucket = false
        flashlightOn = false
        photo = false
        iPad = false
        iPadUnlock = false
        crazyFaceCompleted = false
        door3Opened = false
    }

    fun loadGameFromSaveFile(savedData: List<Int>) {
        gameStatus = 1

        locationNumber = savedData[0]

        inventory = arrayOf(Pair(savedData[1], savedData[2]),
            Pair(savedData[3], savedData[4]),
            Pair(savedData[5], savedData[6]),
            Pair(savedData[7], savedData[8]),
            Pair(savedData[9], savedData[10]),
            Pair(savedData[11], savedData[12]),
            Pair(savedData[13], savedData[14]))

        box = savedData[15] != 0
        safe1 = savedData[16] != 0
        knife = savedData[17] != 0
        battery = savedData[18] != 0
        door1Opened = savedData[19] != 0
        hammer = savedData[20] != 0
        mirror = savedData[21] != 0
        door2Opened = savedData[22] != 0
        liftRevealed = savedData[23] != 0
        flashlight = savedData[24] != 0
        fork = savedData[25] != 0
        fireplaceOff = savedData[26] != 0
        safe2 = savedData[27] != 0
        emptyBucket = savedData[28] != 0
        scoop = savedData[29] != 0
        stopperOff = savedData[30] != 0
        fullBucket = savedData[31] != 0
        flashlightOn = savedData[32] != 0
        photo = savedData[33] != 0
        iPad = savedData[34] != 0
        iPadUnlock = savedData[35] != 0
        crazyFaceCompleted = savedData[36] != 0
        door3Opened = savedData[37] != 0
    }

    // returns true if the GUI needs to be updated
    fun nextMove(x: Int, y: Int): Boolean {
        var someChanges = false
        when(locationNumber) {
            1, 2 -> {
                if (x in 27..130 && y in 165..350) {
                    someChanges = true
                    locationNumber = 5
                }

                if (x in 335..405 && y in 210..270) {
                    someChanges = true
                    locationNumber = if (box) 7 else 6
                }

                if (x in 490..666 && y in 360..525) {
                    someChanges = true
                    locationNumber = if (safe1) 4 else 3
                }

                if (x > 680) {
                    someChanges = true
                    locationNumber = if (knife) 9 else 8
                }
            }

            3 -> {
                if (y > 490) {
                    someChanges = true
                    locationNumber = if (box) 2 else 1
                }

                if (x in 370..410 && y in 200..230) {
                    someChanges = true
                    locationNumber = 4
                    safe1 = true
                    addToInventory(2)
                }
            }

            4, 5, 7 -> {
                if (y > 490) {
                    someChanges = true
                    locationNumber = if (box) 2 else 1
                }
            }

            6 -> {
                if (y > 505) {
                    someChanges = true
                    locationNumber = if (box) 2 else 1
                }

                if (x in 400..490 && y in 290..410) {
                    someChanges = true
                    locationNumber = 7
                    box = true
                    addToInventory(1)
                }
            }

            8, 9 -> {
                if (x < 70) {
                    someChanges = true
                    locationNumber = if (box) 2 else 1
                }

                if (x in 125..455 && y in 230..360) {
                    someChanges = true
                    locationNumber = if (knife) 11 else 10
                }

                if (x in 515..750 && y in 5..480) {
                    someChanges = true
                    if (door1Opened) {
                        locationNumber = 14
                        sound = 2
                    } else {
                        if (checkSelectedItems(5)) {
                            door1Opened = true
                            locationNumber = 14
                            sound = 2
                            removeFromInventory(5)
                        } else {
                            speech = "The door is closed."
                        }
                    }
                }
            }

            10 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (knife) 9 else 8
                }

                if (x in 115..200 && y in 410..480) {
                    someChanges = true
                    locationNumber = if (battery) 13 else 12
                }

                if (x in 560..660 && y in 360..415) {
                    someChanges = true
                    locationNumber = 11
                    knife = true
                    addToInventory(3)
                }
            }

            11 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (knife) 9 else 8
                }

                if (x in 115..200 && y in 410..480) {
                    someChanges = true
                    locationNumber = if (battery) 13 else 12
                }
            }

            12 -> {
                if (y > 500) {
                    someChanges = true
                    locationNumber = if (knife) 11 else 10
                }

                if (x in 330..370 && y in 305..345) {
                    someChanges = true
                    locationNumber = 13
                    battery = true
                    addToInventory(4)
                }
            }

            13 -> {
                if (y > 500) {
                    someChanges = true
                    locationNumber = if (knife) 11 else 10
                }
            }

            14 -> {
                if (x > 660) {
                    someChanges = true
                    locationNumber = if (hammer) 18 else 17
                }

                if (x < 50) {
                    someChanges = true
                    locationNumber = if (door3Opened) 16 else 15
                }

                if (x in 410..660 && y in 10..500) {
                    someChanges = true
                    locationNumber = 9
                    sound = 2
                }

                if (x in 87..280 && y in 10..430) {
                    someChanges = true
                    locationNumber = 22
                }
            }

            15 -> {
                if (x > 690) {
                    someChanges = true
                    locationNumber = 14
                }

                if (x < 60) {
                    someChanges = true
                    locationNumber = if (hammer) 18 else 17
                }

                if (x in 240..435 && y in 25..500) {
                    someChanges = true
                    if (checkSelectedItems(5) && crazyFaceCompleted) {
                        door3Opened = true
                        locationNumber = 16
                        removeFromInventory(5)
                    } else {
                        speech = "The door is closed."
                    }
                }
            }

            16 -> {
                if (x > 690) {
                    someChanges = true
                    locationNumber = 14
                }

                if (x < 50) {
                    someChanges = true
                    locationNumber = if (hammer) 18 else 17
                }

                if (x in 170..530 && y in 60..520) {
                    someChanges = true
                    sound = 3
                    gameStatus = 2
                }
            }

            17, 18 -> {
                if (x > 710) {
                    someChanges = true
                    locationNumber = if (door3Opened) 16 else 15
                }

                if (x < 50) {
                    someChanges = true
                    locationNumber = 14
                }

                if (x in 50..205 && y in 20..290) {
                    someChanges = true
                    locationNumber = if (hammer) 20 else 19
                }

                if (x in 550..710 && y in 5..390) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }
            }

            19 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (hammer) 18 else 17
                }

                if (x in 235..345 && y in 380..440) {
                    someChanges = true
                    locationNumber = 20
                    hammer = true
                    addToInventory(6)
                }

                if (x in 90..125 && y in 400..440) {
                    if (checkSelectedItems(7)) {
                        someChanges = true
                        locationNumber = 21
                        resetAllSelectionsInInventory()
                    }
                }
            }

            20 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (hammer) 18 else 17
                }

                if (x in 90..125 && y in 400..440) {
                    if (checkSelectedItems(7)) {
                        someChanges = true
                        locationNumber = 21
                        resetAllSelectionsInInventory()
                    }
                }
            }

            21 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (hammer) 20 else 19
                }
            }

            22 -> {
                if (y > 500) {
                    someChanges = true
                    locationNumber = 14
                }

                if (x in 100..160 && y in 215..240) {
                    if (checkSelectedItems(13) && crazyFaceCompleted) {
                        someChanges = true
                        removeFromInventory(13)
                        removeFromInventory(15)
                        addToInventory(5)
                    }
                }

                if (x > 615 && y in 260..500) {
                    someChanges = true
                    locationNumber = if (mirror) 25 else 24
                }
            }

            24 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 14
                }

                if (x < 30) {
                    someChanges = true
                    locationNumber = 22
                }

                if (x in 335..370 && y in 245..275) {
                    someChanges = true
                    locationNumber = 25
                    mirror = true
                    addToInventory(7)
                }
            }

            25 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 14
                }

                if (x < 30) {
                    someChanges = true
                    locationNumber = 22
                }
            }

            26 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (hammer) 18 else 17
                }

                if (x < 90 && y < 500) {
                    someChanges = true
                    locationNumber = if (fork) 32 else 31
                }

                if (x in 112..125 && y in 95..120) {
                    if (checkSelectedItems(6) && photo) {
                        someChanges = true
                        liftRevealed = true
                        removeFromInventory(6)
                        removeFromInventory(14)
                        locationNumber = 27
                    }
                }

                if (x in 260..450 && y in 170..280) {
                    someChanges = true
                    locationNumber = if (flashlight) 30 else 29
                }

                if (x > 615 && y in 105..270) {
                    someChanges = true
                    if (door2Opened) {
                        locationNumber = if (emptyBucket) 35 else 34
                    } else {
                        if (checkSelectedItems(5)) {
                            door2Opened = true
                            locationNumber = if (emptyBucket) 35 else 34
                            removeFromInventory(5)
                        } else {
                            speech = "The door is closed."
                        }
                    }
                }
            }

            27 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 18
                }

                if (x < 90 && y < 500) {
                    someChanges = true
                    locationNumber = 32
                }

                if (x in 135..210 && y in 115..230) {
                    someChanges = true
                    locationNumber = if (flashlightOn) 41 else 40
                }

                if (x in 260..450 && y in 170..280) {
                    someChanges = true
                    locationNumber = if (flashlight) 30 else 29
                }

                if (x > 615 && y in 105..270) {
                    someChanges = true
                    locationNumber = 35
                }
            }

            29 -> {
                if (x > 35 && y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x < 33 && y in 485..510) {
                    someChanges = true
                    locationNumber = 30
                    flashlight = true
                    addToInventory(8)
                }
            }

            30 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }
            }

            31 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x > 500) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x in 455..540 && y in 240..280) {
                    someChanges = true
                    locationNumber = 32
                    fork = true
                    addToInventory(10)
                }

                if (x in 70..190 && y in 140..290) {
                    someChanges = true
                    locationNumber = 33
                }
            }

            32 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x > 500) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x in 70..190 && y in 140..290) {
                    if (!fireplaceOff) {
                        someChanges = true
                        locationNumber = 33
                    }
                }
            }

            33 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (fork) 32 else 31
                }

                if (x in 240..455 && y in 305..440) {
                    someChanges = true
                    if (checkSelectedItems(12)) {
                        fireplaceOff = true
                        locationNumber = 32
                        removeFromInventory(12)
                        addToInventory(2)
                    } else {
                        speech = "There is something in the fireplace. I can't take it with my hands."
                    }
                }
            }

            34 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x < 60) {
                    someChanges = true
                    locationNumber = if (stopperOff) 39 else 38
                }

                if (x in 315..355 && y in 250..290) {
                    someChanges = true
                    locationNumber = 35
                    emptyBucket = true
                    addToInventory(11)
                }

                if (x in 135..275 && y in 135..245) {
                    someChanges = true
                    locationNumber = if (scoop) 37 else 36
                }
            }

            35 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x < 60) {
                    someChanges = true
                    locationNumber = if (stopperOff) 39 else 38
                }

                if (x in 135..275 && y in 135..245) {
                    someChanges = true
                    locationNumber = if (scoop) 37 else 36
                }
            }

            36 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (emptyBucket) 35 else 34
                }

                if (x in 545..660 && y in 255..350) {
                    someChanges = true
                    locationNumber = 37
                    scoop = true
                    addToInventory(13)
                }
            }

            37 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (emptyBucket) 35 else 34
                }
            }

            38 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x > 510) {
                    someChanges = true
                    locationNumber = if (emptyBucket) 35 else 34
                }

                if (x in 150..260 && y in 280..350) {
                    someChanges = true
                    if (checkSelectedItems(10)) {
                        stopperOff = true
                        locationNumber = 39
                        removeFromInventory(10)
                    } else {
                        speech = "The faucet is clogged. No water is coming."
                    }
                }
            }

            39 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = if (liftRevealed) 27 else 26
                }

                if (x > 510) {
                    someChanges = true
                    locationNumber = if (emptyBucket) 35 else 34
                }

                if (x in 150..260 && y in 280..350) {
                    if (checkSelectedItems(11) && stopperOff) {
                        someChanges = true
                        fullBucket = true
                        removeFromInventory(11)
                        addToInventory(12)
                    }
                }
            }

            40 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 27
                }

                if (x in 190..500 && y in 0..490) {
                    someChanges = true
                    if (checkSelectedItems(9)) {
                        flashlightOn = true
                        locationNumber = 41
                        removeFromInventory(9)
                    } else {
                        speech = "It's too dark."
                    }
                }
            }

            41 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 27
                }

                if (x in 215..315 && y in 220..325) {
                    someChanges = true
                    locationNumber = 42
                }
            }

            42 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 41
                }

                if (x in 290..415 && y in 160..315) {
                    someChanges = true
                    locationNumber = 43
                }
            }

            43 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 42
                }

                if (x in 60..280 && y in 160..390) {
                    someChanges = true
                    locationNumber = 46
                }

                if (x in 560..730 && y in 200..360) {
                    someChanges = true
                    locationNumber = if (iPad) 45 else 44
                }
            }

            44 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 43
                }

                if (x in 380..425 && y in 210..245) {
                    someChanges = true
                    locationNumber = 45
                    iPad = true
                    addToInventory(15)
                }
            }

            45 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 43
                }
            }

            46 -> {
                if (y > 480) {
                    someChanges = true
                    locationNumber = 43
                }
            }
        }

        return someChanges
    }

    private fun addToInventory(number: Int) {
        sound = 1
        for (i in 0 until 7) {
            if (inventory[i].first == 0) {
                inventory[i] = Pair(number, 0)
                break
            }
        }
    }

    private fun removeFromInventory(number: Int) {
        for (i in 0 until 7) {
            if (inventory[i].first == number) {
                for (j in i until 6)
                    inventory[j] = inventory[j + 1]
                inventory[6] = Pair(0, 0)
            }
        }
    }

    // 1, 3 = box and knife; 1 = box
    private fun checkSelectedItems(obj1: Int, obj2: Int = 0): Boolean {
        var fl1 = false
        var fl2 = obj2 == 0

        val expectedNumberOfSelectedItems = if (fl2) 1 else 2
        var numberOfSelectedItems = 0

        for (i in 0 until 7) {
            if (inventory[i].second == 0) continue
            numberOfSelectedItems++

            if (inventory[i].first == obj1) fl1 = true
            if (fl2) continue
            if (inventory[i].first == obj2) fl2 = true
        }

        return fl1 && fl2 && (expectedNumberOfSelectedItems == numberOfSelectedItems)
    }

    private fun resetAllSelectionsInInventory() {
        for (i in inventory.indices) {
            if (inventory[i].second == 1)
                inventory[i] = Pair(inventory[i].first, 0)
        }
    }

    // returns true if need to disable crazyFacePane
    fun inventory(number: Int) {
        if (inventory[number].first == 0) return

        if (inventory[number].second == 0) {
            inventory[number] = Pair(inventory[number].first, 1)

            // safe
            if (inventory[number].first == 2) {
                locationTmp = locationNumber
                locationNumber = 47
            }

            // photo
            if (inventory[number].first == 14) {
                locationTmp = locationNumber
                locationNumber = 28
            }

            // iPad
            if (inventory[number].first == 15) {
                locationTmp = locationNumber
                locationNumber =
                    if (!iPadUnlock) { 47 }
                    else { if (crazyFaceCompleted) { 23 }
                    else { 48 }
                }
            }
        } else {
            inventory[number] = Pair(inventory[number].first, 0)

            if (inventory[number].first == 2 || inventory[number].first == 14)
                locationNumber = locationTmp

            if (inventory[number].first == 15)
                locationNumber = locationTmp
        }
    }

    // returns true if merge was successful
    fun merge(): Boolean {
        if (checkSelectedItems(1, 3)) {
            removeFromInventory(1)
            removeFromInventory(3)
            addToInventory(5)
            return true
        }

        if (checkSelectedItems(4, 8)) {
            removeFromInventory(4)
            removeFromInventory(8)
            addToInventory(9)
            return true
        }

        return false
    }

    fun passwords(str: String): Boolean {
        if (!door2Opened) {
            // safe1
            if (str == "921") {
                removeFromInventory(2)
                addToInventory(5)
                locationNumber = locationTmp
                return true
            }
        } else {
            if (!iPad) {
                // safe2
                if (str == "7869") {
                    removeFromInventory(2)
                    addToInventory(14)
                    photo = true
                    locationNumber = locationTmp
                    return true
                }
            } else {
                // iPad
                if (str == "583782") {
                    removeFromInventory(7)
                    iPadUnlock = true
                    locationNumber = 48 // 48 = crazy face
                    return true
                }
            }
        }

        return false
    }

    fun crazyFaceCompleted() {
        crazyFaceCompleted = true
        locationNumber = 23
    }

    // game progress for saving
    fun getProgress(): String {
        gameStatus = 0
        val result = StringBuilder()

        result
            .append(locationNumber).append(" ")

            .append(inventory[0].first).append(" ").append(inventory[0].second).append(" ")
            .append(inventory[1].first).append(" ").append(inventory[1].second).append(" ")
            .append(inventory[2].first).append(" ").append(inventory[2].second).append(" ")
            .append(inventory[3].first).append(" ").append(inventory[3].second).append(" ")
            .append(inventory[4].first).append(" ").append(inventory[4].second).append(" ")
            .append(inventory[5].first).append(" ").append(inventory[5].second).append(" ")
            .append(inventory[6].first).append(" ").append(inventory[6].second).append(" ")

            .append(boolToInt(box)).append(" ").append(boolToInt(safe1)).append(" ")
            .append(boolToInt(knife)).append(" ").append(boolToInt(battery)).append(" ")
            .append(boolToInt(door1Opened)).append(" ").append(boolToInt(hammer)).append(" ")
            .append(boolToInt(mirror)).append(" ").append(boolToInt(door2Opened)).append(" ")
            .append(boolToInt(liftRevealed)).append(" ").append(boolToInt(flashlight)).append(" ")
            .append(boolToInt(fork)).append(" ").append(boolToInt(fireplaceOff)).append(" ")
            .append(boolToInt(safe2)).append(" ").append(boolToInt(emptyBucket)).append(" ")
            .append(boolToInt(scoop)).append(" ").append(boolToInt(stopperOff)).append(" ")
            .append(boolToInt(fullBucket)).append(" ").append(boolToInt(flashlightOn)).append(" ")
            .append(boolToInt(photo)).append(" ").append(boolToInt(iPad)).append(" ")
            .append(boolToInt(iPadUnlock)).append(" ").append(boolToInt(crazyFaceCompleted)).append(" ")
            .append(boolToInt(door3Opened))

        return result.toString()
    }

    private fun boolToInt(fl: Boolean) = if (fl) 1 else 0
}