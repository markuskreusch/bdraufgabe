package de.markuskreusch.bdraufgabe.rest.controllers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ninjasquad.springmockk.MockkBean
import de.markuskreusch.bdraufgabe.entities.Book
import de.markuskreusch.bdraufgabe.entities.BookStatus.AVAILABLE
import de.markuskreusch.bdraufgabe.entities.BookStatus.BORROWED
import de.markuskreusch.bdraufgabe.repositories.BooksRepository
import de.markuskreusch.bdraufgabe.rest.dtos.BookDto
import io.mockk.every
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import java.net.URI
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerTest {

    @MockkBean
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun testListBooks() {
        // given
        val id1 = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabb")
        val id2 = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabc")
        every { booksRepository.findAll() }.returns(listOf(
                Book(
                        id = id1,
                        title = "Title1",
                        author = "Author1",
                        status = AVAILABLE
                ),
                Book(
                        id = id2,
                        title = "Title2",
                        author = "Author2",
                        status = BORROWED
                )
        ))

        // when
        val books = restTemplate.getForObject<String>(URI.create("/books")) ?: error("Result expected")

        // then
        assertEquals("""
            [
                {
                    "id":$id1,
                    "title":"Title1",
                    "author":"Author1",
                    "status":"AVAILABLE"
                },{
                    "id":$id2,
                    "title":"Title2",
                    "author":"Author2",
                    "status":"BORROWED"
                }
            ]            
        """.trimIndent(), books, false)
    }

    @Test
    fun testCreateBook() {

    }

    /*
     * Usually there should be testcases for the other methods as well but because of the limited time I decided to
     * just implement a test for list and handle the rest in the integration test.
     */

}
