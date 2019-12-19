package com.example.ernaehrungstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.historie:
                Toast.makeText(MainActivity.this, "Historie ausgewählt", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gewichtSpeichern:
                Toast.makeText(MainActivity.this, "Gewicht speichern ausgewählt", Toast.LENGTH_SHORT).show();
                break;
            case R.id.gerichteBearbeiten:
                Toast.makeText(MainActivity.this, "Gerichte bearbeiten ausgewählt", Toast.LENGTH_SHORT).show();
                break;
            case R.id.einstellungen:
                Toast.makeText(MainActivity.this, "Einstellungen ausgewählt", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
