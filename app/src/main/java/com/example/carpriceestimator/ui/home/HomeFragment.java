package com.example.carpriceestimator.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.carpriceestimator.Constants;
import com.example.carpriceestimator.R;
import com.example.carpriceestimator.api.RetrofitBuilder;
import com.example.carpriceestimator.api.VpicEndPointInterface;
import com.example.carpriceestimator.entity.Car;
import com.example.carpriceestimator.entity.DecodedCar;
import com.example.carpriceestimator.utility.CarDecoder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    FirebaseVisionImage image = null;
    Bitmap selectedImage;
    ImageView vinImage;
    ConstraintLayout carDetailsLayout;
    //VIN Edit text
    EditText etVIN;
    TextView carName;
    TextView carVIN;
    TextView make, model, modelYear, bodyClass, doors, maufacturer, price;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);

        final TextView textView = root.findViewById(R.id.tvHome);
        textView.setText(String.format("User: %s", sharedPreferences.getString(Constants.USER_EMAIL, "")));

        Button cameraButton = root.findViewById(R.id.btnCamera);
        Button detailButton = root.findViewById(R.id.btnCarDetail);

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
        maufacturer = root.findViewById(R.id.textViewCarManufactureNameData);
        price = root.findViewById(R.id.textViewPriceData);

        carDetailsLayout.setVisibility(View.INVISIBLE);
        cameraButton.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            selectImage(getContext());
        });

        detailButton.setOnClickListener(v -> {
            getCarDetails(etVIN.getText().toString().trim());
        });


        return root;
    }

    private void getCarDetails(String vin) {
        Retrofit retrofit = RetrofitBuilder.getInstance();
        VpicEndPointInterface apiService = retrofit.create(VpicEndPointInterface.class);

        Call<Car> call = apiService.getCar(vin);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                int statusCode = response.code();
                Car car = response.body();
                try {
                    DecodedCar decodedCar = CarDecoder.decode(car);
                    //FINAL CAR DETAIL
                    Log.i("CAR", decodedCar.toString());
                    carVIN.setText(decodedCar.getVin());
                    carName.setText(decodedCar.getMake() + "\n" + decodedCar.getModel());
                    make.setText(decodedCar.getMake());
                    model.setText(decodedCar.getModel());
                    modelYear.setText(decodedCar.getModelYear());
                    bodyClass.setText(decodedCar.getBodyClass());
                    doors.setText(String.valueOf(decodedCar.getDoors()));
                    maufacturer.setText(decodedCar.getManufactureName());
                    price.setText("TO BE IMPLEMENTED");
                    carDetailsLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Please try to scan again or enter the VIN in text.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickPhoto, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        selectedImage = (Bitmap) data.getExtras().get("data");
                        vinImage.setImageBitmap(selectedImage);
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
                                String picturePath = cursor.getString(columnIndex);
                                vinImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
                                for (FirebaseVisionText.Element element : line.getElements()) {
                                    String textBlock = element.getText();
                                    textBlock = textBlock.replaceAll("\\s+", "");
                                    Log.d("MLBlock", textBlock.toString());

                                    if (textBlock.matches("(?=.*\\d|=.*[A-Z])(?=.*[A-Z])[A-Z0-9]{17}")) {
                                        getCarDetails(textBlock);
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
    }
}