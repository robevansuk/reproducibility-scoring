import Analyser.getLexicalDiversity
import play.api.libs.json.Json
import scalaj.http.Http

import scala.io.Source.fromFile

object ArticleAnalysis {

  val domain = "all"
  val client = "reproducibilityHack"
  val queryParams = s"domain=${domain}&client=${client}"
  val hubapiarticles = s"http://hub-api.live.cf.private.springer.com/api/v1/articles?page=1&pageSize=10&${queryParams}"

  def getArticleData(jsonPathItem: String): List[String] = {
    val request = Http(hubapiarticles)
    val result = request.execute()
    val jsonResponse = Json.parse(result.body.toString)
    val articleElements = jsonResponse \ "articles" \\ jsonPathItem
    articleElements.map(_.toString().replace("\"", "")).toList
  }

//  def getArticleContent(location: String) : String = {
//    val request = Http(s"${location}")
//    val response = request.execute().body
//    if (response!="" && response.toString().indexOf("name headline>")>0){
//      val result = response.toString().substring(response.toString().indexOf("name headline>"))
//      println(result.toString())
//      result.toString()
//    } else { "" }
//
//  }

  def getArticleContentFromFile(location: String) = {
    val lines = fromFile(location).mkString
    lines
  }

  def articleAnalysis() = {

    val basePath = "/Users/ren7881/dev/reproducibility-socring/src/main/resources/"
    val titleList = List("Nucleus Accumbens, a new sleep-regulating area through the integration of motivational stimul",
      "Vitamin D: missing link between hypertension and muscle mass",
      "Quicker, easier posterior restorations"
    )
    val contentFileList = List( "content_001.txt", "content_002.txt", "content_003.txt")
    val citationFileList = List("citations_001.txt", "citations_002.txt", "content_003.txt")

    var i = 0
    for (location <- contentFileList) {
      val content = getArticleContentFromFile(basePath + location)

      val lexicalDiversityScore = getLexicalDiversity(content)
      println (s"$lexicalDiversityScore -> ${titleList(i)}")
      i = i + 1
    }

  }

}
