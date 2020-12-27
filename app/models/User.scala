package models

import java.util.UUID

case class User(nickname: String, password: String, identifier: UUID)

