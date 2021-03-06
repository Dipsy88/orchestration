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

package io.github.cloudiator.persistance;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;

/**
 * Created by daniel on 06.10.15.
 */
@Entity
class TemplateOptions extends Model {

  @Nullable
  String userData;
  @ElementCollection
  @MapKeyColumn(name = "tagName")
  @Column(name = "tagValue")
  private Map<String, String> tags;

  /**
   * Empty constructor for hibernate.
   */
  protected TemplateOptions() {

  }

  public Map<String, String> tags() {
    return ImmutableMap.copyOf(tags);
  }

  public Optional<String> userData() {
    return Optional.ofNullable(userData);
  }

  public void addTag(String tagName, String tagValue) {
    if (tags == null) {
      tags = new HashMap<>();
    }
    this.tags.put(tagName, tagValue);
  }
}
