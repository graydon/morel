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

### Not supported
* identifiers: longid
* expressions: type annotation,
  `raise` (exception raising),
  `handle` (exception handling),
  `while` (iteration)
* patterns: type annotation,
  `as` (layered patterns)

### Constants

<pre>
con    ::= int                 // integer
           word                // word
           float               // floating point
           char                // character
           string              // string
int	   ::= ⟨~⟩num              // decimal
           ⟨~⟩0xhex            // hexadecimal
word   ::= 0wnum               // decimal
           0wxhex              // hexadecimal
float  ::= ⟨~⟩num.num          // floating point
           ⟨~⟩num⟨.num⟩e⟨~⟩num // scientific
char   ::= #"ascii"            // character
string ::= "⟨ascii⟩*"          // string
num    ::= ⟨digit⟩+            // number
hex    ::= ⟨digit | letter⟩+   // hexadecimal number (letters may
                               // only be in the range A-F)
ascii  ::= ...                 // single non-" ASCII character or
                               // \-headed escape sequence
</pre>

## Identifiers

<pre>
id     ::= letter⟨letter | digit | ' | _⟩* // alphanumeric
           ⟨! | % | & | $ | # | +
           | - | / | : | < | = | >
           | ? | @ | \ | ~ | ` | ^
           | | | *⟩+           // symbolic (not allowed for type
                               // variables or module language
                               // identifiers)
var    ::= '⟨letter | digit | ' | _⟩* // unconstrained
            ''⟨letter | digit | ' | _⟩* // equality
lab    ::= id                  // identifier
           num                 // number (may not start with 0)
</pre>

### Expressions

exp    ::= con            // constant
           ⟨op⟩ id        // value or constructor identifier
           exp1 exp2      // application
           exp1 id exp2   // infix application
           ( exp )        // parentheses
           ( exp1 , ... , expn ) // tuple (n ≠ 1)
           { ⟨exprow⟩ }   // record
           # lab          // record selector
           [ exp1 , ... , expn ] // list (n ≥ 0)
           ( exp1 ; ... ; expn ) // sequence (n ≥ 2)
           let dec in exp1 ; ... ; expn end // local declaration (n ≥ 1)
           exp1 andalso exp2 // conjunction
           exp1 orelse exp2 // disjunction
           if exp1 then exp2 else exp3 // conditional
           case exp of match // case analysis
           fn match       // function
exprow ::= ⟨lab =⟩ exp ⟨, exprow⟩ // expression row
match  ::= pat => exp ⟨| match⟩ // match
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


