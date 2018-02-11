import scala.collection.mutable.HashMap
import scala.io.Source

/**
  *
  */
object tracker {
	val recipients = HashMap.empty[String, recipient]
	val donors = HashMap.empty[String, Set[Int]]
	private val percentile: Int = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/percentile.txt").getLines.next().toInt
	
	/**
	  *
	  * @param newDonation the newly received donation
	  */
	def register(newDonation: donation): Unit = {
		// Add the donor to the registry
		if (donors.isDefinedAt(newDonation.donorId))
			donors(newDonation.donorId) += newDonation.year
		else
			donors(newDonation.donorId) = Set(newDonation.year)
		
		// Aggregate the recipient objects, if applicable
		//recipients
	}
	
	/** Answer the question: Did this donor donate in this year?
	  *
	  * @param donorId the unique id of the donor
	  * @param year the year
	  * @return
	  */
	def donatedInYear(donorId: String, year: Int): Boolean = donors.getOrElse(donorId, Set()).contains(year)
	
	def percentileValue: Int = (recipients.values.map(_.amount).toList.sorted).apply((percentile / 100.0 * recipients.count(_ => true)).toInt)
}
