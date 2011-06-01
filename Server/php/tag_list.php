<?php
include ("connection.php");

// Parameters
$req_latitude = $_GET["latitude"];
if (!$req_latitude) 
	$req_latitude = 37.558900;
$req_longitude = $_GET["longitude"];
if (!$req_longitude)
 	$req_longitude= 126.334580;


// Fetch Thread List
$query = "SELECT * FROM tags WHERE 1=1 ";
$query.= " LIMIT 13";

//echo $query;
$result = mysql_query($query, $connect) or die(" : ".mysql_error());

// XML Output
header("Content-Type: text/xml;");
$writer = new XMLWriter();
$writer->openURI('php://output');
$writer->startDocument('1.0','UTF-8');
$writer->setIndent(4);

$writer->startElement('TAGS');
$writer->startElement('CLOUD');
$writer->writeAttribute('name', "인기 태그");
while($row=mysql_fetch_array($result))
{
	$writer->startElement('TAG');
	$writer->writeAttribute('tag_srl', $row[tag_srl]);
	$writer->writeAttribute('name', $row[name]);
	$writer->writeAttribute('color', $row[color]);
	$writer->writeAttribute('size', $row[size]);
	$writer->writeAttribute('score_day', $row[score_day]);
	$writer->writeAttribute('score_week', $row[score_week]);
	$writer->writeAttribute('score_month', $row[score_month]);
	$writer->writeAttribute('score_year', $row[score_year]);
	$writer->endElement();
}
$writer->endElement();

// 1000km 이내에 있는 쿼리들 중 가까운 순서대로 가져온다
// Geo Location
$MAX_DISTANCE = 100.0; //(in Km)

// List Query 
$query = "SELECT DISTINCT tags.* FROM (SELECT tag_srl FROM document_tags, (SELECT document_srl, TRUNCATE (GeoDistance(latitude, longitude, $req_latitude, $req_longitude), 3) AS distance_km FROM documents WHERE GeoDistance(latitude, longitude, $req_latitude, $req_longitude) < 1000 ORDER BY distance_km ASC) AS tbl_a ";
$query.= "WHERE document_tags.document_srl = tbl_a.document_srl) AS tbl_b, tags WHERE tbl_b.tag_srl = tags.tag_srl LIMIT 13";

//$query = "SELECT tags.* FROM tags, (SELECT DISTINCT tag_srl FROM document_tags LEFT OUTER JOIN (SELECT document_srl, TRUNCATE(GeoDistance(latitude, longitude, $req_latitude, $req_longitude), 3) AS distance_km FROM documents ";
//$query.= "WHERE GeoDistance(latitude, longitude, $req_latitude, $req_longitude) < $MAX_DISTANCE) AS tbl_nearest ON document_tags.document_srl = tbl_nearest.document_srl ORDER BY distance_km ASC) AS tbl_nearest_tag_srls WHERE tags.tag_srl = tbl_nearest_tag_srls.tag_srl LIMIT 13" ;
$result = mysql_query($query, $connect) or die(" : ".mysql_error());

$writer->startElement('CLOUD');
$writer->writeAttribute('name', "근처에서 올라온 태그");
$writer->writeAttribute('latitude', $req_latitude);
$writer->writeAttribute('longitude', $req_longitude);
$writer->writeAttribute('max_distance', $MAX_DISTANCE);
//$writer->writeAttribute('query', $query);

while($row=mysql_fetch_array($result))
{
	$writer->startElement('TAG');
	$writer->writeAttribute('tag_srl', $row[tag_srl]);
	$writer->writeAttribute('name', $row[name]);
	$writer->writeAttribute('color', $row[color]);
	$writer->writeAttribute('size', $row[size]);
	$writer->writeAttribute('score_day', $row[score_day]);
	$writer->writeAttribute('score_week', $row[score_week]);
	$writer->writeAttribute('score_month', $row[score_month]);
	$writer->writeAttribute('score_year', $row[score_year]);
	$writer->endElement();
}
$writer->endElement();
$writer->endElement();
$writer->endDocument();
$writer->flush();
?>