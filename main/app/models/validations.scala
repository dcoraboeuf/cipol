package models

import javax.persistence._
 
import play.db.jpa._

@Entity
class Validation (
	val hash: String,
	val author: String,
	val message: String
)
extends Model
