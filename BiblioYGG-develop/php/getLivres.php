<?php

require 'BD.inc.php';


if (isset($_POST['request']) && $_POST['request'] == "livres") {
    $sql = "SELECT * from livres;";
    $stmt = $conn->prepare($sql);
    $stmt->execute();

    $livres = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($livres);
} elseif (isset($_POST['request']) && $_POST['request'] == "email") {
    $sql = "SELECT * from livres where email = :email;";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':email' => $_POST['email']));

    $livres = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($livres);
} elseif (isset($_POST['request']) && $_POST['request'] == "primary") {
    $sql = "SELECT * from livres where email = :email AND nom = :nom;";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':email' => $_POST['email'], ':nom' => $_POST['nom']));

    $livres = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($livres);
} else {
    echo "error";
}


$conn = null;
