<?php
 include_once './dbConnect.php';
 
 function dispInfo(){
   $db = new dbConnect();
 $start = strtotime("25 March 2017");
 
//End point of our date range.
$end = strtotime("30 March 2017");
 $timestamp = mt_rand($start, $end);
//Custom range.
$timestamp = mt_rand($start, $end);
 
//Print it out.
$a= date("Y-m-d", $timestamp);
echo $a;
  // Mảng JSON
  $mobileArray=array();
   $result = mysql_query("SELECT * FROM nongnghiep where Date ='$a'");
   $row = mysql_fetch_row($result);
  //Đọc dữ liệu từ MySQL
	
    $temp = $row[2];
	$hum = $row[3];
	$stt = $row[4];
	
    // Mảng JSON
    array_push($mobileArray,new Mobile($temp,$hum,$stt));

 
	
 
 // Thiết lập header là JSON
 header('Content-Type: application/json');
 
 // Hiển thị kết quả
 echo json_encode($mobileArray);
}
 class Mobile{
		var $temp;
		var $humid;
		var $stt;
		function Mobile($i,$n,$p){
			$this->temp=$i;
			$this->humid=$n;
			$this->stt=$p;
		}
	}
dispInfo();
?>