/*
 * Licensed to Julian Hyde under one or more contributor license
 * agreements.  See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Julian Hyde licenses this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.  You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */
package net.hydromatic.sml.eval;

import net.hydromatic.sml.type.Binding;

import java.util.HashMap;
import java.util.Map;

/** Evaluation environment. */
public class Environment {
  final Map<String, Binding> valueMap = new HashMap<>();

  public Binding get(String name) {
    final Binding value = valueMap.get(name);
    if (value == null) {
      throw new AssertionError("expected value for " + name);
    }
    return value;
  }

}

// End Environment.java