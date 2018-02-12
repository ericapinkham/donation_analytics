import scala.collection.mutable.HashMap

/**
  * Keeps track of recipients, donations and percentiles
  * @param percentile the integer percentile we're asked to calculate
  */
class Tracker(val percentile: Int) {
	val recipients = HashMap.empty[String, Recipient]
	val donors = HashMap.empty[String, Set[Int]]
	var contributions: List[Double] = Nil
	
	/**
	  * Takes a donation and processes its information
	  * @param newDonation the newly received donation
	  */
	def register(newDonation: Donation): Unit = {
		// Add the donor to the registry
		if (donors.isDefinedAt(newDonation.donorId))
			donors(newDonation.donorId) += newDonation.year
		else
			donors(newDonation.donorId) = Set(newDonation.year)
		
		// If this donation is from a repeat donor, add it's amount to contributions for percentile tracking
		if (isFromRepeatDonor(newDonation)) contributions = newDonation.amount :: contributions
	}
	
	/**
	  * Responsible for generating and maintaining recipient records
	  * @param newDonation the new donation to process
	  * @return A new recipient object
	  */
	def getRecipient(newDonation: Donation): Recipient = {
		// Generate a recipient object
		val newRecipient = Recipient(this)(newDonation)
		
		// Aggregate or set
		if (recipients.isDefinedAt(newRecipient.id))
			recipients(newRecipient.id) += newRecipient
		else
			recipients(newRecipient.id) = newRecipient
		
		// return the newly registered recipient
		recipients(newRecipient.id)
	}
	
	/**
	  * Answer the question: Did this donor donate in this year?
	  * @param donorId the unique id of the donor
	  * @param year the year
	  * @return True if the donor made a donation in the requested year
	  */
	def donatedInYear(donorId: String, year: Int): Boolean = donors.getOrElse(donorId, Set()).contains(year)
	
	/**
	  * Determines if the donation came from a repeat donor
	  * @param newDonation the donation object
	  * @return
	  */
	def isFromRepeatDonor(newDonation: Donation): Boolean = donatedInYear(newDonation.donorId, newDonation.year - 1)
	
	/**
	  * Calculates the percentile
	  * @return the percentile
	  */
	def percentileValue: Int = math.round(contributions.sorted.apply((percentile / 100.0 * contributions.length).toInt)).toInt
}
