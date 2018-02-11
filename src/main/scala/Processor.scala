import scala.collection.mutable.HashMap
import scala.io.Source

class Processor extends App {
	val lines = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/itcont.txt").getLines()
	
	for (newDonation <- lines.map(parser.parseLine(_))) {
		if (newDonation.isValid & newDonation.isFromRepeatDonor) {
		
		}
	}
//


}
