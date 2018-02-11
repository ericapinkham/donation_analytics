object Main extends App {
	// Process the arguments and create the fileIO object which handles reading and writing to files
	val fileIO = args match {
		case Array(itcontFile, percentileFile, outputFile) => new FileIO(itcontFile, percentileFile, outputFile)
		case Array(itcontFile, percentileFile) => new FileIO(itcontFile, percentileFile)
		case _ => throw new Error(s"Invalid parameters: ${args.mkString(" ")}")
	}
	
	// This keeps track of everything important
	val tracker = new Tracker(fileIO.percentileValue)
	
	// Iterate through each donation and process donations one at a time
	for (newDonation <- fileIO.readLines().map(Parser.parseLine)) {
		// Check if the donation is valid
		if (newDonation.isValid) {
			// Register all valid donations. This keeps track of donor/zip and years they donated, along with how their contribution
			tracker.register(newDonation)
			
			// If this is a repeat donor, we create a new recipient object to write to file
			if (newDonation.isFromRepeatDonor) {
				val newRecipient = tracker.processRecipient(newDonation)
				fileIO.writeLine(newRecipient.toString)
			}
		}
	}
	
	// Close the file
	fileIO.closeOutputFile()
}
