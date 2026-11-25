package com.example.bt1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class Product {
    String productName;
    Integer productPrice;
    String productDescription;
    String productSpecification;
    String category;
    int productImage;

    Product(String productName, Integer productPrice, String productDescription, String productSpecification, String category, int productImage) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productSpecification = productSpecification;
        this.category = category;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }
    public int getProductPrice() {
        return productPrice;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public String getProductSpecification() {
        return productSpecification;
    }
    public String getCategory() {
        return category;
    }
    public int getProductImage() {
        return productImage;
    }
}

class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> productList;

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.image.setImageResource(product.getProductImage());
        holder.name.setText(product.getProductName());
        holder.description.setText(product.getProductDescription());
        holder.price.setText(String.format("%,d đ", product.getProductPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showProduct = new Intent(context, ProductDetailActivity.class);
                showProduct.putExtra(Constants.KEY_PRODUCT_NAME, product.getProductName());
                showProduct.putExtra(Constants.KEY_PRODUCT_IMAGE, product.getProductImage());
                showProduct.putExtra(Constants.KEY_PRODUCT_PRICE, product.getProductPrice());
                showProduct.putExtra(Constants.KEY_PRODUCT_DESCRIPTION, product.getProductDescription());
                showProduct.putExtra(Constants.KEY_PRODUCT_SPECIFICATION, product.getProductSpecification());
                context.startActivity(showProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, description, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            description = itemView.findViewById(R.id.productDescription);
            price = itemView.findViewById(R.id.productPrice);
        }
    }
}

