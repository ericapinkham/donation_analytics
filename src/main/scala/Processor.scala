import scala.io.Source

object Processor extends App {
	val lines = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/itcont.txt").getLines()
	
	for (newDonation <- lines.map(Parser.parseLine)) {
		if (newDonation.isValid) {
			Tracker.register(newDonation)
			
			if (newDonation.isFromRepeatDonor) {
				val newRecipient = Tracker.processRecipient(newDonation)
				println(newRecipient)
			}
		}
	}
}
