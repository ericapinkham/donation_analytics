import java.io.{BufferedWriter, File, FileWriter}
import scala.io.Source

/**
  * A helper class for reading and writing to disk
  * @param itcontPath the path to the itcont.txt file
  * @param percentilePath the path to the percentile.txt file
  * @param outputPath the path to the desired ouput file
  */
class FileIO(itcontPath: String, percentilePath: String, outputPath: String = "./output/repeat_donors.txt") {
	
	/**
	  * Reads lines from the file specified
	  * @return a list of lines from the file
	  */
	def readLines(): Iterator[String] = Source.fromFile(itcontPath).getLines()
	
	/** Lazy vals for writing the output */
	lazy val outputFile: java.io.File = new File(outputPath)
	lazy val outputWriter: java.io.BufferedWriter = new BufferedWriter(new FileWriter(outputFile))
	
	/** The percentile we're asked to calculate */
	lazy val percentileValue: Int = Source.fromFile(percentilePath).mkString.trim().toInt
	
	/**
	  * Writes the line to the file already set up
	  * @param line the line to write
	  */
	def writeLine(line: String): Unit = outputWriter.write(line + "\n")
	
	/**
	  * Flush the buffered file
	  */
	def flushOutput(): Unit = outputWriter.flush()
	
	/**
	  * Close the file
	  */
	def closeOutputFile(): Unit = {
		outputWriter.flush()
		outputWriter.close()
	}
	
}
