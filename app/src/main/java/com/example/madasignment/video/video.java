package com.example.madasignment.video;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class video extends AppCompatActivity {

    private RecyclerView transcriptionList;
    private videoTranscriptionAdapter adapter;
    private List<videoTranscription> transcriptionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        WebView webView = findViewById(R.id.youtube_webview);
        transcriptionList = findViewById(R.id.transcription_list);
        Switch toggleTranscription = findViewById(R.id.toggle_transcription);

        // Enable JavaScript and other settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Use WebChromeClient to support video playback
        webView.setWebChromeClient(new WebChromeClient());

        // Get the video ID and transcription file name from intent extras
        String videoId = getIntent().getStringExtra("videoUrl");
        String transcriptionFile = getIntent().getStringExtra("transcriptionFile");

        if (videoId == null || transcriptionFile == null) {
            Log.e("VideoError", "Missing video ID or transcription file.");
            return;
        }

        // Load and parse transcription data
        transcriptionData = loadAndParseTranscription(transcriptionFile);

        // Set up transcription list
        setupTranscriptionList();

        // Synchronize subtitles with the video
        syncTranscriptionWithVideo(webView);

        // Handle toggle for transcription visibility
        toggleTranscription.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                transcriptionList.setVisibility(View.VISIBLE); // Show the transcription
            } else {
                transcriptionList.setVisibility(View.GONE); // Hide the transcription
            }
        });

        // Load the video in the WebView
        loadVideoInWebView(webView, videoId);
    }

    private void setupTranscriptionList() {
        transcriptionList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new videoTranscriptionAdapter(transcriptionData);
        transcriptionList.setAdapter(adapter);
    }

    private List<videoTranscription> loadAndParseTranscription(String fileName) {
        List<videoTranscription> transcriptionList = new ArrayList<>();
        try {
            // Load the JSON file from the raw folder
            int resourceId = getResources().getIdentifier(fileName, "raw", getPackageName());
            InputStream is = getResources().openRawResource(resourceId);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            // Parse JSON
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String startTime = obj.getString("start_time");
                String endTime = obj.getString("end_time");
                String chinese = obj.getString("chinese");
                String english = obj.getString("english");
                transcriptionList.add(new videoTranscription(startTime, endTime, chinese, english));
            }
        } catch (Exception e) {
            Log.e("TranscriptionError", "Error loading transcription: " + e.getMessage());
        }
        return transcriptionList;
    }

    private void syncTranscriptionWithVideo(WebView webView) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Evaluate JavaScript to get the current playback time from the YouTube video
                webView.evaluateJavascript("player.getCurrentTime()", currentTime -> {
                    try {
                        if (currentTime == null || currentTime.equals("null") || currentTime.equals("\"\"")) {
                            Log.e("VideoSync", "Playback time not available yet.");
                            handler.postDelayed(this, 500);
                            return;
                        }

                        // Parse the current playback time
                        double videoTime = Double.parseDouble(currentTime);

                        // Synchronize transcription with the playback time
                        boolean transcriptionFound = false;
                        for (int i = 0; i < transcriptionData.size(); i++) {
                            double startTime = timeToSeconds(transcriptionData.get(i).startTime);
                            double endTime = timeToSeconds(transcriptionData.get(i).endTime);

                            if (videoTime >= startTime && videoTime <= endTime) {
                                // Highlight the active transcription
                                adapter.setActiveIndex(i);
                                transcriptionFound = true;
                                break;
                            }
                        }

                        if (!transcriptionFound) {
                            // No transcription matches the current playback time
                            adapter.setActiveIndex(-1);
                        }
                    } catch (Exception e) {
                        Log.e("VideoSync", "Error parsing video playback time: " + e.getMessage(), e);
                    }

                    // Schedule the next synchronization
                    handler.postDelayed(this, 500); // Update every 500 milliseconds
                });
            }
        };

        // Start the synchronization loop
        handler.post(runnable);
    }


    private double timeToSeconds(String time) {
        String[] parts = time.split("[:.]");
        return Integer.parseInt(parts[0]) * 3600 + Integer.parseInt(parts[1]) * 60 + Double.parseDouble(parts[2]);
    }

    private void loadVideoInWebView(WebView webView, String videoId) {
        String html = "<html>" +
                "<body style='margin:0;padding:0;'>" +
                "<div id='player'></div>" +
                "<script>" +
                "  var tag = document.createElement('script');" +
                "  tag.src = 'https://www.youtube.com/iframe_api';" +
                "  var firstScriptTag = document.getElementsByTagName('script')[0];" +
                "  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);" +
                "" +
                "  var player;" +
                "  function onYouTubeIframeAPIReady() {" +
                "    player = new YT.Player('player', {" +
                "      height: '100%'," +
                "      width: '100%'," +
                "      videoId: '" + videoId + "'," +
                "      events: {" +
                "        'onReady': onPlayerReady" +
                "      }" +
                "    });" +
                "  }" +
                "" +
                "  function onPlayerReady(event) {" +
                "    event.target.playVideo();" +
                "  }" +
                "</script>" +
                "</body>" +
                "</html>";

        webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null);
    }
}
