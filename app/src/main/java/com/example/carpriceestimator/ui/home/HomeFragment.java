package com.example.carpriceestimator.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.carpriceestimator.Constants;
import com.example.carpriceestimator.R;
import com.example.carpriceestimator.api.CarPriceInterface;
import com.example.carpriceestimator.api.RetrofitBuilder;
import com.example.carpriceestimator.api.VpicEndPointInterface;
import com.example.carpriceestimator.entity.Car;
import com.example.carpriceestimator.entity.DecodedCar;
import com.example.carpriceestimator.entity.PriceResult;
import com.example.carpriceestimator.utility.CarDecoder;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    FirebaseVisionImage image = null;
    Bitmap selectedImage;
    ImageView vinImage;
    String picturePath;
    ConstraintLayout carDetailsLayout;
    boolean vinScanned = false;
    int FLAG_TAKE_IMAGE = 0;

    //VIN Edit text
    EditText etVIN;
    TextView carName;
    TextView carVIN;
    TextView make, model, modelYear, bodyClass, doors, manufacturer, price;
    CardView cardViewDetail;
    ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayoutPrice;
    //VM
    private HomeViewModel homeViewModel;

    //Final result
    DecodedCar decodedCar = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        final TextView textView = root.findViewById(R.id.tvHome);
        textView.setText(String.format("User: %s", sharedPreferences.getString(Constants.USER_EMAIL, "")));

        Button cameraButton = root.findViewById(R.id.btnCamera);
        Button detailButton = root.findViewById(R.id.btnCarDetail);
        shimmerFrameLayout = root.findViewById(R.id.facebookShimmerLayout);
        shimmerFrameLayoutPrice = root.findViewById(R.id.facebookShimmerLayoutPrice);
        carDetailsLayout = root.findViewById(R.id.carDetailsLayout);
        carName = root.findViewById(R.id.carName);
        carVIN = root.findViewById(R.id.carVIN);
        vinImage = root.findViewById(R.id.carimage);
        etVIN = root.findViewById(R.id.etVIN);
        bodyClass = root.findViewById(R.id.textViewCarBodyClassData);
        make = root.findViewById(R.id.textViewCarMakeData);
        model = root.findViewById(R.id.textViewCarModelData);
        modelYear = root.findViewById(R.id.textViewCarModelYearData);
        doors = root.findViewById(R.id.textViewDoorsData);
        manufacturer = root.findViewById(R.id.textViewCarManufactureNameData);
        price = root.findViewById(R.id.textViewPrice);
        //progressBar = root.findViewById(R.id.progressBarHome);
        cardViewDetail = root.findViewById(R.id.cardViewDetail);

        carDetailsLayout.setVisibility(View.INVISIBLE);
        shimmerFrameLayout.setVisibility(View.INVISIBLE);
        shimmerFrameLayoutPrice.setVisibility(View.INVISIBLE);
        cameraButton.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            selectImage(getContext());
        });

        detailButton.setOnClickListener(v -> getCarDetails(etVIN.getText().toString().trim()));
        cardViewDetail.setOnClickListener(view -> calculatePrice());

        return root;
    }

    private void calculatePrice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Mileage");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.mileage_edittext, (ViewGroup) getView(), false);
        final EditText odometer = viewInflated.findViewById(R.id.et_mileage);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> getPrice(Integer.parseInt(odometer.getText().toString().trim())));
        builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    private void getPrice(int odometer) {
        shimmerFrameLayoutPrice.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitBuilder.getPriceInstance();
        CarPriceInterface apiService = retrofit.create(CarPriceInterface.class);

        String make = this.make.getText().toString().toLowerCase().trim();
        String name = model.getText().toString().toLowerCase().trim();
        int year = Integer.parseInt(modelYear.getText().toString().trim());
        Call<PriceResult> call = apiService.getPrice(make, name, year, odometer);

        call.enqueue(new Callback<PriceResult>() {
            @Override
            public void onResponse(Call<PriceResult> call, Response<PriceResult> response) {
                shimmerFrameLayoutPrice.setVisibility(View.INVISIBLE);
                Log.i("PRICE", response.body().toString());
                PriceResult priceResult = response.body();
                if(priceResult.getResultValid() == 1){
                    decodedCar.setPrice(priceResult.getPrice());
                    homeViewModel.insert(decodedCar);
                    price.setText("Estimated price: $"+decodedCar.getPrice());
                }else{
                    price.setText("CAR NOT FOUND !");
                }
            }

            @Override
            public void onFailure(Call<PriceResult> call, Throwable t) {
                shimmerFrameLayoutPrice.setVisibility(View.INVISIBLE);
                Log.i("PRICE-FAILED", t.toString());
            }
        });
    }

    private void getCarDetails(String vin) {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitBuilder.getInstance();
        VpicEndPointInterface apiService = retrofit.create(VpicEndPointInterface.class);

        Call<Car> call = apiService.getCar(vin);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                Car car = response.body();
                try {
                    //global
                    decodedCar = CarDecoder.decode(car);
                    if (decodedCar != null) {
                        homeViewModel.insert(decodedCar);
                        homeViewModel.deleteNotRecentCars(sharedPreferences.getInt(Constants.RECENT_RECORDS, 5));
                    }
                    //FINAL CAR DETAIL
                    Log.i("CAR", decodedCar.toString());
                    carVIN.setText(decodedCar.getVin());
                    carName.setText(decodedCar.getMake() + "\n" + decodedCar.getModel());
                    make.setText(decodedCar.getMake());
                    model.setText(decodedCar.getModel());
                    modelYear.setText(decodedCar.getModelYear());
                    bodyClass.setText(decodedCar.getBodyClass());
                    doors.setText(String.valueOf(decodedCar.getDoors()));
                    manufacturer.setText(decodedCar.getManufactureName());
                    price.setText("Tap on the picture to get estimated price.");
                    vinImage.setImageResource(R.drawable.car);
                    carDetailsLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Please try to scan again or enter the VIN in text.", Toast.LENGTH_SHORT).show();
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                // Log error here since request failed
            }
        });
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Capture or Choose a picture of VIN");
        builder.setItems(options, (dialog, item) -> {

            if (options[item].equals("Take Photo")) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 10);
                } else {
                    FLAG_TAKE_IMAGE = 1;
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }

            } else if (options[item].equals("Choose from Gallery")) {

                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 20);
                }
                else {
                    FLAG_TAKE_IMAGE = 0;
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickPhoto, 1);
                }
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 10);
        }

        if(requestCode == 1){
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickPhoto, 20);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = (Bitmap) data.getExtras().get("data");
                        image = FirebaseVisionImage.fromBitmap(selectedImage);
                        recognizeTextCloud(image);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = requireContext().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);
                                image = FirebaseVisionImage.fromBitmap(BitmapFactory.decodeFile(picturePath));
                                recognizeTextCloud(image);
                            }
                        }
                    }
                    break;
            }
        }
    }


    private void recognizeTextCloud(FirebaseVisionImage image) {
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "hi"))
                .build();

        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer();

        Task<FirebaseVisionText> result = detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
                            Rect boundingBox = block.getBoundingBox();
                            Point[] cornerPoints = block.getCornerPoints();

                            for (FirebaseVisionText.Line line : block.getLines()) {
                                String text = block.getText();
                                text = text.replaceAll("\\s+", "");
                                if (text.matches("(?=.*\\d|=.*[A-Z])(?=.*[A-Z])[A-Z0-9]{17}")) {
                                    vinScanned = true;
                                    getCarDetails(line.getText());
                                }
                                for (FirebaseVisionText.Element element : line.getElements()) {
                                    String textBlock = element.getText();
                                    textBlock = textBlock.replaceAll("\\s+", "");
                                    Log.d("MLBlock", textBlock);
                                    if (textBlock.matches("(?=.*\\d|=.*[A-Z])(?=.*[A-Z])[A-Z0-9]{17}")) {
                                        vinScanned = true;
                                        getCarDetails(textBlock);
                                    }
                                }
                            }
                        }
                        if (!vinScanned) {
                            Toast.makeText(getContext(), "Please try to scan again or enter the VIN in text.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                });
    }
}