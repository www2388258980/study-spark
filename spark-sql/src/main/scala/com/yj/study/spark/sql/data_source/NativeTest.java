package com.yj.study.spark.sql.data_source;



import org.apache.hadoop.io.nativeio.NativeIO;

import java.io.IOException;

public class NativeTest {
		public static void main(String[] args) throws IOException {
				NativeIO.Windows.access("G:\\上海至数\\dynamic_source.sql",
								NativeIO.Windows.AccessRight.ACCESS_EXECUTE);
		}
}
