import scala.collection.mutable.HashMap
import scala.io.Source

class processor extends App {
	val lines = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/itcont.txt").getLines()
//
//	val recipientMap = HashMap.empty[String, recipient]
//	//val donorMap = HashMap.empty[String, donor]
//
////	def processNew[T <: donationObject](map: HashMap[String, T])(newObject: T): Unit = {
////		if (map.isDefinedAt(newObject.id))
////			map(newObject.id) += newObject
////		else
////			map(newObject.id) = newObject
////	}
////
//	def processNewRecipient(newRecipient: recipient): Unit = processNew(recipientMap)(newRecipient)
//
//	//def processNewDonor(newDonor: donor): Unit = processNew(donorMap)(newDonor)
//
//	def process(): Unit = {
//
//		for (line <- lines) {
//			val newDonation = parser.parseLine(line)
//
//			// if this is a repeat
////			processNewDonor(newDonor)
////			// then do this
////			processNewRecipient(newRecipient)
//
//
//		}

//	}

}
