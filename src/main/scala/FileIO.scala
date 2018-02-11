import java.io.{BufferedOutputStream, BufferedWriter, File, FileWriter}

import scala.io.Source

class FileIO(workingDirectory: String) {
	
	private def joinPath(path1: String, path2: String): String = new File(path1, path2).toString
	
	def readLines(): Iterator[String] = Source.fromFile(joinPath(workingDirectory, "input/itcont.txt")).getLines()
	
	val outputFile: java.io.File = new File(joinPath(workingDirectory, "output/repeat_donors.txt"))
	val outputWriter: java.io.BufferedWriter = new BufferedWriter(new FileWriter(outputFile))
	
	def writeLine(line: String): Unit = outputWriter.write(line)
	
	def flushOutput(): Unit = outputWriter.flush()
	
	def closeOutputFile(): Unit = {
		outputWriter.flush()
		outputWriter.close()
	}
	
}
