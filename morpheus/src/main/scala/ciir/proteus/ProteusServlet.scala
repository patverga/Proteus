package ciir.proteus

import ciir.proteus.ProteusFunctions._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.util.{Duration,Future}
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit
import org.apache.thrift.protocol._
import org.scalatra._
import scalate.ScalateSupport
import scala.collection.JavaConversions._
import scala.collection.mutable.MapBuilder
import scala.collection.mutable.Set
import java.util.ArrayList
import org.slf4j.{Logger, LoggerFactory}

// For json response handling (to AJAX requests)
import net.liftweb.json.compact
import net.liftweb.json.render
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Serialization.{read, write}
import net.liftweb.json.Serialization
import net.liftweb.json.NoTypeHints
import org.scalatra.Ok
import org.scalatra.NotFound
import org.scalatra.servlet.RichSession

// For monogodb access
import com.mongodb.casbah.Imports._

// For configuration
case class Site(host: String, port: Int)

object ProteusServlet {
  var hosts : Seq[Site] = Seq[Site]()
  
  // Connect to MongoDB, select the proteus database
  val mongoConn = MongoConnection()
  val mongoColl = mongoConn("proteus")
}

class ProteusServlet extends ScalatraServlet 
with ScalateSupport 
with FakeDataGenerator {
import ProteusServlet._

  // implicit value for json serialization format
  implicit val formats = Serialization.formats(NoTypeHints);
  val logger = LoggerFactory.getLogger(this.getClass)
  val kNumSearchResults = 10

  // Load the first server parameters
  // TODO: extend these to work from multiple sites
  val auraServer = ProteusServlet.hosts.first
  val dataService = ClientBuilder()
  .hosts(new InetSocketAddress(auraServer.host, auraServer.port))
  .codec(ThriftClientFramedCodec())
  .hostConnectionLimit(1)
  .tcpConnectTimeout(Duration(1, TimeUnit.SECONDS))
  .retries(2)
  .build()
  val dataClient = new ProteusProvider.FinagledClient(dataService)
  logger.info("Started listening to aura server " + auraServer.host
	      + " on port " + auraServer.port.toString)


  def renderHTML(template: String, originalArgs: (String, Any)*) = {
    contentType = "text/html"
    val args = session.contains("items") match {
      case true => ("items" -> session("items")) +: originalArgs
      case false => originalArgs
    }
    scaml(template, args:_*)
  }

  get("/") { renderHTML("index") }
  get("/index") { renderHTML("index") }
  get("/about") { renderHTML("about") }
  get("/contact") { renderHTML("contact") }
  get("/details") {
    val aid = ProteusFunctions.externalId(params("id"))
    val request = LookupRequest(List(aid))
    val futureResponse = dataClient.lookup(request)
    val response = futureResponse()
    val obj = response.objects.head
    renderHTML("details", "pObject" -> obj)
  }

  get("/status") {
    val response = dataClient.status()()
    renderHTML("status", 
	       "siteId" -> response.siteId,
	       "collectionData" -> response.collectionData,
	       "linkData" -> response.linkData,
	       "topicData" -> response.topicData)
  }

  post("/related") {
    val beliefs = multiParams("chosenResult").map {
      scoreElement => {
	val aid = externalId(scoreElement)
	SearchResult(id = aid, score = (1.0).toDouble)
      }
    }
    val targetTypes = if (multiParams("targetType").contains("all")) {
      kReturnableTypes.map { rt: String => ProteusType.valueOf(rt).get }
    } else {
      multiParams("targetType").map {
	tElem =>
	  ProteusType.valueOf(tElem)
      }.filter(_.isDefined).map(_.get)
    }
    val rrequest = RelatedRequest(beliefs = beliefs,
				  targetTypes = targetTypes)
    val response = dataClient.related(rrequest)()
    renderHTML("search", "results" -> splitResults(response.results))
  }
  
  // Create user
  put("/users/:username") {
    val newUser = MongoDBObject("user" -> params("username"))
    mongoColl("users").findOne(newUser) match {
      case Some(foundUser) => {
	Conflict(write(String.format("User '%s' already exists.", params("username"))))
      }
      case None => {
	mongoColl("users") += newUser
	logger.info("Creating user '{}'", newUser("user"))
	Created()
      }    
    }
  }

  // Get the user information given a session key
  get("/users/:sessionid") {
    contentType = "application/json"
    mongoColl("sessions").findOne(MongoDBObject("key" -> params("sessionid"))) match {
      case Some(session) => {
	mongoColl("users").findOne(MongoDBObject("user" -> session("user"))) match {
	  case Some(userdata) => {
	    println("Userdata: " + userdata.toString)
	    Ok(userdata)
	  }
	  case None => NotFound(write("No user found for session."))
	}
      }
      case None => NotFound(write("No session found."))
    }
  }

  // Delete the user with the given session key (i.e. must be logged in to delete.)
  delete("/users/:sessionid") {
    contentType = "application/json"
    mongoColl("sessions").findOne(MongoDBObject("key" -> params("sessionid"))) match {
      case Some(session) => {
	val username = session("user").asInstanceOf[String]
	mongoColl("sessions") -= session  // end session
	mongoColl("users") -= MongoDBObject("user" -> username) // remove user
	Ok()
      }
      case None => {
	Conflict(write("Unable to delete user. No session found."))
      }
    }
  }

  // Log in the user (establish a 'session' key).
  put("/sessions/:username") {
    val username = params("username")
    contentType = "application/json"
    mongoColl("users").findOne(MongoDBObject("user" -> username)) match {
      case Some(foundUser) => {
	logger.info("Logging in user '{}'", username)
	// Currently a hack to use the object id as the session key.
	// I from a security standpoint this is wildly irresponsible
	// but hey, nothing too important or private here yet.
	val initialRecord = MongoDBObject("user" -> username)
	mongoColl("sessions") += initialRecord
	mongoColl("sessions").findOne(initialRecord) match {
	  case Some(found) => {
	    val key = found("_id").toString
	    found += "key" -> key
	    mongoColl("sessions").update(initialRecord, found)
	    Created(write(key))
	  }
	  case None => {
	    InternalServerError(write("Unable to create session."))
	  }
	}
      }
      case None => {
	Conflict(write("User not found."))
      }
    }
  }

  // End a session
  delete("/sessions/:key") {
    mongoColl("sessions").findOne(MongoDBObject("key" -> params("key"))) match {
      case Some(session) => {
	mongoColl("sessions") -= session
	Ok()
      }
      case None => {
	NotFound(write("Key is invalid."))
      }
    }
  }

  get("/addItemToSession/:id") {
    val id = params("id")
    logger.info("Adding item {} to session", id)
    if (!session.contains("items")) {
      session("items") = Set[String]()
    }
    session("items").asInstanceOf[Set[String]].add(id)
  }

  get("/removeItemFromSession/:id") {
    val id = params("id")
    logger.info("Removing item {} from session", id)
    if (session.contains("items")) {
      session("items").asInstanceOf[Set[String]].remove(id)
    }
  }

  get("/transform") {
    val transformType = TransformType(params("tv").toInt)
    val srcAid = externalId(params("did"))
    val targetType = ProteusType.valueOf(params("t"))
    val trequest = TransformRequest(transformType = transformType,
				    referenceId = srcAid,
				    targetType = targetType)
    val response = dataClient.transform(trequest)()
    val objects = response.objects.toList
    renderHTML("viewobjects", "pObjects" -> objects)
  }

  get("/lookup") {
    val accessIds = multiParams("id") map { 
      pid => 
	ProteusFunctions.externalId(pid)
    }
    val request = LookupRequest(accessIds)
    val response = dataClient.lookup(request)()
    // Need to split the results by type
    var splitResults = Map[String, AnyRef]()
    for (typeStr : String <- kReturnableTypes) {
      val filteredByType = response.objects.filter { 
	obj : ProteusObject => 
	  obj.id.`type` == ProteusType.valueOf(typeStr).get
      }
      // If we found any results of that type in the filter,
      // then add it as a typed result list.
      if (filteredByType.length > 0) {
	splitResults += (typeStr -> filteredByType)
      }
    }
    renderHTML("lookup", "result" -> splitResults)
  }

  get("/search") {
    var actuals = Seq[(String, Any)]() 
    
    // If we have a query, put together a SearchRequest and ship it
    if (params.contains("q")) {
      // Request this many
      var count = kNumSearchResults
      if (params.contains("n")) {
        count = params("n").toInt
      }
      
      val requestedTypes = if (multiParams("st").contains("all")) {
	kReturnableTypes.map { rt : String => ProteusType.valueOf(rt).get }
      } else {
	multiParams("st") map { 
	  str => 
	    ProteusType.valueOf(str).get
	}
      }
      
      var rgq : Option[String] = None
      if (params.contains("rgq")) {
        rgq = Some(params("rgq"))
      }
      val parameters = RequestParameters(count, 0)
      val request = SearchRequest(rawQuery = params("q"), 
				  types = requestedTypes, 
				  parameters = Some(parameters),
				  rawGalagoQuery = rgq)

      val futureResponse = dataClient.search(request)
      val actualResponse = futureResponse()
      // Need to split the results by type
      actuals = ("results" -> splitResults(actualResponse.results)) +: actuals
      actuals = ("q" -> params("q")) +: actuals
    }
    renderHTML("search", actuals:_*)
  }

  get("/graphview") {
    renderHTML("graphview")
  }

  get("/wordhistory") {
    var actuals = Seq[(String, Any)]()
    if (params.contains("w")) {
      val words = params("w").split(" ").toList
      val response = dataClient.wordFrequencies(words)()
      actuals = ("frequencies" -> response.toMap) +: actuals
      actuals = ("w" -> params("w")) +: actuals
    }
    renderHTML("wordhistory", actuals:_*)
  }

  def splitResults(results: Seq[SearchResult]) : Map[String, AnyRef] = {
    val splitBuilder = Map.newBuilder[String, AnyRef]
    for (typeStr : String <- kReturnableTypes) {
      val filteredByType = results.filter { 
	result => 
	  result.id.`type` == ProteusType.valueOf(typeStr).get
      }
      // If we found any results of that type in the filter,
      // then add it as a typed result list.
      if (filteredByType.length > 0) {
	splitBuilder += (typeStr -> filteredByType)
      }
    }
    return splitBuilder.result
  }

  notFound {
    serveStaticResource() getOrElse resourceNotFound()
  }
}
