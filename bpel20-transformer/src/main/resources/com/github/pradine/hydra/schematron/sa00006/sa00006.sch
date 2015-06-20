<!--
  #%L
  bpel20-transformer
  %%
  Copyright (C) 2015 the original author or authors.
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  -->
<sch:pattern xmlns:sch="http://purl.oclc.org/dsdl/schematron"
	id="SA00006">
	<sch:title>SA00006</sch:title>
	<sch:rule context="bpel:rethrow">
		<sch:assert test="ancestor::bpel:catch|ancestor::bpel:catchAll">
			The &lt;rethrow&gt; activity MUST
			only be
			used within a
			faultHandler (i.e. &lt;catch&gt; and
			&lt;catchAll&gt;
			elements).
		</sch:assert>
	</sch:rule>
</sch:pattern>