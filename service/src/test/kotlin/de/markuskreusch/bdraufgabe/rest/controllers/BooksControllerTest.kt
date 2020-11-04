package de.markuskreusch.bdraufgabe.rest.controllers

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.ninjasquad.springmockk.MockkBean
import de.markuskreusch.bdraufgabe.entities.Book
import de.markuskreusch.bdraufgabe.entities.BookStatus.AVAILABLE
import de.markuskreusch.bdraufgabe.entities.BookStatus.BORROWED
import de.markuskreusch.bdraufgabe.repositories.BooksRepository
import de.markuskreusch.bdraufgabe.rest.dtos.BookDto
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForObject
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.APPLICATION_JSON
import java.net.URI
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerTest {

    @MockkBean
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `list books`() {
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
                    "id":"$id1",
                    "title":"Title1",
                    "author":"Author1",
                    "status":"AVAILABLE"
                },{
                    "id":"$id2",
                    "title":"Title2",
                    "author":"Author2",
                    "status":"BORROWED"
                }
            ]            
        """.trimIndent(), books, false)
    }

    @Test
    fun `create book`() {
        // given
        val id = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabb")
        val storedBook = slot<Book>()
        every { booksRepository.save(capture(storedBook)) }.returns(Book(
                id,
                "Title2",
                "Author2",
                BORROWED
        ))

        // when
        val book = restTemplate.postForObject(URI.create("/books"), BookDto(
                id = null,
                title = "Title1",
                author = "Author1",
                status = AVAILABLE
        ), String::class.java)

        // then
        assertThat(storedBook.captured.title).isEqualTo("Title1")
        assertThat(storedBook.captured.author).isEqualTo("Author1")
        assertThat(storedBook.captured.status).isEqualTo(AVAILABLE)
        assertEquals("""
            {
                "id":"$id",
                "title":"Title2",
                "author":"Author2",
                "status":"BORROWED"
            }
        """.trimIndent(), book, false)

    }

    @Test
    fun `delete non existing book`() {
        // given
        val id = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabb")
        every { booksRepository.findByIdOrNull(id) }.returns(null)

        // when
        val response = restTemplate.exchange<Unit>(URI.create("/books/$id"), DELETE)

        // then
        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `delete existing book`() {
        // given
        val id = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabb")
        val book = Book(
                id,
                "Title",
                "Author",
                AVAILABLE
        )
        every { booksRepository.findByIdOrNull(id) }.returns(book)
        every { booksRepository.delete(book) }.returns(Unit)

        // when
        val response = restTemplate.exchange<Unit>(URI.create("/books/$id"), DELETE)

        // then
        verify { booksRepository.delete(book) }
        assertThat(response.statusCode).isEqualTo(NO_CONTENT)
    }

    @Test
    fun `update non existing book`() {
        // given
        val id = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabb")
        every { booksRepository.findByIdOrNull(id) }.returns(null)

        // when
        val headers = HttpHeaders().apply {
            contentType = APPLICATION_JSON
        }
        val entity = HttpEntity("""
            {
                "title": "NewTitle",
                "author": "NewAuthor",
                "status": "AVAILABLE"
            }
        """.trimIndent(), headers)
        val response = restTemplate.exchange<Unit>(URI.create("/books/$id"), PUT, entity)

        // then
        assertThat(response.statusCode).isEqualTo(NOT_FOUND)
    }

    @Test
    fun `update title and author of existing book`() {
        // given
        val id = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabb")
        val book = Book(
                id,
                "Title",
                "Author",
                BORROWED
        )
        every { booksRepository.findByIdOrNull(id) }.returns(book)
        every { booksRepository.save(book) }.returns(book)

        // when
        val headers = HttpHeaders().apply {
            contentType = APPLICATION_JSON
        }
        val entity = HttpEntity("""
            {
                "title": "NewTitle",
                "author": "NewAuthor"
            }
        """.trimIndent(), headers)
        val response = restTemplate.exchange<String>(URI.create("/books/$id"), PUT, entity)

        // then
        verify { booksRepository.save(book) }
        assertThat(response.statusCode).isEqualTo(OK)
        assertEquals("""
            {
                "id":"$id",
                "title":"NewTitle",
                "author":"NewAuthor",
                "status":"BORROWED"
            }
        """.trimIndent(), response.body, false)
    }

    @Test
    fun `update status of existing book`() {
        // given
        val id = UUID.fromString("3273a27e-44fe-495e-97e9-f5399e72dabb")
        val book = Book(
                id,
                "Title",
                "Author",
                BORROWED
        )
        every { booksRepository.findByIdOrNull(id) }.returns(book)
        every { booksRepository.save(book) }.returns(book)

        // when
        val headers = HttpHeaders().apply {
            contentType = APPLICATION_JSON
        }
        val entity = HttpEntity("""
            {
                "status": "AVAILABLE"
            }
        """.trimIndent(), headers)
        val response = restTemplate.exchange<String>(URI.create("/books/$id"), PUT, entity)

        // then
        verify { booksRepository.save(book) }
        assertThat(response.statusCode).isEqualTo(OK)
        assertEquals("""
            {
                "id":"$id",
                "title":"Title",
                "author":"Author",
                "status":"AVAILABLE"
            }
        """.trimIndent(), response.body, false)
    }

}
