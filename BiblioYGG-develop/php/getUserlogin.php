<?php

require 'BD.inc.php';

if (isset($_POST['email']) && isset($_POST['password'])) {
    $sql = "SELECT * from utilisateurs where email = lower(:email) and password = :password;";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':email' => $_POST['email'], ':password' => $_POST['password']));
    $user = $stmt->fetch();

    echo json_encode($user);

} else {
    echo 'error';
}


$conn = null;
