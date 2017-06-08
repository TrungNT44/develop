package apt.tutorial.connectmysqldb;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class HelpActivity extends Activity {
    Button btnHDSD,btnCanhBao,btnThongTin,btnCauHoi,btnTacGia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help);
        addUIElement();
        btnHDSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,HuongDanSuDungActivity.class);
                startActivity(intent);
            }
        });
        btnCanhBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,NewsActivity.class);
                startActivity(intent);

            }
        });
        btnTacGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,AuthorActivity.class);
                startActivity(intent);
            }
        });
    }
    public void addUIElement(){
        btnHDSD = (Button) findViewById(R.id.btnHDSD);
        btnCanhBao= (Button) findViewById(R.id.btnCanhBao);
        btnThongTin= (Button) findViewById(R.id.btnTTHuuIch);
        btnCauHoi= (Button) findViewById(R.id.btnCauHoi);
        btnTacGia= (Button) findViewById(R.id.btnTacGia);
    }
}
