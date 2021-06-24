package com.yj.study.spark.sql.data_source

import com.yj.study.spark.sql.{LoggerTrait, SparkSessionUtils}

// Generic File Source Options
object GenericFileSourceOption extends LoggerTrait {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionUtils.getLocalSparkSession()

    // 开启跳过错误文件，不是parquet文件，也会继续执行下去
    spark.sql("set spark.sql.files.ignoreCorruptFiles=true")
    val testCorruptDF = spark.read.parquet(this.getClass.getResource("/dir1/").getPath
    ) //this.getClass.getResource("/dir1/dir2/").getPath
    testCorruptDF.show()


    spark.sql("set spark.sql.files.ignoreMissingFiles=true")
    // 不好测试，因为该参数的意思是构造完DataFrame后删除文件也能继续运行
    //    spark.read.parquet("/1.parquet").show();

    println("pathGlobFilter test")
    spark.read
      .option("pathGlobFilter", "*.parquet")
      .parquet(this.getClass.getResource("/dir1/").getPath)
      .show()

    println("recursiveFileLookup test")
    spark.read
      .option("recursiveFileLookup","true")
      .option("pathGlobFilter", "*.parquet")
      .parquet(this.getClass.getResource("/dir1/").getPath)
      .show()

  }
}
