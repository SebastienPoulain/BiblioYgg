<?php


$fichier_photo = basename($_FILES["photo"]["name"]);
$target = "img/" . $fichier_photo;
if (move_uploaded_file($_FILES["photo"]["tmp_name"], $target)) 
{
echo json_encode("Sauvegarde réussie" , JSON_UNESCAPED_UNICODE);
} 
else 
{
	echo json_encode("Problème lors de la sauvegarde" , JSON_UNESCAPED_UNICODE);
}      

