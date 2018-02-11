import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source

class FileIO(itcontPath: String, percentilePath: String, outputPath: String = "./output/repeat_donors.txt") {

	def readLines(): Iterator[String] = Source.fromFile(itcontPath).getLines()
	
	lazy val outputFile: java.io.File = new File(outputPath)
	lazy val outputWriter: java.io.BufferedWriter = new BufferedWriter(new FileWriter(outputFile))
	lazy val percentileValue: Int = Source.fromFile(percentilePath).mkString.trim().toInt
	
	def writeLine(line: String): Unit = outputWriter.write(line + "\n")
	
	def flushOutput(): Unit = outputWriter.flush()
	
	def closeOutputFile(): Unit = {
		outputWriter.flush()
		outputWriter.close()
	}
	
}
