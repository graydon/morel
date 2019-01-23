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
package net.hydromatic.sml.util;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/** Robinson's unification algorithm. */
public class RobinsonUnifier extends Unifier {
  /**
   * Applies s1 to the elements of s2 and adds them into a single list.
   */
  static Map<Variable, Term> compose(Map<Variable, Term> s1,
      Map<Variable, Term> s2) {
    Map<Variable, Term> composed = new HashMap<>(s1);
    for (Map.Entry<Variable, Term> entry2 : s2.entrySet()) {
      composed.put(entry2.getKey(), entry2.getValue().apply(s1));
    }
    return composed;
  }

  private @Nullable Substitution sequenceUnify(Sequence lhs,
      Sequence rhs) {
    if (lhs.terms.size() != rhs.terms.size()) {
      return null;
    }
    if (lhs.terms.isEmpty()) {
      return EMPTY;
    }
    Term firstLhs = lhs.terms.get(0);
    Term firstRhs = rhs.terms.get(0);
    Substitution subs1 = unify(firstLhs, firstRhs);
    if (subs1 != null) {
      Sequence restLhs = sequenceApply(subs1.resultMap, skip(lhs.terms));
      Sequence restRhs = sequenceApply(subs1.resultMap, skip(rhs.terms));
      Substitution subs2 = sequenceUnify(restLhs, restRhs);
      if (subs2 != null) {
        Map<Variable, Term> joined = new HashMap<>();
        joined.putAll(subs1.resultMap);
        joined.putAll(subs2.resultMap);
        return new Substitution(joined);
      }
    }
    return null;
  }

  private static <E> List<E> skip(List<E> list) {
    return list.subList(1, list.size());
  }

  public @Nullable Substitution unify(List<TermTerm> termPairs) {
    switch (termPairs.size()) {
    case 1:
      return unify(termPairs.get(0).left, termPairs.get(0).right);
    default:
      throw new AssertionError();
    }
  }


  public @Nullable Substitution unify(Term lhs, Term rhs) {
    if (lhs instanceof Variable) {
      return new Substitution(ImmutableMap.of((Variable) lhs, rhs));
    }
    if (rhs instanceof Variable) {
      return new Substitution(ImmutableMap.of((Variable) rhs, lhs));
    }
    if (lhs instanceof Atom && rhs instanceof Atom) {
      return lhs == rhs ? EMPTY : null;
    }
    if (lhs instanceof Sequence && rhs instanceof Sequence) {
      return sequenceUnify((Sequence) lhs, (Sequence) rhs);
    }
    return null;
  }

}

// End RobinsonUnifier.java
