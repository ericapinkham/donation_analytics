object Parser {
	/** Reads a line from itcont.txt and returns a new Donation object
	  *
	  * @param l the line to parse
	  * @return
	  */
	def parseLine(l: String): Donation = {
		l.split('|') match {
			case Array(cmte_id, amndt_ind, rpt_tp, transaction_pgi, image_num, transaction_tp, entity_tp, name, city, state, zip_code, employer, occupation, transaction_dt, transaction_amt, other_id, tran_id, file_num, memo_cd, memo_text, sub_id)
				=> Donation(cmte_id, name, zip_code, transaction_dt, transaction_amt, other_id)
			case _ => throw new Error(s"$l doesn't unpack properly")
		}

	}
}
