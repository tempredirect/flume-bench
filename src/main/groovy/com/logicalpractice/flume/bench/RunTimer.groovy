package com.logicalpractice.flume.bench

import groovy.transform.Canonical
import io.dropwizard.util.Duration

/**
 *
 */
@Canonical
class RunTimer {

  static runFor(Duration duration, Closure closure) {
    long start = System.nanoTime()
    long endTime = start + duration.toNanoseconds()

    while(System.nanoTime() < endTime) {
      closure.call(1000)
    }
  }
}
