/*
 * Copyright (c) 2014-2018 University of Ulm
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.cloudiator.iaas.node.messaging;

import com.google.inject.Inject;
import org.cloudiator.iaas.node.NodeRequestQueue;
import org.cloudiator.iaas.node.NodeRequestQueue.NodeRequest;
import org.cloudiator.messages.Node.NodeRequestMessage;
import org.cloudiator.messaging.MessageInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRequestListener implements Runnable {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(NodeRequestListener.class);
  private final MessageInterface messageInterface;
  private final NodeRequestQueue nodeRequestQueue;

  @Inject
  public NodeRequestListener(MessageInterface messageInterface,
      NodeRequestQueue nodeRequestQueue) {
    this.messageInterface = messageInterface;
    this.nodeRequestQueue = nodeRequestQueue;
  }


  @Override
  public void run() {
    messageInterface.subscribe(NodeRequestMessage.class, NodeRequestMessage.parser(),
        (id, content) -> {
          LOGGER.info(String.format("Receiving new node request %s. Adding to queue.", content));
          nodeRequestQueue.addRequest(NodeRequest.of(content, id));
        });
  }
}