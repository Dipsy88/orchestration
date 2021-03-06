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

package io.github.cloudiator.iaas.vm.messaging;

import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uniulm.omi.cloudiator.sword.domain.Cloud;
import de.uniulm.omi.cloudiator.sword.multicloud.service.CloudRegistry;
import io.github.cloudiator.messaging.CloudMessageToCloudConverter;
import org.cloudiator.messages.Cloud.CloudEvent;
import org.cloudiator.messages.entities.IaasEntities.CloudState;
import org.cloudiator.messaging.MessageCallback;
import org.cloudiator.messaging.MessageInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class CloudEventSubscriber implements Runnable {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(CloudEventSubscriber.class);
  private final MessageInterface messageInterface;
  private final CloudRegistry cloudRegistry;
  private final CloudMessageToCloudConverter cloudMessageToCloudConverter = CloudMessageToCloudConverter.INSTANCE;

  @Inject
  public CloudEventSubscriber(MessageInterface messageInterface,
      CloudRegistry cloudRegistry) {
    this.messageInterface = messageInterface;
    this.cloudRegistry = cloudRegistry;
  }

  @Override
  public void run() {
    messageInterface.subscribe(CloudEvent.class, CloudEvent.parser(),
        new MessageCallback<CloudEvent>() {
          @Override
          public void accept(String id, CloudEvent content) {

            final Cloud cloud = cloudMessageToCloudConverter.apply(content.getCloud());
            LOGGER.info(String
                .format("%s is receiving event for cloud %s. State changed from %s to %s.", this,
                    cloud, content.getFrom(), content.getTo()));

            if (content.getCloud().getState().equals(CloudState.CLOUD_STATE_OK) && !cloudRegistry
                .isRegistered(cloud)) {
              LOGGER.debug(
                  String.format("%s is registering new cloud %s to cloud registry", this, cloud));
              cloudRegistry.register(cloud);
            }

            if (content.getCloud().getState().equals(CloudState.CLOUD_STATE_DELETED) || content
                .getCloud().getState().equals(CloudState.CLOUD_STATE_ERROR)) {
              if (cloudRegistry.isRegistered(cloud)) {
                LOGGER.debug(
                    String
                        .format("%s is removing failed or deleted cloud %s from cloud registry",
                            this,
                            cloud));
                cloudRegistry.unregister(cloud);
              }
            }


          }
        });
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }
}
