import org.scalatest.FunSuite

import scala.io.Source

class TestSuite extends FunSuite {
	val lines = Source.fromFile("/home/eric/random_projects/donation_analytics/insight_testsuite/tests/test_1/input/itcont.txt").getLines().toList
	
	test("cmte_id 01") {
		parser.parseLine(lines(0)).cmte_id === "C00629618"
	}
	
	test("name 01") {
		parser.parseLine(lines(1)).day === "DEEHAN, WILLIAM N"
	}
}
