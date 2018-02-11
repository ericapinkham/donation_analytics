import org.scalatest.FunSuite

import scala.io.Source

class TestSuite extends FunSuite {
	val lines = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/itcont.txt").getLines().toList
	val donations = lines.map(parser.parseLine(_))
	
	test("cmte_id 01") {
		assert(donations(0).cmte_id === "C00629618")
	}

	test("name 01") {
		assert(donations(1).name === "DEEHAN, WILLIAM N")
	}
	
	test("donation.toString") {
		assert(donations(0).toString === "donation:C00629618|PEREZ, JOHN A|90017|2017|40.0|H6CA34245")
	}
	
	test("isValid 01: Invalid test case") {
		assert(donations(0).isValid === false)
	}
	
	test("isValid 02: Valid test case") {
		assert(donations(1).isValid === true)
	}
	
	test("tracker 01: year of new donation object") {
		tracker.register(donations(2))
		val trackerYear = tracker.donors(donations(2).donorId)
		assert(trackerYear.contains(donations(2).year))
	}
}
