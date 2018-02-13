import FEC.{Donation, Processor, Recipient, Tracker}
import org.scalatest.FunSuite

class TestSuite extends FunSuite {
	
	trait TestOutput extends Processor {
		var output: String = ""
		val tracker = new Tracker(50)
		override def writeLine(line: String): Unit = {output += line + "\n"}
		val dataSet: String = ""
		private lazy val xmlNode = xml.XML.load("./src/test/resources/testing_data.xml")
		def inputData: String = (xmlNode \\ dataSet \ "Input" text).trim
		def outputData: String = (xmlNode \\ dataSet \ "Output" text).trim
		lazy val lines: Iterator[String] = inputData.split("\n").map(_.trim()).toIterator
	}
	
	test("Data extraction 01") {
		new TestOutput {
			val donation = Donation("C00051979|N|M2|P|201702069044298762|15|IND|ARGO, DANA C|HOOKSETT|NH|03106|COLUMBIA GAS OF MASSACHUSETTS|MGR OPERATIONS CENTER|01312017|218||PR263168521136|1147789||P/R DEDUCTION ($109.45 BI-WEEKLY)|4020820171370030914")
			val recipient: Recipient = tracker.getRecipient(donation)
			assert(donation.year === 2017)
			assert(donation.cmte_id === "C00051979")
			assert(donation.amount === 218)
			assert(donation.zip === "03106")
			assert(donation.name === "ARGO, DANA C")
			assert(donation.isValid === true)
			assert(recipient.amount === donation.amount)
			assert(recipient.cmte_id === donation.cmte_id)
			assert(recipient.year === donation.year)
		}
	}
	
	test("Data extraction 02") {
		new TestOutput {
			val donation = Donation("C00473249|N|M2|P|201702019042407767|15|IND|RICH, LOU|SCOTTSDALE|AZ|85259||RETIRED|01202017|575||SA11AI.5181|1147031|||4020820171370027428")
			val recipient: Recipient = tracker.getRecipient(donation)
			assert(donation.year === 2017)
			assert(donation.cmte_id === "C00473249")
			assert(donation.amount === 575)
			assert(donation.zip === "85259")
			assert(donation.name === "RICH, LOU")
			assert(donation.isValid === true)
			assert(recipient.amount === donation.amount)
			assert(recipient.cmte_id === donation.cmte_id)
			assert(recipient.year === donation.year)
		}
	}
	
	test("Data extraction 03") {
		new TestOutput {
			val donation = Donation("C00455733|A|M2|P|201702029042408821|15|IND|REILLY, EDWARD|HUNTINGTON BEACH|CA|92646|FLAGSTAR BANK|PROFESSIONAL - SALES MANAGER|01302017|234||SA11AI.28935|1147202|||4020820171370029022")
			val recipient: Recipient = tracker.getRecipient(donation)
			assert(donation.year === 2017)
			assert(donation.cmte_id === "C00455733")
			assert(donation.amount === 234)
			assert(donation.zip === "92646")
			assert(donation.name === "REILLY, EDWARD")
			assert(donation.isValid === true)
			assert(recipient.amount === donation.amount)
			assert(recipient.cmte_id === donation.cmte_id)
			assert(recipient.year === donation.year)
			
		}
	}
	
	test("Data extraction 04: other id non-empty") {
		new TestOutput {
			val donation = Donation("C00193433|N|M2|P|201702149049351986|15|IND|DAVIS, SHIRLEY ROSS MS.|SAN FRANCISCO|CA|94123|NOT-EMPLOYED|NOT-EMPLOYED|01062017|50|FOO|4927522|1148908|||4021420171370794302")
			assert(donation.year === 2017)
			assert(donation.cmte_id === "C00193433")
			assert(donation.amount === 50)
			assert(donation.zip === "94123")
			assert(donation.name === "DAVIS, SHIRLEY ROSS MS.")
			assert(donation.isValid === false)
		}
	}
	
	test("Data extraction 05: Empty Name") {
		new TestOutput {
			val donation = Donation("C00193433|N|M2|P|201702149049351986|15|IND||SAN FRANCISCO|CA|94123|NOT-EMPLOYED|NOT-EMPLOYED|01062017|50||4927522|1148908|||4021420171370794302")
			assert(donation.year === 2017)
			assert(donation.cmte_id === "C00193433")
			assert(donation.amount === 50)
			assert(donation.zip === "94123")
			assert(donation.name === "")
			assert(donation.isValid === false)
		}
	}
	
