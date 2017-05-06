<?php
 include_once './dbConnect.php';
 
 function dispInfo(){
   $db = new dbConnect();
 
  // Mảng JSON
  $NongNghiepArray=array();

 
  //Đọc dữ liệu từ MySQL
	$date=date("Y-m-d");
    $temp = rand(20,38);
    $humid = rand(40,90)." %";
	$bool = rand(0,1);
	if($bool == 0) $stt ="troi mua";
	else $stt="troi nang";
	
    // Mảng JSON
    array_push($NongNghiepArray,new NongNghiep($date,$temp,$humid,$stt));

 
	
 
 // Thiết lập header là JSON
 header('Content-Type: application/json');
 
 // Hiển thị kết quả
 echo json_encode($NongNghiepArray);
}
 class NongNghiep{
	 var $date;
		var $temp;
		var $humid;
		var $stt;
		function NongNghiep($d,$i,$n,$p){
			$this->date=$d;
			$this->temp=$i;
			$this->humid=$n;
			$this->stt=$p;
		}
	}
dispInfo();
?>