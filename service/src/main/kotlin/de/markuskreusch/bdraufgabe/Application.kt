package de.markuskreusch.bdraufgabe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class Application {

    @Bean
    fun corsConfiguration(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET","POST","PUT","DELETE")
            }
        }
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
