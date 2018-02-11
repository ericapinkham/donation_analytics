/** A constructor object for the Recipient class */
object Recipient {
	def apply(cmte_id: String, zip5: String, year: Int, amount: Double, transactions: Int): Recipient = new Recipient(cmte_id, zip5, year, amount, transactions)
	def apply(cmte_id: String, zip5: String, year: Int, amount: Double): Recipient = new Recipient(cmte_id, zip5, year, amount, 1)
	def apply(donation: Donation): Recipient = donation match {
		case Donation(cmte_id, _, zip, year, amount, _) => new Recipient(cmte_id, zip, year, amount, 1)
		case _ => throw new Error(s"Invalid parameter @donation: $donation")
	}
}

/** A class to store and calculate values related to recipients
  *
  * @param cmte_id the id of the recipient
  * @param zip5 5 digit zip code
  * @param year 4 digit year
  * @param amount the amount of the donation
  * @param transactions the number of transactions for this id and zip code
  */
case class Recipient(cmte_id: String, zip5: String, year: Int, amount: Double, transactions: Int) {
	/** Asks the tracker for the current percentile */
	val percentile: Int = Tracker.percentileValue
	
	/** An identifer for this object */
	val id: String = s"$cmte_id|$zip5|$year"
	
	/** Combines two recipient records into a new recipient record
	  *
	  * @param other another recipient record
	  * @return a new recipient which is the "aggregate" of the two
	  */
	def +(other: Recipient): Recipient = (this, other) match {
		case (Recipient(cmte_idX, zip5X, yearX, amountX, transactionsX), Recipient(cmte_idY, zip5Y, yearY, amountY, transactionsY))
			if cmte_idX == cmte_idY & zip5X == zip5Y & yearX == yearY
			=> Recipient(cmte_idX, zip5X, yearX, amountX + amountY, transactionsX + transactionsY)
		case _ => throw new Error(s"Non-compatible recipients: $this, $other")
	}
	
	/** Makes this object printable */
	override def toString: String = s"$cmte_id|$zip5|$year|$percentile|$amount|$transactions"
}
