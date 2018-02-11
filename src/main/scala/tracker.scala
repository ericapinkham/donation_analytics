import scala.collection.mutable.HashMap

/**
  *
  */
object tracker {
	val recipients = HashMap.empty[String, recipient]
	val donors = HashMap.empty[String, Set[Int]]
	
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

}
