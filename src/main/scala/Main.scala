object Main extends App with Processor {
	// Process the arguments and create the fileIO object which handles reading and writing to files
	val fileIO = args match {
		case Array(itcontFile, percentileFile, outputFile) => new FileIO(itcontFile, percentileFile, outputFile)
		case Array(itcontFile, percentileFile) => new FileIO(itcontFile, percentileFile)
		case _ => throw new Error(s"Invalid parameters: ${args.mkString(" ")}")
	}
	
	// This keeps track of everything important
	val tracker = new Tracker(fileIO.percentileValue)
	
	// Execute
	processDonations()
}
