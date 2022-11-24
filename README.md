# Flink exercises for UPM's Master in Data Science's Cloud Computing course

To properly run this project, it is needed a flink docker image to be able to run it locally.

## Exercises

1. Create a simple **Flink Java** program that reads the file `sensorData.csv` and writes the lines of the file into a file called `exercise1.csv`
2. Create a simple **Flink Java** program that reads the file `sensorData.csv`, creates a `Tuple3 <Long, String, Double>` for each line of the file and writes the tuples corresponding to `“sensor1”` in a file named `exercise2.csv`.
3. Create a simple **Flink Java** program that reads the file `sensorData.csv`, creates two `Tuple3 <Long, String, Double>` from each line of the file. The first tuple represents the temperature in Celsius degrees and the second tuple represents the temperature in Fahrenheit degrees $\frac{Cº * 9}{5} + 32$ and the name of the sensor has to be modified from *sensorX* to *sensorX-F* where *X* is the number of the sensor. At the end it writes the tuples into a file named `exercise3.csv`.
