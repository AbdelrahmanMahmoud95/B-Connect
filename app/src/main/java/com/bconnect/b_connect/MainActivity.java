package com.bconnect.b_connect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String ToActivity, activation_flag, emp_name;
    Dialog dialog;
    Dialog dialogOrder;
    int snakINT = 0;
    int is_noti_mess = 0;
    SharedPreferencesSR SPsr = new SharedPreferencesSR();

    TextView sign_out_tv, emp_name_tv;

    CardView active_card, cont_card, amount_card, sup_card, install_card, order_card, mess_card, mysall_card;

    int click_time = 0;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            basic();


        deleteCache(MainActivity.this);

    }


    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }


    public void basic() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        sign_out_tv = (TextView) findViewById(R.id.sign_out_tv);
        emp_name_tv = (TextView) findViewById(R.id.emp_name_tv);

        active_card = (CardView) findViewById(R.id.active_card);

        cont_card = (CardView) findViewById(R.id.cont_card);
        amount_card = (CardView) findViewById(R.id.amount_card);
        sup_card = (CardView) findViewById(R.id.sup_card);
        install_card = (CardView) findViewById(R.id.install_card);
        order_card = (CardView) findViewById(R.id.order_card);
        mess_card = (CardView) findViewById(R.id.mess_card);
        mysall_card = (CardView) findViewById(R.id.mysall_card);

        Intent intent2 = getIntent();

        try {
            if (intent2.getExtras().getString("snack").toString().length() > 0) {
                snakINT = Integer.parseInt(intent2.getExtras().getString("snack").toString());
            }


        } catch (Exception ex) {
            snakINT = 0;
        }

        try {
            if (intent2.getExtras().getString("is_noti_mess").toString().length() > 0) {
                is_noti_mess = Integer.parseInt(intent2.getExtras().getString("is_noti_mess").toString());
            }

        } catch (Exception ex) {
            is_noti_mess = 0;
        }
        if (snakINT == 1) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "اهلا بعودتك " + SPsr.getSP(getApplicationContext(), "empsername").toString(), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        if (is_noti_mess == 1) {

            AlarmMessToLogin("عذرا !", "فقد الإتصال بالسيرفر , يمكنك التحقق من الانترنت , او اعادة تهيئة البرنامج بالضغط على زر  (تأكيد)", "تأكيد", "لا");
        }


        dialog = new Dialog(MainActivity.this); // Context, this, etc.
        dialog.dismiss();
        dialogOrder = new Dialog(MainActivity.this); // Context, this, etc.
        dialogOrder.dismiss();
        ToActivity = "0";
        activation_flag = "b";
        if (SPsr.getSP(MainActivity.this, "empsername").length() > 0) {
            emp_name = SPsr.getSP(MainActivity.this, "empsername");

        } else {
            emp_name = "";
        }

