package com.borjalapa.climb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.borjalapa.climb.configuracion.configuracion;
import com.borjalapa.climb.ui.inventario.InventarioFragment;
import com.borjalapa.climb.ui.mapa.MapaFragment;
import com.borjalapa.climb.ui.rutas.RutasFragment;
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
                .withHeaderBackground(R.drawable.mountain)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withEmail(correo)
                                .withTextColor(Color.BLACK)
                                .withIcon(getResources().getDrawable(R.drawable.user))
                )
                .build();


        //Elementos del materialdrawer
        mDrawer = new DrawerBuilder()
                .withActivity(Main_Page.this)
                .withAccountHeader(headerResult)
                .withToolbar(tbBarraTareas)
                .withActionBarDrawerToggle(true)
                .withDrawerGravity(Gravity.START)
                .withSliderBackgroundColor(getResources().getColor(android.R.color.white))
                .withSelectedItem(3)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(3)
                                .withName("Configuracion")
                                .withIcon(android.R.drawable.ic_menu_preferences),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withIdentifier(4)
                                .withName("Cerrar Menu")
                                .withIcon(android.R.drawable.ic_notification_clear_all),
                        new SecondaryDrawerItem()
                                .withIdentifier(5)
                                .withName("Cerrar Sesión")
                                .withIcon(android.R.drawable.ic_lock_power_off)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1: {
                                Intent ir_rutas = new Intent(Main_Page.this, RutasFragment.class);
                                startActivity(ir_rutas);
                                break;
                            }
                            case 2: {
                                Intent ir_inventario = new Intent(Main_Page.this, InventarioFragment.class);
                                startActivity(ir_inventario);
                                break;
                            }
                            case 3: {
                                Intent ir_configuracion = new Intent(Main_Page.this, configuracion.class);
                                startActivity(ir_configuracion);
                            }
                            case 4: {
                                break;
                            }
                            case 5: {
                                mAuth.signOut();
                                Intent ir_inicio = new Intent(Main_Page.this, MainActivity.class);
                                startActivity(ir_inicio);
                            }
                        }
                        return false;
                    }
                }).build();
    }



}

