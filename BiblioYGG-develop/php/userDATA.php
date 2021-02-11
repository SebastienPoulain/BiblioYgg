<?php

require 'BD.inc.php';


$sql = "INSERT INTO utilisateurs(email, password, nom, prenom, tel) values(:email, :password, :nom, :prenom, :tel);";
$stmt = $conn->prepare($sql);
$stmt->execute(array(':email' => $_POST['email'], ':password' => $_POST['password'], ':nom' => $_POST['nom'], ':prenom' => $_POST['prenom'], ':tel' => $_POST['tel']));

$conn = null;
