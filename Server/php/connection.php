<?php
// Connection
$connect = mysql_connect("localhost","memeplex","memeplex") or die("DB  : ".mysql_error());
mysql_select_db("memeplex") or die("DB  : ".mysql_error());

$query = "SET NAMES utf8;";
$result = mysql_query($query, $connect) or die(" : ".mysql_error());

?>