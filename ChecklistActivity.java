package com.example.ovbha;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChecklistActivity extends AppCompatActivity {

    private List<ChecklistItem> checklistItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        // Populate checklist with sample items
        checklistItems.add(new ChecklistItem("First Aid Kit"));
        checklistItems.add(new ChecklistItem("Flashlight"));
        checklistItems.add(new ChecklistItem("Water Bottles"));
        checklistItems.add(new ChecklistItem("Non-perishable Food"));
        checklistItems.add(new ChecklistItem("Blanket"));

        setupChecklist();
    }

    private void setupChecklist() {
        LinearLayout checklistLayout = findViewById(R.id.checklistLayout);
        for (ChecklistItem item : checklistItems) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item.getName());
            checkBox.setChecked(item.isChecked());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> item.setChecked(isChecked));
            checklistLayout.addView(checkBox);
        }
    }
}
class ChecklistItem {
    private String name;
    private boolean checked;

    public ChecklistItem(String name) {
        this.name = name;
        this.checked = false;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
