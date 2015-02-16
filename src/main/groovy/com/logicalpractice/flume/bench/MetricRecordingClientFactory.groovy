package com.logicalpractice.flume.bench

import com.codahale.metrics.MetricRegistry
import groovy.transform.Canonical
import groovy.transform.CompileStatic

/**
 *
 */
@Canonical
class MetricRecordingClientFactory implements FlumeClientFactory {

  @Delegate
  FlumeClientFactory delegate

  MetricRegistry metricRegistry

  @Override
  @CompileStatic
  FlumeClient create() {
    new MetricRecordingClient(delegate.create(), metricRegistry)
  }

}
