<?php
 include_once './dbConnect.php';
 
 function dispInfo(){
   $db = new dbConnect();
	$a = $_POST["so1"];
  // Mảng JSON
  $response = array();
  $response["mobile"] = array();
  $mobileArray=array();
  // Câu lệnh Select dùng để xem dữ liệu
  $result = mysql_query("SELECT * FROM nongnghiep where Date = ('$a')");

  //Đọc dữ liệu từ MySQL
  while($row = mysql_fetch_array($result)){
	$dat = $row[1];
    $temp = $row[2];
    $humid = $row[3];
	$stt = $row[4];
	
    // Mảng JSON
    array_push($mobileArray,new Mobile($dat,$temp,$humid,$stt));
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
		var $stt;
		function Mobile($d,$i,$n,$p){
			$this->date=$d;
			$this->temp=$i;
			$this->humid=$n;
			$this->stt=$p;
		}
	}
dispInfo();
?>