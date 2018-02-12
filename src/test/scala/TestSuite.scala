import org.scalatest.FunSuite

import scala.io.Source

class TestSuite extends FunSuite {

	trait test1 {
		// Process the arguments and create the fileIO object which handles reading and writing to files
		val fileIO = new FileIO("./insight_testsuite/tests/test_1/input/itcont.txt", "./insight_testsuite/tests/test_1/input/percentile.txt")
		// This keeps track of everything important
		val tracker = new Tracker(fileIO.percentileValue)
		val donations: List[Donation] = fileIO.readLines().map(Donation(_)).toList
	}
	
	trait OutOfOrder {
		val lines = """C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312016|484||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
		C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312015|384||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339
		C00384516|N|M2|P|201702039042410893|15|IND|SABOURIN, JOE|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312017|230||PR1890575345050|1147350||P/R DEDUCTION ($115.00 BI-WEEKLY)|4020820171370029335""".split("\n")
		
		val tracker = new Tracker(50)
		
		
		
		
	}
	
	test("cmte_id 01") {
		new test1 {
			assert(donations(0).cmte_id === "C00629618")
		}
		
	}

	test("name 01") {
		new test1 {
			assert(donations(1).name === "DEEHAN, WILLIAM N")
		}
	}
	
	test("donation.toString") {
		new test1 {
			assert(donations(0).toString === "donation:C00629618|PEREZ, JOHN A|90017|2017|40.0|H6CA34245")
		}
	}
	
	test("isValid 01: Invalid test case") {
		new test1 {
			assert(donations(0).isValid === false)
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
}