	test("Data extraction 06: [0-9]{9} zip") {
		new TestOutput {
			val donation = Donation("C00193433|N|M2|P|201702149049352092|15|IND|MCLEOD, MIA MS.|LOS ALAMOS|NM|875471234|NOT-EMPLOYED|RETIRED|01162017|500||4934845|1148908|||4021420171370794621")
			val recipient: Recipient = tracker.getRecipient(donation)
			assert(donation.year === 2017)
			assert(donation.cmte_id === "C00193433")
			assert(donation.amount === 500)
			assert(donation.zip === "87547")
			assert(donation.name === "MCLEOD, MIA MS.")
			assert(donation.isValid === true)
			assert(recipient.amount === donation.amount)
			assert(recipient.cmte_id === donation.cmte_id)
			assert(recipient.year === donation.year)
		}
	}
	
	test("Data extraction 06: Invalid committee id") {
		new TestOutput {
			val donation = Donation("INSIGHTPLEASEACCEPTME|N|M2|P|201702149049352145|15|IND|TOKUGAWA, DIANE A. MS.|BERKELEY|CA|94709|TPMF|PATHOLOGIST|01182017|300||4941522|1148908|||4021420171370794780")
			assert(donation.year === 2017)
			assert(donation.cmte_id === "INSIGHTPLEASEACCEPTME")
			assert(donation.amount === 300)
			assert(donation.zip === "94709")
			assert(donation.name === "TOKUGAWA, DIANE A. MS.")
			assert(donation.isValid === false)
		}
	}
	
	test("Recipient aggregation 01") {
		new TestOutput {
			val donation1 = Donation("C00193433|N|M2|P|201702149049352092|15|IND|MR FAKE|LOS ALAMOS|NM|875471234|NOT-EMPLOYED|RETIRED|01162017|500||4934845|1148908|||4021420171370794621")
			val recipient1: Recipient = tracker.getRecipient(donation1)
			val donation2 = Donation("C00193433|N|M2|P|201702149049352092|15|IND|MR FAKE|LOS ALAMOS|NM|875471234|NOT-EMPLOYED|RETIRED|01162017|1000||4934845|1148908|||4021420171370794621")
			val recipient2: Recipient = tracker.getRecipient(donation2) // This is now aggregated with recipient one.
			assert(recipient2.year === 2017)
			assert(recipient2.cmte_id === recipient2.cmte_id)
			assert(recipient2.transactions === 2)
			assert(recipient2.zip5 === recipient1.zip5)
			assert(recipient2.amount === 1500)
		}
	}
	
	trait OutOfOrder extends TestOutput {
		override val dataSet: String = "OutOfOrder"
	}
	test("Out of order donations") {
		new OutOfOrder {
			processDonations(lines)
			assert(this.output.stripLineEnd === outputData)
		}
	}
	
	trait DuplicateDonations extends TestOutput {
		override val dataSet: String = "DuplicateDonations"
	}
	test("Duplicate donations") {
		new DuplicateDonations {
			processDonations(lines)
			assert(this.output.stripLineEnd  === outputData)
		}
	}
	
	trait VerySimilarDonations extends TestOutput {
		override val dataSet: String = "VerySimilarDonations"
	}
	test("Similar donations, same person, same year") {
		new DuplicateDonations {
			processDonations(lines)
			assert(this.output.stripLineEnd  === outputData)
		}
	}
	
	trait YearlyDonations extends TestOutput {
		override val dataSet: String = "YearlyDonations"
	}
	test("Joe Donates Yearly") {
		new YearlyDonations {
			processDonations(lines)
			assert(this.output.stripLineEnd === outputData)
			assert(tracker.percentileValue === 484)
		}
	}
	
	trait JoeIsDoingBetterFinanciallyIn2017 extends TestOutput {
		override val dataSet: String = "JoeIsDoingBetterFinanciallyIn2017"
	}
	test("Joe Is Doing Better Financially In 2017") {
		new JoeIsDoingBetterFinanciallyIn2017 {
			processDonations(lines)
			assert(tracker.percentileValue === 1000)
		}
	}
	
	test("Percentile Computation") {
		val l = Trees.RedBlackTree.Tree(List(15, 20, 35, 40, 50))
		assert(Tracker.computePercentile(5)(l) === 15)
		assert(Tracker.computePercentile(30)(l) === 20)
		assert(Tracker.computePercentile(40)(l) === 20)
		assert(Tracker.computePercentile(50)(l) === 35)
		assert(Tracker.computePercentile(100)(l) === 50)
	}
}
