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

package io.github.cloudiator.messaging;

import de.uniulm.omi.cloudiator.sword.domain.Image;
import de.uniulm.omi.cloudiator.sword.domain.ImageBuilder;
import de.uniulm.omi.cloudiator.util.TwoWayConverter;
import org.cloudiator.messages.entities.IaasEntities;
import org.cloudiator.messages.entities.IaasEntities.Image.Builder;

/**
 * Created by daniel on 07.06.17.
 */
public class ImageMessageToImageConverter implements TwoWayConverter<IaasEntities.Image, Image> {

  public static final ImageMessageToImageConverter INSTANCE = new ImageMessageToImageConverter();
  private static final LocationMessageToLocationConverter LOCATION_CONVERTER = LocationMessageToLocationConverter.INSTANCE;
  private OperatingSystemConverter operatingSystemConverter = new OperatingSystemConverter();
  private ImageMessageToImageConverter() {
  }

  @Override
  public IaasEntities.Image applyBack(Image image) {
    Builder builder = IaasEntities.Image.newBuilder().setId(image.id())
        .setProviderId(image.providerId())
        .setName(image.name())
        .setOperationSystem(operatingSystemConverter.applyBack(image.operatingSystem()));
    if (image.location().isPresent()) {
      builder.setLocation(LOCATION_CONVERTER.applyBack(image.location().get()));
    }
    return builder.build();
  }

  @Override
  public Image apply(IaasEntities.Image image) {
    return ImageBuilder.newBuilder().id(image.getId()).providerId(image.getProviderId())
        .name(image.getName()).os(operatingSystemConverter.apply(image.getOperationSystem()))
        .location(LOCATION_CONVERTER.apply(image.getLocation())).build();
  }
}
