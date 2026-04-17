package dev.bitstachio.interdimensional_bazaar.product.domain

/**
 * DangerLevel — Enum representing the danger classification of a product.
 */
enum class DangerLevel(val dbValue: String) {
    SAFE("Safe"),
    MODERATE("Moderate"),
    DANGEROUS("Dangerous"),
    EXTINCTION_CLASS("Extinction Class");

    companion object {
        fun fromDbValue(value: String): DangerLevel =
            entries.find { it.dbValue == value }
                ?: throw IllegalArgumentException("Unknown danger level: $value")
    }
}
