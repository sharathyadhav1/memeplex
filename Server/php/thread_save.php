<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<?php
include ("connection.php");

// Parameters
$req_document_srl = $_POST["document_srl"];
$req_device_id = $_POST["device_id"];
$req_tag_srl_list = $_POST["tag_srl_list"];
$req_nick_name = $_POST["nick_name"];
$req_picture_path = $_POST["picture_path"];
$req_audio_path = $_POST["audio_path"];
$req_latitude = $_POST["latitude"];
$req_longitude = $_POST["longitude"];
$req_content = $_POST["content"];

// File Upload
$is_error = false;
if($_FILES["picture"]["name"] && $_FILES["picture"]["error"] > 0)
{
	$is_error = true;
}
else
{
	$tmp_name = $_FILES["picture"]["tmp_name"][$key];
	$name = $_FILES["picture"]["name"][$key];

	if (is_uploaded_file($_FILES['picture']['tmp_name'])) // 파일이 성공적으로 업로드 되었을 경우 
	{
		$savepath = "/Users/memeplex/Sites/userdata/";
		chdir($savepath);

		$next_increment 	= 0;
		$qShowStatus 		= "SHOW TABLE STATUS LIKE 'documents'";
		$qShowStatusResult 	= mysql_query($qShowStatus) or $is_error = true;

		$row = mysql_fetch_assoc($qShowStatusResult);
		$next_increment = $row['Auto_increment'];

		//echo "File Upload Succeeded<br/>";
		if (!is_dir($next_increment)) {
			mkdir($next_increment,0777);
		}

		$savepath.= $next_increment."/"; // Savepath - /userdata/next_increment

		move_uploaded_file($_FILES["picture"]["tmp_name"], $savepath.$_FILES["picture"]["name"]); // 임시폴더에 저장된 파일을 뒤의 인자 경로로 옮긴다.
		//echo "Uploaded file : ".$savepath.$_FILES["picture"]["name"];
		$picture_path = "http://memeplex.ohmyenglish.co.kr/userdata/".$next_increment."/".$_FILES["picture"]["name"];
	}
}

// DB Handling
if ($req_document_srl) // Modify
{
	$query = "UPDATE documents SET nick_name='$req_nick_name', picture_path='$picture_path', audio_path='$audio_path', ";
	$query.= "location='$req_latitude,$req_longitude', ";
	$query.= "device_id ='$req_device_id', ";
	$query.= "content='$req_content' ";
	$query.= "WHERE (document_srl = '$req_document_srl')";
	$result_update = mysql_query($query, $connect) or die("error");

	// delete relations first (will be added again)
	$query = "DELETE FROM document_tags WHERE document_srl = $req_document_srl";
	$result_delete = mysql_query($query, $connect) or die("error");

	// document_srls table
	$array_srl_list = explode(",", $req_tag_srl_list);
	foreach ($array_srl_list as $key=>$val) {
		$query = "INSERT INTO document_tags (document_srl, tag_srl) ";
		$query .= "VALUES (".$req_document_srl.", $val)";
		$result_insert = mysql_query($query, $connect) or die("error");
	}
}
else // New
{
	// document table
	$query = "INSERT INTO documents (nick_name, picture_path, audio_path, location, device_id, content) ";
	$query .= "VALUES ('$req_nick_name', '$picture_path', '$audio_path', '$req_latitude,$req_longitude', '$req_device_id', '$req_comment')";
	$result_insert = mysql_query($query, $connect) or die("error");
	$inserted_document_srl = mysql_insert_id();

	// document_srls table
	$array_srl_list = explode(",", $req_tag_srl_list);
	foreach ($array_srl_list as $key=>$val) {
		$query = "INSERT INTO document_tags (document_srl, tag_srl) ";
		$query .= "VALUES (".$inserted_document_srl.", $val)";
		$result_insert = mysql_query($query, $connect) or die("error");
	}
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