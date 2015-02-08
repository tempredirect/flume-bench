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

import java.util.concurrent.TimeUnit

/**
 *
 */
class FlumeBench {

  static void main(String[] args) {

    BasicConfigurator.configureDefaultContext()

    ServiceLoader<FlumeClient> serviceLoader = ServiceLoader.load(FlumeClient)

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

    Namespace ns = parser.parseArgsOrFail(args)

    FlumeClient client = serviceLoader.iterator().find { it.name == ns.getString("client") } as FlumeClient
    client.initialise(ns.get("host") as HostAndPort, [:])

    MetricRegistry metrics = new MetricRegistry()
    MetricRecordingClient metricRecordingClient = new MetricRecordingClient(client, metrics)

    ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
        .convertRatesTo(TimeUnit.SECONDS)
        .build()

    reporter.start(5, TimeUnit.SECONDS)

    switch(ns.getString("command")) {
      case "max-throughput":
        new MaxThroughput(
            source: new EventSource(),
            client: metricRecordingClient,
            duration: ns.get("duration"),
            metrics: metrics
        ).run()
        break
    }

    reporter.stop()

    reporter.report()
  }

  private static ArgumentType<Duration> durationType() {
    {ArgumentParser p, Argument arg, String value -> Duration.parse(value)}
  }

  private static ArgumentType<HostAndPort> hostAndPortType() {
    {ArgumentParser p, Argument arg, String value -> HostAndPort.fromString(value)}
  }

}
