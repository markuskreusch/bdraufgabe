package de.markuskreusch.bdraufgabe.rest.dtos

import de.markuskreusch.bdraufgabe.entities.BookStatus
import javax.persistence.EnumType
import javax.persistence.Enumerated

class BookPatchDto(

        var title: String?,

        var author: String?,

        @Enumerated(EnumType.STRING)
        var status: BookStatus?

)
