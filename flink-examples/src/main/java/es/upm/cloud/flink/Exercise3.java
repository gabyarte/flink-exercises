/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.upm.cloud.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

public class Exercise3 {

	public static void main(String[] args) throws Exception {
		final ParameterTool params = ParameterTool.fromArgs(args);

		// Sets up the execution environment, which is the main entry point
		// to building Flink applications.
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        // Read from the file
		DataStream<String> dataStream = env.readTextFile(params.get("input"));
        // Use flatMap to return two tuples for each input tuple
        SingleOutputStreamOperator<Tuple3<Long, String, Double>> outputDataStream = dataStream.flatMap(
            new FlatMapFunction<String, Tuple3<Long, String, Double>>(){
                @Override
                public void flatMap(String value, Collector<Tuple3<Long, String, Double>> output) throws Exception {
                    String[] parts = value.split(",");
                    Long time = Long.parseLong(parts[0]);
                    String id = parts[1];
                    Double temperature = Double.parseDouble(parts[2]);
                    output.collect(new Tuple3<Long, String, Double>(time, id, temperature));
                    output.collect(new Tuple3<Long, String, Double>(
                        time,
                        id.concat("-F"),
                        (temperature * 9) / 5 + 32
                    ));
                }
            }
        );
        // Write filtered csv
		outputDataStream.writeAsCsv(params.get("output"), FileSystem.WriteMode.OVERWRITE);
		// Execute program, beginning computation.
		env.execute("MapAndFilterExample");
	}
}
