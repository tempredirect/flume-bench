package com.logicalpractice.flume.bench.clients

import com.logicalpractice.flume.bench.FlumeClient
import groovy.transform.CompileStatic
import org.apache.flume.Event
import org.apache.flume.api.RpcClient

/**
 *
 */
@CompileStatic
abstract class AbstractRpcClient implements FlumeClient {
  RpcClient rpcClient

  @Override
  void send(List<Event> events) {
    rpcClient.appendBatch(events)
  }
}
