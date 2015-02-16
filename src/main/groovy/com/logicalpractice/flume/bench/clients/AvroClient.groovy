package com.logicalpractice.flume.bench.clients

import com.google.common.net.HostAndPort
import com.logicalpractice.flume.bench.FlumeClient
import org.apache.flume.Event
import org.apache.flume.api.RpcClient
import org.apache.flume.api.RpcClientFactory

/**
 *
 */
class AvroClient extends AbstractRpcClient implements FlumeClient {

  @Override
  void initialise(HostAndPort hostAndPort) {
    rpcClient = RpcClientFactory.getDefaultInstance(hostAndPort.getHostText(), hostAndPort.getPort())
  }

}
