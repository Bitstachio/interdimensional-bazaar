package dev.bitstachio.interdimensional_bazaar.product.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * DangerLevelConverter — JPA AttributeConverter for DangerLevel enum.
 *
 * JPA's default enum mapping uses either ordinal (fragile) or name() which
 * would store "EXTINCTION_CLASS" instead of "Extinction Class". This converter
 * maps between the Kotlin enum and the exact MySQL ENUM string values defined
 * in the DB schema, keeping both sides in sync without schema changes.
 */
@Converter(autoApply = true)
class DangerLevelConverter : AttributeConverter<DangerLevel?, String?> {

    override fun convertToDatabaseColumn(attribute: DangerLevel?): String? =
        attribute?.dbValue

    override fun convertToEntityAttribute(dbData: String?): DangerLevel? =
        dbData?.let { DangerLevel.fromDbValue(it) }
}