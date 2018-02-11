object recipient {
	def apply(cmte_id: String, zip5: String, year: Int, amount: Int, transactions: Int): recipient = new recipient(cmte_id, zip5, year, amount, transactions)
	def apply(cmte_id: String, zip5: String, year: Int, amount: Int): recipient = new recipient(cmte_id, zip5, year, amount, 1)
	def None: Any = None
}

case class recipient(cmte_id: String, zip5: String, year: Int, amount: Int, transactions: Int) {
	// to implement
	lazy val percentile: Int = 50
	
	// An identifer for this object
	val id: String = s"$cmte_id|$zip5|$year"
	
	/** Combines two recipient records into a new recipient record
	  *
	  * @param other another recipient record
	  * @return
	  */
	def +(other: recipient): recipient = (this, other) match {
//		case (recipient(_, _, _, _, _), recipient.None) => this
		case (recipient(cmte_idX, zip5X, yearX, amountX, transactionsX), recipient(cmte_idY, zip5Y, yearY, amountY, transactionsY))
			if cmte_idX == cmte_idY & zip5X == zip5Y & yearX == yearY
			=> recipient(cmte_idX, zip5X, yearX, amountX + amountY, transactionsX + transactionsY)
		case _ => throw new Error(s"Non-compatible recipients: $this, $other")
	}
	
	/** Makes this object printable */
	override def toString: String = s"$cmte_id|$zip5|$year|$percentile|$amount|$transactions"
}
