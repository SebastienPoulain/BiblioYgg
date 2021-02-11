<?php

require 'BD.inc.php';

if (isset($_POST['request']) && $_POST['request'] == "add") {
  $sql = "INSERT INTO livres(nom, prix, auteur, cat, description, stat, email, imgpath) values(:nom, :prix, :auteur, :cat, :description, :stat, :email, :imgpath);";
  $stmt = $conn->prepare($sql);
  $stmt->execute(array(':nom' => $_POST['nom'], ':prix' => $_POST['prix'], ':auteur' => $_POST['auteur'], ':cat' => $_POST['cat'], ':description' => $_POST['description'], ':stat' => $_POST['stat'], ':email' => $_POST['email'], ':imgpath' => $_POST['imgpath']));
} elseif (isset($_POST['request']) && $_POST['request'] == "update") {
  $sql = "UPDATE livres set nom = :nom, prix = :prix, auteur = :auteur, cat = :cat, description = :description, stat = :stat where email = :email and nom = :title";
  $stmt = $conn->prepare($sql);
  $stmt->execute(array(':nom' => $_POST['nom'], ':prix' => $_POST['prix'], ':auteur' => $_POST['auteur'], ':cat' => $_POST['cat'], ':description' => $_POST['description'], ':stat' => $_POST['stat'], ':email' => $_POST['email'], ':title' => $_POST['title']));
} elseif (isset($_POST['request']) && $_POST['request'] == "delete") {
  $sql = "DELETE FROM livres where email = :email and nom = :title";
  $stmt = $conn->prepare($sql);
  $stmt->execute(array(':email' => $_POST['email'], ':title' => $_POST['title']));
} else {
  echo "error";
}

$conn = null;