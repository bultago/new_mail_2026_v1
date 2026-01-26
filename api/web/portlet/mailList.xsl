<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"/>
	
	<xsl:template match="mailbox">
		<html>
			<head>
				<title><xsl:value-of select="folderName"/></title>
			</head>
			<body>
				<table border="1" width="100%">
				<xsl:for-each select="messageList/message">
					<tr>
						<th>msgUid</th>
						<th>subject</th>
						<th>personName</th>
						<th>personEmail</th>
						<th>sentDate</th>
						<th>size</th>
					</tr>
					<tr>
						<td><xsl:value-of select="msgUid"/></td>
						<td><xsl:value-of select="subject"/></td>
						<td><xsl:value-of select="personName"/></td>
						<td><xsl:value-of select="personEmail"/></td>
						<td><xsl:value-of select="sentDate"/></td>
						<td><xsl:value-of select="size"/></td>
					</tr>
				</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
	
</xsl:stylesheet>