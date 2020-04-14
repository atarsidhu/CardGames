package theoatar.cardgames;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GoFishSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_fish_settings);

        Button btnDifficult = findViewById(R.id.btnDifficulty);
        btnDifficult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        DifficultyPopUp dialog = new DifficultyPopUp();
        dialog.show(getSupportFragmentManager(), "Dialog");
    }
}
