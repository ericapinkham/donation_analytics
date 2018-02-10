import scala.collection.mutable
import scala.io.Source

class processor extends App {
	val lines = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/itcont.txt").getLines()
	
	val recipientMap = scala.collection.mutable.HashMap.empty[String, recipient]
	val donorMap = scala.collection.mutable.HashMap.empty[String, donor]
	
	def processNew[T <: donationObject](map: mutable.HashMap[String, T])(newObject: T): Unit = {
		if (map.isDefinedAt(newObject.id))
			map(newObject.id) += newObject
		else
			map(newObject.id) = newObject
	}
	def processNewRecipient(newRecipient: recipient): Unit = processNew(recipientMap)(newRecipient)
	
	def processNewDonor(newDonor: donor): Unit = processNew(donorMap)(newDonor)
	
	
	def process(): Unit = {

		for (line <- lines) {
			val (newRecipient, newDonor) = parser.parseLine(line)
			
			processNewDonor(newDonor)
			
			processNewRecipient(newRecipient)
			
			
		}
		
		
		
	}
	

}
