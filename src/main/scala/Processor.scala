/**
  * Abstracted methods to be executed by the Main object
  */
trait Processor {
	val fileIO: FileIO
	val tracker: Tracker
	
	/**
	  * The workhorse method. Processes all lines in the file.
	  */
	def processDonations(): Unit = {
		// Iterate through each donation and process donations one at a time
		for (newDonation <- fileIO.readLines().map(Donation(_))) {
			// Check if the donation is valid
			if (newDonation.isValid) {
				// Register all valid donations. This keeps track of donor/zip and years they donated, along with how their contribution
				tracker.register(newDonation)
				
				// If this is a repeat donor, we create a new recipient object to write to file
				if (tracker.isFromRepeatDonor(newDonation)) {
					val newRecipient = tracker.getRecipient(newDonation)
					fileIO.writeLine(newRecipient.toString)
				}
			}
		}
		
		// Close the file
		fileIO.closeOutputFile()
	}
}