//        View headerView = navigationView.getHeaderView(0);
//        TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        try {
            if (emp_name.toString().length() > 0) {


                emp_name_tv.setText(emp_name);
            } else {
                stopService(new Intent(MainActivity.this, NotiService.class));

                SPsr.removeAllSP(MainActivity.this);
                finish();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        } catch (Exception ex) {
            stopService(new Intent(MainActivity.this, NotiService.class));

            SPsr.removeAllSP(MainActivity.this);
            finish();
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


        sign_out_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_warning_24dp)
                        .setTitle("تسجيل الخروج")
                        .setMessage("هل انت متأكد انك تريد تسجيل الخروج")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stopService(new Intent(MainActivity.this, NotiService.class));

                                SPsr.removeAllSP(MainActivity.this);
                                finish();
                                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("snack", "1");
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton("لا", null)
                        .show();
            }
        });
        active_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_time += 1;
                int myclick = 5;

                if (click_time != myclick) {
                    Toast.makeText(getApplicationContext(), "تسجيل الخروج " + String.valueOf(myclick - click_time), Toast.LENGTH_SHORT).show();
                }


                if (click_time == 5) {
                    click_time = 0;
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.ic_warning_24dp)
                            .setTitle("تسجيل الخروج")
                            .setMessage("هل انت متأكد انك تريد تسجيل الخروج")
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    stopService(new Intent(MainActivity.this, NotiService.class));

                                    SPsr.removeAllSP(MainActivity.this);
                                    finish();
                                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("snack", "1");
                                    startActivity(intent);
                                }

                            })
                            .setNegativeButton("لا", null)
                            .show();

                }
            }
        });


        active_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_warning_24dp)
                        .setTitle("تسجيل الخروج")
                        .setMessage("هل انت متأكد انك تريد تسجيل الخروج")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stopService(new Intent(MainActivity.this, NotiService.class));

                                SPsr.removeAllSP(MainActivity.this);
                                finish();
                                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("snack", "1");
                                startActivity(intent);
                            }

                        })
                        .setNegativeButton("لا", null)
                        .show();

                return false;
            }
        });

        cont_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.dialog_contract);
                dialog.show();
            }
        });
        amount_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToActivity = "5";
                activation_flag = "a";

                Intent i = new Intent(MainActivity.this, AmountPaybackListActivity.class);
                i.putExtra("ToActivity", ToActivity);
                i.putExtra("activation_flag", activation_flag);
                startActivity(i);
            }
        });
        sup_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SupSearchActivity.class);
                startActivity(intent);
            }
        });
        install_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UnderInstalListActivity.class);
                startActivity(intent);
            }
        });
        order_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOrder.setContentView(R.layout.dialog_order);
                dialogOrder.show();
            }
        });
        mess_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MessListActivity.class);
                startActivity(i);
            }
        });
        mysall_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MySaleListActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("الخروج من البرنامج")
                .setMessage("هل انت متأكد انك تريد الخروج من البرنامج")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("لا", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            finish();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_new_con) {
            ToActivity = "2";
            activation_flag = "a";

            Intent i = new Intent(MainActivity.this, BranchsActivity.class);
            i.putExtra("ToActivity", ToActivity);
            i.putExtra("activation_flag", activation_flag);
            startActivity(i);

        } else if (id == R.id.nav_sup) {
            Intent intent = new Intent(MainActivity.this, SupSearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_inst) {
            Intent intent = new Intent(MainActivity.this, UnderInstalListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_order) {
            ToActivity = "3";
            activation_flag = "a";

            Intent i = new Intent(MainActivity.this, BranchsActivity.class);
            i.putExtra("ToActivity", ToActivity);
            i.putExtra("activation_flag", activation_flag);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            stopService(new Intent(MainActivity.this, NotiService.class));

            SPsr.removeAllSP(MainActivity.this);
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("snack", "1");
            startActivity(intent);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void new_con_open(View view) {

        ToActivity = "2";
        activation_flag = "a";

        Intent i = new Intent(MainActivity.this, BranchsActivity.class);
        i.putExtra("ToActivity", ToActivity);
        i.putExtra("activation_flag", activation_flag);
        startActivity(i);

        dialog.dismiss();

    }

    public void edit_con_open(View view) {
        ToActivity = "1";
        activation_flag = "b";

        Intent i = new Intent(MainActivity.this, BranchsActivity.class);
        i.putExtra("ToActivity", ToActivity);
        i.putExtra("activation_flag", activation_flag);
        startActivity(i);

        dialog.dismiss();

    }

    public void orderopen(View view) {
        ToActivity = "3";
        activation_flag = "a";
        dialogOrder.dismiss();

        Intent i = new Intent(MainActivity.this, BranchsActivity.class);
        i.putExtra("ToActivity", ToActivity);
        i.putExtra("activation_flag", activation_flag);
        startActivity(i);
    }

    public void supuni_open(View view) {

        ToActivity = "4";
        activation_flag = "a";
        dialogOrder.dismiss();

        Intent i = new Intent(MainActivity.this, UnitApproveActivity.class);
        i.putExtra("ToActivity", ToActivity);
        i.putExtra("activation_flag", activation_flag);
        startActivity(i);

    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    public void StartService() {
        startService(new Intent(MainActivity.this, NotiService.class));
    }


    public void AlarmMessToLogin(String Title, String message, String k1, String k2) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setTitle(Title);
        builder1.setMessage(message);

        builder1.setCancelable(true);

        builder1.setPositiveButton(
                k1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        stopService(new Intent(MainActivity.this, NotiService.class));

                        SPsr.removeAllSP(MainActivity.this);
                        finish();
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }
                });

        builder1.setNegativeButton(
                k2,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        StartService();

    }

    @Override
    protected void onResume() {
        super.onResume();
        StartService();
    }
}
