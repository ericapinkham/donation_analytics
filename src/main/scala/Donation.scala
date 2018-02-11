import scala.util.matching.Regex

object Donation {
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
	def apply(cmte_id: String, name: String, zip_code: String, transaction_dt: String, transaction_amt: String, other_id: String): Donation =
		new Donation(cmte_id, name, extractZip(zip_code), extractYear(transaction_dt), extractAmount(transaction_amt), other_id)
	
	/**
	  *
	  * @param pattern the regex pattern to use
	  * @param searchString the string being searched
	  * @return
	  */
	def regexMatch(pattern: Regex)(searchString: String): Boolean = {
		searchString match {
			case pattern(_*) => true
			case _ => false
		}
	}
	
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
case class Donation(cmte_id: String, name: String, zip: String, year: Int, amount: Double, other_id: String) {
	/** Determines whether this is a valid donation. I.e., all fields are readable and parsed correctly.*/
	val isValid: Boolean = {
		List(
			Donation.regexMatch("""^[a-zA-Z\d]{9}""".r)(cmte_id),
			Donation.regexMatch("""^.{0,200}$""".r)(name),
			Donation.regexMatch("""^\d{5}$""".r)(zip),
			year > 2000, // this is dumb
			amount >= 0.0,
			other_id == ""
		).foldLeft(true)((a,b) => a & b)
	}
	
	/** Whether this is from a repeat donor or not */
	val isFromRepeatDonor: Boolean = tracker.donatedInYear(donorId, year - 1)
	
	/** A "unique" identifier for this object */
	lazy val donorId: String = s"$zip|$name"
	

	override def toString: String = s"donation:$cmte_id|$name|$zip|$year|$amount|$other_id"
}