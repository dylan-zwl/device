package com.tapc.platform.model.vaplayer;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

public class VaPlayListAnalysis {
    public PlayEntity getvaInfor(InputStream xml) throws Exception {
        PlayEntity vaPlayList = new PlayEntity();
        vaPlayList.setEvtList(new ArrayList<String>());
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8");
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String nodeName = pullParser.getName();
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if ("name".equals(nodeName)) {
                        vaPlayList.setName(pullParser.nextText());
                    } else if ("description".equals(nodeName)) {
                        vaPlayList.setDescription(pullParser.nextText());
                    } else if ("location".equals(nodeName)) {
                        vaPlayList.setLocation(pullParser.nextText());
                    } else if ("still".equals(nodeName)) {
                        vaPlayList.setStill(pullParser.nextText().replace("\\", "/"));
                    } else if ("uniqueid".equals(nodeName)) {
                        vaPlayList.setUniqueid(pullParser.nextText());
                    } else if ("video".equals(nodeName)) {
                        vaPlayList.getEvtList().add(pullParser.nextText().replace("\\", "/"));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            event = pullParser.next();
        }
        return vaPlayList;
    }
}
