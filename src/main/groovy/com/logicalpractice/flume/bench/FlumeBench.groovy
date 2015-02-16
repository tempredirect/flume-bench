package com.logicalpractice.flume.bench

import ch.qos.logback.classic.BasicConfigurator
import ch.qos.logback.classic.Level
import com.codahale.metrics.ConsoleReporter
import com.codahale.metrics.MetricRegistry
import com.google.common.net.HostAndPort
import io.dropwizard.util.Duration
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.Argument
import net.sourceforge.argparse4j.inf.ArgumentParser
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.ArgumentType
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 *
 */
class FlumeBench {

  Logger logger = LoggerFactory.getLogger(FlumeBench)

  static void main(String[] args) {

    BasicConfigurator.configureDefaultContext()

    new FlumeBench().run(args)
  }

  def run(String [] args) {
    ServiceLoader<FlumeClientFactory> serviceLoader = ServiceLoader.load(FlumeClientFactory)

    ArgumentParser parser = ArgumentParsers.newArgumentParser("flume-bench")

    parser.defaultHelp(true)

    parser.addArgument("--log-level")
        .setDefault(Level.WARN)
        .type(Level)

    parser.addArgument("--client")
        .setDefault("avro")
        .choices(serviceLoader.iterator().collect { it.name })

    parser.addArgument("--duration")
        .setDefault(Duration.parse("5m"))
        .type(durationType())

    parser.addArgument("host")
        .setDefault('localhost:8001')
        .type(hostAndPortType())

    Subparser maxThroughput = parser.addSubparsers().addParser("max-throughput")
    maxThroughput
        .setDefault("command", "max-throughput")
        .help("run at the max possible event")

    maxThroughput.addArgument("--concurrency")
        .type(Integer)
        .setDefault(1)
        .help("number of threads attempting write to each client")

    Namespace ns = parser.parseArgsOrFail(args)

    FlumeClientFactory clientFactory = serviceLoader.iterator()
        .find { it.name == ns.getString("client") } as FlumeClientFactory

    clientFactory.configure([:])

    MetricRegistry metrics = new MetricRegistry()

    ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
        .convertRatesTo(TimeUnit.SECONDS)
        .build()

    reporter.start(5, TimeUnit.SECONDS)

    MetricRecordingClientFactory metricRecordingClientFactory = new MetricRecordingClientFactory(clientFactory, metrics)

    ExecutorService executorService = Executors.newCachedThreadPool()

    Throwable lastError = null
    switch(ns.getString("command")) {
      case "max-throughput":
        def concurrency = ns.getInt("concurrency")
        def tasks = (1..concurrency).collect {
          def client = metricRecordingClientFactory.create()
          client.initialise(ns.get("host") as HostAndPort)
          new MaxThroughput(
              source: new EventSource(),
              client: client,
              duration: ns.get("duration"),
              metrics: metrics
          )
        }
        def futures = executorService.invokeAll(tasks.collect(this.&toCallable))

        // report any errors
        futures.each { f ->
          try {
            f.get()
          } catch (ExecutionException e) {
            logger.error("task failed: ${e}")
            lastError = e
          }
        }
        break
    }

    reporter.stop()

    reporter.report()
    if (lastError) {
      logger.error("Run ended in failure lastError: $lastError")
      System.exit(1)
    }
  }

  private static Callable toCallable(Runnable runnable) {
    new Callable() {
      @Override
      Object call() throws Exception {
        runnable.run()
        null
      }
    }
  }

  private static ArgumentType<Duration> durationType() {
    {ArgumentParser p, Argument arg, String value -> Duration.parse(value)}
  }

  private static ArgumentType<HostAndPort> hostAndPortType() {
    {ArgumentParser p, Argument arg, String value -> HostAndPort.fromString(value)}
  }

}
