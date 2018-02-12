object Common {
	/**
	  * Computes percentiles
	  * @param percentile the desired percentile
	  * @param contributions a list of things on which to compute the precentile
	  * @return
	  */
	def computePercentile(percentile: Int)(contributions: List[Int]): Int = {
		contributions.sorted.apply((percentile / 100.0 * contributions.length).ceil.toInt - 1)
	}
}
