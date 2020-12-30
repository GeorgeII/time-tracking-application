package models

import java.time.LocalDateTime
import java.util.UUID

case class SessionToken(userId: Long, token: UUID, expirationDate: LocalDateTime)
