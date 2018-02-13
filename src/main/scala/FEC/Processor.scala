package FEC

/**
  * Abstracted methods to be executed by the Main object
  */
trait Processor {
	val tracker: Tracker
	val lines: Iterator[String]
	
	def writeLine(line: String): Unit
	
	/**
	  * The workhorse method. Processes all lines in the file.
	  */
	def processDonations(lines: Iterator[String]): Unit = {
		// Iterate through each donation and process donations one at a time
		for (newDonation <- lines.map(Donation(_)).filter(_.isValid)) {
			// Register all valid donations. This keeps track of donor/zip and years they donated, along with their contribution amount
			tracker.register(newDonation)
			
			// If this is a repeat donor, we create a new recipient object to write to file
			if (tracker.isFromRepeatDonor(newDonation)) {
				val newRecipient = tracker.getRecipient(newDonation)
				writeLine(newRecipient.toString)
			}
		}
	}
}
