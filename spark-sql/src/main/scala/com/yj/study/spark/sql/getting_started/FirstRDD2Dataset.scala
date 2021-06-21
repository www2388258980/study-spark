package com.yj.study.spark.sql.getting_started

import com.yj.study.spark.SparkSessionUtils

object FirstRDD2Dataset {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionUtils.getLocalSparkSession()

    // For implicit conversions from RDDs to DataFrames
    import spark.implicits._
    // Create an RDD of Person objects from a text file, convert it to a Dataframe
    val peopleDF = spark.sparkContext
      .textFile(this.getClass.getResource("/people.txt").getPath)
      .map(_.split(","))
      .map(attr => Person(attr(0), attr(1).trim.toLong))
      .toDF()
    peopleDF.show()

    // Register the DataFrame as a temporary view
    peopleDF.createOrReplaceTempView("people")

    // SQL statements can be run by using the sql methods provided by Spark
    val teenagersDF = spark.sql("select name,age from people where age between 13 and 19")

    // The columns of a row in the result can be accessed by field index
    teenagersDF.map(teenager => "name: " + teenager(0)).show()

    // or by field name
    teenagersDF.map(teenager => "Name: " + teenager.getAs[String]("name")).show()

    // No pre-defined encoders for Dataset[Map[K,V]], define explicitly
    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]

    println("-" * 20)
    // row.getValuesMap[T] retrieves multiple columns at once into a Map[String, T]
    val arr = teenagersDF.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect()
    // Array(Map("name" -> "Justin", "age" -> 19))
    arr.foreach(r => {
      println(r.get("name") + ": " + r.get("age"))
    })
  }
}
