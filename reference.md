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
