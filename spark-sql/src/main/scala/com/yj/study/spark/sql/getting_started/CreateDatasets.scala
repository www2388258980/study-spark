package com.yj.study.spark.sql.getting_started

import com.yj.study.spark.SparkSessionUtils

case class Person(name: String, age: Long)

object CreateDatasets {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionUtils.getLocalSparkSession()


    import spark.implicits._
    // Encoders are created for case classes
    val caseClassDS = Seq(Person("Andy", 22)).toDS
    caseClassDS.show()

    // Encoders for most common types are automatically provided by importing spark.implicits._
    val primitiveDS = Seq(1, 2, 3).toDS()
    primitiveDS.map(_ + 1).collect() // Returns: Array(2, 3, 4)

    // DataFrames can be converted to a Dataset by providing a class. Mapping will be done by name
    val peopleDS = spark.read.json(this.getClass.getResource("/people.json").getPath)
      .as[Person]
    peopleDS.show()

  }
}
