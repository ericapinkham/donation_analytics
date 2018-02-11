import scala.io.Source

object Processor extends App {
	val workingDirectory = "/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_2"
	
	val fileIO = new FileIO(workingDirectory)
	
	val lines = fileIO.readLines()
	
	for (newDonation <- lines.map(Parser.parseLine)) {
		if (newDonation.isValid) {
			Tracker.register(newDonation)
			
			if (newDonation.isFromRepeatDonor) {
				val newRecipient = Tracker.processRecipient(newDonation)
				println(newRecipient)
				fileIO.writeLine(newRecipient.toString)
			}
		}
	}
	
	// Close the file
	fileIO.closeOutputFile()
}
