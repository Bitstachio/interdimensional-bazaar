package dev.bitstachio.interdimensional_bazaar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
class InterdimensionalBazaarApplication

fun main(args: Array<String>) {
	runApplication<InterdimensionalBazaarApplication>(*args)
}
