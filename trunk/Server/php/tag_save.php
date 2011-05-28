<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<?php
include ("connection.php");

// Parameters
$req_tag_srl = $_GET["tag_srl"];
$req_tag_name = $_GET["tag_name"];
$req_tag_color = $_GET["tag_color"];
$req_tag_size = $_GET["tag_size"];

// DB Handling
if ($req_tag_srl) // Modify
{
	$query = "UPDATE tags SET name='$req_tag_name', color='$req_tag_color', size='$req_tag_size' ";
	$query.= "WHERE (tag_srl = '$req_tag_srl')";
	$result_update = mysql_query($query, $connect) or die("error");
}
else // New
{
	// tag table
	$query = "INSERT INTO tags (name, color, size) ";
	$query .= "VALUES ('$req_tag_name', '$req_tag_color', '$req_tag_size')";
	$result_insert = mysql_query($query, $connect) or die("error");
}

// XML Output
$writer = new XMLWriter();
$writer->openURI('php://output');
$writer->startDocument('1.0','UTF-8');
$writer->setIndent(4);
if ($is_error == false)
{
	$writer->startElement('RESPONSE');
	$writer->writeAttribute('result', 'success');
	$writer->endElement();
}
else
{
	$writer->startElement('RESPONSE');
	$writer->writeAttribute('result', 'fail');
	$writer->endElement();
}
$writer->endDocument();
$writer->flush();
?>