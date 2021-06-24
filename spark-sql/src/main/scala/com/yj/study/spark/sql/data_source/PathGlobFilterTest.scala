package com.yj.study.spark.sql.data_source

import com.yj.study.spark.sql.{LoggerTrait, SparkSessionUtils}

// 对pathGlobFilter这个参数进行测试
// 测试内容是：不改变分区搜索行为这个特点
// recursiveFileLookup
// 改变分区搜索行为


object PathGlobFilterTest extends LoggerTrait {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionUtils.getLocalSparkSession()

    /*
     * 总结起来就是说pathGlobFilter读取数据源时，如果碰到目录a=b的形式，会把当成a当成字段，b当成字段值。
     */

    val recuresDF = spark.read
      .option("recursiveFileLookup", "true")
      .json("F:\\spark\\spark-sql\\src\\main\\resources\\name")

    recuresDF.write.partitionBy("_c0")
      .saveAsTable("partition_test_1")
    spark.sql("select * from partition_test_1").show()

    println("=" * 20)

    val pathDF = spark.read
      .option("pathGlobFilter", "*.json")
      .format("json")
      .load("F:\\spark\\spark-sql\\src\\main\\resources\\name")
    pathDF.write.partitionBy("name")
      .saveAsTable("partition_test_2")
    spark.sql("select * from partition_test_2").show()

  }
}
