package com.borjalapa.climb;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class Main_Page extends AppCompatActivity {

    Toolbar tbBarraTareas;
    Drawer mDrawer;

    final String correo = "";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mAuth = FirebaseAuth.getInstance();


        //Toolbar
        tbBarraTareas = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tbBarraTareas);

        //Añadir MaterialDrawer totalmente vacío
        new DrawerBuilder().withActivity(Main_Page.this).build();
        hacer_materialdrawer();
    }


    public void hacer_materialdrawer(){
        FirebaseUser usuario = mAuth.getCurrentUser();
        String correo = usuario.getEmail();
        Log.i("correo", correo);

        //builder de la cabecera del materialdrawer
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(Main_Page.this)
                .withHeaderBackground(R.mipmap.ic_launcher)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withEmail(correo)
                                .withIcon(getResources().getDrawable(R.mipmap.ic_launcher_round))
                )
                .build();


        //Elementos del materialdrawer
        mDrawer = new DrawerBuilder()
                .withActivity(Main_Page.this)
                .withAccountHeader(headerResult)
                .withToolbar(tbBarraTareas)
                .withActionBarDrawerToggle(true)
                .withDrawerGravity(Gravity.START)
                .withSliderBackgroundColor(getResources().getColor(android.R.color.darker_gray))
                .withSelectedItem(2)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(1)
                                .withName("Opcion 1")
                                .withIcon(android.R.drawable.btn_star_big_on),
                        new PrimaryDrawerItem()
                                .withIdentifier(2)
                                .withName("Opcion 2")
                                .withIcon(android.R.drawable.arrow_down_float),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(3)
                                .withName("Cerrar Menu")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1: {
                                Toast.makeText(Main_Page.this, "Opcion 1 pulsada", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 2: {
                                Toast.makeText(Main_Page.this, "Opcion 2 pulsada", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case 3: {
                                Toast.makeText(Main_Page.this, "Cerrar Menú", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        return false;
                    }
                }).build();
    }



}

