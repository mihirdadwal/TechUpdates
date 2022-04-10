package com.example.top10apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipOutputStream;

public class MainActivity extends AppCompatActivity {
    private ListView listapps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listapps=(ListView) findViewById(R.id.xmlListView);

        downloadURL("https://gadgets.ndtv.com/rss/mobiles/feeds");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        String feedURL;

        switch (id)
        {
            case R.id.mnuFree:
                feedURL="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=25/xml";
                break;
            case R.id.mnuPaid:
                feedURL="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=25/xml";
                break;
            case R.id.mnuSongs:
                feedURL="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=25/xml";
                break;
            case R.id.mnuMicrosoft:
                    feedURL = "https://gadgets.ndtv.com/rss/microsoft/feeds";
                    break;
            case R.id.mnuSmartphone :
                feedURL="https://gadgets.ndtv.com/rss/mobiles/feeds";
                break;
            case R.id.mnuGames:
                feedURL="https://gadgets.ndtv.com/rss/games/feeds";
                break;
            default:  return super.onOptionsItemSelected(item);

        }
        downloadURL(feedURL);
        return true;

    }

    private void downloadURL(String feedUrl)
    {
        DownloadData downloaddata = new DownloadData();
        downloaddata.execute(feedUrl);

    }

    private class DownloadData extends AsyncTask<String,Void,String>
    {
        private static final String TAG = "DownloadData";
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParseApplications parseapplication = new ParseApplications();
            parseapplication.parse(s);

//            ArrayAdapter<FeedEntry> arrayadapter = new ArrayAdapter<>(MainActivity.this  , R.layout.list_item , parseapplication.getApplications());
//            listapps.setAdapter(arrayadapter);

            FeedAdapter feedadapter = new FeedAdapter(MainActivity.this,R.layout.list_record,parseapplication.getApplications());
            listapps.setAdapter(feedadapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null)
                Log.e(TAG, "doInBackground: error downloading" );
            return rssFeed;
        }
        private String downloadXML(String URLpath)
        {
            StringBuilder xmlresult = new StringBuilder();
            try {
                URL url = new URL(URLpath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: response code :" + response);
//                InputStream inputstream= connection.getInputStream();
//                InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
//                BufferedReader bufferedReader = new BufferedReader(inputstreamreader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsread;
                char[] inputbuffer = new char[500];
                while (true){
                    charsread = reader.read(inputbuffer);
                    if (charsread<0)
                        break;
                    if (charsread>0)
                        xmlresult.append(String.copyValueOf(inputbuffer,0,charsread));
                }
                reader.close();

                return xmlresult.toString();
            }catch (MalformedURLException e)
            {
                Log.e(TAG, "downloadXML: invalid url" + e.getMessage() );
            }catch (IOException e){
                Log.e(TAG, "downloadXML: IO exception reading data" + e.getMessage() );
            }catch (SecurityException e){
                Log.e(TAG, "downloadXML: security excepiton . needs permission ? "+ e.getMessage() );
            }
            return null;
        }
    }

}

