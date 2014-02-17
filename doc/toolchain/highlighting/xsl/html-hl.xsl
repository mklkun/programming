<?xml version='1.0'?>
<!--
		DO NOT EDIT THIS FILE UNLESS YOU ARE IN THE DOCUMENTATION PROJECT

		This file is shared by all ProActive projects. If you have to modify it,
		please refer to the INSTALL file in the root of the Documentation project
		to know how to do it properly.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:d="http://docbook.org/ns/docbook"
	xmlns:xslthl="http://xslthl.sf.net"
	exclude-result-prefixes="xslthl d"
	version='1.0'>

<!-- ********************************************************************
     $Id: highlight.xsl 7266 2007-08-22 11:58:42Z xmldoc $
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://docbook.sf.net/release/xsl/current/ for
     and other information.

     ******************************************************************** -->

	<xsl:template match='xslthl:keyword'>
		<b class="hl-keyword"><i style="color: #0101ff"><xsl:apply-templates/></i></b>
	</xsl:template>

	<xsl:template match='xslthl:string'>
		<b class="hl-string"><i style="color: #ff2aff"><xsl:apply-templates/></i></b>
	</xsl:template>

	<xsl:template match='xslthl:comment'>
		<i class="hl-comment" style="color: #016101"><xsl:apply-templates/></i>
	</xsl:template>

	<xsl:template match='xslthl:tag'>
		<b class="hl-tag"><i style="color: #0101ff"><xsl:apply-templates/></i></b>
	</xsl:template>

	<xsl:template match='xslthl:attribute'>
		<span class="hl-attribute" style="color: #ff0101"><xsl:apply-templates/></span>
	</xsl:template>

	<xsl:template match='xslthl:value'>
		<span class="hl-value" style="color: #ff2aff"><xsl:apply-templates/></span>
	</xsl:template>

	<xsl:template match='xslthl:html'>
		<b><i style="color: #0101ff"><xsl:apply-templates/></i></b>
	</xsl:template>

	<xsl:template match='xslthl:xslt'>
		<b style="color: #aa00ff"><xsl:apply-templates/></b>
	</xsl:template>

	<xsl:template match='xslthl:section'>
		<b><xsl:apply-templates/></b>
	</xsl:template>

</xsl:stylesheet>
