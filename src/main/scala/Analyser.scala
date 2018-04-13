

object Analyser {

  def getLexicalDiversity(data : String): String = {
    val noDashes = data.replace("-", " ")
    val noPunctuation = noDashes.replaceAll("""[^a-zA-Z0-9\s]""", "")
    val lowerCaseData = noPunctuation.toLowerCase
    lexicalDiversityScore(lowerCaseData.split(" ").toList, getWords(lowerCaseData.split(" ").toList)).toString
  }

  def getWords(data : List[String]): Map[String, Int] = {

    def loop(map: Map[String, Int], ls: List[String]): Map[String, Int] = ls match {
      case hd :: tail => {
        val newMap = map + (hd -> (map.get(hd).getOrElse(0)+1))
        loop(newMap, tail)
      }
      case _ => map
    }

    val result = loop(Map.empty, data)
//    println (result)
    result
  }

  def getLexicalDiversity(data : List[String]): String = {
    lexicalDiversityScore(data, getWords(data))
  }

  def lexicalDiversityScore(data: List[String], wordFrequencyMap: Map[String, Int]): String = {
    BigDecimal(wordFrequencyMap.size)./(data.size).setScale(3, BigDecimal.RoundingMode.HALF_UP).toString()
  }

}
