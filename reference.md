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
