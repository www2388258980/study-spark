package com.yj.study.spark.sql

object Test extends LoggerTrait {
  def main(args: Array[String]): Unit = {

    //    val spark = SparkSession
    //      .builder()
    //      .appName("Spark SQL basic example")
    //      .config("spark.testing.memory", "471859200")
    //      .master("local[*]")
    //      .getOrCreate()

    val spark = SparkSessionUtils.getLocalSparkSession()
    val df = spark.read.json(this.getClass.getResource("/people.json").getPath)
    df.show()

    import spark.implicits._
    df.printSchema()
    df.select("name").show()
    df.select($"name", $"age" + 1).show()
    df.filter($"age" > 21).show()
    df.groupBy("age").count().show()

    df.createOrReplaceTempView("people")
    val sqlr = spark.sql("select * from people")
    sqlr.show()

    df.createGlobalTempView("people")
    spark.sql("SELECT * FROM global_temp.people").show()
    spark.newSession().sql("select * from global_temp.people").show()
  }
}
