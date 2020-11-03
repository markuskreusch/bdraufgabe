package de.markuskreusch.bdraufgabe.rest.mappers

import de.markuskreusch.bdraufgabe.entities.Book
import de.markuskreusch.bdraufgabe.rest.dtos.BookDto
import de.markuskreusch.bdraufgabe.rest.dtos.BookPatchDto
import org.springframework.stereotype.Component
import java.util.*

@Component
class BooksMapper {

    fun toBook(bookDto: BookDto): Book {
        return Book(
                id = bookDto.id ?: UUID.randomUUID(),
                author = bookDto.author,
                title = bookDto.title,
                status = bookDto.status
        )
    }

    fun toDto(book: Book): BookDto {
        return BookDto(
                id = book.id,
                author = book.author,
                title = book.title,
                status = book.status
        )
    }

    fun applyPatch(book: Book, patch: BookPatchDto) {
        patch.author?.let { book.author = it }
        patch.title?.let { book.title = it }
        patch.status?.let { book.status = it }
    }

}
