<?php
include ("connection.php");

// Parameters
$req_device_id = $_GET["device_id"];
$req_document_srl = $_GET["document_srl"];

// Search Parameters
$req_tag_and_list = $_GET["and"];
$req_tag_or_list = $_GET["or"];
$req_tag_not_list = $_GET["not"];

// Fetch Thread List
$query = "SELECT documents.*, tag_srls FROM documents LEFT OUTER JOIN (SELECT document_srl, GROUP_CONCAT(tag_srl) tag_srls FROM document_tags GROUP BY document_srl) AS tbl_tag_srls ON documents.document_srl = tbl_tag_srls.document_srl WHERE 1=1 ";

// 자기가 남긴 글 보기
if ($req_device_id) {
	$query.= " AND device_id = '".$req_device_id."'";
}

// 글 하나만 보기
if ($req_document_srl) {
	$query.= " AND documents.document_srl = $req_document_srl";
}
$query.= " ORDER BY timestamp DESC";

//echo $query;
$result = mysql_query($query, $connect) or die(" : ".mysql_error());

// Not 배열
$array_name_list = explode(",", $req_tag_not_list);
$array_srl_not_list = array();
foreach ($array_name_list as $key=>$val) {
	$query_tag = "SELECT tag_srl FROM tags WHERE name='$val'";
	$result_tag = mysql_query($query_tag, $connect) or die("error".__LINE__);
	$row_tag = mysql_fetch_array($result_tag);
	$tag_srl = $row_tag[tag_srl];
	array_push($array_srl_not_list, $tag_srl);
}

// or 배열
$array_name_list = explode(",", $req_tag_or_list);
$array_srl_or_list = array();
foreach ($array_name_list as $key=>$val) {
	$query_tag = "SELECT tag_srl FROM tags WHERE name='$val'";
	$result_tag = mysql_query($query_tag, $connect) or die("error".__LINE__);
	$row_tag = mysql_fetch_array($result_tag);
	$tag_srl = $row_tag[tag_srl];
	array_push($array_srl_or_list, $tag_srl);
}

// and 배열
$array_name_list = explode(",", $req_tag_and_list);
$array_srl_and_list = array();
foreach ($array_name_list as $key=>$val) {
	$query_tag = "SELECT tag_srl FROM tags WHERE name='$val'";
	$result_tag = mysql_query($query_tag, $connect) or die("error".__LINE__);
	$row_tag = mysql_fetch_array($result_tag);
	$tag_srl = $row_tag[tag_srl];
	array_push($array_srl_and_list, $tag_srl);
}
//print_r($array_srl_not_list);
//print_r($array_srl_or_list);
//print_r($array_srl_and_list);

//// XML Output
header("Content-Type: text/xml;");
$writer = new XMLWriter();
$writer->openURI('php://output');
$writer->startDocument('1.0','UTF-8');
$writer->setIndent(4);

$writer->startElement('THREADLIST');

// and, or, not 조건에 따라 출력 여부를 결정
while($row=mysql_fetch_array($result))
{
	$is_ok = true;
	$array_srl_list = explode(",", $row[tag_srls]);
	
	if ($req_tag_or_list) {
		$is_ok = false;
		foreach ($array_srl_or_list as $key=>$val) {
			if (in_array($val, $array_srl_list)) {
				$is_ok = true;
				break; // 통과시킴
			}
		}
	}
	else if ($req_tag_and_list) { 
		$is_ok = true;
		foreach ($array_srl_and_list as $key=>$val) {
			if (!in_array($val, $array_srl_list)) {
				$is_ok = false; // 하나라도 존재 안하면 통과 안됨
			}
		}		
	}

	// Not 처리
	foreach ($array_srl_not_list as $key=>$val) {	
		// Not 리스트에 있으면 is_ok = false 가 됨
		if (in_array($val, $array_srl_list)) {
			$is_ok = false;
			break; 
		}
	}				
	
	if ($is_ok == false) 
		continue;
	
	$query_count = "SELECT count(*) AS comment_count FROM comments WHERE document_srl = ".$row[document_srl];
	$result_count = mysql_query($query_count, $connect) or die("counting error : ".mysql_error());
	$row_count = mysql_fetch_array($result_count);
	
	$writer->startElement('THREAD');
	$writer->writeAttribute('document_srl', $row[document_srl]);
	$writer->writeAttribute('nick_name', $row[nick_name]);
	$writer->writeAttribute('picture_path', $row[picture_path]);
	$writer->writeAttribute('audio_path', $row[audio_path]);
	$writer->writeAttribute('latitude', $row[latitude]);
	$writer->writeAttribute('longitude', $row[longitude]);
	$writer->writeAttribute('timestamp', strtotime("now")-strtotime($row[timestamp]));
	$writer->writeAttribute('device_id', $row[device_id]);
	$writer->writeAttribute('content', $row[content]);
	$writer->writeAttribute('comment_count', $row_count[comment_count]);
	$writer->writeAttribute('tag_srls', $row[tag_srls]);	
	$writer->endElement();
}
$writer->endElement();
$writer->endDocument();
$writer->flush();
?>