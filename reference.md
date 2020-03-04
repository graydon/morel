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
<img align="right" alt="Morel mushroom (credit: OldDesignShop.com)" src="etc/morel.jpg">

# Morel language reference

## Grammar

### Constants

<pre>
<i>con</i>: <i>int</i>              integer
   | <i>float</i>            floating point
   | <i>char</i>             character
   | <i>string</i>           string
<i>int</i>: ['~']<i>num</i>         decimal
   | ['~']'0x'<i>hex</i>     hexadecimal
<i>word</i>: 0wnum           decimal
   | '0wx'<i>hex</i>            hexadecimal
<i>float</i>: ['~'] <i>num</i> '.' <i>num</i>      floating point
   | ['~'] <i>num</i> ['.' <i>num</i>] 'e' ['~'] <i>num</i>   scientific
<i>char</i>: '#"' <i>ascii</i> '"'      character
<i>string</i>: '"' <i>ascii</i>* '"'       string
<i>num</i>: <i>digit</i> <i>digit</i>*             number
<i>hex</i>: (<i>digit</i> | <i>letter</i>) (<i>digit</i> | <i>letter</i>)* hexadecimal number
                               (letters may only be in the range A-F)
<i>ascii</i>: ...              single non-" ASCII character or
                               \-headed escape sequence
</pre>

## Identifiers

<pre>
<i>id</i>:   <i>letter</i> (<i>letter</i> | <i>digit</i> | ''' | '_')*
                         alphanumeric
    | ('!' | '%' | '&' | '$' | '#' | '+' | '-'
      | '/' | ':' | '<' | '=' | '>' | '?' | '@'
      | '\' | '~' | '`' | '^' | '|' | '*')+
                         symbolic (not allowed for type variables
                         or module language identifiers)
<i>var</i>:  ''' (<i>letter</i> | <i>digit</i> | ''' | '_')*
                         unconstrained
      '''' (<i>letter</i> | <i>digit</i> | ''' | '_'⟩*
                         equality
<i>lab</i>:  <i>id</i>                 identifier
      <i>num</i>                number (may not start with 0)
</pre>

### Expressions

<pre>
<i>exp</i>: <i>con</i>            constant
    | [ <b>op</b> ] <i>id</i>     value or constructor identifier
    | <i>exp<sub>1</sub></i> <i>exp<sub>2</sub></i>      application
    | <i>exp<sub>1</sub></i> <i>id</i> <i>exp<sub>2</sub></i>   infix application
    | '(' <i>exp</i> ')'        parentheses
    | '(' <i>exp<sub>1</sub></i> , ... , <i>exp<sub>n</sub></i> ')' tuple (n &ne; 1)
    | '{' <i>expRow</i> '}'   record
    | '#' <i>lab</i>          record selector
    | '[' <i>exp<sub>1</sub></i> , ... , <i>exp<sub>n</sub></i> ']' list (n &ge; 0)
    | '(' <i>exp<sub>1</sub></i> ; ... ; <i>exp<sub>n</sub></i> ')' sequence (n &ge; 2)
    | <b>let</b> <i>dec</i> <b>in</b> <i>exp<sub>1</sub></i> ; ... ; <i>exp<sub>n</sub></i> <b>end</b> local declaration (n ≥ 1)
    | <i>exp<sub>1</sub></i> <b>andalso</b> <i>exp<sub>2</sub></i> conjunction
    | <i>exp<sub>1</sub></i> <b>orelse</b> <i>exp<sub>2</sub></i> disjunction
    | <b>if</b> <i>exp<sub>1</sub></i> <b>then</b> <i>exp<sub>2</sub></i> <b>else</b> <i>exp<sub>3</sub></i>  conditional
    | <b>case</b> <i>exp</i> <b>of</b> <i>match</i>   case analysis
    | <b>fn</b> <i>match</i>       function
<i>expRow</i>: <i>expRowItem</i> [, <i>exprRowItem</i> ]*  expression row
<i>expRowItem</i>: [<i>lab</i> '='] <i>exp</i>
<i>match</i>: <i>matchItem</i> [ '|' <i>matchItem</i> ]*  match
<i>matchItem<i>: <i>pat</i> '=>' <i>exp</i>
</pre>

### Patterns

<pre>
pat     ::= con           // constant
            _             // wildcard
           ⟨op⟩ id        // variable
           ⟨op⟩ id ⟨pat⟩  // construction
           pat1 id pat2   // infix construction
           ( pat )        // parentheses
           ( pat1 , ... , patn ) // tuple (n ≠ 1)
           { ⟨patrow⟩ }   // record
           [ pat1 , ... , patn ] // list (n ≥ 0)
patrow ::= ...            // wildcard
           lab = pat ⟨, patrow⟩ // pattern
           id ⟨: typ⟩ ⟨as pat⟩ ⟨, patrow⟩ // variable
</pre>

### Types

<pre>
typ     ::= var           // variable
            ⟨typ⟩(,) id   // constructor
            ( typ )       // parentheses
            typ1 -> typ2  // function
            typ1 * ... * typn // tuple (n ≥ 2)
            { ⟨typrow⟩ }  // record
typrow  ::= lab : typ ⟨, typrow⟩ // type row
</pre>

### Declarations

not supported: `type`, `withtype` in `datatype`, data type replication, `abstype` (abstract type)

<pre>
dec     ::= val ⟨var⟩(,) valbind // value
            fun ⟨var⟩(,) funbind // function
            type typbind  // type
            datatype datbind // data type
abstype datbind ⟨withtype typbind⟩ with dec end abstract type
exception exnbind       exception
structure strbind       structure (not allowed inside expressions)
empty
dec1 ⟨;⟩ dec2   sequence
local dec1 in dec2 end  local
open longid1 ... longidn        inclusion (n ≥ 1)
nonfix id1 ... idn      nonfix (n ≥ 1)
infix ⟨digit⟩ id1 ... idn       left-associative infix (n ≥ 1)
infixr ⟨digit⟩ id1 ... idn      right-associative infix (n ≥ 1)
valbind ::=     pat = exp ⟨and valbind⟩ destructuring
rec valbind     recursive
funbind ::=     funmatch ⟨and funbind⟩  clausal function
funmatch        ::=     ⟨op⟩ id pat1 ... patn ⟨: typ⟩ = exp ⟨| funmatch⟩        nonfix (n ≥ 1)
pat1 id pat2 ⟨: typ⟩ = exp ⟨| funmatch⟩ infix
( pat1 id pat2 ) pat'1 ... pat'n ⟨: typ⟩ = exp ⟨| funmatch⟩     infix (n ≥ 0)
typbind ::=     ⟨var⟩(,) id = typ ⟨and typbind⟩ abbreviation
datbind ::=     ⟨var⟩(,) id = conbind ⟨and datbind⟩     data type
conbind ::=     id ⟨of typ⟩ ⟨| conbind⟩         data constructor
exnbind         ::=     id ⟨of typ⟩ ⟨and exnbind⟩       generative
id = longid ⟨and exnbind⟩       renaming


### Not supported
* constants: word
* identifiers: longid
* expressions: type annotation,
  `raise` (exception raising),
  `handle` (exception handling),
  `while` (iteration)
* patterns: type annotation,
  `as` (layered patterns)

