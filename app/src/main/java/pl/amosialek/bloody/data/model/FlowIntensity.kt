package pl.amosialek.bloody.data.model

enum class FlowIntensity(val displayName: String, val level: Int) {
    SPOTTING("Spotting", 1),
    LIGHT("Light", 2),
    MEDIUM("Medium", 3),
    HEAVY("Heavy", 4);

    companion object {
        fun fromString(value: String?): FlowIntensity {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
                ?: MEDIUM
        }
    }
}
