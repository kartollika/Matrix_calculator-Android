package kartollika.matrixcalc.startfragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;

import kartollika.matrixcalc.AppRater;
import kartollika.matrixcalc.ChooseOperationActivity;
import kartollika.matrixcalc.App;
import kartollika.matrixcalc.InputMatrixActivity;
import kartollika.matrixcalc.InterstitialShow;
import kartollika.matrixcalc.MainActivity;
import kartollika.matrixcalc.Manifest;
import kartollika.matrixcalc.Matrix;
import kartollika.matrixcalc.R;
import kartollika.matrixcalc.RationalNumber;
import kartollika.matrixcalc.ShowResultActivity;

import static android.app.Activity.RESULT_OK;

public class OperationsFragment extends Fragment implements View.OnClickListener {

    Matrix matrixA;
    Matrix matrixB;

    private AlertDialog dialog;

    private static final int MATRIX_A = 1;
    private static final int MATRIX_B = 2;
    private static final int OPERATION = 3;
    private static final int SOLVE = 4;

    int operation = -1;

    public OperationsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (!getResources().getBoolean(R.bool.isTablet)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operations, container, false);

        Button btnA = view.findViewById(R.id.btnA);
        Button btnB = view.findViewById(R.id.btnB);
        final Button operationSelect = view.findViewById(R.id.operations);
        final Button equals = view.findViewById(R.id.equals);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        operationSelect.setOnClickListener(this);
        equals.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnA: {
                Intent intent = new Intent(getContext(), InputMatrixActivity.class);
                intent.putExtra("matrix", new Matrix(matrixA));
                startActivityForResult(intent, MATRIX_A);
                break;
            }

            case R.id.btnB: {
                Intent intent = new Intent(getContext(), InputMatrixActivity.class);
                intent.putExtra("matrix", new Matrix(matrixB));
                startActivityForResult(intent, MATRIX_B);
                break;
            }

