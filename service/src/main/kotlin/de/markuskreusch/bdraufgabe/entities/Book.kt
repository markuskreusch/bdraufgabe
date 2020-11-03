package de.markuskreusch.bdraufgabe.entities

import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
class Book(

        @Id
        var id: UUID,

        var title: String,

        var author: String,

        @Enumerated(STRING)
        var status: BookStatus

)
