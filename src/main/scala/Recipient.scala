import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTMdefaultNamespaceDeclarationNode

object Recipient {
	def apply(cmte_id: String, zip5: String, year: Int, amount: Double, transactions: Int): Recipient = new Recipient(cmte_id, zip5, year, amount, transactions)
	def apply(cmte_id: String, zip5: String, year: Int, amount: Double): Recipient = new Recipient(cmte_id, zip5, year, amount, 1)
	def apply(donation: Donation): Recipient = donation match {
		case Donation(cmte_id, _, zip, year, amount, _) => new Recipient(cmte_id, zip, year, amount, 1)
		case _ => throw new Error(s"Invalid parameter @donation: $donation")
	}
	def None: Any = None
}

case class Recipient(cmte_id: String, zip5: String, year: Int, amount: Double, transactions: Int) {
	
	lazy val percentile: Int = 50
	
	// An identifer for this object
	val id: String = s"$cmte_id|$zip5|$year"
	
	/** Combines two recipient records into a new recipient record
	  *
	  * @param other another recipient record
	  * @return
	  */
	def +(other: Recipient): Recipient = (this, other) match {
//		case (recipient(_, _, _, _, _), recipient.None) => this
		case (Recipient(cmte_idX, zip5X, yearX, amountX, transactionsX), Recipient(cmte_idY, zip5Y, yearY, amountY, transactionsY))
			if cmte_idX == cmte_idY & zip5X == zip5Y & yearX == yearY
			=> Recipient(cmte_idX, zip5X, yearX, amountX + amountY, transactionsX + transactionsY)
		case _ => throw new Error(s"Non-compatible recipients: $this, $other")
	}
	
	/** Makes this object printable */
	override def toString: String = s"$cmte_id|$zip5|$year|$percentile|$amount|$transactions"
}
