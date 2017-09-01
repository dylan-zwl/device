/**
 * ValUtil.java[v 1.0.0]
 * classes:com.jht.tapc.platform.media.util.ValUtil
 * fch Create of at 2015楠烇拷閺堬拷3閺冿拷娑撳宕?2:06:36
 */
package com.tapc.platform.model.vaplayer;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ValUtil {

    private static final String FILE_PREFIX = "playlist";

    public static ArrayList<PlayEntity> getValList(String path) {
        ArrayList<PlayEntity> list = new ArrayList<PlayEntity>();
        try {
            File FilePath = new File(path);
            ArrayList<String> fileList = new ArrayList<String>();
            File[] Files = FilePath.listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isFile() && (f.getName().startsWith(FILE_PREFIX));
                }
            });

            if (Files != null) {
                if (Files.length > 0) {
                    for (int nums = 0; nums < Files.length; nums++) {
                        fileList.add(Files[nums].getAbsolutePath());
                    }
                }
                Collections.sort(fileList);
                for (String FileDiretory : fileList) {
                    InputStream PlayListxml = new FileInputStream(FileDiretory);

                    Log.d("playlistFile ", FileDiretory);
                    VaPlayListAnalysis mvaPlayListAnalysis = new VaPlayListAnalysis();
                    PlayEntity vaPlayList = new PlayEntity();
                    vaPlayList = mvaPlayListAnalysis.getvaInfor(PlayListxml);
                    vaPlayList.setPath(path);
                    Log.d("PlayList", " name:" + vaPlayList.getName() + " description:" + vaPlayList.getDescription()
                            + " location:" + vaPlayList.getLocation() + " still:" + vaPlayList.getStill() + " uniqueid:"
                            + vaPlayList.getUniqueid());
                    for (String Evtstr : vaPlayList.getEvtList()) {
                        Log.d("PlayList Evt", Evtstr);
                    }
                    list.add(vaPlayList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static VaInfor getvaInfor(InputStream xml) throws Exception {
        VaInfor mvaInfor = new VaInfor();
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(xml, "UTF-8");
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String nodeName = pullParser.getName();
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if ("title".equals(nodeName)) {
                        mvaInfor.title = pullParser.nextText();
                    } else if ("version".equals(nodeName)) {
                        mvaInfor.version = pullParser.nextText();
                    } else if ("minvideospeed".equals(nodeName)) {
                        mvaInfor.minvideospeed = pullParser.nextText();
                    } else if ("maxvideospeed".equals(nodeName)) {
                        mvaInfor.maxvideospeed = pullParser.nextText();
                    } else if ("startspeed".equals(nodeName)) {
                        mvaInfor.startspeed = pullParser.nextText();
                    } else if ("stopspeed".equals(nodeName)) {
                        mvaInfor.stopspeed = pullParser.nextText();
                    } else if ("startrpm".equals(nodeName)) {
                        mvaInfor.startrpm = pullParser.nextText();
                    } else if ("name".equals(nodeName)) {
                        mvaInfor.name = pullParser.nextText();
                    } else if ("key".equals(nodeName)) {
                        mvaInfor.key = pullParser.nextText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            event = pullParser.next();
        }
        return mvaInfor;
    }
}
