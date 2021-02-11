<?php

require 'BD.inc.php';

    $sql = "SELECT * from utilisateurs";
    $stmt = $conn->prepare($sql);
    $stmt->execute();

    $users = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($users);



$conn = null;
