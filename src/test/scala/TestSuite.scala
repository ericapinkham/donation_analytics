import FEC.{Donation, FileIO, Processor, Tracker}
import org.scalatest.FunSuite

class TestSuite extends FunSuite {

	trait test1 {
		// Process the arguments and create the fileIO object which handles reading and writing to files
		val fileIO = new FileIO("./insight_testsuite/tests/test_1/input/itcont.txt", "./insight_testsuite/tests/test_1/input/percentile.txt")
		// This keeps track of everything important
		val tracker = new Tracker(fileIO.percentileValue)
		val donations: List[Donation] = fileIO.readLines().map(Donation(_)).toList
	}
	
	trait TestOutput extends Processor {
		var output: String = ""
		override def writeLine(line: String): Unit = {output += line + "\n"}
		val tracker = new Tracker(50)
		val dataSet: String
		val xmlNode = xml.XML.load("./src/test/resources/testing_data.xml")
		def inputData: String = (xmlNode \\ dataSet \ "Input" text).trim
		def outputData: String = (xmlNode \\ dataSet \ "Output" text).trim
		lazy val lines: Iterator[String] = inputData.split("\n").map(_.trim()).toIterator
		
	}
	
	test("cmte_id 01") {
		new test1 {
			assert(donations.head.cmte_id === "C00629618")
		}
		
	}

	test("name 01") {
		new test1 {
			assert(donations(1).name === "DEEHAN, WILLIAM N")
		}
	}
	
	test("donation.toString") {
		new test1 {
			assert(donations.head.toString === "donation:C00629618|PEREZ, JOHN A|90017|2017|40|H6CA34245")
		}
	}
	
	test("isValid 01: Invalid test case") {
		new test1 {
			assert(donations.head.isValid === false)
		}
	}
	
	test("isValid 02: Valid test case") {
		new test1 {
			assert(donations(1).isValid === true)
		}
	}
	
	test("tracker 01: year of new donation object") {
		new test1 {
			tracker.register(donations(2))
			val trackerYear = tracker.donors(donations(2).donorId)
			assert(trackerYear.contains(donations(2).year))
		}
	}
	
	trait OutOfOrder extends TestOutput {
		val dataSet = "OutOfOrder"
	}
	test("Out of order donations") {
		new OutOfOrder {
			processDonations(lines)
			assert(this.output.stripLineEnd === outputData)
		}
	}
	
	trait DuplicateDonations extends TestOutput {
		val dataSet = "DuplicateDonations"
	}
	test("Duplicate donations") {
		new DuplicateDonations {
			processDonations(lines)
			assert(this.output.stripLineEnd  === outputData)
		}
	}
	
	trait VerySimilarDonations extends TestOutput {
		val dataSet = "VerySimilarDonations"
	}
	test("Similar donations, same person, same year") {
		new DuplicateDonations {
			processDonations(lines)
			assert(this.output.stripLineEnd  === outputData)
		}
	}
	
	trait YearlyDonations extends TestOutput {
		val dataSet = "YearlyDonations"
	}
	test("Joe Donates Yearly") {
		new YearlyDonations {
			processDonations(lines)
			assert(this.output.stripLineEnd === outputData)
			assert(tracker.percentileValue === 484)
		}
	}
	
	trait JoeIsDoingBetterFinanciallyIn2017 extends TestOutput {
		val dataSet = "JoeIsDoingBetterFinanciallyIn2017"
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
