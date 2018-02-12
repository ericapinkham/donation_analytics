import scala.util.matching.Regex

object Donation {
	/**
	  * Constructor for donation class
	  * @param l the line to parse
	  * @return a new Donation object
	  */
	def apply(l: String): Donation = {
		l.split('|') match {
			case Array(cmte_id, _, _, _, _, _, _, name, _, _, zip_code, _, _, transaction_dt, transaction_amt, other_id, _, _, _, _, _) =>
				Donation(cmte_id, name, zip_code, transaction_dt, transaction_amt, other_id)
			case _ => throw new Error(s"$l doesn't unpack properly")
		}
	}
	
	/**
	  * Constructor for donation class
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
	  * Just a simple regex matching function
	  * @param pattern the regex pattern to use
	  * @param searchString the string being searched
	  * @return returns true if match found
	  */
	def regexMatch(pattern: Regex)(searchString: String): Boolean = {
		searchString match {
			case pattern(_*) => true
			case _ => false
		}
	}
	
	private val patternYear = """\d{4}(\d{4})$""".r // for some reason the compiler freaks out if this is in the function definition
	/**
	  * Extracts the year
	  * @param transactionDate the date string from which to extract the year
	  * @return an integer year
	  */
	private def extractYear(transactionDate: String): Int = {
		if (regexMatch(patternYear)(transactionDate))
			transactionDate match {
				case patternYear(year) => year.toInt
				case patternYear(_*) => 0
			}
		else 0
	}
	
	private val patternZip = """(\d{5})\d{0,4}$""".r // for some reason the compiler freaks out if this is in the function definition
	/**
	  * Extracts the 5 digit zip code from a possibly 9 digit zip
	  * @param zip the zip code
	  * @return the 5 digit zip code
	  */
	private def extractZip(zip: String): String = {
		if (regexMatch(patternZip)(zip))
			patternZip.findFirstIn(zip).getOrElse("") match {
				case patternZip(zip5) => zip5
				case patternZip(_*) => ""
			}
		else ""
	}
	
	/**
	  * Extracts the amount. I'm assuming that this is supposed to be rounded though it's not explicit in the instructions.
	  * @param transaction_amt the amount as a string
	  * @return the amount as a Int
	  */
	private def extractAmount(transaction_amt: String): Int = math.round(transaction_amt.toDouble).toInt
}

/**
  * Stores relevant information about the donation
  * @param cmte_id the recipient of the donation
  * @param name the name of the donor
  * @param zip the 5 digit zip code string
  * @param year the year in which the donation was made
  * @param amount the amount of the donation. It's not specified, but this seems to be rounded.
  * @param other_id used for verifying this is the correct type of transaction
  */
case class Donation(cmte_id: String, name: String, zip: String, year: Int, amount: Int, other_id: String) {
	/** Determines whether this is a valid donation. I.e., all fields are readable and parsed correctly.*/
	val isValid: Boolean = {
		List(
			Donation.regexMatch("""^[a-zA-Z\d]{9}""".r)(cmte_id),
			Donation.regexMatch("""^.{0,200}$""".r)(name),
			Donation.regexMatch("""^\d{5}$""".r)(zip),
			year > 1776, // FEC probably didn't collect data before this
			amount >= 0,
			other_id == ""
		).foldLeft(true)((a,b) => a & b)
	}
	
	/** A "unique" identifier for this object */
	lazy val donorId: String = s"$zip|$name"
	
	/** A way to display the donation in human readable format */
	override def toString: String = s"donation:$cmte_id|$name|$zip|$year|$amount|$other_id"
}