package de.markuskreusch.bdraufgabe.repositories

import de.markuskreusch.bdraufgabe.entities.Book
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BooksRepository : CrudRepository<Book, UUID>
