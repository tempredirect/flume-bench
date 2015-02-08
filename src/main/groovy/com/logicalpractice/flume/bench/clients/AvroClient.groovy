package com.logicalpractice.flume.bench.clients

import com.google.common.net.HostAndPort
import com.logicalpractice.flume.bench.FlumeClient
import org.apache.flume.Event
import org.apache.flume.api.RpcClient
import org.apache.flume.api.RpcClientFactory

/**
 *
 */
class AvroClient implements FlumeClient {

  RpcClient rpcClient

  @Override
  void send(List<Event> events) {
    rpcClient.appendBatch(events)
  }

  @Override
  void initialise(HostAndPort hostAndPort, Map<String, String> parameter) {
    rpcClient = RpcClientFactory.getDefaultInstance(hostAndPort.getHostText(), hostAndPort.getPort())
  }

  @Override
  String getName() {
    return "avro"
  }
}
