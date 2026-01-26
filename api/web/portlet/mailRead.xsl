<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"/>
	
	<xsl:template match="mail-content">
		<html>
			<head>
				<title><xsl:value-of select="subject"/></title>
			</head>
			<body>
				<table border="1" width="100%">
				<tr>
					<th>
						folder
					</th>
					<td>
						<xsl:value-of select="folder"/>
					</td>
				</tr>
				<tr>
					<th>
						sentDate
					</th>
					<td>
						<xsl:value-of select="sentDate"/>
					</td>
				</tr>
				<tr>
					<th>
						from
					</th>
					<td>
						<xsl:value-of select="from/personal"/> (<xsl:value-of select="from/address"/>)
					</td>
				</tr>
				<tr>
					<th>
						to
					</th>
					<td>
						<xsl:for-each select="to/email">
							<xsl:value-of select="personal"/> (<xsl:value-of select="address"/>),
						</xsl:for-each>
					</td>
				</tr>
				<tr>
					<th>
						cc
					</th>
					<td>
						<xsl:for-each select="cc/email">
							<xsl:value-of select="personal"/> (<xsl:value-of select="address"/>),
						</xsl:for-each>
					</td>
				</tr>
				<tr>
					<th>
						contents
					</th>
					<td>
						<xsl:value-of select="content"/>
					</td>
				</tr>
				</table>
			</body>
		</html>
	</xsl:template>
	
</xsl:stylesheet>