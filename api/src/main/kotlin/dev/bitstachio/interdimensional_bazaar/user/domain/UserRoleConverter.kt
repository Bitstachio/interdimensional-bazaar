package dev.bitstachio.interdimensional_bazaar.user.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * UserRoleConverter — JPA AttributeConverter for UserRole enum.
 *
 * Maps between the Kotlin UserRole enum and the exact MySQL ENUM string values
 * ('customer', 'admin').
 */
@Converter(autoApply = true)
class UserRoleConverter : AttributeConverter<UserRole?, String?> {

    override fun convertToDatabaseColumn(attribute: UserRole?): String? =
        attribute?.dbValue

    override fun convertToEntityAttribute(dbData: String?): UserRole? =
        dbData?.let { UserRole.fromDbValue(it) }
}