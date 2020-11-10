package command

enum class Ranks(val cooldown: Int, val power: Int) {
    DEFAULT(600, 5),
    DONATOR(0, 10);
}