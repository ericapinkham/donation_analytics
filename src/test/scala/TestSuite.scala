import org.scalatest.FunSuite

import scala.io.Source

class TestSuite extends FunSuite {

	trait Main {
		// Process the arguments and create the fileIO object which handles reading and writing to files
		val fileIO = new FileIO("./insight_testsuite/tests/test_1/input/itcont.txt", "./insight_testsuite/tests/test_1/input/percentile.txt")
		// This keeps track of everything important
		val tracker = new Tracker(fileIO.percentileValue)
		val parser = new Parser(tracker)
		val donations = fileIO.readLines().map(parser.parseLine).toList
	}
	
	test("cmte_id 01") {
		new Main {
			assert(donations(0).cmte_id === "C00629618")
		}
		
	}

	test("name 01") {
		new Main {
			assert(donations(1).name === "DEEHAN, WILLIAM N")
		}
	}
	
	test("donation.toString") {
		new Main {
			assert(donations(0).toString === "donation:C00629618|PEREZ, JOHN A|90017|2017|40.0|H6CA34245")
		}
	}
	
	test("isValid 01: Invalid test case") {
		new Main {
			assert(donations(0).isValid === false)
		}
	}
	
	test("isValid 02: Valid test case") {
		new Main {
			assert(donations(1).isValid === true)
		}
	}
	
//	test("tracker 01: year of new donation object") {
//		new TestingApp {
//			tracker.register(donations(2))
//			val trackerYear = tracker.donors(donations(2).donorId)
//			assert(trackerYear.contains(donations(2).year))
//		}
//	}
}
