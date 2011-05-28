<?php
include ("connection.php");

// Parameters
$req_document_srl = $_GET["document_srl"];
$req_device_id = $_GET["device_id"];
$req_nick_name = $_GET["nick_name"];
$req_latitude = $_GET["latitude"];
$req_longitude = $_GET["longitude"];
$req_content = $_GET["content"];

// File Upload
$is_error = false;

// DB Handling
if ($req_comment_srl) // Modify
{
	$query = "UPDATE comments SET nick_name='$req_nick_name', " ;
	$query.= "latitude='$req_latitude', ";
	$query.= "longitude='$req_longitude', ";
	$query.= "device_id ='$req_device_id', ";
	$query.= "content='$req_content' ";
	$query.= "WHERE (comment_srl = '$req_comment_srl')";
	$result_update = mysql_query($query, $connect) or die("error");
}
else // New
{
	// document table
	$query = "INSERT INTO comments (document_srl, nick_name, latitude, longitude, device_id, content) ";
	$query .= "VALUES ('$req_document_srl', '$req_nick_name', '$req_latitude', '$req_longitude', '$req_device_id', '$req_content')";
	$result_insert = mysql_query($query, $connect) or die("error");
}

// XML Output
header("Content-Type: text/xml;");
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