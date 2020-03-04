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

<table>
<tr><td>con</td><td>::=</td><td>int</td><td>integer</td></tr>
<tr><td>word</td><td>word</td></tr>
<tr><td>float</td><td>floating</td><td>point</td></tr>
<tr><td>char</td><td>character</td></tr>
<tr><td>string</td><td>string</td></tr>
<tr><td>int</td><td>::=</td><td>⟨~⟩num</td><td>decimal</td></tr>
<tr><td>⟨~⟩0xhex</td><td>hexadecimal</td></tr>
<tr><td>word</td><td>::=</td><td>0wnum</td><td>decimal</td></tr>
<tr><td>0wxhex</td><td>hexadecimal</td></tr>
<tr><td>float</td><td>::=</td><td>⟨~⟩num.num</td><td>floating</td><td>point</td></tr>
<tr><td>⟨~⟩num⟨.num⟩e⟨~⟩num</td><td>scientific</td></tr>
<tr><td>char</td><td>::=</td><td>#"ascii"</td><td>character</td></tr>
<tr><td>string</td><td>::=</td><td>"⟨ascii⟩*"</td><td>string</td></tr>
<tr><td>num</td><td>::=</td><td>⟨digit⟩+</td><td>number</td></tr>
<tr><td>hex</td><td>::=</td><td>⟨digit | letter⟩+</td><td>hexadecimal number (letters may only be in the range A-F)</td></tr>
<tr><td>ascii</td><td>::=</td><td>...</td><td>single non-" ASCII character or \-headed escape sequence</td></tr>
</table>