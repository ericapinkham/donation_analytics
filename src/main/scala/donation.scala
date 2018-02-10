// I think i want to do a companion object?

case class donation(cmte_id: String, name: String, zip_code: String, transaction_dt: String, transaction_amt_string: String, other_id: String) {
	// Convert types to whatever we need them to be
	lazy val transaction_amt: Int = transaction_amt_string.toInt
	
	private val pattern = """(\d{4})(\d{2})(\d{2})""" r
	
	val year, month, day = pattern.findFirstIn(transaction_dt).getOrElse("") match {
		case "" => None
		case pattern(yearGroup, monthGroup, dayGroup) => (yearGroup.toInt, monthGroup.toInt, dayGroup.toInt)
	}
	
}