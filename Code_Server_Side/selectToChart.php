<?php
 include_once './dbConnect.php';
 
 function dispInfo(){
   $db = new dbConnect();
$a = $_POST["start"];
$b = $_POST["end"];
  // Mảng JSON
  $mobileArray=array();
  
  
   $result = mysql_query("SELECT Date,Temperature,Humidity FROM nongnghiep WHERE Date Between ('$a') and ('$b') order by Date");
  #$result = mysql_query("SELECT Temperature,Humidity FROM nongnghiep");
   while($row = mysql_fetch_row($result)){
  //Đọc dữ liệu từ MySQL
  $dat = $row[0];
	$tem = $row[1];
	$hum =$row[2];
	
    // Mảng JSON
	array_push($mobileArray,new Mobile($dat,$tem,$hum));
   }
	
 
 // Thiết lập header là JSON
 header('Content-Type: application/json');
 
 // Hiển thị kết quả
 echo json_encode($mobileArray);
}
 class Mobile{
	 var $date;
		var $temp;
		var $humid;
		function Mobile($d,$i,$n){
			$this->date=$d;
			$this->temp=$i;
			$this->humid=$n;
		}
	}
dispInfo();
?>