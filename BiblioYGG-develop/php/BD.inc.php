<?php
$servername = "localhost";
$username = "1719586";
$password = "1719586";
$dbname = "420505ri-equipe_1";

try
{
  $conn = new PDO("mysql:host=$servername;dbname=$dbname",$username,$password);
  $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
  $connState = "Connected successfully";
}

catch(PDOException $e)
{
  $connState = "Connection failed: " . $e->getMessage();
}
