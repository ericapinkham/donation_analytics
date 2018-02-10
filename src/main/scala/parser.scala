object parser {
	
	private val patternYear = """\d{4}(\d{4})$""" r
	private def extractYear(transactionDate: String): Int = {
		patternYear.findFirstIn(transactionDate).getOrElse("") match {
			case patternYear(year) => year.toInt
			case patternYear(_*) => throw new Error(s"Regex matching error: $patternYear, $transactionDate")
		}
	}
	
	private val patternZip = """(\d{5})\d{0,4}$""" r
	def extractZip(zip: String): String = {
		patternZip.findFirstIn(zip).getOrElse("") match {
			case patternZip(zip5) => zip5
			case patternZip(_*) => throw new Error(s"Regex matching error: $patternZip, $zip")
		}
	}
	
	def parseLine(l: String): (recipient, donor) = {
		l.split('|') match {
			case Array(cmte_id, amndt_ind, rpt_tp, transaction_pgi, image_num, transaction_tp, entity_tp, name, city, state, zip_code, employer, occupation, transaction_dt, transaction_amt, other_id, tran_id, file_num, memo_cd, memo_text, sub_id)
				=> (recipient(cmte_id, extractZip(zip_code), extractYear(transaction_dt), transaction_amt.toInt), donor(zip_code, name, extractYear(transaction_dt)))
			case _ => throw new Error(s"$l doesn't unpack properly")
		}


	}
}
