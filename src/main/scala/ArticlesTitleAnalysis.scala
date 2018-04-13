import Analyser.getLexicalDiversity
import play.api.libs.json.Json
import scalaj.http.Http

object ArticlesTitleAnalysis {

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
      println (s"$lexicalDiversityScore -> ${articleTitle.toString().replace("\"","")}")
    }

    articleTitles
  }

}
