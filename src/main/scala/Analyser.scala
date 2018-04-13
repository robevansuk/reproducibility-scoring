import play.api.libs.json.Json
import scalaj.http.Http


object Analyser {


  def articles() = {
    val domain = "all"
    val client = "reproducibilityHack"

    val hubapiarticles = s"http://hub-api.live.cf.private.springer.com/api/v1/articles?page=1&pageSize=10&domain=${domain}&client=${client}"

    val request = Http(hubapiarticles)
    val result = request.execute()
    val jsonResponse = Json.parse(result.body.toString)
    val articleTitles = jsonResponse \ "articles" \\ "title"

    for (articleTitle <- articleTitles) {
      val articleTitleAsArray = articleTitle.toString().replace("\"","").split(" ").toList
      val lexicalDiversityScore = getLexicalDiversity(articleTitleAsArray)
      println (s"$lexicalDiversityScore -> $articleTitle")
    }

    articleTitles
  }


  def getWords(data : List[String]): Map[String, Int] = {

    def loop(map: Map[String, Int], ls: List[String]): Map[String, Int] = ls match {
      case hd :: tail => {
        val newMap = map + (hd -> (map.get(hd).getOrElse(0)+1))
        loop(newMap, tail)
      }
      case _ => map
    }

    loop(Map.empty, data)
  }

  def lexicalDiversityScore(data: List[String], wordFrequencyMap: Map[String, Int]): Double = {
        data.size / wordFrequencyMap.size
  }

  def getLexicalDiversity(data : List[String]): String = {
     lexicalDiversityScore(data, getWords(data)).toString
  }
}
