<!--
{% comment %}
Licensed to Julian Hyde under one or more contributor license
agreements.  See the NOTICE file distributed with this work
for additional information regarding copyright ownership.
Julian Hyde licenses this file to you under the Apache
License, Version 2.0 (the "License"); you may not use this
file except in compliance with the License.  You may obtain a
copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
either express or implied.  See the License for the specific
language governing permissions and limitations under the
License.
{% endcomment %}
-->

# Morel language reference

## Grammar

### Meta-syntax

In this grammar we use the following conventions:

| Syntax      | Meaning
| ----------- | -------
| *symbol*    | Grammar symbol (e.g. *con*)
| **keyword** | Morel keyword (e.g. **if**) and symbol (e.g. **~**, "**(**")
| \[ term \]  | Option: term may occur 0 or 1 times
| term*       | Repetition: term may occur 0 or more times
| 's'         | Quotation: Symbols used in the grammar &mdash; ( ) \[ \] \| * ... &mdash; are quoted when they appear in Morel language

### Constants

<pre>
<i>con</i>:  <i>int</i>                 integer
    | <i>float</i>               floating point
    | <i>char</i>                character
    | <i>string</i>              string
<i>int</i>:  [<b>~</b>]<i>num</i>              decimal
    | [<b>~</b>]<b>0x</b><i>hex</i>            hexadecimal
<i>float</i>: [<b>~</b>]<i>num</i><b>.</b><i>num</i>         floating point
    | [<b>~</b>]<i>num</i>[<b>.</b><i>num</i>]<b>e</b>[<b>~</b>]<i>num</i>
                          scientific
<i>char</i>: <b>#"</b><i>ascii</i><b>"</b>            character
<i>string</i>: <b>"</b><i>ascii</i>*<b>"</b>          string
<i>num</i>:  <i>digit</i> <i>digit</i>*        number
<i>hex</i>:  (<i>digit</i> | <i>letter</i>) (<i>digit</i> | <i>letter</i>)*
                          hexadecimal number (letters may
                          only be in the range A-F)
<i>ascii</i>: ...                single non-" ASCII character or
                          \-headed escape sequence
</pre>

## Identifiers