class FeaturedPagerAdapter extends RecyclerView.Adapter<FeaturedPagerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> productList;

    public FeaturedPagerAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_featured_slider, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (productList.isEmpty()) return;
        int realPosition = position % productList.size();
        final Product product = productList.get(realPosition);

        holder.image.setImageResource(product.getProductImage());
        holder.name.setText(product.getProductName());
        holder.description.setText(product.getProductDescription());
        holder.price.setText(String.format("%,d đ", product.getProductPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showProduct = new Intent(context, ProductDetailActivity.class);
                showProduct.putExtra(Constants.KEY_PRODUCT_NAME, product.getProductName());
                showProduct.putExtra(Constants.KEY_PRODUCT_IMAGE, product.getProductImage());
                showProduct.putExtra(Constants.KEY_PRODUCT_PRICE, product.getProductPrice());
                showProduct.putExtra(Constants.KEY_PRODUCT_DESCRIPTION, product.getProductDescription());
                showProduct.putExtra(Constants.KEY_PRODUCT_SPECIFICATION, product.getProductSpecification());
                context.startActivity(showProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, description, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.featuredImage);
            name = itemView.findViewById(R.id.featuredName);
            description = itemView.findViewById(R.id.featuredDescription);
            price = itemView.findViewById(R.id.featuredPrice);
        }
    }
}

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore fireDB;
    ArrayList<Product> allProducts = new ArrayList<>();
    ArrayList<Product> searchProducts = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> featuredProducts = new ArrayList<>();
    ProductAdapter adapter;
    FeaturedPagerAdapter featuredPagerAdapter;
    RecyclerView recyclerView;
    ViewPager2 featuredViewPager;
    DrawerLayout drawerLayout;
    Button menu, cartHeader;
    NavigationView navigationView;
    AutoCompleteTextView searchBar;
    ArrayAdapter<String> searchBarSuggestion;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fireDB = FirebaseFirestore.getInstance();

        initViews();
        setupListeners();
        loadDataFromFirestore();
    }

    private final Handler sliderHandler = new Handler(Looper.getMainLooper());
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (featuredProducts != null && !featuredProducts.isEmpty()) {
                int currentItem = featuredViewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                featuredViewPager.setCurrentItem(nextItem, true);
            }
            sliderHandler.postDelayed(this, 5000);
        }
    };

    //Tu dong chuyen sau 5s
    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        featuredViewPager = findViewById(R.id.featuredViewPager);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        cartHeader = findViewById(R.id.cartHeader);
        navigationView = findViewById(R.id.navigationView);
        searchBar = findViewById(R.id.searchBar);

        adapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(adapter);

        featuredPagerAdapter = new FeaturedPagerAdapter(this, featuredProducts);
        featuredViewPager.setAdapter(featuredPagerAdapter);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    return true;
                }

                else if (id == R.id.nav_orders) {
                    startActivity(new Intent(MainActivity.this, OrderActivity.class));
                    finish();
                    return true;
                }

                else if (id == R.id.nav_account) {
                    startActivity(new Intent(MainActivity.this, Profile.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        searchBarSuggestion = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>()
        );

        searchBar.setAdapter(searchBarSuggestion);
        searchBar.setThreshold(2);

        searchBar.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedName = (String) parent.getItemAtPosition(position);
                for (Product product : allProducts) {
                    if (product.getProductName().equals(selectedName)) {
                        Intent showProduct = new Intent(MainActivity.this, ProductDetailActivity.class);
                        showProduct.putExtra(Constants.KEY_PRODUCT_NAME, product.getProductName());
                        showProduct.putExtra(Constants.KEY_PRODUCT_IMAGE, product.getProductImage());
                        showProduct.putExtra(Constants.KEY_PRODUCT_PRICE, product.getProductPrice());
                        showProduct.putExtra(Constants.KEY_PRODUCT_DESCRIPTION, product.getProductDescription());
                        showProduct.putExtra(Constants.KEY_PRODUCT_SPECIFICATION, product.getProductSpecification());
                        startActivity(showProduct);
                        break;
                    }
                }
            }
        });
    }

    private void loadDataFromFirestore() {
        fireDB.collection("products").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        allProducts.clear();
                        searchProducts.clear();
                        products.clear();
                        featuredProducts.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("name");
                            Long priceLong = document.getLong("price");

                            int price = priceLong != null ? priceLong.intValue() : 0;
                            String description = document.getString("description");
                            String specification = document.getString("specification");
                            String category = document.getString("category");

                            String imgName = document.getString("imageName");
                            int imageRes = R.drawable.arduino;
                            if (imgName != null && !imgName.isEmpty()) {
                                int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
                                if (resId != 0) {
                                    imageRes = resId;
                                }
                            }
                            Product product = new Product(name, price, description, specification, category, imageRes);
                            allProducts.add(product);
                            searchProducts.add(product);
                            products.add(product);
                        }

                        for (Product p : allProducts) {
                            String n = p.getProductName().toLowerCase();
                            if (n.contains("esp32") || n.contains("raspberry") || n.contains("mosfet") || n.contains("led")) {
                                featuredProducts.add(p);
                            }
                        }

                        //clear lan search thu nhat de search lan 2
                        searchBarSuggestion.clear();
                        for (Product product : searchProducts) {
                            searchBarSuggestion.add(product.getProductName());
                        }

                        searchBarSuggestion.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                        featuredPagerAdapter.notifyDataSetChanged();

                        if (!featuredProducts.isEmpty()) {
                            int middlePosition = Integer.MAX_VALUE / 2;
                            int startPosition = middlePosition - (middlePosition % featuredProducts.size());
                            featuredViewPager.setCurrentItem(startPosition, false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirestoreError", "Lỗi dữ liệu!");
                    }
                });
    }

    private void filterProductsByCategory(String categoryKey) {
        products.clear();
        if (categoryKey.equals("all")) {
            products.addAll(allProducts);
        }

        else {
            for (Product p : allProducts) {
                if (categoryKey.equals(p.getCategory())) {
                    products.add(p);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setupListeners() {
        if (cartHeader != null) {
            cartHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartIntent = new Intent(MainActivity.this, BuyInCart.class);
                    startActivity(cartIntent);
                }
            });
        }

        if (menu != null) {
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        View all = findViewById(R.id.all);
        View controller = findViewById(R.id.controller);
        View component = findViewById(R.id.component);
        View circuit = findViewById(R.id.circuit);

        if (all != null) {
            all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterProductsByCategory("all");
                }
            });
        }

        if (controller != null) {
            controller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterProductsByCategory("controller");
                }
            });
        }

        if (component != null) {
            component.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterProductsByCategory("component");
                }
            });
        }

        if (circuit != null) {
            circuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterProductsByCategory("circuit");
                }
            });
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_logout) {
                    Intent logout = new Intent(MainActivity.this, Login.class);
                    startActivity(logout);
                }

                else if (id == R.id.nav_forgetPassword) {
                    Intent resetPass = new Intent(MainActivity.this, ResetPassword.class);
                    startActivity(resetPass);
                }

                else if (id == R.id.nav_about_us) {
                    Intent about = new Intent(MainActivity.this, AboutUs.class);
                    startActivity(about);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
