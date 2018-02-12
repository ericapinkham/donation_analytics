object Main extends App with Processor {
	override def writeLine(line: String): Unit = fileIO.writeLine(line)
	
	// Process the arguments and create the fileIO object which handles reading and writing to files
	val fileIO = args match {
		case Array(itcontFile, percentileFile, outputFile) => new FileIO(itcontFile, percentileFile, outputFile)
		case Array(itcontFile, percentileFile) => new FileIO(itcontFile, percentileFile)
		case _ => throw new Error(s"Invalid parameters: ${args.mkString(" ")}")
	}
	
	// Read the lines from the file
	val lines = fileIO.readLines()
	
	// This keeps track of everything important
	val tracker = new Tracker(fileIO.percentileValue)
	
	// Execute
	processDonations(lines)
	
	// Close the out put file
	fileIO.closeOutputFile()
}
