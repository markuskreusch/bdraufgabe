package de.markuskreusch.bdraufgabe.rest.dtos

import de.markuskreusch.bdraufgabe.entities.BookStatus
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated

class BookDto(

        var id: UUID?,

        var title: String,

        var author: String,

        @Enumerated(EnumType.STRING)
        var status: BookStatus

)
