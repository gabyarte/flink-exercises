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
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;

public class Exercise2 {

	public static void main(String[] args) throws Exception {
		final ParameterTool params = ParameterTool.fromArgs(args);

		// Sets up the execution environment, which is the main entry point
		// to building Flink applications.
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        // Read from the file
		DataStream<String> dataStream = env.readTextFile(params.get("input"));
        // Parsed the file into tuples
        DataStream<Tuple3<Long, String, Double>> parsedDataStream = dataStream.map(
            new MapFunction<String, Tuple3<Long, String, Double>>(){
                @Override
                public Tuple3<Long, String, Double> map(String value) throws Exception {
                    String[] parts = value.split(",");
                    return new Tuple3<Long, String, Double>(
                        Long.parseLong(parts[0]),
                        parts[1],
                        Double.parseDouble(parts[2])
                    );
                }
            }
        );
        // Filter tuples having sensorId = sensor1
        DataStream<Tuple3<Long, String, Double>> filteredDataStream = parsedDataStream.filter(
            new FilterFunction<Tuple3<Long, String, Double>>() {
                @Override
                public boolean filter(Tuple3<Long, String, Double> value) throws Exception {
                    return value.f1.equals("sensor1");
                }
            }
        );
        // Write filtered csv
		filteredDataStream.writeAsCsv(params.get("output"), FileSystem.WriteMode.OVERWRITE);
		// Execute program, beginning computation.
		env.execute("MapAndFilterExample");
	}
}