<pre>
<i>id</i>:   <i>letter</i> (<i>letter</i> | <i>digit</i> | ''' | <b>_</b>)*
                          alphanumeric
    | <i>symbol</i> <i>symbol</i>*      symbolic (not allowed for type variables
                          or module language identifiers)
<i>symbol</i>: <b>!</b>
    | <b>%</b>
    | <b>&amp;</b>
    | <b>$</b>
    | <b>#</b>
    | <b>+</b>
    | <b>-</b>
    | <b>/</b>
    | <b>:</b>
    | <b>&lt;</b>
    | <b>=</b>
    | <b>&gt;</b>
    | <b>?</b>
    | <b>@</b>
    | <b>\</b>
    | <b>~</b>
    | <b>`</b>
    | <b>^</b>
    | '<b>|</b>'
    | '<b>*</b>'
<i>var</i>:  '''(<i>letter</i> | <i>digit</i> | ''' | <b>_</b>)*
                          unconstrained
      ''''(<i>letter</i> | <i>digit</i> | ''' | <b>_</b>⟩*
                          equality
<i>lab</i>:  <i>id</i>                  identifier
      <i>num</i>                 number (may not start with 0)
</pre>

### Expressions

<pre>
<i>exp</i>:  <i>con</i>                 constant
    | [ <b>op</b> ] <i>id</i>           value or constructor identifier
    | <i>exp<sub>1</sub></i> <i>exp<sub>2</sub></i>            application
    | <i>exp<sub>1</sub></i> <i>id</i> <i>exp<sub>2</sub></i>         infix application
    | '<b>(</b>' <i>exp</i> '<b>)</b>'         parentheses
    | '<b>(</b>' <i>exp<sub>1</sub></i> <b>,</b> ... <b>,</b> <i>exp<sub>n</sub></i> '<b>)</b>'
                          tuple (n &ne; 1)
    | <b>{</b> [ <i>exprow</i> ] <b>}</b>      record
    | <b>#</b><i>lab</i>                record selector
    | '<b>[</b>' <i>exp<sub>1</sub></i> <b>,</b> ... <b>,</b> <i>exp<sub>n</sub></i> '<b>]</b>'
                          list (n &ge; 0)
    | '<b>(</b>' <i>exp<sub>1</sub></i> <b>;</b> ... <b>;</b> <i>exp<sub>n</sub></i> '<b>)</b>'
                          sequence (n &ge; 2)
    | <b>let</b> <i>dec</i> <b>in</b> <i>exp<sub>1</sub></i> ; ... ; <i>exp<sub>n</sub></i> <b>end</b>
                          local declaration (n ≥ 1)
    | <i>exp<sub>1</sub></i> <b>andalso</b> <i>exp<sub>2</sub></i>    conjunction
    | <i>exp<sub>1</sub></i> <b>orelse</b> <i>exp<sub>2</sub></i>     disjunction
    | <b>if</b> <i>exp<sub>1</sub></i> <b>then</b> <i>exp<sub>2</sub></i> <b>else</b> <i>exp<sub>3</sub></i>
                          conditional
    | <b>case</b> <i>exp</i> <b>of</b> <i>match</i>   case analysis
    | <b>fn</b> <i>match</i>            function
<i>exprow</i>: <i>exprowItem</i> [<b>,</b> <i>exprowItem</i> ]*
                          expression row
<i>exprowItem</i>: [<i>lab</i> <b>=</b>] <i>exp</i>
<i>match</i>: <i>matchItem</i> [ '<b>|</b>' <i>matchItem</i> ]*
                          match
<i>matchItem</i>: <i>pat</i> <b>=&gt;</b> <i>exp</i>
</pre>

### Patterns

<pre>
<i>pat</i>: <i>con</i>                  constant
    | <b>_</b>                   wildcard
    | [ <b>op</b> ] <i>id</i>           variable
    | [ <b>op</b> ] <i>id</i> [ <i>pat</i> ]   construction
    | <i>pat<sub>1</sub></i> <i>id</i> <i>pat<sub>2</sub></i>         infix construction
    | '<b>(</b>' <i>pat</i> '<b>)</b>'         parentheses
    | '<b>(</b>' <i>pat<sub>1</sub></i> , ... , <i>pat<sub>n</sub></i> '<b>)</b>'
                          tuple (n &ne; 1)
    | <b>{</b> [ <i>patrow</i> ] <b>}</b>      record
    | '<b>[</b>' <i>pat<sub>1</sub></i> <b>,</b> ... <b>,</b> <i>pat<sub>n</sub></i> '<b>]</b>'
                          list (n &ge; 0)
<i>patrow</i>: '<b>...</b>'             wildcard
    | <i>lab</i> <b>=</b> <i>pat</i> [<b>,</b> <i>patrow</i>] pattern
    | <i>id</i> [<b>,</b> <i>patrow</i>]       variable
</pre>

### Types

<pre>
<i>typ</i>:  <i>var</i>                 variable
    | [ <i>typ</i> ] <i>id</i>          constructor
    | '<b>(</b>' <i>typ</i> [<b>,</b> <i>typ</i> ]* '<b>)</b>' <i>id</i>
                          constructor
    | '<b>(</b>' <i>typ</i> '<b>)</b>'         parentheses
    | <i>typ<sub>1</sub></i> <b>-&gt;</b> <i>typ<sub>2</sub></i>        function
    | <i>typ<sub>1</sub></i> '<b>*</b>' ... '<b>*</b>' <i>typ<sub>n</sub></i>
                          tuple (n &ge; 2)
    | <b>{</b> [ <i>typrow</i> ] <b>}</b>      record
<i>typrow</i>: <i>lab</i> : <i>typ</i> [, <i>typrow</i>]
                          type row
</pre>

### Declarations

<pre>
<i>dec</i>:  <i>vals</i> <i>valbind</i>        value
    | <b>fun</b> <i>vars</i> <i>funbind</i>    function
    | <b>datatype</b> <i>datbind</i>    data type
    | <i>empty</i>
    | <i>dec<sub>1</sub></i> [<b>;</b>] <i>dec<sub>2</sub></i>        sequence
<i>valbind</i>: <i>pat</i> <b>=</b> <i>exp</i> [ <b>and</b> <i>valbind</i> ]*
                          destructuring
    | <b>rec</b> <i>valbind</i>         recursive
<i>funbind</i>: <i>funmatch</i> [ <b>and</b> <i>funmatch</i> ]*
                          clausal function
<i>funmatch</i>: <i>funmatchItem</i> [ '<b>|</b>' funmatchItem ]*
<i>funmatchItem</i>: [ <b>op</b> ] <i>id</i> <i>pat<sub>1</sub></i> ... <i>pat<sub>n</sub></i> <b>=</b> <i>exp</i>
                          nonfix (n &ge; 1)
    | <i>pat<sub>1</sub></i> <i>id</i> <i>pat<sub>2</sub></i> <b>=</b> <i>exp</i>
                          infix
    | '<b>(</b>' <i>pat<sub>1</sub></i> <i>id</i> <i>pat<sub>2</sub></i> '<b>)</b>' <i>pat'<sub>1</sub></i> ... <i>pat'<sub>n</sub></i> = <i>exp</i>
                          infix (n &ge; 0)
<i>datbind</i>: <i>datbindItem</i> [ <b>and</b> <i>datbindItem</i> ]*
                          data type
<i>datbindItem</i>: <i>vars</i> <i>id</i> <b>=</b> <i>conbind</i>
<i>conbind</i>: <i>conbindItem</i> [ '<b>|</b>' <i>conbindItem</i> ]*
                          data constructor
<i>conbindItem</i>: <i>id</i> [ <b>of</b> <i>typ</i> ]
<i>vals</i>: <i>val</i>
    | '<b>(</b>' <i>val</i> [<b>,</b> <i>val</i>]* '<b>)</b>'
<i>vars</i>: <i>var</i>
    | '<b>(</b>' <i>var</i> [<b>,</b> <i>var</i>]* '<b>)</b>'
</pre>

### Built-in operators

| Operator | Precedence | Meaning
| :------- | ---------: | :------
| *        |    infix 7 | Multiplication
| /        |    infix 7 | Division
| div      |    infix 7 | Integer division
| mod      |    infix 7 | Modulo
| +        |    infix 6 | Plus
| -        |    infix 6 | Minus
| ^        |    infix 6 | String concatenate
| ~        |   prefix 6 | Negate
| ::       |   infixr 5 | List cons
| @        |   infixr 5 | List append
| &lt;=    |    infix 4 | Less than or equal
| &lt;     |    infix 4 | Less than
| &gt;=    |    infix 4 | Greater than or equal
| &gt;     |    infix 4 | Greater than
| =        |    infix 4 | Equal
| &lt;&gt; |    infix 4 | Not equal
| :=       |    infix 3 | Assign
| o        |    infix 3 | Compose

### Built-in functions

| Name | Type | Description
| ---- | ---- | -----------
| true | bool | Literal true
| false | bool | Literal false
| not | bool &rarr; bool | Logical not
| abs | int &rarr; int | Absolute value
| ignore | &alpha; &rarr; unit | Ignores its argument
| String.maxSize | int | The longest allowed size of a string
| String.size | string &rarr; int | "size s" returns \|s\|, the number of characters in string s
| String.sub | string * int &rarr; char | "sub (s, i)" returns the i(th) character of s, counting from zero. This raises `Subscript` if i &lt; 0 or \|s\| &le; i
| String.extract | string * int * int option &rarr; string | "extract (s, i, NONE)" and "extract (s, i, SOME j)" return substrings of s. The first returns the substring of s from the i(th) character to the end of the string, i.e., the string s\[i..\|s\|-1\]. This raises `Subscript` if i &lt; 0 or \|s\| &lt; i.<br><br>The second form returns the substring of size j starting at index i, i.e., the string s\[i..i+j-1\]. It raises `Subscript` if i &lt; 0 or j &lt; 0 or \|s\| &lt; i + j. Note that, if defined, `extract` returns the empty string when i = \|s\|.
| String.substring | string * int * int &rarr; string | "substring (s, i, j)" returns the substring s\[i..i+j-1\], i.e., the substring of size j starting at index i. This is equivalent to extract(s, i, SOME j).
| String.concat | string list &rarr; string | "concat l" is the concatenation of all the strings in l. This raises `Size` if the sum of all the sizes is greater than maxSize.
| String.concatWith | string &rarr; string list &rarr; string | "concatWith s l" returns the concatenation of the strings in the list `l` using the string `s` as a separator. This raises `Size` if the size of the resulting string would be greater than `maxSize`.
| String.str | char &rarr; string | "str c" is the string of size one containing the character `c`
| String.implode | char list &rarr; string | "implode l" generates the string containing the characters in the list `l`. This is equivalent to `concat (List.map str l)`. This raises `Size` if the resulting string would have size greater than `maxSize`.
| String.explode | string &rarr; char list | "explode s" is the list of characters in the string `s`.
| String.map | (char &rarr; char) &rarr; string &rarr; string | "map f s" applies `f` to each element of `s` from left to right, returning the resulting string. It is equivalent to `implode(List.map f (explode s))`.
| String.translate | (char &rarr; string) &rarr; string &rarr; string | "translate f s" returns the string generated from `s` by mapping each character in `s` by `f`. It is equivalent to `concat(List.map f (explode s))`.
| String.isPrefix | string &rarr; string &rarr; bool | "isPrefix s1 s2" returns `true` if the string `s1` is a prefix of the string `s2`. Note that the empty string is a prefix of any string, and that a string is a prefix of itself.
| String.isSubstring | string &rarr; string &rarr; bool | "isSubstring s1 s2" returns `true` if the string `s1` is a substring of the string `s2`. Note that the empty string is a substring of any string, and that a string is a substring of itself.
| String.isSuffix | string &rarr; string &rarr; bool | "isSuffix s1 s2" returns `true` if the string `s1` is a suffix of the string `s2`. Note that the empty string is a suffix of any string, and that a string is a suffix of itself.
| List.nil | &alpha; list | "nil" is the empty list.
| List.null | &alpha; list &rarr; bool | "null l" returns `true` if the list `l` is empty.
| List.length | &alpha; list &rarr; int | "length l" returns the number of elements in the list `l`.
| List.at | &alpha; list * &alpha; list &rarr; &alpha; list | "l1 @ l2" returns the list that is the concatenation of `l1` and `l2`.
| List.hd | &alpha; list &rarr; &alpha; | "hd l" returns the first element of `l`. It raises `Empty` if `l` is `nil`.
| List.tl | &alpha; list &rarr; &alpha; list | "tl l" returns all but the first element of `l`. It raises `Empty` if `l` is `nil`.

  /** Function "List.last", of type "&alpha; list &rarr; &alpha;".
   *
   * <p>"last l" returns the last element of l. It raises {@code Empty} if l is
   * nil.
   */
  LIST_LAST("List.last", ts ->
      ts.forallType(1, h -> ts.fnType(h.list(0), h.get(0)))),

  /** Function "List.getItem", of type "&alpha; list &rarr;
   * (&alpha; * &alpha; list) option".
   *
   * <p>"getItem l" returns {@code NONE} if the list is empty, and
   * {@code SOME(hd l,tl l)} otherwise. This function is particularly useful for
   * creating value readers from lists of characters. For example, Int.scan
   * StringCvt.DEC getItem has the type {@code (int,char list) StringCvt.reader}
   * and can be used to scan decimal integers from lists of characters.
   */
  // TODO: make it return an option
  LIST_GET_ITEM("List.getItem", ts ->
      ts.forallType(1, h ->
          ts.fnType(h.list(0), ts.tupleType(h.get(0), h.list(0))))),

  /** Function "List.nth", of type "&alpha; list * int &rarr; &alpha;".
   *
   * <p>"nth (l, i)" returns the i(th) element of the list l, counting from 0.
   * It raises {@code Subscript} if i &lt; 0 or i &ge; length l. We have
   * nth(l,0) = hd l, ignoring exceptions.
   */
  LIST_NTH("List.nth", ts ->
      ts.forallType(1, h -> ts.fnType(ts.tupleType(h.list(0), INT), h.get(0)))),

  /** Function "List.take", of type "&alpha; list * int &rarr; &alpha; list".
   *
   * <p>"take (l, i)" returns the first i elements of the list l. It raises
   * {@code Subscript} if i &lt; 0 or i &gt; length l.
   * We have take(l, length l) = l.
   */
  LIST_TAKE("List.take", ts ->
      ts.forallType(1, h ->
          ts.fnType(ts.tupleType(h.list(0), INT), h.list(0)))),

  /** Function "List.drop", of type "&alpha; list * int &rarr; &alpha; list".
   *
   * <p>"drop (l, i)" returns what is left after dropping the first i elements
   * of the list l.
   *
   * <p>It raises {@code Subscript} if i &lt; 0 or i &gt; length l.
   *
   * <p>It holds that
   * {@code take(l, i) @ drop(l, i) = l} when 0 &le; i &le; length l.
   *
   * <p>We also have {@code drop(l, length l) = []}.
   */
  LIST_DROP("List.drop", ts ->
      ts.forallType(1, h ->
          ts.fnType(ts.tupleType(h.list(0), INT), h.list(0)))),

  /** Function "List.rev", of type "&alpha; list &rarr; &alpha; list".
   *
   * <p>"rev l" returns a list consisting of l's elements in reverse order.
   */
  LIST_REV("List.rev", ts ->
      ts.forallType(1, h -> ts.fnType(h.list(0), h.list(0)))),

  /** Function "List.concat", of type "&alpha; list list &rarr; &alpha; list".
   *
   * <p>"concat l" returns the list that is the concatenation of all the lists
   * in l in order.
   * {@code concat[l1,l2,...ln] = l1 @ l2 @ ... @ ln}
   */
  LIST_CONCAT("List.concat", ts ->
      ts.forallType(1, h -> ts.fnType(ts.listType(h.list(0)), h.list(0)))),

  /** Function "List.revAppend", of type "&alpha; list * &alpha; list &rarr;
   * &alpha; list".
   *
   * <p>"revAppend (l1, l2)" returns (rev l1) @ l2.
   */
  LIST_REV_APPEND("List.revAppend", ts ->
      ts.forallType(1, h ->
          ts.fnType(ts.tupleType(h.list(0), h.list(0)), h.list(0)))),

  /** Function "List.app", of type "(&alpha; &rarr; unit) &rarr; &alpha; list
   * &rarr; unit".
   *
   * <p>"app f l" applies f to the elements of l, from left to right.
   */
  LIST_APP("List.app", ts ->
      ts.forallType(1, h ->
          ts.fnType(ts.fnType(h.get(0), UNIT), h.list(0), UNIT))),

  /** Function "List.map", of type
   * "(&alpha; &rarr; &beta;) &rarr; &alpha; list &rarr; &beta; list".
   *
   * <p>"map f l" applies f to each element of l from left to right, returning
   * the list of results.
   */
  LIST_MAP("List.map", "map", ts ->
      ts.forallType(2, t ->
          ts.fnType(ts.fnType(t.get(0), t.get(1)),
              ts.listType(t.get(0)), ts.listType(t.get(1))))),

  /** Function "List.mapPartial", of type
   * "(&alpha; &rarr; &beta; option) &rarr; &alpha; list &rarr; &beta; list".
   *
   * <p>"mapPartial f l" applies f to each element of l from left to right,
   * returning a list of results, with SOME stripped, where f was defined. f is
   * not defined for an element of l if f applied to the element returns NONE.
   * The above expression is equivalent to:
   * {@code ((map valOf) o (filter isSome) o (map f)) l}
   */
  // TODO: make this take option
  LIST_MAP_PARTIAL("List.mapPartial", ts ->
      ts.forallType(2, h ->
          ts.fnType(ts.fnType(h.get(0), h.get(1)), h.list(0), h.list(1)))),

  /** Function "List.find", of type "(&alpha; &rarr; bool) &rarr; &alpha; list
   * &rarr; &alpha; option".
   *
   * <p>"find f l" applies f to each element x of the list l, from left to
   * right, until {@code f x} evaluates to true. It returns SOME(x) if such an x
   * exists; otherwise it returns NONE.
   */
  LIST_FIND("List.find", ts ->
      ts.forallType(1, h -> ts.fnType(h.predicate(0), h.list(0), h.get(0)))),

  /** Function "List.filter", of type
   * "(&alpha; &rarr; bool) &rarr; &alpha; list &rarr; &alpha; list".
   *
   * <p>"filter f l" applies f to each element x of l, from left to right, and
   * returns the list of those x for which {@code f x} evaluated to true, in the
   * same order as they occurred in the argument list.
   */
  LIST_FILTER("List.filter", ts ->
      ts.forallType(1, h -> ts.fnType(h.predicate(0), h.list(0), h.list(0)))),

  /** Function "List.partition", of type "(&alpha; &rarr; bool) &rarr;
   * &alpha; list &rarr; &alpha; list * &alpha; list".
   *
   * <p>"partition f l" applies f to each element x of l, from left to right,
   * and returns a pair (pos, neg) where pos is the list of those x for which
   * {@code f x} evaluated to true, and neg is the list of those for which
   * {@code f x} evaluated to false. The elements of pos and neg retain the same
   * relative order they possessed in l.
   */
  LIST_PARTITION("List.partition", ts ->
      ts.forallType(1, h ->
          ts.fnType(h.predicate(0), h.list(0),
              ts.tupleType(h.list(0), h.list(0))))),

  /** Function "List.foldl", of type "(&alpha; * &beta; &rarr; &beta;) &rarr;
   *  &beta; &rarr; &alpha; list &rarr; &beta;".
   *
   * <p>"foldl f init [x1, x2, ..., xn]" returns
   * {@code f(xn,...,f(x2, f(x1, init))...)}
   * or {@code init} if the list is empty.
   */
  LIST_FOLDL("List.foldl", ts ->
      ts.forallType(2, h ->
          ts.fnType(ts.fnType(ts.tupleType(h.get(0), h.get(1)), h.get(1)),
              h.get(1), h.list(0), h.get(1)))),

  /** Function "List.foldr", of type "(&alpha; * &beta; &rarr; &beta;) &rarr;
   *  &beta; &rarr; &alpha; list &rarr; &beta;".
   *
   * <p>"foldr f init [x1, x2, ..., xn]" returns
   * {@code f(x1, f(x2, ..., f(xn, init)...))}
   * or {@code init} if the list is empty.
   */
  LIST_FOLDR("List.foldr", ts ->
      ts.forallType(2, h ->
          ts.fnType(ts.fnType(ts.tupleType(h.get(0), h.get(1)), h.get(1)),
              h.get(1), h.list(0), h.get(1)))),

  /** Function "List.exists", of type "(&alpha; &rarr; bool) &rarr; &alpha; list
   * &rarr; bool".
   *
   * <p>"exists f l" applies f to each element x of the list l, from left to
   * right, until {@code f x} evaluates to true; it returns true if such an x
   * exists and false otherwise.
   */
  LIST_EXISTS("List.exists", ts ->
      ts.forallType(1, h -> ts.fnType(h.predicate(0), h.list(0), BOOL))),

  /** Function "List.all", of type
   * "(&alpha; &rarr; bool) &rarr; &alpha; list &rarr; bool".
   *
   * <p>"all f l" applies f to each element x of the list l, from left to right,
   * until {@code f x} evaluates to false; it returns false if such an x exists
   * and true otherwise. It is equivalent to not(exists (not o f) l)).
   */
  LIST_ALL("List.all", ts ->
      ts.forallType(1, h -> ts.fnType(h.predicate(0), h.list(0), BOOL))),

  /** Function "List.tabulate", of type
   * "int * (int &rarr; &alpha;) &rarr; &alpha; list".
   *
   * <p>"tabulate (n, f)" returns a list of length n equal to
   * {@code [f(0), f(1), ..., f(n-1)]}, created from left to right. It raises
   * {@code Size} if n &lt; 0.
   */
  LIST_TABULATE("List.tabulate", ts ->
      ts.forallType(1, h ->
          ts.fnType(ts.tupleType(INT, ts.fnType(INT, h.get(0))), h.list(0)))),

  /** Function "List.collate", of type "(&alpha; * &alpha; &rarr; order)
   * &rarr; &alpha; list * &alpha; list &rarr; order".
   *
   * <p>"collate f (l1, l2)" performs lexicographic comparison of the two lists
   * using the given ordering f on the list elements.
   */
  LIST_COLLATE("List.collate", ts -> {
    final Type order = INT; // TODO:
    return ts.forallType(1, h ->
        ts.fnType(ts.fnType(ts.tupleType(h.get(0), h.get(0)), order),
            ts.tupleType(h.list(0), h.list(0)),
            order));
  }),

  /** Function "Relational.count", aka "count", of type "int list &rarr; int".
   *
   * <p>Often used with {@code group}:
   *
   * <blockquote>
   *   <pre>
   *     from e in emps
   *     group (#deptno e) as deptno
   *       compute sum of (#id e) as sumId
   *   </pre>
   * </blockquote>
   */
  RELATIONAL_COUNT("Relational.count", "count", ts ->
      ts.forallType(1, h -> ts.fnType(h.list(0), INT))),

  /** Function "Relational.sum", aka "sum", of type "int list &rarr; int".
   *
   * <p>Often used with {@code group}:
   *
   * <blockquote>
   *   <pre>
   *     from e in emps
   *     group (#deptno e) as deptno
   *       compute sum of (#id e) as sumId
   *   </pre>
   * </blockquote>
   */
  RELATIONAL_SUM("Relational.sum", "sum", ts ->
      ts.fnType(ts.listType(INT), INT)),

  /** Function "Sys.env", aka "env", of type "unit &rarr; string list". */
  SYS_ENV("Sys.env", "env", ts ->
      ts.fnType(UNIT, ts.listType(ts.tupleType(STRING, STRING))));

### Differences between Morel and SML

In Standard ML but not in Morel:
* `word` constant
* `longid` identifier
* type annotations ("`:` *typ*") (appears in expressions, patterns, and *funmatch*)
* `longid` identifier
* exceptions (`raise`, `handle`, `exception`)
* `while` loop
* `as` (layered patterns)
* data type replication (`type`)
* `withtype` in `datatype` declaration
* abstract type (`abstype`)
* modules (`structure` and `signature`)
* local declarations (`local`)
* operator declarations (`nonfix`, `infix`, `infixr`)
* `open`

In Morel but not Standard ML:
* `from` expression
* "*lab* `=`" is optional in `exprow`
