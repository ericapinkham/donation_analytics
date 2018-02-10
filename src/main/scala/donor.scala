object donor{
	def apply(zip5: String, name: String, year: Int, transactions: Int = 1): donor = new donor(zip5, name, year, transactions)
}

case class donor(zip5: String, name: String, year: Int, transactions: Int = 1) extends donationObject  {
	override val id: String = s"$name|$zip5|$year"
}