            case R.id.operations: {
                Intent intent = new Intent(getContext(), ChooseOperationActivity.class);
                intent.putExtra("operation", operation);
                startActivityForResult(intent, OPERATION);
                break;
            }
            case R.id.equals: {
                Intent intent = new Intent(getContext(), ShowResultActivity.class);

                switch (operation) {
                    case -1:
                        Toast.makeText(getContext(), getResources().getString(R.string.choose_one), Toast.LENGTH_LONG).show();
                        intent = new Intent(getContext(), ChooseOperationActivity.class);
                        startActivityForResult(intent, OPERATION);
                        break;

                        /* SUM AND DIFFERENCE */
                    case 0:
                    case 1:
                    case 4:
                    case 5:
                        if (matrixA.getRowCount() != matrixB.getRowCount() ||
                                matrixA.getColumnCount() != matrixB.getColumnCount()) {
                            Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        intent.putExtra("operation", operation);
                        if (operation < 4) {
                            intent.putExtra("matrix1", new Matrix(matrixA));
                            intent.putExtra("matrix2", new Matrix(matrixB));
                        } else {
                            intent.putExtra("matrix1", new Matrix(matrixB));
                            intent.putExtra("matrix2", new Matrix(matrixA));
                        }

                        startActivityForResult(intent, SOLVE);
                        break;

                        /* MULTIPLY BY CONST */
                    case 2:
                    case 6:
                        dialog = showInputDialog(0, intent);
                        dialog.show();
                        break;

                        /* MATRIX MULTIPLY */
                    case 3:
                    case 7:
                        intent.putExtra("operation", operation);
                        if (operation == 3) {
                            if (matrixA.getColumnCount() != matrixB.getRowCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("matrix1", new Matrix(matrixA));
                            intent.putExtra("matrix2", new Matrix(matrixB));
                        } else {
                            if (matrixB.getColumnCount() != matrixA.getRowCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("matrix1", new Matrix(matrixB));
                            intent.putExtra("matrix2", new Matrix(matrixA));
                        }

                        startActivityForResult(intent, SOLVE);
                        break;

                        /* DETERMINANT */
                    case 8:
                    case 12:
                        if (operation == 8) {
                            if (matrixA.getRowCount() != matrixA.getColumnCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("operation", operation);
                            intent.putExtra("matrix", new Matrix(matrixA));
                        } else {
                            if (matrixB.getRowCount() != matrixB.getColumnCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("operation", operation);
                            intent.putExtra("matrix", new Matrix(matrixB));
                        }

                        startActivityForResult(intent, SOLVE);
                        break;

                        /* INVERSE MATRIX */
                    case 9:
                    case 13:
                        if (operation == 9) {
                            if (matrixA.getRowCount() != matrixA.getColumnCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("operation", operation);
                            intent.putExtra("matrix", new Matrix(matrixA));
                        } else {
                            if (matrixB.getRowCount() != matrixB.getColumnCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("operation", operation);
                            intent.putExtra("matrix", new Matrix(matrixB));
                        }

                        startActivityForResult(intent, SOLVE);
                        break;

                        /* TRANSPOSE */
                    case 10:
                    case 14:
                        intent.putExtra("operation", operation);
                        if (operation == 10) {
                            intent.putExtra("matrix", new Matrix(matrixA));
                        } else {
                            intent.putExtra("matrix", new Matrix(matrixB));
                        }
                        startActivityForResult(intent, SOLVE);
                        break;

                        /* POWER */
                    case 11:
                    case 15:
                        intent.putExtra("operation", operation);
                        if (operation == 11) {
                            if (matrixA.getRowCount() != matrixA.getColumnCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("matrix", new Matrix(matrixA));
                        } else {
                            if (matrixB.getRowCount() != matrixB.getColumnCount()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.invalid_dim), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            intent.putExtra("matrix", new Matrix(matrixB));
                        }
                        dialog = showInputDialog(1, intent);
                        dialog.show();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("operation", operation);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                operation = savedInstanceState.getInt("operation");
            } catch (Exception e) {
                operation = -1;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = ((MainActivity) getActivity()).getPrefs();
        int defDimN = Integer.parseInt(preferences.getString("defDimN", "3"));
        int defDimM = Integer.parseInt(preferences.getString("defDimM", "3"));
        operation = preferences.getInt("operation", -1);
        String operationString = preferences.getString("operationText", getResources().getString(R.string.choose_operation));

        if (operation > -1) {
            Button operationSelect = getActivity().findViewById(R.id.operations);
            operationSelect.setText(Html.fromHtml(operationString));
        }

        matrixA = App.matrices[0];
        if (matrixA == null) {
            matrixA = new Matrix(null, defDimN, defDimM);
        }
        if (!matrixA.isEdited()) {
            matrixA = new Matrix(null, defDimN, defDimM);
        }


        matrixB = App.matrices[1];
        if (matrixB == null) {
            matrixB = new Matrix(null, defDimN, defDimM);
        }

        if (!matrixB.isEdited()) {
            matrixB = new Matrix(null, defDimN, defDimM);
        }


        String sA = "<big>A</big><sup><small>" + String.valueOf(matrixA.getRowCount())
                + "x" + String.valueOf(matrixA.getColumnCount()) + "</small></sup>";

        ((Button) getActivity().findViewById(R.id.btnA)).setText(Html.fromHtml(sA));

        String sB = "<big>B</big><sup><small>" + String.valueOf(matrixB.getRowCount())
                + "x" + String.valueOf(matrixB.getColumnCount()) + "</small></sup>";

        ((Button) getActivity().findViewById(R.id.btnB)).setText(Html.fromHtml(sB));
    }

    AlertDialog showInputDialog(final int param, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (param == 0) {
            builder.setTitle(getResources().getString(R.string.input_const));
        } else {
            builder.setTitle(getResources().getString(R.string.input_pow));
        }
        final EditText input = new EditText(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        input.setLayoutParams(layoutParams);

        builder.setView(input);

        if (param == 1) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        builder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (param == 0) {
                            intent.putExtra("operation", operation);
                            intent.putExtra("matrix", new Matrix(matrixA));

                            try {
                                RationalNumber r = RationalNumber.parseRational(input.getText().toString());
                                intent.putExtra("constNumerator", r.getNumerator());
                                intent.putExtra("constDenominator", r.getDenominator());
                            } catch (Exception e) {
                                Toast.makeText(getContext(), getResources().getString(R.string.number_not_found), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            try {
                                intent.putExtra("power", Integer.parseInt(input.getText().toString()));
                            } catch (NumberFormatException e) {
                                Toast.makeText(getContext(), getResources().getString(R.string.number_not_found), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        dialogInterface.dismiss();
                        startActivityForResult(intent, SOLVE);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            if (requestCode != SOLVE) {
                return;
            }
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MATRIX_A:
                    if (data.getExtras() != null) {
                        Matrix pmatrix = data.getExtras().getParcelable("matrix");
                        if (pmatrix != null) {
                            matrixA = pmatrix;
                            matrixA.setEdited();
                        } else {
                            return;
                        }
                        App.matrices[0] = matrixA;
                        break;
                    }

                case MATRIX_B:
                    if (data.getExtras() != null) {
                        Matrix pmatrix = data.getExtras().getParcelable("matrix");
                        if (pmatrix != null) {
                            matrixB = pmatrix;
                            matrixB.setEdited();
                        } else {
                            return;
                        }
                        App.matrices[1] = matrixB;
                        break;
                    }

                case OPERATION:
                    SharedPreferences preferences = ((MainActivity) getActivity()).getPrefs();
                    SharedPreferences.Editor editor = preferences.edit();
                    String s = data.getStringExtra("text");
                    editor.putString("operationText", s);

                    operation = data.getIntExtra("operation", -1);
                    editor.putInt("operation", operation);

                    Button operationSelect = getActivity().findViewById(R.id.operations);
                    operationSelect.setText(Html.fromHtml(s));
                    editor.apply();
                    break;

                case SOLVE:
                    AppRater.appLaunched(getContext(), getActivity().getFragmentManager());
                    InterstitialShow.showInterstitialAd();
            }
        }
    }

    @Override
    public void onDetach() {
        App.matrices[0] = matrixA;
        App.matrices[1] = matrixB;

        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null)
            dialog.dismiss();
    }
}
