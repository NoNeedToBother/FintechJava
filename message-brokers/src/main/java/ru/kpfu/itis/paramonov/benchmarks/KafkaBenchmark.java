package ru.kpfu.itis.paramonov.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import ru.kpfu.itis.paramonov.BrokersApplication;
import ru.kpfu.itis.paramonov.kafka.consumer.KafkaBenchmarkConsumer;
import ru.kpfu.itis.paramonov.kafka.producer.KafkaProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvmArgsAppend = {"-Xms2g", "-Xmx2g"}, value = 0)
@Measurement(iterations = 3)
public class KafkaBenchmark {

    private ConfigurableApplicationContext context;

    private List<KafkaProducer> producers;

    private List<KafkaBenchmarkConsumer> consumers;

    private KafkaTemplate<String, String> kafkaTemplate;

    private void baseBenchmark(
            int producerAmount,
            int consumerAmount
    ) {
        for (int i = 0; i < producerAmount; i++) {
            producers.add(new KafkaProducer(kafkaTemplate));
        }

        for (int i = 0; i < consumerAmount; i++) {
            consumers.add(new KafkaBenchmarkConsumer());
        }
    }

    @Setup(Level.Trial)
    public void setUp() {
        context = SpringApplication.run(BrokersApplication.class);
        this.kafkaTemplate = (KafkaTemplate<String, String>) context.getBean("kafkaTemplate");
        producers = new ArrayList<>();
        consumers = new ArrayList<>();
    }

    @Benchmark
    public void simpleBenchmark() {
        baseBenchmark(1, 1);

        for (var producer: producers) {
            producer.sendMessage("benchmark-topic", "Simple message");
        }
    }

    @Benchmark
    public void loadBalancingBenchmark() {
        baseBenchmark(3, 1);
        for (var producer: producers) {
            producer.sendMessage("benchmark-topic", "Simple message");
        }
    }

    @Benchmark
    public void multipleConsumersBenchmark() {
        baseBenchmark(1, 3);
        for (var producer: producers) {
            producer.sendMessage("benchmark-topic", "Simple message");
        }
    }

    @Benchmark
    public void loadBalancingAndMultipleConsumersBenchmark() {
        baseBenchmark(3, 3);
        for (var producer: producers) {
            producer.sendMessage("benchmark-topic", "Simple message");
        }
    }

    @Benchmark
    public void stressTestBenchmark() {
        baseBenchmark(10, 10);
        for (var producer: producers) {
            producer.sendMessage("benchmark-topic", "Simple message");
        }
    }

    @TearDown(Level.Iteration)
    public void cleanLists() {
        producers.clear();

        consumers.clear();
    }

    @TearDown(Level.Trial)
    public void close() {
        context.close();
    }
}
