package raghav.developer.e_kart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirts,sportsTshirts,femaleDresses,sweater;
    private ImageView glasses,walletBagsPurses,hats,shoes;
    private ImageView headphones,laptops,watches,mobiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tshirts = findViewById(R.id.tshirts);
        sportsTshirts = findViewById(R.id.sports_tshirts);
        femaleDresses = findViewById(R.id.female_dresses);
        sweater = findViewById(R.id.sweaters);
        glasses = findViewById(R.id.glasses);
        walletBagsPurses = findViewById(R.id.walletsBagsPurses);
        hats = findViewById(R.id.hats);
        shoes = findViewById(R.id.shoes);
        headphones = findViewById(R.id.headphones);
        laptops = findViewById(R.id.laptop_pc);
        watches = findViewById(R.id.watches);
        mobiles = findViewById(R.id.mobile_phones);

        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);

            }
        });

        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","tShirts");
                startActivity(intent);

            }
        });

        sportsTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Sports tshirts");
                startActivity(intent);

            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);

            }
        });

        sweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);

            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);

            }
        });

        walletBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","WalletsBagsPurses");
                startActivity(intent);

            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Hats");
                startActivity(intent);

            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);

            }
        });

        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Headphones");
                startActivity(intent);

            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);

            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);

            }
        });

        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Mobiles");
                startActivity(intent);

            }
        });

    }
}
