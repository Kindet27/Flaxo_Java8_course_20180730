package c_spliterator.part2;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import org.openjdk.jmh.annotations.*;

//mvn package -DskipTests
//java -jar target/benchmarks.jar ArrayIntBenchmark

@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class ArrayIntBenchmark {

    @Param({"100000"})
    public int length;

    public int[] array;

    @Setup
    public void setup() {
        array = new int[length];

        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt();
        }
    }


    @Benchmark
    public long baiseline_seq() {
        return Arrays.stream(array)
                .sequential()
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long baiseline_par() {
        return Arrays.stream(array)
                .parallel()
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long test_seq() {
        final boolean parallel = false;
        return StreamSupport.intStream(new ArrayExample.IntArraySpliterator(array), parallel)
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long test_par() {
        final boolean parallel = true;
        return StreamSupport.intStream(new ArrayExample.IntArraySpliterator(array), parallel)
                .asLongStream()
                .sum();
    }
}
