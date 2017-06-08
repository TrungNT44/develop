package apt.tutorial.connectmysqldb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HuongDanSuDungActivity extends AppCompatActivity {
    TextView tvMotaChucNang,tvChucNangXemDL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huong_dan_su_dung);
        tvMotaChucNang = (TextView) findViewById(R.id.tvMoTaChucNang);
        tvChucNangXemDL = (TextView) findViewById(R.id.tvChucNangXemDL);

        String moTaChucNang = "Ứng dụng hỗ trợ giám sát các thông số môi trường trong nhà kính trồng rau";
        String chucNangXemDL = "Xem các thông số hiện tại của nhà kính hoặc xem dữ liệu đã lưu của các ngày trước";

        tvMotaChucNang.setText(moTaChucNang);
        tvChucNangXemDL.setText(chucNangXemDL);

    }
}
