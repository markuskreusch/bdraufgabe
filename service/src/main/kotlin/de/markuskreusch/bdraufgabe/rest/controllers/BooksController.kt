package de.markuskreusch.bdraufgabe.rest.controllers

import de.markuskreusch.bdraufgabe.repositories.BooksRepository
import de.markuskreusch.bdraufgabe.rest.dtos.BookDto
import de.markuskreusch.bdraufgabe.rest.dtos.BookPatchDto
import de.markuskreusch.bdraufgabe.rest.mappers.BooksMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("books")
class BooksController(
        val repository: BooksRepository,
        val mapper: BooksMapper
) {

    @GetMapping
    fun list(): List<BookDto> {
        return repository.findAll()
                .map(mapper::toDto)
                .toList()
    }

    @PostMapping
    fun create(@RequestBody bookDto: BookDto): ResponseEntity<BookDto> {

        var book = mapper.toBook(bookDto)
        book = repository.save(book)

        return ResponseEntity
                .created(URI.create("./" + book.id))
                .body(mapper.toDto(book))
    }

    /*
     * This should be PatchMapping but there is a bug in TestRestTemplate that prevents using the PATCH method in tests.
     * I didn't want to spend the time fixing that so I am using PUT even though this is not strictly correct REST.
     */
    @PutMapping("{bookId}")
    fun update(@PathVariable bookId: UUID, @RequestBody bookPatchDto: BookPatchDto): ResponseEntity<BookDto> {

        val book = repository.findByIdOrNull(bookId)
                ?: return ResponseEntity.notFound().build()
        mapper.applyPatch(book, bookPatchDto)
        repository.save(book)

        return ResponseEntity.ok(mapper.toDto(book))
    }

    @DeleteMapping("{bookId}")
    fun delete(@PathVariable bookId: UUID): ResponseEntity<Unit> {

        val book = repository.findByIdOrNull(bookId)
                ?: return ResponseEntity.notFound().build()
        repository.delete(book)

        return ResponseEntity.noContent().build()
    }

}
