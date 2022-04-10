package com.example.top10apps;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    public ParseApplications() {
        this.applications=new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }
    public boolean parse(String xmlData){
        boolean status = true;
        FeedEntry currentRecord=null;
        boolean inEntry = false;
        String textValue="";
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType=xpp.getEventType();

            while(eventType!=XmlPullParser.END_DOCUMENT)
            {
                String tagname = xpp.getName();
                switch (eventType)
                {
                    case XmlPullParser.START_TAG :
                        Log.d(TAG, "parse: Starting tag for :" + tagname);
                        if ("entry".equalsIgnoreCase(tagname) || "item".equalsIgnoreCase(tagname))
                        {
                            inEntry= true;
                            currentRecord = new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT :
                        textValue= xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for " + tagname);
                        if (inEntry)
                        {
                            if ("entry".equalsIgnoreCase(tagname) || "item".equalsIgnoreCase(tagname))
                            {
                                applications.add(currentRecord);
                                inEntry=false;
                            }else if ("name".equalsIgnoreCase(tagname) || "title".equalsIgnoreCase(tagname))
                            {
                                currentRecord.setName(textValue);
                            }else if ("artist".equalsIgnoreCase(tagname) || "category".equalsIgnoreCase(tagname))
                            {
                                currentRecord.setArtist(textValue);
                            }else if ("releasedate".equalsIgnoreCase(tagname))
                            {
                                currentRecord.setReleasedate(textValue);
                            }
                            else if ("summary".equalsIgnoreCase(tagname) || "description".equalsIgnoreCase(tagname))
                            {
                                currentRecord.setSummary(textValue);
                            }else if ("image".equalsIgnoreCase(tagname))
                            {
                                currentRecord.setImageURl(textValue);
                            }
                            break;
                        }
                    default:
                }
                eventType=xpp.next();
            }
            for (FeedEntry app:applications)
            {
                Log.d(TAG, "parse: ************");
                Log.d(TAG, app.toString());
            }
        }catch (Exception e){
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}
