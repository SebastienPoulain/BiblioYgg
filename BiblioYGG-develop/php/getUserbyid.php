<?php

require 'BD.inc.php';

if (isset($_POST['id'])) {
    $sql = "SELECT * from utilisateurs where id = :id;";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(':id' => $_POST['id']));
    $user = $stmt->fetch();

    echo json_encode($user);

} else {
    echo 'error';
}


$conn = null;
