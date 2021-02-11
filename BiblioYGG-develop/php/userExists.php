<?php

require 'BD.inc.php';

if (isset($_POST['email'])) {
    $sql = "SELECT * from utilisateurs where email = lower(:email);";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':email' => $_POST['email']));
    $ctr = $stmt->fetchColumn();

    if ($ctr == 0) {
        echo json_encode(array('response' => 'false'));
    } else {
        echo "true";
    }
} else {
    echo "error";
}


$conn = null;
