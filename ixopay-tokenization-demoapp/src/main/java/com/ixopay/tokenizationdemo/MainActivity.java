package com.ixopay.tokenizationdemo;

import java.io.IOException;

import com.ixopay.api.tokenization.CardData;
import com.ixopay.api.tokenization.InvalidParameterException;
import com.ixopay.api.tokenization.Token;
import com.ixopay.api.tokenization.TokenizationApi;
import com.ixopay.api.tokenization.TokenizationApiException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private TextView tv_token;
	private TextView tv_fingerprint;

	private EditText te_cardholder;
	private EditText te_pan;
	private EditText te_month;
	private EditText te_year;
	private EditText te_cvv;

	private Snackbar loadingBar;
	private TokenizationApi api;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {

		api = TokenizationApi.builder()
			.gatewayHost(getString(R.string.ixopay_gateway))
			.tokenizationHost(getString(R.string.ixopay_tokenization))
			.integrationKey(getString(R.string.ixopay_integrationkey))
			.build();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		tv_token = findViewById(R.id.tv_token);
		tv_fingerprint = findViewById(R.id.tv_fingerprint);
		te_cardholder = findViewById(R.id.te_cardholder);
		te_pan = findViewById(R.id.te_pan);
		te_month = findViewById(R.id.te_month);
		te_year = findViewById(R.id.te_year);
		te_cvv = findViewById(R.id.te_cvv);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view -> {
			tv_token.setText("");
			tv_fingerprint.setText("");
			loadingBar = Snackbar.make(view, "Receiving token...", Snackbar.LENGTH_LONG);
			loadingBar.show();
			new TokenizationTask().execute(extractCardData());
		});
	}

	private CardData extractCardData() {
		int month = 0;
		int year = 0;
		if( !te_month.getText().toString().isEmpty() ) {
			month = Integer.parseInt(te_month.getText().toString());
		}

		if( !te_year.getText().toString().isEmpty() ) {
			year = Integer.parseInt(te_year.getText().toString());
		}

		return new CardData(
			te_pan.getText().toString(),
			te_cvv.getText().toString(),
			te_cardholder.getText().toString(),
			month,
			year
		);
	}

	private void processException( Exception ex ) {
		if( ex instanceof InvalidParameterException ) {
			InvalidParameterException e = (InvalidParameterException)ex;
			Snackbar.make(findViewById(R.id.fab), "Input verification failed", Snackbar.LENGTH_LONG).show();
			if( e.cvvError != null )
				te_cvv.setError(e.cvvError.toString());
			if( e.monthError != null )
				te_month.setError(e.monthError.toString());
			if( e.yearError != null )
				te_year.setError(e.yearError.toString());
			if( e.panError != null )
				te_pan.setError(e.panError.toString());
		} else if( ex instanceof TokenizationApiException ) {
			TokenizationApiException e = (TokenizationApiException)ex;
			e.printStackTrace();
			Snackbar.make(findViewById(R.id.fab), "Exception occurred: " + e.message, Snackbar.LENGTH_LONG).show();
		}

	}

	@SuppressLint("StaticFieldLeak")
	private class TokenizationTask extends AsyncTask<CardData,Void,Token> {
		Exception exception;

		@Override
		protected Token doInBackground( CardData... cardData ) {
			exception = null;
			try {
				return api.tokenizeCardData(cardData[0]);
			} catch( TokenizationApiException | InvalidParameterException e ) {
				exception = e;
				return null;
			} catch( IOException e ) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute( Token token ) {
			loadingBar.dismiss();
			te_pan.setError(null);
			te_cvv.setError(null);
			te_month.setError(null);
			te_year.setError(null);
			if( token != null ) {
				tv_token.setText(token.token);
				tv_fingerprint.setText(token.fingerprint);
			} else if( exception != null ) {
				processException(exception);
			}
		}
	}
}
