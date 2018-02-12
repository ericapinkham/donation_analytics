/** A constructor object for the Recipient class */
object Recipient {
	/**
	  * Generates a new recipient
	  * @param cmte_id the id of the recipient
	  * @param zip5 the 5 digit zip code
	  * @param year the year
	  * @param amount the amount of the transaction
	  * @param transactions the number of transactions made to this recipient by repeat donors
	  * @return a new recipient object
	  */
	def apply(cmte_id: String, zip5: String, year: Int, amount: Double, transactions: Int, tracker: Tracker): Recipient = new Recipient(cmte_id, zip5, year, amount, transactions, tracker)

	/**
	  * Generates a new recipient
	  * @param donation the donation from which to generate a recipient object
	  * @return a new recipient object
	  */
	def apply(donation: Donation): Recipient = donation match {
		case Donation(cmte_id, _, zip, year, amount, _, tracker) => new Recipient(cmte_id, zip, year, amount, 1, tracker)
		case _ => throw new Error(s"Invalid parameter @donation: $donation")
	}
}

/**
  * A class to store and calculate values related to recipients
  * @param cmte_id the id of the recipient
  * @param zip5 5 digit zip code
  * @param year 4 digit year
  * @param amount the amount of the donation
  * @param transactions the number of transactions for this id and zip code
  */
case class Recipient(cmte_id: String, zip5: String, year: Int, amount: Double, transactions: Int = 1, tracker: Tracker) {
	/** Asks the tracker for the current percentile */
	val percentile: Int = tracker.percentileValue
	
	/** An identifer for this object */
	val id: String = s"$cmte_id|$zip5|$year"
	
	/**
	  * Combines two recipient records into a new recipient record
	  * @param other another recipient record
	  * @return a new recipient which is the "aggregate" of the two
	  */
	def +(other: Recipient): Recipient = (this, other) match {
		case (Recipient(cmte_idX, zip5X, yearX, amountX, transactionsX, _), Recipient(cmte_idY, zip5Y, yearY, amountY, transactionsY,_))
			if cmte_idX == cmte_idY & zip5X == zip5Y & yearX == yearY
			=> Recipient(cmte_idX, zip5X, yearX, amountX + amountY, transactionsX + transactionsY, tracker)
			// Ok, this is a little sketchy since the trackers could be different and these objects would still add, but don't do that.
		case _ => throw new Error(s"Non-compatible recipients: $this, $other")
	}
	
	/** Makes this object printable */
	override def toString: String = s"$cmte_id|$zip5|$year|$percentile|$amount|$transactions"
}
