<?php
include ("connection.php");

// Parameters
$req_device_id = $_GET["device_id"];
$req_document_srl = $_GET["document_srl"];

// Fetch Thread List
$query = "SELECT FROM comments WHERE 1=1 ";

// 자기가 남긴 코멘트 보기
if ($req_device_id) {
	$query.= " AND device_id = '".$req_device_id."'";
}

$query.= " AND document_srl = $req_document_srl";
$query.= " ORDER BY timestamp DESC";

//echo $query;
$result = mysql_query($query, $connect) or die(" : ".mysql_error());

// XML Output
$writer = new XMLWriter();
$writer->openURI('php://output');
$writer->startDocument('1.0','UTF-8');
$writer->setIndent(4);

$writer->startElement('THREADLIST');
while($row=mysql_fetch_array($result))
{
	$writer->startElement('THREAD');
	$writer->writeAttribute('document_srl', $row[document_srl]);
	$writer->writeAttribute('nick_name', $row[nick_name]);
	$writer->writeAttribute('latitude', $row[latitude]);
	$writer->writeAttribute('longitude', $row[longitude]);
	$writer->writeAttribute('timestamp', $row[timestamp]);
	$writer->writeAttribute('device_id', $row[device_id]);
	$writer->writeAttribute('content', $row[content]);
	$writer->endElement();
}
$writer->endElement();
$writer->endDocument();
$writer->flush();
?>