import scala.collection.mutable.HashMap
import scala.io.Source

/**
  *
  */
object Tracker {
	val recipients = HashMap.empty[String, Recipient]
	val donors = HashMap.empty[String, Set[Int]]
	var contributions: List[Double] = Nil
	private val percentile: Int = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/percentile.txt").getLines.next().toInt
	
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
	
	def percentileValue: Double = (recipients.values.map(_.amount).toList.sorted).apply((percentile / 100.0 * recipients.count(_ => true)).toInt)
}
