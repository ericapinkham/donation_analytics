import scala.collection.mutable.HashMap
import scala.io.Source

/**
  *
  */
class Tracker(val percentile: Int) {
	val recipients = HashMap.empty[String, Recipient]
	val donors = HashMap.empty[String, Set[Int]]
	var contributions: List[Double] = Nil
	
	/**
	  *
	  * @param newDonation the newly received donation
	  */
	def register(newDonation: Donation): Unit = {
		// Add the donor to the registry
		if (donors.isDefinedAt(newDonation.donorId))
			donors(newDonation.donorId) += newDonation.year
		else
			donors(newDonation.donorId) = Set(newDonation.year)
		
		// If this donation is from a repeat donor, add it's amount to contributions for percentile tracking
		if (newDonation.isFromRepeatDonor) contributions = newDonation.amount :: contributions
	}
	
	def processRecipient(newDonation: Donation): Recipient = {
		// Generate a recipient object
		val newRecipient = Recipient(newDonation)
		
		// Aggregate or set
		if (recipients.isDefinedAt(newRecipient.id))
			recipients(newRecipient.id) += newRecipient
		else
			recipients(newRecipient.id) = newRecipient
		
		
		// return the newly registered recipient
		recipients(newRecipient.id)
	}
	
	/** Answer the question: Did this donor donate in this year?
	  *
	  * @param donorId the unique id of the donor
	  * @param year the year
	  * @return
	  */
	def donatedInYear(donorId: String, year: Int): Boolean = donors.getOrElse(donorId, Set()).contains(year)
	
	def percentileValue: Int = math.round(contributions.sorted.apply((percentile / 100.0 * contributions.length).toInt)).toInt
}
