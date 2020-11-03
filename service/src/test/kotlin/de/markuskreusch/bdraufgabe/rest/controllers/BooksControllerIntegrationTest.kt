package de.markuskreusch.bdraufgabe.rest.controllers

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.*
import org.springframework.http.HttpMethod.*
import org.springframework.http.HttpStatus.*
import java.net.URI
import java.util.*
import javax.transaction.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    @Transactional
    fun testListBooks() {
        // given
        val book1Id = createBook()
        val book2Id = createBook()
        updateBook(book1Id)
        deleteBook(book2Id)

        // when
        val books = restTemplate.getForObject<String>(URI.create("/books")) ?: error("Result expected")

        // then
        assertEquals("""
            [
                {
                    "id":"$book1Id",
                    "title":"Changed Title",
                    "author":"Changed Author",
                    "status":"BORROWED"
                }
            ]            
        """.trimIndent(), books, false)
    }

    private fun createBook(): UUID {
        val headers = HttpHeaders().apply {
            set("Content-Type", "application/json")
        }

        val entity = HttpEntity("""
            {
                "title": "Title",
                "author": "Author",
                "status": "AVAILABLE"
            }
            """.trimIndent(), headers)

        val response = restTemplate
                .postForEntity("/books", entity, Unit::class.java)

        assertThat(response.statusCode).isEqualTo(CREATED)
        return UUID.fromString(response.headers.location.toString().substring(2))
    }

    private fun updateBook(book1Id: UUID) {
        val headers = HttpHeaders().apply {
            set("Content-Type", "application/json")
        }

        val entity = HttpEntity("""
            {
                "title": "Changed Title",
                "author": "Changed Author",
                "status": "BORROWED"
            }
            """.trimIndent(), headers)

        val response = restTemplate
                .exchange("/books/{bookId}", PUT, entity, Unit::class.java, book1Id.toString())

        assertThat(response.statusCode).isEqualTo(OK)
    }

    private fun deleteBook(book2Id: UUID) {
        val resonse = restTemplate
                .exchange("/books/{bookId}", DELETE, null, Unit::class.java, book2Id.toString())

        assertThat(resonse.statusCode).isEqualTo(NO_CONTENT)
    }

}
