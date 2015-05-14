<sch:pattern xmlns:sch="http://purl.oclc.org/dsdl/schematron" id="all">
	<sch:rule context="bpel:rethrow">
		<sch:assert test="ancestor::bpel:catch|ancestor::bpel:catchAll">
			SA00006: The &lt;rethrow&gt; activity MUST
			only be used within a
			faultHandler (i.e. &lt;catch&gt; and
			&lt;catchAll&gt; elements).
		</sch:assert>
	</sch:rule>
</sch:pattern>