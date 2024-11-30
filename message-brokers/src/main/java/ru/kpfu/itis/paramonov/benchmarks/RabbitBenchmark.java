package ru.kpfu.itis.paramonov.benchmarks;

/*import org.openjdk.jmh.annotations.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.kpfu.itis.paramonov.BrokersApplication;
import ru.kpfu.itis.paramonov.rabbit.config.RabbitMqPropertiesConfiguration;
import ru.kpfu.itis.paramonov.rabbit.consumer.RabbitMqConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvmArgsAppend = {"-Xms2g", "-Xmx2g"}, value = 0)
@Measurement(iterations = 3)
public class RabbitBenchmark {

    private ConfigurableApplicationContext context;

    private List<RabbitMqConsumer> consumers;

    private RabbitTemplate rabbitTemplate;

    private RabbitMqPropertiesConfiguration rabbitConfig;

    private void baseBenchmark(
            int consumerAmount
    ) {

        for (int i = 0; i < consumerAmount; i++) {
            consumers.add(new RabbitMqConsumer());
        }
    }

    @Setup(Level.Trial)
    public void setUp() {
        context = SpringApplication.run(BrokersApplication.class);
        this.rabbitTemplate = (RabbitTemplate) context.getBean("rabbitTemplate");
        this.rabbitConfig = (RabbitMqPropertiesConfiguration) context.getBean("rabbitMqPropertiesConfiguration");

        consumers = new ArrayList<>();
    }

    @Benchmark
    public void simpleBenchmark() {
        baseBenchmark(1);

        int producerAmount = 1;

        for (int i = 0; i < producerAmount; i++) {
            rabbitTemplate.convertAndSend(rabbitConfig.getTopics().getBenchmarkTopic().getName(),
                    "benchmark.test", "Simple message");
        }
    }

    @Benchmark
    public void loadBalancingBenchmark() {
        baseBenchmark(1);

        int producerAmount = 3;

        for (int i = 0; i < producerAmount; i++) {
            rabbitTemplate.convertAndSend(rabbitConfig.getTopics().getBenchmarkTopic().getName(),
                    "benchmark.test", "Simple message");
        }
    }

    @Benchmark
    public void multipleConsumersBenchmark() {
        baseBenchmark(3);

        int producerAmount = 1;

        for (int i = 0; i < producerAmount; i++) {
            rabbitTemplate.convertAndSend(rabbitConfig.getTopics().getBenchmarkTopic().getName(),
                    "benchmark.test", "Simple message");
        }
    }

    @Benchmark
    public void loadBalancingAndMultipleConsumersBenchmark() {
        baseBenchmark(3);

        int producerAmount = 3;

        for (int i = 0; i < producerAmount; i++) {
            rabbitTemplate.convertAndSend(rabbitConfig.getTopics().getBenchmarkTopic().getName(),
                    "benchmark.test", "Simple message");
        }
    }

    @Benchmark
    public void stressTestBenchmark() {
        baseBenchmark(10);

        int producerAmount = 10;

        for (int i = 0; i < producerAmount; i++) {
            rabbitTemplate.convertAndSend(rabbitConfig.getTopics().getBenchmarkTopic().getName(),
                    "benchmark.test", "Simple message");
        }
    }



    @TearDown(Level.Iteration)
    public void cleanLists() {
        consumers.clear();
    }

    @TearDown(Level.Trial)
    public void close() {
        context.close();
    }

}*/
