import scala.util.matching.Regex

object donation {
	/** Constructor for donation class
	  *
	  * @param cmte_id recipient of donation
	  * @param name donor's name
	  * @param zip_code the (5/9) digit zip code as a string
	  * @param transaction_dt the timestamp mmddyyyy
	  * @param transaction_amt the amount of the transaction as a string
	  * @param other_id used for determining if the donation is valid
	  * @return
	  */
	def apply(cmte_id: String, name: String, zip_code: String, transaction_dt: String, transaction_amt: String, other_id: String): donation =
		new donation(cmte_id, name, extractZip(zip_code), extractYear(transaction_dt), extractAmount(transaction_amt), other_id)
	
	
	private val patternYear = """\d{4}(\d{4})$""" r
	private def extractYear(transactionDate: String): Int = {
		patternYear.findFirstIn(transactionDate).getOrElse("") match {
			case patternYear(year) => year.toInt
			case patternYear(_*) => 0
		}
	}
	
	private val patternZip = """(\d{5})\d{0,4}$""" r
	private def extractZip(zip: String): String = {
		patternZip.findFirstIn(zip).getOrElse("") match {
			case patternZip(zip5) => zip5
			case patternZip(_*) => ""
		}
	}
	
	private def extractAmount(transaction_amt: String): Double = transaction_amt.toDouble
}

/** Stores relevant information about the donation
  *
  * @param cmte_id the recipient of the donation
  * @param name the name of the donor
  * @param zip the 5 digit zip code string
  * @param year the year in which the donation was made
  * @param amount the amount of the donation
  * @param other_id used for verifying this is the correct type of transaction
  */
case class donation(cmte_id: String, name: String, zip: String, year: Int, amount: Double, other_id: String) {
	/**
	  *
	  * @param pattern the regex pattern to use
	  * @param searchString the string being searched
	  * @return
	  */
	def regexMatch(pattern: Regex)(searchString: String) = {
		searchString match {
			case pattern(_*) => true
			case _ => false
		}
	}
	
	/** Determines whether this is a valid donation. I.e., all fields are readable and parsed correctly.*/
	val isValid: Boolean = {
		
		List(
			regexMatch("""^[a-zA-Z\d]{9}""".r)(cmte_id),
			regexMatch("""^.{0,200}$""".r)(name),
			regexMatch("""^\d{5}$""".r)(zip),
			year > 2000, // this is dumb
			amount >= 0.0,
			other_id == ""
		).foldLeft(true)((a,b) => a & b)
	}
	
	lazy val isFromRepeatDonor: Boolean = {
		/**
		  * Determines whether this is from a repeat donor.
		  */
		true
	}
	
	override def toString: String = s"donation:$cmte_id|$name|$zip|$year|$amount|$other_id"
}