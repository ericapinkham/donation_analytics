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
	}
	
	trait OutOfOrder extends TestOutput {
		val lines: Iterator[String] = """C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
		C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312015|384||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
		C00384516|N|M2|P|201702039042410893|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312017|230||PR1890575345050|1147350||P/R DEDUCTION ($115.00 BI-WEEKLY)|4020820171370029335""".split("\n").map(_.trim()).toIterator
	}
	
	trait DuplicateDonations extends TestOutput {
		val lines: Iterator[String] = """C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339""".split("\n").map(_.trim()).toIterator
	}
	
	trait VerySimilarDonations extends TestOutput {
		val lines: Iterator[String] = """C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|03312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|06302016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|12312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339""".split("\n").map(_.trim()).toIterator
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
	
	test("Out of order donations") {
		new OutOfOrder {
			processDonations(lines)
			assert(this.output === "C00384516|02895|2017|230|230|1\n")
		}
	}
	
	test("Duplicate donations") {
		new DuplicateDonations {
			processDonations(lines)
			assert(this.output === "")
		}
	}
	
	test("Similar donations, same person, same year") {
		new DuplicateDonations {
			processDonations(lines)
			assert(this.output === "")
		}
	}
	
	trait YearlyDonations extends TestOutput {
		val lines: Iterator[String] = """C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01012014|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01012015|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01012016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01012017|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339""".split("\n").map(_.trim()).toIterator
	}
	test("Joe Donates Yearly") {
		new YearlyDonations {
			processDonations(lines)
			assert(this.output === "C00384516|02895|2015|484|484|1\nC00384516|02895|2016|484|484|1\nC00384516|02895|2017|484|484|1\n")
			assert(tracker.percentileValue === 484)
		}
	}
	
	trait JoeIsDoingBetterFinanciallyIn2017 extends TestOutput {
		val lines: Iterator[String] = """C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01012016|5||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01012017|500||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|04012017|1000||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
				 C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|07012017|1500||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339""".split("\n").map(_.trim()).toIterator
	}
	test("Joe Is Doing Better Financially In 2017") {
		new JoeIsDoingBetterFinanciallyIn2017 {
			processDonations(lines)
			assert(this.output === "C00384516|02895|2017|500|500|1\nC00384516|02895|2017|500|1500|2\nC00384516|02895|2017|1000|3000|3\n")
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
