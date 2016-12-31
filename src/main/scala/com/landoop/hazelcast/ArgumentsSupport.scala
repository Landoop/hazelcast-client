package com.landoop.hazelcast

case class ApplicationArguments(host: String,
                                user: String,
                                password: String,
                                query: String)

trait ArgumentsSupport {

  val usage =
    """
      | Hazelcast-Client: Run with arguments:
      |
      |  -host  hostname:port
      |  -user  hazelcast username
      |  -pass  hazelcast password
      |  -query hazelcast query
    """.stripMargin

  def parseArgumentsOrFail(args: Array[String]): ApplicationArguments = {
    if (args.length < 1) {
      println(usage)
      sys.exit(1)
    }
    val arglist = args.toList
    type OptionMap = Map[Symbol, Any]

    def nextOption(map: OptionMap, list: List[String]): OptionMap =
      list match {
        case Nil => map
        // PARAMETERS
        case "-host" :: value :: tail =>
          nextOption(map ++ Map('host -> value.toString), tail)
        case "-user" :: value :: tail =>
          nextOption(map ++ Map('user -> value.toString), tail)
        case "-pass" :: value :: tail =>
          nextOption(map ++ Map('pass -> value.toString), tail)
        case "-query" :: value :: tail =>
          nextOption(map ++ Map('query -> value.toString), tail)
        case option :: tail => println("Unknown option " + option)
          nextOption(map, tail)
        // sys.exit(1)
      }

    val options: Map[Symbol, Any] = nextOption(Map(), arglist)
    validateArguments(options)


    val arn = if (options.contains('arn)) options('arn).toString else ""
    val arnKey = if (options.contains('arnSessionName)) options('arnSessionName).toString else ""

    val applicationArgs = ApplicationArguments(
      host = options('host).toString,
      user = options('user).toString,
      password = options('pass).toString,
      query = options('query).toString)
    println(applicationArgs)
    applicationArgs

  }

  def validateArguments(options: Map[Symbol, Any]) = {
    val requiredConfigs = List('host, 'user, 'pass, 'query)

    requiredConfigs.foreach { symbol =>
      if (!options.contains(symbol)) {
        println(s"You need to supply the argument : $symbol")
        println(usage)
        sys.exit(1)
      }
    }
  }

}
